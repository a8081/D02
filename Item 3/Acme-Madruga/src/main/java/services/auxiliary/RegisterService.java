
package services.auxiliary;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import security.UserAccount;
import security.UserAccountRepository;
import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.MemberService;
import services.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;
import forms.ActorFrom;
import forms.BrotherhoodForm;

@Service
@Transactional
public class RegisterService {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private UserAccountService		userAccountService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private MemberService			memberService;
	@Autowired
	private BrotherhoodService		brotherhoodService;


	public Administrator saveAdmin(final Administrator admin, final BindingResult binding) {
		Administrator result;
		final UserAccount ua = admin.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (admin.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			admin.setUserAccount(ua);
			result = this.administratorService.save(admin);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Administrator old = this.administratorService.findOne(admin.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.administratorService.save(admin);

		}

		return result;
	}

	public Member saveMember(final Member member, final BindingResult binding) {
		Member result;
		final UserAccount ua = member.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (member.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			member.setUserAccount(ua);
			result = this.memberService.save(member);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Member old = this.memberService.findOne(member.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.memberService.save(member);

		}

		return result;
	}

	public ActorFrom inyect(final Actor actor) {
		final ActorFrom result = new ActorFrom();

		result.setAddress(actor.getAddress());
		result.setEmail(actor.getEmail());
		result.setId(actor.getId());
		result.setMiddleName(actor.getMiddleName());
		result.setName(actor.getName());
		result.setPhone(actor.getPhone());
		result.setPhoto(actor.getPhoto());
		result.setSurname(actor.getSurname());
		result.setUserAccountpassword(actor.getUserAccount().getPassword());
		result.setUserAccountuser(actor.getUserAccount().getUsername());
		result.setVersion(actor.getVersion());

		return result;
	}

	public BrotherhoodForm inyect(final Brotherhood brotherhood) {
		final BrotherhoodForm result = new BrotherhoodForm();

		result.setAddress(brotherhood.getAddress());
		result.setEmail(brotherhood.getEmail());
		result.setId(brotherhood.getId());
		result.setMiddleName(brotherhood.getMiddleName());
		result.setName(brotherhood.getName());
		result.setPhone(brotherhood.getPhone());
		result.setPhoto(brotherhood.getPhoto());
		result.setSurname(brotherhood.getSurname());
		result.setUserAccountpassword(brotherhood.getUserAccount().getPassword());
		result.setUserAccountuser(brotherhood.getUserAccount().getUsername());
		result.setVersion(brotherhood.getVersion());

		result.setTitle(brotherhood.getTitle());
		result.setPictures(brotherhood.getPictures());

		return result;
	}

	public Brotherhood saveBrotherhood(final Brotherhood brotherhood, final BindingResult binding) {
		Brotherhood result;
		final UserAccount ua = brotherhood.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		System.out.println();
		if (brotherhood.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			final Date moment = new Date(System.currentTimeMillis() - 1);

			brotherhood.setDate(moment);
			ua.setPassword(hash);
			brotherhood.setUserAccount(ua);

			result = this.brotherhoodService.save(brotherhood);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {

			final Brotherhood old = this.brotherhoodService.findOne(brotherhood.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.brotherhoodService.save(brotherhood);

		}

		return result;

	}
}
