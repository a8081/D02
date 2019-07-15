
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;
import domain.Request;

@Service
@Transactional
public class RequestService {

	@Autowired
	private RequestRepository	requestRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MessageService		messageService;


	// ======================= CRUD ================================
	/**
	 * Este metodo devuelve todos los Request's a los que puede acceder el actor autentificado.
	 * 
	 * @return Collection<Request>
	 * @author jmsx
	 */
	public Collection<Request> findAll() {
		final Actor principal = this.actorService.findByPrincipal();
		Collection<Request> res = new HashSet<>();
		if (this.actorService.checkAuthority(principal, Authority.MEMBER))
			res = this.requestRepository.findByMember(principal.getUserAccount().getId());
		else if (this.actorService.checkAuthority(principal, Authority.BROTHERHOOD))
			res = (this.requestRepository.findByBrotherhood(principal.getUserAccount().getId()));
		return res;
	}
	/**
	 * Este metodo devuelve Request si el actor tiene acceos a el.
	 * 
	 * @return Request
	 * @author jmsx
	 */
	public Request findOne(final Integer idRequest) {
		final Actor principal = this.actorService.findByPrincipal();
		Request res = this.requestRepository.findOne(idRequest);
		Assert.notNull(res, "Not found Request to this id");
		Boolean isAccepted = false;
		if (this.actorService.checkAuthority(principal, Authority.MEMBER))
			isAccepted = res.getMember().getUserAccount().getId() == principal.getUserAccount().getId();
		else if (this.actorService.checkAuthority(principal, Authority.BROTHERHOOD))
			isAccepted = this.requestRepository.checkBrotherhoodAccess(principal.getUserAccount().getId(), idRequest);
		Assert.isTrue(isAccepted, "RequesService - findOne - Access Denied");
		res = isAccepted ? res : null;
		return res;
	}
	/**
	 * Devuelve un Request vacio.
	 * 
	 * @return Request
	 * @author jmsx
	 */
	public Request create() {
		final Request res = new Request();
		return res;
	}

	public Request save(Request req) {
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isMember = this.actorService.checkAuthority(principal, Authority.MEMBER);
		final Boolean isBrotherhood = this.actorService.checkAuthority(principal, Authority.BROTHERHOOD);
		Assert.isTrue(req.getParade().getMode().equals("FINAL"));
		if (req.getId() == 0) {
			// Creacion de Request, esta debe estar PENDING
			// Assert.isTrue(req.getStatus().equals(Request.PENDING), "Request must be create as PENDING");
			req.setStatus("PENDING");
			req.setMember(this.memberService.findByPrincipal());
			final Date moment = new Date(System.currentTimeMillis() - 1);
			req.setMoment(moment);
			final boolean hasMemberRequestToParade = this.requestRepository.hasMemberRequestToParade(req.getParade().getId(), req.getMember().getUserAccount().getId());
			Assert.isTrue(!hasMemberRequestToParade, "A member cannot request twice to the same parade");
			Assert.isTrue((req.getRow() == null && req.getColumn() == null && req.getExplanation() == null), "Row, column and explanation attributes only can be set by brotherhood");
			Assert.isTrue(this.paradeService.findAllAvailableByMemberId(req.getMember()).contains(req.getParade()));
		} else {
			Assert.isTrue(!isMember, "A member cannot update the request");
			Assert.isTrue(isBrotherhood, "Only brotherhood can update a Request (to change it's status)");
			Assert.isTrue(this.requestRepository.checkBrotherhoodAccess(principal.getUserAccount().getId(), req.getId()), "This Brotherhood haven't access to this request");
			if (req.getStatus().equals("REJECTED")) {
				Assert.isTrue(!(req.getExplanation() == "" || req.getExplanation() == null), "If Request is REJECTED must have a explanation");
				this.messageService.requestStatusChangedMessage(req);
			} else if (req.getStatus().equals("APPROVED")) {
				Assert.isTrue(!this.paradeRequested(req.getParade().getId()));
				Assert.isTrue((req.getExplanation() == "" || req.getExplanation() == null), "A explanation musn't be written if you approve the request");
				final boolean rowIsNull = req.getRow() == null || req.getRow() > req.getParade().getMaxRows();
				final boolean columnIsNull = req.getColumn() == null || req.getColumn() > req.getParade().getMaxColumns();
				Assert.isTrue(!(rowIsNull || columnIsNull), "If Request is APPROVED, row and column cannot be null or greater than maximum allowed");
				Assert.isTrue(this.requestRepository.availableRowColumn(req.getRow(), req.getColumn(), req.getParade().getId()), "If Request is APPROVED, row and column assigned by brotherhood must be unique");
				this.messageService.requestStatusChangedMessage(req);
			}
		}
		req = this.requestRepository.save(req);
		return req;
	}

	public boolean availableRowColumn(final Request req) {
		Assert.notNull(req);
		return this.requestRepository.availableRowColumn(req.getRow(), req.getColumn(), req.getParade().getId());
	}

	public boolean availableProcessionPositions(final Parade procession) {
		final int total = procession.getMaxColumns() * procession.getMaxRows();
		final int totalRequests = this.requestRepository.totalApprovedRequestByProcession(procession.getId());
		return total < totalRequests;
	}

	public void delete(final Request req) {
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isMember = this.actorService.checkAuthority(principal, Authority.MEMBER);
		Assert.isTrue(isMember, "Only a member can delete a request");
		final int reqMemberId = req.getMember().getUserAccount().getId();
		final int principalId = principal.getUserAccount().getId();
		Assert.isTrue(reqMemberId == principalId, "Member must be the request owner");
		final Request request = this.requestRepository.findOne(req.getId());
		Assert.isTrue(request.getStatus().equals(Request.PENDING), "The Request must be PENDING");
		this.requestRepository.delete(req.getId());
	}

	/**
	 * This method suggest a good position automatically,
	 * to an approved request to a parade. The system understand
	 * a good position as a lower number of row-column.
	 * 
	 * @return Tuple implemented as a two elements List of Integer.
	 * 
	 * @author a8081
	 * */
	public List<Integer> suggestPosition(final Parade parade) {
		final int paradeId = parade.getId();
		boolean availablePosition = false;
		final List<Integer> res = new ArrayList<>();
		for (int i = 1; i <= parade.getMaxRows(); i++) {
			for (int j = 1; j <= parade.getMaxColumns(); j++) {
				availablePosition = this.requestRepository.availableRowColumn(i, j, paradeId);
				if (availablePosition) {
					res.add(i);
					res.add(j);
					break;
				}
				j++;
			}
			if (availablePosition)
				break;
			i++;
		}
		Assert.isTrue(res.size() == 2, "Suggest position must contains two elements, row and columns - List size != 2");
		return res;
	}
	/**
	 * This method allow members request to a parade, creating a new Request with principal member
	 * and parade given as id parameter, as its attributes.
	 * 
	 * @author a8081
	 * */
	public Request requestToParade(final Integer paradeId) {
		final Request req = this.create();
		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.isTrue(parade.getMode().equals("FINAL"), "A member cannot access to a parade in draft mode, so he or she cannot request to it");
		req.setParade(parade);
		final Request retrieved = this.save(req);
		return retrieved;

	}

	public Collection<Request> findByParade(final Integer paradeId) {
		final int principalId = this.actorService.findByPrincipal().getUserAccount().getId();
		final Parade p = this.paradeService.findOne(paradeId);
		final Collection<Request> cr = this.requestRepository.findByProcesion(paradeId);
		Assert.isTrue(p.getBrotherhood().getUserAccount().getId() == principalId, "Access to request denied, principal hasn't enough privilegies");
		return cr;
	}

	public Request findByParadeMember(final Integer paradeId) {
		Assert.isTrue(paradeId != 0);
		Request res = null;
		final Member principal = this.memberService.findByPrincipal();
		this.paradeService.findOne(paradeId);
		final Collection<Request> cr = this.requestRepository.findByProcesion(paradeId);
		for (final Request request : cr)
			if (request.getMember().equals(principal)) {
				res = request;
				break;
			}
		return res;
	}

	/**
	 * Return true if the parade pass as a parameter is already requested (one approved request).
	 * 
	 * @author a8081
	 * */
	public Boolean paradeRequested(final Integer paradeId) {
		Assert.isTrue(paradeId != 0);
		final boolean res = this.requestRepository.paradeRequested(paradeId);
		return res;
	}

	public Collection<Request> findApprovedBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Request> res = this.requestRepository.findApprovedBrotherhood(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Request> findRejectedBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Request> res = this.requestRepository.findRejectedBrotherhood(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Request> findPendingBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Request> res = this.requestRepository.findPendingBrotherhood(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Double findPendingRequestRadio() {
		Double result = this.requestRepository.findPendingRequestRatio();
		if (result == null)
			result = 0.0;
		return result;
	}
	public Double findApprovedRequestRadio() {
		Double result = this.requestRepository.findApprovedRequestRatio();
		if (result == null)
			result = 0.0;
		return result;
	}

	public Double findRejectedRequestRadio() {
		Double result = this.requestRepository.findRejectedRequestRatio();
		if (result == null)
			result = 0.0;
		return result;
	}

	public Double findPendingRequestByParadeRadio(final Integer id) {
		Double result = this.requestRepository.findPendingRequestByParadeRatio(id);
		if (result == null)
			result = 0.0;
		return result;
	}

	public Double findApprovedRequestByParadeRadio(final Integer id) {
		Double result = this.requestRepository.findApprovedRequestByParadeRatio(id);
		if (result == null)
			result = 0.0;
		return result;
	}

	public Double findRejectedRequestByParadeRadio(final Integer id) {
		Double result = this.requestRepository.findRejectedRequestByParadeRatio(id);
		if (result == null)
			result = 0.0;
		return result;
	}

}
