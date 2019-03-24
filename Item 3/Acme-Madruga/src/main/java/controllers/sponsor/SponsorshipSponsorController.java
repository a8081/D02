
package controllers.sponsor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorService		sponsorService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


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
	public ModelAndView create() {
		final ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.create();
		result = this.createEditModelAndView(sponsorship);

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
	public ModelAndView save(@Valid final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String errorMessage = "sponsorship.commit.error";
				if (oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();
				result = this.createEditModelAndView(sponsorship, errorMessage);
			}
		return result;
	}

	// =================REACTIVATE=================

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "reactivate")
	public ModelAndView activate(@Valid final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
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
	public ModelAndView deactivate(@Valid final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
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
			result.addObject("parade", sponsorship);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	//ANCILLIARY METHODS

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		final ModelAndView result;

		final Collection<Parade> parades = this.paradeService.paradesAvailableSponsor();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("parades", parades);
		//result.addObject("requestURI", "/sponsorship/sponsor/edit.do?sponsorshipId=" + sponsorship.getId());
		result.addObject("paradesAvailable", this.paradeService.paradesAvailable());
		result.addObject("message", messageCode);
		return result;
	}

}
