
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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
	private MemberRepository	memberRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private Validator			validator;


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

	public Member findByRequestId(final int requestId) {
		Assert.isTrue(requestId != 0);
		final Member member = this.memberRepository.findByRequestId(requestId);
		return member;
	}

	public Member findByEnrolmentId(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);
		final Member member = this.memberRepository.findByEnrolmentId(enrolmentId);
		return member;
	}

	public Collection<Member> allMembersFromBrotherhood() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.BROTHERHOOD));
		final Collection<Member> all = this.memberRepository.allMembersFromBrotherhood(principal.getUserAccount().getId());
		return all;
	}

	public Collection<Member> allMembersByBrotherhood(final int brotherhoodUAId) {
		final Collection<Member> all = this.memberRepository.allMembersFromBrotherhood(brotherhoodUAId);
		return all;
	}

	public Member reconstruct(final ActorFrom actorForm, final BindingResult binding) {
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

		this.validator.validate(member, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return member;
	}

	public List<Member> getMembersTenPercent() {
		final Integer[] members = this.memberRepository.getMembersTenPercent();
		final List<Member> result = new ArrayList<Member>();
		if (members != null || members.length > 0)
			for (final Integer id : members)
				result.add(this.findOne(id));
		return result;
	}

}
