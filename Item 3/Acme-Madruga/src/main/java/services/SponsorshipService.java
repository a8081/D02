
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private ParadeService			paradeService;


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
			Assert.notNull(s.getCreditCard(), "You must to set a credit card to create a sponsorship");
			Assert.isTrue(this.paradeService.exists(s.getParade().getId()), "You must sponsors to a parade of the system");
		} else
			Assert.isTrue(ss.contains(s), "You only can modify your sponsorships, you haven't access to this resource");
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
}
