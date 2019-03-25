
package controllers.sponsor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/parade/sponsor")
public class ParadeSponsorController extends AbstractController {

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Parade> parades;
		Collection<Parade> myParades;
		final Sponsor s = this.sponsorService.findByPrincipal();

		parades = this.paradeService.findAllAccepted();
		myParades = this.paradeService.findAllParadeBySponsor(s);

		result = new ModelAndView("parade/list");
		result.addObject("sponsorparades", myParades);
		result.addObject("parades", parades);
		result.addObject("lang", this.lang);
		result.addObject("rol", "sponsor");
		result.addObject("requetURI", "parade/sponsor/listSponsorParade.do");

		return result;

	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		if (parade != null && parade.getMode().equals("FINAL")) {
			result = new ModelAndView("parade/display");
			result.addObject("parade", parade);
			result.addObject("rol", "sponsor");
			final String imgbanner = this.sponsorshipService.findRandomSponsorship(paradeId).getBanner();
			result.addObject("imgbanner", imgbanner);
			final String targetpage = this.sponsorshipService.findRandomSponsorship(paradeId).getTargetPage();
			result.addObject("targetpage", targetpage);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

}
