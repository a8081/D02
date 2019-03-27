
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Sponsor;
import forms.ActorFrom;

@Service
@Transactional
public class SponsorService {

	@Autowired
	private SponsorRepository	sponsorRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


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

	public Collection<Sponsor> findAll() {

		final Collection<Sponsor> result = this.sponsorRepository.findAll();
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

	public Sponsor reconstruct(final ActorFrom actorForm, final BindingResult binding) {
		Sponsor sponsor;
		if (actorForm.getId() == 0) {
			sponsor = this.create();
			sponsor.setName(actorForm.getName());
			sponsor.setMiddleName(actorForm.getMiddleName());
			sponsor.setSurname(actorForm.getSurname());
			sponsor.setPhoto(actorForm.getPhoto());
			sponsor.setPhone(actorForm.getPhone());
			sponsor.setEmail(actorForm.getEmail());
			sponsor.setAddress(actorForm.getAddress());
			sponsor.setScore(0.0);
			sponsor.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.SPONSOR);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			sponsor.setUserAccount(account);
		} else {
			sponsor = this.sponsorRepository.findOne(actorForm.getId());
			sponsor.setName(actorForm.getName());
			sponsor.setMiddleName(actorForm.getMiddleName());
			sponsor.setSurname(actorForm.getSurname());
			sponsor.setPhoto(actorForm.getPhoto());
			sponsor.setPhone(actorForm.getPhone());
			sponsor.setEmail(actorForm.getEmail());
			sponsor.setAddress(actorForm.getAddress());
			final UserAccount account = this.userAccountService.findOne(sponsor.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			sponsor.setUserAccount(account);
		}

		this.validator.validate(sponsor, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return sponsor;
	}

}
