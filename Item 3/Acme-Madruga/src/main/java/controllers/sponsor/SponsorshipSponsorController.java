
package controllers.sponsor;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private SponsorshipService				sponsorshipService;

	@Autowired
	private ParadeService					paradeService;

	@Autowired
	private SponsorService					sponsorService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// =================LIST=================

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Sponsorship> sponsorships;

		sponsorships = this.sponsorshipService.findAllBySponsor();
		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);
		result.addObject("lang", this.lang);
		result.addObject("requestURI", "/sponsorship/sponsor/list.do");
		return result;

	}

	// =================CREATE=================

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		final ModelAndView result;
		Sponsorship sponsorship;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		sponsorship = this.sponsorshipService.create();
		result = this.createEditModelAndView(sponsorship);
		result.addObject("parade", parade);

		return result;
	}

	// =================EDIT=================

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		final Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		if (sponsorship != null) {
			final Collection<Sponsorship> ss = this.sponsorshipService.findAllBySponsor();
			if (ss.contains(sponsorship))
				result = this.createEditModelAndView(sponsorship);
			else
				result = new ModelAndView("redirect:/misc/403.jsp");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.reconstruct(sponsorshipForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:list.do");
			} catch (final ValidationException oops) {
				result = this.createEditModelAndView(sponsorshipForm);
			} catch (final Throwable oops) {
				String errorMessage = "sponsorship.commit.error";
				if (oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();
				result = this.createEditModelAndView(sponsorshipForm, errorMessage);
			}
		return result;
	}

	// =================REACTIVATE=================

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "reactivate")
	public ModelAndView activate(@Valid final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.reconstruct(sponsorshipForm, binding);

		try {
			this.sponsorshipService.reactivate(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}
		return result;
	}

	// =================DEACTIVATE=================

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "deactivate")
	public ModelAndView deactivate(@Valid final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.reconstruct(sponsorshipForm, binding);

		try {
			this.sponsorshipService.deactivate(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}
		return result;
	}

	// =================DISPLAY=================

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Sponsorship sponsorship;
		final Sponsor sponsor = this.sponsorService.findByPrincipal();

		sponsorship = this.sponsorshipService.findByParade(paradeId, sponsor.getUserAccount().getId());

		if (sponsorship != null) {
			result = new ModelAndView("sponsorship/display");
			result.addObject("sponsorship", sponsorship);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	//ANCILLIARY METHODS

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorship, final String messageCode) {
		final ModelAndView result;

		final Collection<Parade> parades = this.paradeService.paradesAvailableSponsor();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("parades", parades);
		//result.addObject("requestURI", "/sponsorship/sponsor/edit.do?sponsorshipId=" + sponsorship.getId());
		result.addObject("message", messageCode);
		result.addObject("makes", this.configurationParametersService.find().getCreditCardMake());
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		final ModelAndView result;
		SponsorshipForm sponsorshipForm;

		if (sponsorship.getId() == 0)
			sponsorshipForm = new SponsorshipForm();
		else
			sponsorshipForm = this.sponsorshipService.inyect(sponsorship);

		final Collection<Parade> parades = this.paradeService.paradesAvailableSponsor();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorshipForm);
		result.addObject("parades", parades);
		//result.addObject("requestURI", "/sponsorship/sponsor/edit.do?sponsorshipId=" + sponsorship.getId());
		result.addObject("makes", this.configurationParametersService.find().getCreditCardMake());
		result.addObject("message", messageCode);
		return result;
	}

}
