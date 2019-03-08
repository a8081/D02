
package controllers;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationParametersService;
import services.EnrolmentService;
import services.MemberService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Actor;
import domain.Area;
import domain.Brotherhood;
import domain.Member;
import forms.BrotherhoodAreaForm;
import forms.BrotherhoodForm;

@Controller
@RequestMapping("/brotherhood")
public class BrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private AreaService						areaService;

	@Autowired
	private MemberService					memberService;

	@Autowired
	private EnrolmentService				enrolmentService;

	@Autowired
	private MemberController				memberController;

	@Autowired
	private RegisterService					registerService;

	@Autowired
	private UserAccountService				userAccountService;

	@Autowired
	private ActorService					actorService;


	// CONSTRUCTOR -----------------------------------------------------------

	public BrotherhoodController() {
		super();
	}

	// CREATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final BrotherhoodForm brotherhoodForm = new BrotherhoodForm();
		result = new ModelAndView("brotherhood/edit");
		result.addObject("brotherhoodForm", brotherhoodForm);
		return result;
	}
	// DISPLAY  ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayPrincipal() {
		final ModelAndView result;

		Brotherhood brotherhood;
		brotherhood = this.brotherhoodService.findByPrincipal();

		if (brotherhood != null) {
			result = new ModelAndView("brotherhood/display");
			result.addObject("brotherhood", brotherhood);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	@RequestMapping(value = "/displayTabla", method = RequestMethod.GET)
	public ModelAndView displayTabla(@RequestParam final int brotherhoodId) {
		final ModelAndView result;

		Brotherhood brotherhood;
		brotherhood = this.brotherhoodService.findOne(brotherhoodId);

		if (brotherhood != null) {
			final int principal = this.actorService.findByPrincipal().getId();
			result = new ModelAndView("brotherhood/display");
			result.addObject("brotherhood", brotherhood);
			final boolean displayButtons = principal == brotherhood.getId();
			result.addObject("displayButtons", displayButtons);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("brotherhood/edit");
		Brotherhood brotherhood;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			brotherhoodForm.setTermsAndCondicions(false);
			result.addObject("brotherhoodForm", brotherhoodForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(brotherhoodForm, Authority.BROTHERHOOD);
				brotherhood = this.brotherhoodService.reconstruct(brotherhoodForm);
				brotherhood.setUserAccount(ua);
				this.registerService.saveBrotherhood(brotherhood, binding);
				result.addObject("alert", "brotherhood.edit.correct");
				result.addObject("brotherhoodForm", brotherhoodForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "brotherhood.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				brotherhoodForm.setTermsAndCondicions(false);
				result.addObject("brotherhoodForm", brotherhoodForm);
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("brotherhood/edit");
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final BrotherhoodForm actor = this.registerService.inyect(brotherhood);
		actor.setTermsAndCondicions(true);
		result.addObject("brotherhoodForm", actor);
		return result;

	}

	// LIST MY BROTHERHOODS  ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Member member = this.memberService.findByPrincipal();
		final Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.findAllMyBrotherHoodByMember();

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("brotherhood/list");
		result.addObject("lang", lang);
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("member", member);
		result.addObject("leave", true);
		result.addObject("displayEnrolment", true);
		result.addObject("requestURI", "brotherhood/list.do");

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// LIST ALL BROTHERHOOD FREE  ---------------------------------------------------------------		

	@RequestMapping(value = "/allBrotherhoodsFree", method = RequestMethod.GET)
	public ModelAndView allBrotherhoodsFree() {
		final ModelAndView result;
		final Member member = this.memberService.findByPrincipal();
		final Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.AllBrotherhoodsFree();

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("brotherhood/list");
		result.addObject("lang", lang);
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("member", member);
		result.addObject("ok", true);
		result.addObject("requestURI", "brotherhood/allBrotherhoodsFree.do");

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// LIST ALL BROTHERHOODS  ---------------------------------------------------------------		

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {
		final ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		final Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.findAll();

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("brotherhood/list");
		result.addObject("lang", lang);
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("actor", actor);
		result.addObject("requestURI", "brotherhood/listAll.do");

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// LIST BROTHERHOODS HAS BELONGED  ---------------------------------------------------------------		

	@RequestMapping(value = "/brotherhoodsHasBelonged", method = RequestMethod.GET)
	public ModelAndView brotherhoodsHasBelonged() {
		final ModelAndView result;
		final Member member = this.memberService.findByPrincipal();
		final Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodService.brotherhoodsHasBelonged();

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("brotherhood/list");
		result.addObject("lang", lang);
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("member", member);
		result.addObject("requestURI", "brotherhood/brotherhoodsHasBelonged.do");

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// DROP OUT  ---------------------------------------------------------------		

	@RequestMapping(value = "/dropOut", method = RequestMethod.GET)
	public ModelAndView dropOut(@RequestParam final int memberId) {
		final ModelAndView result;
		final Member member = this.memberService.findOne(memberId);

		this.enrolmentService.dropOut(member);

		result = this.memberController.list();

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// EDIT  ---------------------------------------------------------------		

	@RequestMapping(value = "/assignArea", method = RequestMethod.GET)
	public ModelAndView assignArea() {
		ModelAndView result;
		final Brotherhood principal = this.brotherhoodService.findByPrincipal();

		if (principal != null) {
			Assert.isNull(principal.getArea(), "No puedes actualizar el área.");
			result = this.createEditModelAndView2(principal);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/assignArea", method = RequestMethod.POST, params = "saveArea")
	public ModelAndView saveArea(@Valid final BrotherhoodAreaForm brotherhoodAreaForm, final BindingResult binding) {
		ModelAndView result;

		final Brotherhood brotherhood = this.brotherhoodService.reconstruct2(brotherhoodAreaForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView2(brotherhood);
		else
			try {
				this.brotherhoodService.save(brotherhood);
				result = this.displayPrincipal();
				final String banner = this.configurationParametersService.findBanner();
				result.addObject("banner", banner);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView2(brotherhood, "enrolment.commit.error");
			}

		return result;
	}

	// ANCILLARY METHODS  ---------------------------------------------------------------		

	protected ModelAndView createEditModelAndView(final Brotherhood brotherhood) {
		ModelAndView result;

		result = this.createEditModelAndView(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Brotherhood brotherhood, final String messageCode) {
		final ModelAndView result;
		final List<Area> libres = (List<Area>) this.areaService.findAll();

		result = new ModelAndView("brotherhood/edit");
		result.addObject("brotherhood", brotherhood);
		result.addObject("areas", libres);

		result.addObject("message", messageCode);
		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Brotherhood brotherhood) {
		ModelAndView result;

		result = this.createEditModelAndView2(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Brotherhood brotherhood, final String messageCode) {
		final ModelAndView result;
		final List<Area> libres = (List<Area>) this.areaService.findAll();

		result = new ModelAndView("brotherhood/edit2");
		result.addObject("brotherhood", this.constructPruned(brotherhood));
		result.addObject("areas", libres);

		result.addObject("message", messageCode);
		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	public BrotherhoodAreaForm constructPruned(final Brotherhood brotherhood) {
		final BrotherhoodAreaForm pruned = new BrotherhoodAreaForm();

		pruned.setId(brotherhood.getId());
		pruned.setVersion(brotherhood.getVersion());
		pruned.setArea(brotherhood.getArea());

		return pruned;
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
}
