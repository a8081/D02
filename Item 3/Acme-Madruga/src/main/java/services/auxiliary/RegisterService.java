
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
import services.AdministratorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.MemberService;
import services.SponsorService;
import services.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Sponsor;
import forms.ActorFrom;
import forms.BrotherhoodForm;
import forms.ChapterForm;

@Service
@Transactional
public class RegisterService {

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

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private ChapterService			chapterService;


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

	public Sponsor saveSponsor(final Sponsor sponsor, final BindingResult binding) {
		Sponsor result;
		final UserAccount ua = sponsor.getUserAccount();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		if (sponsor.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");
			ua.setPassword(hash);
			sponsor.setUserAccount(ua);
			result = this.sponsorService.save(sponsor);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {
			final Sponsor old = this.sponsorService.findOne(sponsor.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.sponsorService.save(sponsor);

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

	//To encode the password. It also checks if the username already exists in case of new registration
	public Chapter saveChapter(final Chapter chapter, final BindingResult binding) {
		Chapter result;

		final UserAccount ua = chapter.getUserAccount();

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(ua.getPassword(), null);
		System.out.println();
		if (chapter.getId() == 0) {
			Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			ua.setPassword(hash);
			chapter.setUserAccount(ua);

			result = this.chapterService.save(chapter);
			UserAccount uaSaved = result.getUserAccount();
			uaSaved.setAuthorities(ua.getAuthorities());
			uaSaved.setUsername(ua.getUsername());
			uaSaved.setPassword(ua.getPassword());
			uaSaved = this.userAccountService.save(uaSaved);
			result.setUserAccount(uaSaved);
		} else {

			final Chapter old = this.chapterService.findOne(chapter.getId());

			ua.setPassword(hash);
			if (!old.getUserAccount().getUsername().equals(ua.getUsername()))
				Assert.isTrue(this.userAccountRepository.findByUsername(ua.getUsername()) == null, "The username is register");

			result = this.chapterService.save(chapter);

		}

		return result;

	}

	public ChapterForm inyect(final Chapter chapter) {
		final ChapterForm result = new ChapterForm();

		result.setAddress(chapter.getAddress());
		result.setEmail(chapter.getEmail());
		result.setId(chapter.getId());
		result.setMiddleName(chapter.getMiddleName());
		result.setName(chapter.getName());
		result.setPhone(chapter.getPhone());
		result.setPhoto(chapter.getPhoto());
		result.setSurname(chapter.getSurname());
		result.setUserAccountpassword(chapter.getUserAccount().getPassword());
		result.setUserAccountuser(chapter.getUserAccount().getUsername());
		result.setVersion(chapter.getVersion());

		result.setTitle(chapter.getTitle());

		return result;
	}
}
