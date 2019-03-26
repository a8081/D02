
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.ParadeRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Chapter;
import domain.Float;
import domain.Member;
import domain.Parade;
import domain.Segment;
import domain.Sponsor;
import forms.ParadeChapterForm;
import forms.ParadeForm;

@Service
@Transactional
public class ParadeService {

	@Autowired
	private ParadeRepository	paradeRepository;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private FloatService		floatService;

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private SegmentService		segmentService;

	@Autowired
	private SponsorService		sponsorService;


	//@Autowired
	//private Validator			validator;

	public Parade create() {
		final Parade parade = new Parade();

		final Collection<Float> floats = new ArrayList<>();
		final List<Segment> segments = new ArrayList<>();
		parade.setFloats(floats);
		parade.setSegments(segments);

		//		parade.setBrotherhood(this.brotherhoodService.findByPrincipal());
		parade.setMode("DRAFT");
		parade.setStatus("DEFAULT");
		final Date moment = new Date(System.currentTimeMillis());
		parade.setTicker(this.generateTicker(moment));

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		parade.setBrotherhood(brotherhood);

		return parade;

	}

	public Collection<Parade> findAll() {
		final Collection<Parade> result = this.paradeRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findAllFinalMode() {
		final Collection<Parade> result = this.paradeRepository.findAllFinalMode();
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findAllAccepted() {
		final Collection<Parade> result = this.paradeRepository.findAllAccepted();
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findAllFinalModeByBrotherhood(final int id) {
		final Collection<Parade> result = this.paradeRepository.findAllFinalModeByBrotherhood(id);
		Assert.notNull(result);

		return result;
	}

	public Collection<Parade> findAllByPrincipal() {
		Collection<Parade> res = new ArrayList<>();
		final Actor principal = this.actorService.findByPrincipal();
		final Boolean isBrotherhood = this.actorService.checkAuthority(principal, Authority.BROTHERHOOD);
		final Boolean isMember = this.actorService.checkAuthority(principal, Authority.MEMBER);

		if (isBrotherhood)
			res = this.paradeRepository.findAllParadeByBrotherhoodId(principal.getUserAccount().getId());
		else if (isMember)
			res = this.paradeRepository.findAllParadeByBMemberId(principal.getUserAccount().getId());
		//Si salta puede ser un Admin
		Assert.notNull(res);
		return res;
	}

	public Parade findOne(final int paradeId) {
		Assert.isTrue(paradeId != 0);
		final Parade res = this.paradeRepository.findOne(paradeId);
		Assert.notNull(res);
		return res;
	}

	Collection<Parade> findParades(final String keyword, final Date minDate, final Date maxDate, final String area) {
		final Collection<Parade> res = this.paradeRepository.findParades(keyword, minDate, maxDate, area);
		Assert.notNull(res);
		return res;
	}

	public Parade save(final Parade parade) {
		Assert.notNull(parade);
		final Brotherhood brotherhoodPrincipal = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(parade.getBrotherhood().equals(brotherhoodPrincipal), "You must be the owner of the parade");
		Parade result;

		if (brotherhoodPrincipal.getArea() != null) {
			Assert.notEmpty(parade.getFloats(), "A parade must have some floats assigned to be saved");
			final Collection<Float> fs = this.floatService.findByBrotherhood(brotherhoodPrincipal);
			Assert.isTrue(fs.containsAll(parade.getFloats()), "You must be the owner of the parade and parade floats must corresponds to your parades");

			if (parade.getId() == 0) {
				parade.setBrotherhood(brotherhoodPrincipal);
				parade.setMode("DRAFT");
				parade.setStatus("DEFAULT");
				Assert.isNull(parade.getRejectionReason(), "A new parade cannot has rejection reason");
				final Date moment = new Date(System.currentTimeMillis());
				parade.setTicker(this.generateTicker(moment));
			} else {
				final String ticker = this.findOne(parade.getId()).getTicker();
				Assert.isTrue(ticker.equals(parade.getTicker()), "Ticker cannot be modified");
				Assert.isTrue(!parade.getMode().equals("FINAL"), "Cannot edit a parade in FINAL mode");
				Assert.isTrue(parade.getStatus().equals("DEFAULT"), "Cannot change parade status being a brotherhood");
				Assert.isTrue(parade.getRejectionReason() == null, "Cannot set rejection reason being a brotherhood");
			}
		}
		result = this.paradeRepository.save(parade);
		return result;
	}
	/**
	 * Make a copy of one of the parade (given as parameter) of the brotherhood logged. It set a new ticker, clear its status and its rejection reason, and changes it to draft mode.
	 * 
	 * @author a8081
	 * */
	public Parade copyBrotherhoodParade(final int paradeId) {

		final Parade parade = this.findOne(paradeId);
		Assert.notNull(parade);
		Parade newParade;
		final Brotherhood brotherhoodPrincipal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(brotherhoodPrincipal.equals(parade.getBrotherhood()), "Brotherhood only can copy one of their parades");
		Assert.notEmpty(parade.getFloats(), "A parade must have some floats assigned to be saved");
		Assert.isTrue(this.floatService.findByBrotherhood(brotherhoodPrincipal).containsAll(parade.getFloats()));

		newParade = new Parade();
		//		No es necesario hacer esto ya que se setea en el metodo save
		//
		//		newParade.setBrotherhood(brotherhoodPrincipal);
		//		newParade.setMode("DRAFT");
		//		newParade.setStatus("DEFAULT");
		//		final Date moment = new Date(System.currentTimeMillis());
		//		newParade.setTicker(this.generateTicker(moment));

		newParade.setTitle(parade.getTitle());
		newParade.setDescription(parade.getDescription());
		newParade.setMaxColumns(parade.getMaxColumns());
		newParade.setMaxRows(parade.getMaxRows());
		newParade.setFloats(parade.getFloats());
		final List<Segment> pathCopied = this.segmentService.copyPath(parade.getSegments());
		newParade.setSegments(pathCopied);
		newParade.setMoment(parade.getMoment());

		final Parade result = this.paradeRepository.save(newParade);

		return result;
	}

	public void delete(final Parade parade) {
		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0);

		final Parade retrieved = this.findOne(parade.getId());

		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(retrieved.getBrotherhood().equals(principal));
		this.paradeRepository.delete(parade);

	}

	/* ========================= OTHER METHODS =========================== */

	private String generateTicker(final Date date) {
		String res = "";
		final SimpleDateFormat myFormat = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);
		final String YYMMMDDD = myFormat.format(date);
		final String word = RandomStringUtils.randomAlphabetic(5).toUpperCase();
		final String tickr = YYMMMDDD + '-' + word;
		res = tickr;

		if (this.hasDuplicate(res))
			this.generateTicker(date);
		return res;
	}

	private boolean hasDuplicate(final String ticker) {

		boolean res = true;
		try {
			if (this.paradeRepository.getParadeWithTicker(ticker).isEmpty())
				res = false;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * It returns all parades which principal isn't enrolled. The principal must be a member.
	 * 
	 * @author a8081
	 * */
	public Collection<Parade> paradesAvailable() {
		final Member principal = this.memberService.findByPrincipal();
		final Collection<Parade> memberparades = this.paradeRepository.findAllParadeByBMemberId(principal.getUserAccount().getId());
		final Collection<Parade> parades = this.findAllFinalMode();
		parades.removeAll(memberparades);
		return parades;
	}

	/**
	 * It returns all parades which principal has not sponsors yet. The principal must be a sponsor.
	 * 
	 * @author a8081
	 * */
	public Collection<Parade> paradesAvailableSponsor() {
		Collection<Parade> myParades;
		Collection<Parade> parades;
		final Sponsor s = this.sponsorService.findByPrincipal();
		parades = this.findAllAccepted();
		myParades = this.findAllParadeBySponsor(s);
		parades.removeAll(myParades);
		return parades;
	}

	public Collection<Parade> findAllParadeBySponsor(final Sponsor s) {
		Collection<Parade> res;
		res = this.paradeRepository.findAllParadeBySponsor(s.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public boolean exists(final Integer paradeId) {
		Assert.isTrue(paradeId != 0, "parade id cannot be zero");
		return this.paradeRepository.exists(paradeId);
	}

	public Parade toFinalMode(final int paradeId) {
		final Parade parade = this.findOne(paradeId);
		final Parade result;
		final Brotherhood bro = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(bro.getArea() != null);
		Assert.isTrue(parade.getBrotherhood() == bro, "Actor who want to edit parade mode to FINAL is not his owner");
		Assert.isTrue(parade.getMode().equals("DRAFT"), "To set final mode, parade must be in draft mode");
		Assert.isTrue(parade.getStatus().equals("DEFAULT"), "Parades must have status default if they are in draft mode");
		parade.setMode("FINAL");
		parade.setStatus("SUBMITTED");
		result = this.paradeRepository.save(parade);
		return result;
	}

	public Parade acceptParade(final int paradeId) {
		final Parade parade = this.findOne(paradeId);
		final Parade result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		Assert.isTrue(parade.getBrotherhood().getArea() == chapter.getArea(), "No puede aceptar una parade que no pertenece al �rea que coordina.");
		Assert.isTrue(parade.getMode().equals("FINAL"), "La parade que desea aceptar no ha sido guardada en modo final.");
		Assert.isTrue(parade.getStatus().equals("SUBMITTED"), "No puede aceptar una parade que su estado sea distinto a Submitted");
		if (chapter.getArea() != null)
			parade.setStatus("ACCEPTED");
		result = this.paradeRepository.save(parade);
		return result;
	}

	public Parade rejectParade(final Parade received) {
		final Parade result;
		final Parade parade = this.findOne(received.getId());
		final String reason = received.getRejectionReason();
		final Chapter chapter = this.chapterService.findByPrincipal();
		Assert.isTrue(reason != null && reason != "", "To reject a parade yo must provide a rejection reason");
		Assert.isTrue(parade.getBrotherhood().getArea() == chapter.getArea(), "No puede rechazar una parade que no pertenece al area que coordina.");
		Assert.isTrue(parade.getMode().equals("FINAL"), "La parade que desea rechazar no ha sido guardada en modo final");
		Assert.isTrue(parade.getStatus().equals("SUBMITTED"), "No puede rechazar una parade que su estado sea distinto a Submitted");
		if (chapter.getArea() != null) {
			parade.setStatus("REJECTED");
			parade.setRejectionReason(received.getRejectionReason());
		}
		result = this.paradeRepository.save(parade);
		return result;

	}

	// This method is not used because it doesn't make sense to have a pruned object in parade
	public Parade reconstruct(final ParadeForm pform, final BindingResult binding) {
		Parade result;

		if (pform.getId() == 0) {
			result = this.create();
			final Date moment = new Date(System.currentTimeMillis());
			result.setTicker(this.generateTicker(moment));
		} else
			result = this.findOne(pform.getId());

		result.setTitle(pform.getTitle());
		result.setDescription(pform.getDescription());
		result.setMaxRows(pform.getMaxRows());
		result.setMaxColumns(pform.getMaxColumns());
		result.setFloats(pform.getFloats());
		result.setMoment(pform.getMoment());

		//this.validator.validate(result, binding);

		return result;
	}

	public List<Parade> getParadesThirtyDays() {
		final List<Parade> result = this.paradeRepository.getParadesThirtyDays();
		Assert.notNull(result);
		return result;
	}

	public Collection<Parade> findAllFinalModeAccepted(final int areaId) {
		final Chapter principal = this.chapterService.findByPrincipal();
		final Chapter areaChapter = this.chapterService.findChapterByArea(areaId);
		Assert.isTrue(principal == areaChapter, "You're not the owner of this area");
		final Collection<Parade> res = this.paradeRepository.findAllFinalModeAcceptedByArea(areaId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllFinalModeRejected(final int areaId) {
		final Chapter principal = this.chapterService.findByPrincipal();
		final Chapter areaChapter = this.chapterService.findChapterByArea(areaId);
		Assert.isTrue(principal == areaChapter, "You're not the owner of this area");
		final Collection<Parade> res = this.paradeRepository.findAllFinalModeRejectedByArea(areaId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllFinalModeSubmitted(final int areaId) {
		final Chapter principal = this.chapterService.findByPrincipal();
		final Chapter areaChapter = this.chapterService.findChapterByArea(areaId);
		Assert.isTrue(principal == areaChapter, "You're not the owner of this area");
		final Collection<Parade> res = this.paradeRepository.findAllFinalModeSubmittedByArea(areaId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllAcceptedByBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Parade> res = this.paradeRepository.findAllAcceptedByBrotherhood(principal.getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllRejectedByBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Parade> res = this.paradeRepository.findAllRejectedByBrotherhood(principal.getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllSubmittedByBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Parade> res = this.paradeRepository.findAllSubmittedByBrotherhood(principal.getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllDefaultByBrotherhood() {
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
		final Collection<Parade> res = this.paradeRepository.findAllDefaultByBrotherhood(principal.getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Parade> findAllParadeByBrotherhoodId(final Integer broUAId) {
		Collection<Parade> res = new ArrayList<>();
		res = this.paradeRepository.findAllParadeByBrotherhoodId(broUAId);
		Assert.notNull(res);
		return res;
	}

	public Double findRatioDraftVsFinalParades() {
		final Double result = this.paradeRepository.findRatioDraftVsFinalParades();
		Assert.notNull(result);
		return result;
	}

	public Double findSubmittedParadesRatio() {
		final Double result = this.paradeRepository.findSubmittedParadesRatio();
		Assert.notNull(result);
		return result;
	}

	public Double findAcceptedParadesRatio() {
		final Double result = this.paradeRepository.findAcceptedParadesRatio();
		Assert.notNull(result);
		return result;
	}

	public Double findRejectedParadesRatio() {
		final Double result = this.paradeRepository.findRejectedParadesRatio();
		Assert.notNull(result);
		return result;
	}

	public Parade findParadeBySegment(final Integer segmentId) {
		Parade res;
		res = this.paradeRepository.findParadeBySegment(segmentId);
		return res;
	}

	public Parade reconstruct2(final ParadeChapterForm paradeChapterForm, final BindingResult binding) {
		Parade result;

		Assert.isTrue(paradeChapterForm.getId() != 0);

		result = this.findOne(paradeChapterForm.getId());

		result.setId(paradeChapterForm.getId());
		result.setVersion(paradeChapterForm.getVersion());
		result.setStatus(paradeChapterForm.getStatus());
		result.setRejectionReason(paradeChapterForm.getRejectionReason());

		return result;
	}

	public void flush() {
		this.paradeRepository.flush();
	}
}
