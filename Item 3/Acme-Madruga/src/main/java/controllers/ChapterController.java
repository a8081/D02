
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import services.ActorService;
import services.AreaService;
import services.ChapterService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Actor;
import domain.Area;
import domain.Chapter;
import forms.ChapterAreaForm;
import forms.ChapterForm;

@Controller
@RequestMapping("/chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private RegisterService		registerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private AreaService			areaService;


	// Constructors -----------------------------------------------------------
	public ChapterController() {
		super();
	}

	// CREATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ChapterForm chapterForm = new ChapterForm();
		result = new ModelAndView("chapter/edit");
		result.addObject("chapterForm", chapterForm);
		return result;
	}

	//EDIT --------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("chapter/edit");
		final Chapter chapter = this.chapterService.findByPrincipal();
		//inyectamos en el objeto formulario los dtaos del chapter logueado
		final ChapterForm chapterForm = this.registerService.inyect(chapter);
		chapterForm.setTermsAndCondicions(true);
		result.addObject("chapterForm", chapterForm);
		return result;

	}

	// SAVE ------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ChapterForm chapterForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("chapter/edit");
		Chapter chapter;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			chapterForm.setTermsAndCondicions(false);
			result.addObject("chapterForm", chapterForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(chapterForm, Authority.CHAPTER);
				chapter = this.chapterService.reconstruct(chapterForm);
				chapter.setUserAccount(ua);
				this.registerService.saveChapter(chapter, binding);
				result.addObject("alert", "brotherhood.edit.correct");
				result.addObject("chapterForm", chapterForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "brotherhood.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				chapterForm.setTermsAndCondicions(false);
				result.addObject("chapterForm", chapterForm);
			}
		return result;
	}

	//GDPR
	@RequestMapping(value = "/deletePersonalData")
	public ModelAndView deletePersonalData() {
		final Actor principal = this.actorService.findByPrincipal();
		principal.setAddress("");
		principal.setEmail("");
		principal.setMiddleName("");
		//principal.setName("");
		principal.setPhone("");
		principal.setPhoto("");
		principal.setScore(0.0);
		principal.setSpammer(false);
		//principal.setSurname("");
		final Authority ban = new Authority();
		ban.setAuthority(Authority.BANNED);
		principal.getUserAccount().getAuthorities().add(ban);
		this.actorService.save(principal);

		final ModelAndView result = new ModelAndView("redirect:../j_spring_security_logout");
		return result;
	}

	// DISPLAY  ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayPrincipal() {
		final ModelAndView result;

		final Chapter chapter = this.chapterService.findByPrincipal();

		if (chapter != null) {
			result = new ModelAndView("chapter/display");
			result.addObject("chapter", chapter);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// EDIT  ---------------------------------------------------------------		

	@RequestMapping(value = "/assignArea", method = RequestMethod.GET)
	public ModelAndView assignArea() {
		ModelAndView result;
		final Chapter principal = this.chapterService.findByPrincipal();

		if (principal != null) {
			Assert.isNull(principal.getArea(), "No puedes actualizar el área.");
			result = this.createEditModelAndView2(principal);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/assignArea", method = RequestMethod.POST, params = "saveArea")
	public ModelAndView saveArea(@Valid final ChapterAreaForm chapterAreaForm, final BindingResult binding) {
		ModelAndView result;

		final Chapter chapter = this.chapterService.reconstructChapterArea(chapterAreaForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView2(chapter);
		else
			try {
				this.chapterService.save(chapter);
				result = this.displayPrincipal();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView2(chapter, "enrolment.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Chapter chapter) {
		ModelAndView result;

		result = this.createEditModelAndView2(chapter, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Chapter chapter, final String messageCode) {
		final ModelAndView result;
		final List<Area> libres = this.areaService.findFreeOfChapter();

		result = new ModelAndView("chapter/assignArea");
		result.addObject("chapter", this.constructPruned(chapter));
		result.addObject("areas", libres);

		result.addObject("message", messageCode);

		return result;
	}

	public ChapterAreaForm constructPruned(final Chapter chapter) {
		final ChapterAreaForm pruned = new ChapterAreaForm();

		pruned.setId(chapter.getId());
		pruned.setVersion(chapter.getVersion());
		pruned.setArea(chapter.getArea());

		return pruned;
	}
}
