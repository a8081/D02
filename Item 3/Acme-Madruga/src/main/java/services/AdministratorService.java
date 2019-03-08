
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import forms.ActorFrom;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository						administratorRepository;

	@Autowired
	private ActorService								actorService;

	@Autowired
	private FolderService								folderService;

	@Autowired
	private UserAccountService							userAccountService;
	@Autowired
	private org.springframework.validation.Validator	validator;


	public Administrator create() {
		final Administrator a = new Administrator();

		this.actorService.setAuthorityUserAccount(Authority.ADMIN, a);

		return a;
	}

	public Administrator save(final Administrator a) {
		// It must be an administrator who create another one, or who modify himself
		this.findByPrincipal();
		Assert.notNull(a);
		Administrator result;
		this.actorService.checkForSpamWords(a);
		if (a.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.ADMIN, a);
			result = this.administratorRepository.save(a);
			this.folderService.setFoldersByDefault(result);
		} else
			result = (Administrator) this.actorService.save(a);

		return result;
	}
	public Administrator findOne(final int id) {
		Assert.isTrue(id != 0);
		final Administrator result = this.administratorRepository.findOne(id);
		Assert.notNull(result);
		return result;
	}

	public Administrator findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Administrator a = this.findByUserId(user.getId());
		Assert.notNull(a);
		final boolean bool = this.actorService.checkAuthority(a, Authority.ADMIN);
		Assert.isTrue(bool);

		return a;
	}

	public Administrator findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Administrator a = this.administratorRepository.findByUserId(id);
		return a;
	}

	public Administrator reconstruct(final ActorFrom actorForm) {
		Administrator admin;
		if (actorForm.getId() == 0) {
			admin = this.create();
			admin.setName(actorForm.getName());
			admin.setMiddleName(actorForm.getMiddleName());
			admin.setSurname(actorForm.getSurname());
			admin.setPhoto(actorForm.getPhoto());
			admin.setPhone(actorForm.getPhone());
			admin.setEmail(actorForm.getEmail());
			admin.setAddress(actorForm.getAddress());
			admin.setScore(0.0);
			admin.setSpammer(false);

		} else {
			admin = this.administratorRepository.findOne(actorForm.getId());
			admin.setName(actorForm.getName());
			admin.setMiddleName(actorForm.getMiddleName());
			admin.setSurname(actorForm.getSurname());
			admin.setPhoto(actorForm.getPhoto());
			admin.setPhone(actorForm.getPhone());
			admin.setEmail(actorForm.getEmail());
			admin.setAddress(actorForm.getAddress());
			final UserAccount account = this.userAccountService.findOne(admin.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			admin.setUserAccount(account);
		}
		return admin;
	}
}
