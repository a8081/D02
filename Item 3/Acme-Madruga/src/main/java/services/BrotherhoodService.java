
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Area;
import domain.Brotherhood;
import forms.BrotherhoodAreaForm;
import forms.BrotherhoodForm;

@Service
@Transactional
public class BrotherhoodService {

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private FolderService			folderService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private Validator				validator;


	public Brotherhood create() {
		final Brotherhood brotherhood = new Brotherhood();
		this.actorService.setAuthorityUserAccount(Authority.BROTHERHOOD, brotherhood);
		return brotherhood;
	}

	public Collection<Brotherhood> findAll() {
		final Collection<Brotherhood> result = this.brotherhoodRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Brotherhood findOne(final int brotherhoodId) {
		Assert.isTrue(brotherhoodId != 0);
		final Brotherhood result = this.brotherhoodRepository.findOne(brotherhoodId);
		Assert.notNull(result, "Find One de brotherhood es nulo");
		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Brotherhood result;

		if (brotherhood.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.BROTHERHOOD, brotherhood);
			result = this.brotherhoodRepository.save(brotherhood);

		} else {
			this.actorService.checkForSpamWords(brotherhood);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == brotherhood.getId(), "You only can edit your info");
			//			final Brotherhood old = this.brotherhoodRepository.findOne(brotherhood.getId());
			result = (Brotherhood) this.actorService.save(brotherhood);
		}
		return result;
	}

	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		Assert.isTrue(this.findByPrincipal().equals(brotherhood));
		Assert.isTrue(brotherhood.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == brotherhood.getId(), "You only can edit your info");
		Assert.isTrue(this.brotherhoodRepository.exists(brotherhood.getId()));
		this.brotherhoodRepository.delete(brotherhood);
	}

	/* ========================= OTHER METHODS =========================== */

	public Brotherhood findByPrincipal() {
		final UserAccount user = LoginService.getPrincipal();
		Assert.notNull(user);

		final Brotherhood brotherhood = this.findByUserId(user.getId());
		Assert.notNull(brotherhood);
		final boolean bool = this.actorService.checkAuthority(brotherhood, Authority.BROTHERHOOD);
		Assert.isTrue(bool);

		return brotherhood;
	}

	public Brotherhood findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Brotherhood brotherhood = this.brotherhoodRepository.findByUserId(id);
		return brotherhood;
	}

	public Collection<Brotherhood> findAllMyBrotherHoodByMember() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.MEMBER));
		final Collection<Brotherhood> all = this.brotherhoodRepository.findAllBrotherHoodByMember(principal.getUserAccount().getId());
		return all;
	}

	public void areaSet(final Area area) {
		Assert.notNull(area);
		final Brotherhood principal = this.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.BROTHERHOOD));
		Assert.isTrue(principal.getArea() == null);
		principal.setArea(area);
		this.save(principal);
	}

	public Collection<Brotherhood> AllBrotherhoodsFree() {
		final List<Brotherhood> all = this.brotherhoodRepository.findAll();
		final List<Brotherhood> ocupadas = (List<Brotherhood>) this.findAllMyBrotherHoodByMember();
		all.removeAll(ocupadas);
		return all;
	}

	public Collection<Brotherhood> brotherhoodsHasBelonged() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.MEMBER));
		final Collection<Brotherhood> res = this.brotherhoodRepository.brotherhoodsHasBelonged(principal.getUserAccount().getId());
		return res;
	}

	public Brotherhood reconstruct(final BrotherhoodForm brotherhoodForm) {
		Brotherhood brotherhood;
		if (brotherhoodForm.getId() == 0) {
			brotherhood = new Brotherhood();
			brotherhood.setName(brotherhoodForm.getName());
			brotherhood.setMiddleName(brotherhoodForm.getMiddleName());
			brotherhood.setSurname(brotherhoodForm.getSurname());
			brotherhood.setPhoto(brotherhoodForm.getPhoto());
			brotherhood.setPhone(brotherhoodForm.getPhone());
			brotherhood.setEmail(brotherhoodForm.getEmail());
			brotherhood.setAddress(brotherhoodForm.getAddress());
			brotherhood.setScore(0.0);
			brotherhood.setSpammer(false);
			brotherhood.setTitle(brotherhoodForm.getTitle());
			brotherhood.setPictures(brotherhoodForm.getPictures());
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.BROTHERHOOD);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(brotherhoodForm.getUserAccountuser());
			account.setPassword(brotherhoodForm.getUserAccountpassword());
			brotherhood.setUserAccount(account);
		} else {
			brotherhood = this.brotherhoodRepository.findOne(brotherhoodForm.getId());
			brotherhood.setName(brotherhoodForm.getName());
			brotherhood.setMiddleName(brotherhoodForm.getMiddleName());
			brotherhood.setSurname(brotherhoodForm.getSurname());
			brotherhood.setPhoto(brotherhoodForm.getPhoto());
			brotherhood.setPhone(brotherhoodForm.getPhone());
			brotherhood.setEmail(brotherhoodForm.getEmail());
			brotherhood.setAddress(brotherhoodForm.getAddress());
			brotherhood.setTitle(brotherhoodForm.getTitle());
			brotherhood.setPictures(brotherhoodForm.getPictures());
			final UserAccount account = this.userAccountService.findOne(brotherhood.getUserAccount().getId());
			account.setUsername(brotherhoodForm.getUserAccountuser());
			account.setPassword(brotherhoodForm.getUserAccountpassword());
			brotherhood.setUserAccount(account);
		}
		return brotherhood;

	}

	public Double[] getStatisticsOfMembersPerBrotherhood() {
		final Double[] result = this.brotherhoodRepository.getStatisticsOfMembersPerBrotherhood();
		Assert.notNull(result);
		return result;
	}

	public List<Brotherhood> getSmallestBrotherhood() {
		final Integer[] bs = this.brotherhoodRepository.getSmallestBrotherhood();
		Assert.notNull(bs);
		final List<Brotherhood> result = new ArrayList<Brotherhood>();
		for (final Integer id : bs)
			result.add(this.findOne(id));
		return result;
	}

	public List<Brotherhood> getLargestBrotherhood() {
		final Integer[] bs = this.brotherhoodRepository.getLargestBrotherhood();
		Assert.notNull(bs);
		final List<Brotherhood> result = new ArrayList<Brotherhood>();
		for (final Integer id : bs)
			result.add(this.findOne(id));
		return result;
	}

	public Brotherhood reconstruct2(final BrotherhoodAreaForm brotherhoodAreaForm, final BindingResult binding) {
		Brotherhood result;
		Assert.isTrue(brotherhoodAreaForm.getId() != 0);

		result = this.findOne(brotherhoodAreaForm.getId());

		result.setId(brotherhoodAreaForm.getId());
		result.setVersion(brotherhoodAreaForm.getVersion());
		result.setArea(brotherhoodAreaForm.getArea());

		this.validator.validate(result, binding);

		return result;
	}
}
