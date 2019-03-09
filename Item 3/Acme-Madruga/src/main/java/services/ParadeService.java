
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
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Float;
import domain.Member;
import domain.Parade;
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
	private Validator			validator;


	public Parade create() {
		final Parade parade = new Parade();

		final Collection<Float> floats = new ArrayList<>();
		parade.setFloats(floats);

		parade.setBrotherhood(this.brotherhoodService.findByPrincipal());
		parade.setMode("DRAFT");
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
		final Actor principal = this.actorService.findByPrincipal();
		final Parade result;
		final Boolean isBrotherhood = this.actorService.checkAuthority(principal, Authority.BROTHERHOOD);
		final Brotherhood bro = this.brotherhoodService.findByUserId(principal.getUserAccount().getId());

		if (isBrotherhood && bro.getArea() != null) {
			final Brotherhood brotherhoodPrincipal = this.brotherhoodService.findByPrincipal();
			Assert.notEmpty(parade.getFloats(), "A parade must have some floats assigned to be saved");
			Assert.isTrue(this.floatService.findByBrotherhood(brotherhoodPrincipal).containsAll(parade.getFloats()));

			if (parade.getId() == 0) {
				parade.setBrotherhood(brotherhoodPrincipal);
				parade.setMode("DRAFT");
				final Date moment = new Date(System.currentTimeMillis());
				parade.setTicker(this.generateTicker(moment));
			} else {
				Assert.isTrue(!parade.getMode().equals("FINAL"), "Cannot edit a parade in FINAL mode");
				Assert.isTrue(parade.getBrotherhood() == this.brotherhoodService.findByPrincipal());
			}
		}
		result = this.paradeRepository.save(parade);
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

	public boolean exists(final Integer paradeId) {
		Assert.isTrue(paradeId != 0, "parade id cannot be zero");
		return this.paradeRepository.exists(paradeId);
	}

	public Parade toFinalMode(final int paradeId) {
		final Parade parade = this.findOne(paradeId);
		final Parade result;
		final Brotherhood bro = this.brotherhoodService.findByPrincipal();
		Assert.isTrue(parade.getBrotherhood() == bro, "Actor who want to edit parade mode to FINAL is not his owner");
		Assert.isTrue(parade.getMode().equals("DRAFT"));
		if (bro.getArea() != null)
			parade.setMode("FINAL");
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

		this.validator.validate(result, binding);

		return result;
	}

	public List<Parade> getParadesThirtyDays() {
		final List<Parade> result = this.paradeRepository.getParadesThirtyDays();
		Assert.notNull(result);
		return result;
	}
}
