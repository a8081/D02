/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import services.ActorService;
import services.AdministratorService;
import services.FolderService;
import services.UserAccountService;
import services.auxiliary.RegisterService;
import domain.Actor;
import domain.Administrator;
import forms.ActorFrom;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private UserAccountService		accountService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private RegisterService			registerService;
	@Autowired
	private FolderService			folderService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final ActorFrom actorForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("administrator/edit");
		Administrator admin;
		if (binding.hasErrors()) {
			result.addObject("errors", binding.getAllErrors());
			result.addObject("actorForm", actorForm);
		} else
			try {
				final UserAccount ua = this.accountService.reconstruct(actorForm, Authority.ADMIN);
				admin = this.administratorService.reconstruct(actorForm);
				admin.setUserAccount(ua);
				this.registerService.saveAdmin(admin, binding);
				result.addObject("alert", "administartor.edit.correct");
				result.addObject("actorForm", actorForm);
			} catch (final Throwable e) {
				if (e.getMessage().contains("username is register"))
					result.addObject("alert", "administartor.edit.usernameIsUsed");
				result.addObject("errors", binding.getAllErrors());
				result.addObject("actorForm", actorForm);
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		result = new ModelAndView("administrator/edit");
		final Administrator admin = this.administratorService.findByPrincipal();
		final ActorFrom actor = this.registerService.inyect(admin);
		actor.setTermsAndCondicions(true);
		result.addObject("actorForm", actor);
		return result;

	}

	@RequestMapping("/create")
	public ModelAndView create() {
		ModelAndView result = new ModelAndView();
		final ActorFrom admin = new ActorFrom();
		result = new ModelAndView("administrator/edit");
		result.addObject("actorForm", admin);
		return result;
	}

	@RequestMapping("/display")
	public ModelAndView display() {
		ModelAndView result = new ModelAndView();
		final Administrator admin = this.administratorService.findByPrincipal();
		result = new ModelAndView("administrator/display");
		result.addObject("administrator", admin);
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

}
