
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.ChapterRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Area;
import domain.Chapter;
import forms.ChapterAreaForm;
import forms.ChapterForm;

@Service
@Transactional
public class ChapterService {

	@Autowired
	private ChapterRepository	chapterRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	//@Autowired
	//private Validator			validator;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private FolderService		folderService;


	public Chapter create() {
		final Chapter chapter = new Chapter();
		this.actorService.setAuthorityUserAccount(Authority.CHAPTER, chapter);
		return chapter;
	}

	public Collection<Chapter> findAll() {
		final Collection<Chapter> result = this.chapterRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Chapter findOne(final int chapterId) {
		Assert.isTrue(chapterId != 0);
		final Chapter result = this.chapterRepository.findOne(chapterId);
		Assert.notNull(result, "Find One de chapter es nulo");
		return result;
	}

	public Chapter save(final Chapter chapter) {
		Assert.notNull(chapter);
		Chapter result;
		final Area newArea = chapter.getArea();

		if (chapter.getId() == 0) {
			this.actorService.setAuthorityUserAccount(Authority.CHAPTER, chapter);
			Assert.isTrue(newArea == null || !this.hasAnyChapterThisArea(newArea.getId()), "The selected area has been already assigned to another chapter");
			result = this.chapterRepository.save(chapter);
			this.folderService.setFoldersByDefault(result);
		} else {
			this.actorService.checkForSpamWords(chapter);
			final Actor principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == chapter.getId(), "You only can edit your info");

			final Chapter principalChapter = this.findByPrincipal();
			Assert.isTrue((principalChapter.getArea() == null || principalChapter.getArea() == newArea), "Once an area is self-assigned, it cannot be changed");
			result = (Chapter) this.actorService.save(chapter);
		}
		return result;
	}

	// TODO: delete all information but name including folders and their messages (but no as senders!!)
	public void delete(final Chapter chapter) {
		Assert.notNull(chapter);
		Assert.isTrue(this.findByPrincipal().equals(chapter));
		Assert.isTrue(chapter.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == chapter.getId(), "You only can edit your info");
		Assert.isTrue(this.chapterRepository.exists(chapter.getId()));
		this.chapterRepository.delete(chapter);
	}

	/* ========================= OTHER METHODS =========================== */

	public Chapter findByPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Chapter chapter = this.findByUserId(userAccount.getId());
		Assert.notNull(chapter);
		final boolean bool = this.actorService.checkAuthority(chapter, Authority.CHAPTER);
		Assert.isTrue(bool);

		return chapter;
	}

	public Chapter findByUserId(final int id) {
		Assert.isTrue(id != 0);
		final Chapter chapter = this.chapterRepository.findByUserId(id);
		return chapter;
	}

	public Chapter findChapterByArea(final int areaId) {
		final Chapter res = this.chapterRepository.findChapterByArea(areaId);
		Assert.notNull(res, "This area id doesn't corresponds to an assigned area, so there's no chapter to this area");
		return res;
	}

	/**
	 * Return true if this area has been assinged to any chapter, if area is free, it returns false.
	 * 
	 * @author a8081
	 * */
	public boolean hasAnyChapterThisArea(final int areaId) {
		return this.chapterRepository.hasAnyChapterThisArea(areaId);
	}

	public void assignAreaToChapter(final int areaId) {
		final Area area = this.areaService.findOne(areaId);
		Assert.notNull(area);
		final Chapter principal = this.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.CHAPTER));
		Assert.isTrue(principal.getArea() == null);
		principal.setArea(area);
		this.save(principal);
	}

	//	public Double[] getStatisticsOfMembersPerChapter() {
	//		final Double[] result = this.chapterRepository.getStatisticsOfMembersPerChapter();
	//		Assert.notNull(result);
	//		return result;
	//	}

	public Chapter reconstruct(final ChapterForm chapterForm) {
		Chapter chapter;
		if (chapterForm.getId() == 0) {
			chapter = new Chapter();
			chapter.setName(chapterForm.getName());
			chapter.setMiddleName(chapterForm.getMiddleName());
			chapter.setSurname(chapterForm.getSurname());
			chapter.setPhoto(chapterForm.getPhoto());
			chapter.setPhone(chapterForm.getPhone());
			chapter.setEmail(chapterForm.getEmail());
			chapter.setAddress(chapterForm.getAddress());
			chapter.setScore(0.0);
			chapter.setSpammer(false);
			chapter.setTitle(chapterForm.getTitle());
			final UserAccount account = this.userAccountService.create();
			final Collection<Authority> authorities = new ArrayList<>();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.CHAPTER);
			authorities.add(auth);
			account.setAuthorities(authorities);
			account.setUsername(chapterForm.getUserAccountuser());
			account.setPassword(chapterForm.getUserAccountpassword());
			chapter.setUserAccount(account);
		} else {
			chapter = this.chapterRepository.findOne(chapterForm.getId());
			chapter.setName(chapterForm.getName());
			chapter.setMiddleName(chapterForm.getMiddleName());
			chapter.setSurname(chapterForm.getSurname());
			chapter.setPhoto(chapterForm.getPhoto());
			chapter.setPhone(chapterForm.getPhone());
			chapter.setEmail(chapterForm.getEmail());
			chapter.setAddress(chapterForm.getAddress());
			chapter.setTitle(chapterForm.getTitle());
			final UserAccount account = this.userAccountService.findOne(chapter.getUserAccount().getId());
			account.setUsername(chapterForm.getUserAccountuser());
			account.setPassword(chapterForm.getUserAccountpassword());
			chapter.setUserAccount(account);
		}
		return chapter;

	}

	public Chapter reconstructChapterArea(final ChapterAreaForm chapterAreaForm, final BindingResult binding) {
		Chapter result;
		Assert.isTrue(chapterAreaForm.getId() != 0);

		result = this.findOne(chapterAreaForm.getId()); //ya que consideramos el id del objeto ChapterAreaForm como el id del chapter

		result.setId(chapterAreaForm.getId());
		result.setVersion(chapterAreaForm.getVersion());
		result.setArea(chapterAreaForm.getArea());

		//this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.chapterRepository.flush();
	}

	public Double[] getStatisticsOfParadesPerChapter() {
		final Double[] result = this.chapterRepository.getStatisticsOfParadesPerChapter();
		Assert.notNull(result);
		return result;
	}

	public Collection<Chapter> findTenPerCentMoreParadesThanAverage() {
		final Collection<Chapter> result = this.chapterRepository.findTenPerCentMoreParadesThanAverage();
		Assert.notNull(result);
		return result;
	}
}
