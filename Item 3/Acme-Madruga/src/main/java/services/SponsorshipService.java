
package services;

import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private Validator				validator;


	public Sponsorship create() {
		this.sponsorService.findByPrincipal();
		return new Sponsorship();
	}

	public Sponsorship findOne(final int id) {
		final Sponsorship s = this.sponsorshipRepository.findOne(id);
		return s;
	}

	public Collection<Sponsorship> findAllBySponsor() {
		final Sponsor principal = this.sponsorService.findByPrincipal();
		final Collection<Sponsorship> res = this.findAllByUserId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Sponsorship save(final Sponsorship s) {
		Assert.notNull(s);

		final Sponsor principal = this.sponsorService.findByPrincipal();
		final Collection<Sponsorship> ss = this.findAllByUserId(principal.getUserAccount().getId());

		if (s.getId() == 0) {
			s.setActivated(true);
			s.setSponsor(principal);
			Assert.isTrue(this.sponsorshipRepository.availableSponsorshipParade(s.getParade().getId(), principal.getUserAccount().getId()), "Cannot sponsors twice the same parade");
			Assert.notNull(s.getCreditCard(), "You must to set a credit card to create a sponsorship");
			Assert.isTrue(this.paradeService.exists(s.getParade().getId()), "You must sponsors to a parade of the system");
			Assert.isTrue(!this.expiredCreditCard(s.getCreditCard()));
		} else {
			Assert.isTrue(ss.contains(s), "You only can modify your sponsorships, you haven't access to this resource");
			Assert.isTrue(!this.expiredCreditCard(s.getCreditCard()));
		}
		final Sponsorship saved = this.sponsorshipRepository.save(s);
		return saved;
	}

	public void deactivate(final Sponsorship sp) {
		sp.setActivated(false);
		this.save(sp);
	}

	public void reactivate(final Sponsorship sp) {
		sp.setActivated(true);
		this.save(sp);
	}

	private Collection<Sponsorship> findAllByUserId(final int id) {
		Assert.isTrue(id != 0);
		return this.sponsorshipRepository.findAllByUserId(id);
	}

	public Collection<Sponsorship> findAll() {
		final Collection<Sponsorship> res = this.sponsorshipRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Sponsorship findRandomSponsorship(final int paradeId) {
		Sponsorship res;
		final Collection<Sponsorship> sponsorships = this.sponsorshipRepository.findByParade(paradeId);
		if (sponsorships.size() > 0) {
			final int randomNumber = (int) (Math.random() * sponsorships.size());
			res = (Sponsorship) sponsorships.toArray()[randomNumber];
		} else
			res = null;
		return res;
	}

	public Collection<Sponsorship> findByParade(final int paradeId) {
		final Collection<Sponsorship> res;
		res = this.sponsorshipRepository.findByParade(paradeId);
		Assert.notNull(res);
		return res;
	}

	public Sponsorship findByParade(final int paradeId, final int sponsorUAId) {
		final Sponsorship res;
		res = this.sponsorshipRepository.findByParade(paradeId, sponsorUAId);
		Assert.notNull(res);
		return res;
	}

	public Collection<Sponsorship> findAllActivateByUserId(final Sponsor s) {
		final Collection<Sponsorship> res;
		res = this.sponsorshipRepository.findAllActivateByUserId(s.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Sponsorship> findAllDeactivateByUserId(final Sponsor s) {
		Collection<Sponsorship> res;
		res = this.sponsorshipRepository.findAllDeactivateByUserId(s.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	/**
	 * Launch a process that automatically deactivates the sponsorships whose credit cards have expired. It only can be launch by administrators.
	 * 
	 * @author a8081
	 * */
	public void deactivateExpiredCreditCard() {
		this.administratorService.findByPrincipal();
		final Date now = new Date();
		final Collection<Sponsorship> sp = this.sponsorshipRepository.expiredSponsorships(now.getMonth() + 1, now.getYear() % 100);
		for (final Sponsorship s : sp) {
			s.setActivated(false);
			this.sponsorshipRepository.save(s);
		}
	}

	public SponsorshipForm inyect(final Sponsorship sponsorship) {
		final SponsorshipForm result = new SponsorshipForm();

		result.setId(sponsorship.getId());
		result.setVersion(sponsorship.getVersion());
		result.setBanner(sponsorship.getBanner());
		result.setTargetPage(sponsorship.getTargetPage());
		result.setActivated(sponsorship.isActivated());
		result.setParade(sponsorship.getParade());
		result.setNumber(sponsorship.getCreditCard().getNumber());
		result.setHolderName(sponsorship.getCreditCard().getHolderName());
		result.setMake(sponsorship.getCreditCard().getMake());
		result.setExpirationMonth(sponsorship.getCreditCard().getExpirationMonth());
		result.setExpirationYear(sponsorship.getCreditCard().getExpirationYear());
		result.setCvv(sponsorship.getCreditCard().getCvv());

		return result;
	}

	public Sponsorship reconstruct(final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		Sponsorship sponsorship;
		final CreditCard creditCard = new CreditCard();

		if (sponsorshipForm.getId() == 0) {
			sponsorship = this.create();
			sponsorship.setParade(sponsorshipForm.getParade());
		} else
			sponsorship = this.sponsorshipRepository.findOne(sponsorshipForm.getId());

		final String number = sponsorshipForm.getNumber().replace(" ", "");
		creditCard.setMake(sponsorshipForm.getMake());
		creditCard.setNumber(number);
		creditCard.setHolderName(sponsorshipForm.getHolderName());
		creditCard.setExpirationMonth(sponsorshipForm.getExpirationMonth());
		creditCard.setExpirationYear(sponsorshipForm.getExpirationYear());
		creditCard.setCvv(sponsorshipForm.getCvv());
		sponsorship.setActivated(sponsorshipForm.isActivated());
		sponsorship.setBanner(sponsorshipForm.getBanner());
		sponsorship.setTargetPage(sponsorshipForm.getTargetPage());
		sponsorship.setSponsor(this.sponsorService.findByPrincipal());
		sponsorship.setCreditCard(creditCard);

		this.validator.validate(sponsorship, binding);

		this.sponsorshipRepository.flush();

		if (binding.hasErrors())
			throw new ValidationException();

		return sponsorship;
	}

	public Sponsorship findOneSponsorship(final int sponsorshipId) {
		final Sponsor sponsor = this.sponsorService.findByPrincipal();
		final Sponsorship sponsorship = this.findOne(sponsorshipId);
		Assert.isTrue(sponsorship.getSponsor().getId() == sponsor.getId());
		return sponsorship;
	}

	// ancilliary
	boolean expiredCreditCard(final CreditCard c) {
		boolean res = false;
		final Date now = new Date();
		final boolean mesCaducado = c.getExpirationMonth() < (now.getMonth() + 1);
		final boolean mismoAnyo = (c.getExpirationYear()) == (now.getYear() % 100);
		final boolean anyoCaducado = (c.getExpirationYear()) < (now.getYear() % 100);
		if (anyoCaducado || (mismoAnyo && mesCaducado))
			res = true;

		return res;
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}
}
