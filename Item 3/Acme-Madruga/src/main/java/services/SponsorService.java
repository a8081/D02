
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Sponsor;

@Service
@Transactional
public class SponsorService {

	@Autowired
	private SponsorRepository	sponsorRepository;

	@Autowired
	private ActorService		actorService;

	private FolderService		folderService;


	public Sponsor create() {
		final Sponsor s = new Sponsor();
		this.actorService.setAuthorityUserAccount(Authority.SPONSOR, s);
		return s;
	}

	public Sponsor findByUserId(final int id) {
		Assert.isTrue(id != 0);
		return this.sponsorRepository.findByUserId(id);
	}

	public Sponsor findOne(final int id) {
		Assert.isTrue(id != 0);
		final Sponsor result = this.sponsorRepository.findOne(id);
		Assert.notNull(result);
		return result;
	}

	public Sponsor findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Sponsor s = this.findByUserId(user.getId());
		Assert.notNull(s);
		final boolean bool = this.actorService.checkAuthority(s, Authority.SPONSOR);
		Assert.isTrue(bool);

		return s;
	}

	public Sponsor save(final Sponsor s) {
		Assert.notNull(s);
		Sponsor result;
		this.actorService.checkForSpamWords(s);
		if (s.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.SPONSOR, s);
			result = this.sponsorRepository.save(s);
			this.folderService.setFoldersByDefault(s);
		} else {
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == s.getId(), "You only can edit your info");
			result = (Sponsor) this.actorService.save(s);
		}
		return result;
	}

}
