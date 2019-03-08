
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Finder;
import domain.Member;
import forms.ActorFrom;

@Service
@Transactional
public class MemberService {

	@Autowired
	private MemberRepository							memberRepository;

	@Autowired
	private ActorService								actorService;

	@Autowired
	private FolderService								folderService;

	@Autowired
	private UserAccountService							userAccountService;

	@Autowired
	private org.springframework.validation.Validator	validator;

	@Autowired
	private EnrolmentService							enrolmentService;

	@Autowired
	private FinderService								finderService;


	public Member create() {
		final Member member = new Member();
		this.actorService.setAuthorityUserAccount(Authority.MEMBER, member);

		return member;
	}

	public Collection<Member> findAll() {

		final Collection<Member> result = this.memberRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Member findOne(final int memberId) {
		Assert.isTrue(memberId != 0);
		final Member result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);
		return result;
	}

	public Member save(final Member member) {
		Assert.notNull(member);
		Member result;

		if (member.getId() == 0) {
			final Finder finder = this.finderService.createForNewMember();
			member.setFinder(finder);
			this.actorService.setAuthorityUserAccount(Authority.MEMBER, member);
			result = this.memberRepository.save(member);
			this.folderService.setFoldersByDefault(result);
		} else {
			this.actorService.checkForSpamWords(member);
			result = (Member) this.actorService.save(member);
		}
		return result;
	}
	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(this.findByPrincipal().equals(member));
		Assert.isTrue(member.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == member.getId(), "You only can edit your info");
		Assert.isTrue(this.memberRepository.exists(member.getId()));
		this.memberRepository.delete(member);
	}

	/* ========================= OTHER METHODS =========================== */

	public Member findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Member member = this.findByUserId(user.getId());
		Assert.notNull(member);

		final boolean bool = this.actorService.checkAuthority(member, Authority.MEMBER);
		Assert.isTrue(bool);

		return member;
	}

	public Member findByUserId(final int userAccountId) {
		Assert.isTrue(userAccountId != 0);
		final Member member = this.memberRepository.findByUserId(userAccountId);
		return member;
	}

	public Collection<Member> allMembersFromBrotherhood() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.BROTHERHOOD));
		final Collection<Member> all = this.memberRepository.allMembersFromBrotherhood(principal.getUserAccount().getId());
		return all;
	}

	public Member reconstruct(final ActorFrom actorForm) {
		Member member;
		if (actorForm.getId() == 0) {
			member = this.create();
			member.setName(actorForm.getName());
			member.setMiddleName(actorForm.getMiddleName());
			member.setSurname(actorForm.getSurname());
			member.setPhoto(actorForm.getPhoto());
			member.setPhone(actorForm.getPhone());
			member.setEmail(actorForm.getEmail());
			member.setAddress(actorForm.getAddress());
			member.setScore(0.0);
			member.setSpammer(false);
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.MEMBER);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			member.setUserAccount(account);
		} else {
			member = this.memberRepository.findOne(actorForm.getId());
			member.setName(actorForm.getName());
			member.setMiddleName(actorForm.getMiddleName());
			member.setSurname(actorForm.getSurname());
			member.setPhoto(actorForm.getPhoto());
			member.setPhone(actorForm.getPhone());
			member.setEmail(actorForm.getEmail());
			member.setAddress(actorForm.getAddress());
			final UserAccount account = this.userAccountService.findOne(member.getUserAccount().getId());
			account.setUsername(actorForm.getUserAccountuser());
			account.setPassword(actorForm.getUserAccountpassword());
			member.setUserAccount(account);
		}
		return member;
	}

	public List<Member> getMembersTenPercent() {
		final List<Member> result = this.memberRepository.getMembersTenPercent();
		Assert.notNull(result);
		return result;
	}

}
