
package controllers;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import services.ActorService;
import services.FolderService;
import services.SponsorService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Actor;
import domain.Sponsor;
import forms.ActorFrom;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController {

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private RegisterService		registerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private FolderService		folderService;


	// Constructors -----------------------------------------------------------
	public SponsorController() {
		super();
	}

	// Create -----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ActorFrom sponsor = new ActorFrom();
		result = new ModelAndView("sponsor/edit");
		result.addObject("actorForm", sponsor);
		return result;
	}

	// Display -----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Sponsor sponsor = this.sponsorService.findByPrincipal();
		if (sponsor != null) {
			result = new ModelAndView("sponsor/display");
			result.addObject("sponsor", sponsor);
			result.addObject("displayButtons", true);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;

	}

	@RequestMapping(value = "/displayTabla", method = RequestMethod.GET)
	public ModelAndView displayTabla(@RequestParam final int sponsorId) {
		final ModelAndView result;

		Sponsor sponsor;
		sponsor = this.sponsorService.findOne(sponsorId);

		if (sponsor != null) {
			final int principal = this.actorService.findByPrincipal().getId();
			result = new ModelAndView("sponsor/display");
			result.addObject("sponsor", sponsor);
			final boolean displayButtons = principal == sponsor.getId();
			result.addObject("displayButtons", displayButtons);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// Save -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorFrom actorForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("sponsor/edit");
		Sponsor sponsor;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			actorForm.setTermsAndCondicions(false);
			result.addObject("actorForm", actorForm);
		} else
			try {
				final UserAccount ua = this.userAccountService.reconstruct(actorForm, Authority.SPONSOR);
				sponsor = this.sponsorService.reconstruct(actorForm, binding);
				sponsor.setUserAccount(ua);
				this.registerService.saveSponsor(sponsor, binding);
				result.addObject("alert", "sponsor.edit.correct");
				result.addObject("actorForm", actorForm);
			} catch (final ValidationException oops) {
				result = this.createEditModelAndViewForm(actorForm, null);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "sponsor.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				actorForm.setTermsAndCondicions(false);
				result.addObject("actorForm", actorForm);
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("sponsor/edit");
		final Sponsor sponsor = this.sponsorService.findByPrincipal();
		final ActorFrom actor = this.registerService.inyect(sponsor);
		actor.setTermsAndCondicions(true);
		result.addObject("actorForm", actor);
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

		this.folderService.deleteActorFolders(principal);

		final ModelAndView result = new ModelAndView("redirect:../j_spring_security_logout");
		return result;
	}

	protected ModelAndView createEditModelAndViewForm(final ActorFrom sponsor, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("sponsor/edit");
		result.addObject("actorForm", sponsor); //actorForm es el model atrtibute del form

		result.addObject("message", messageCode);

		return result;
	}

}
