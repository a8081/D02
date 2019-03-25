
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SponsorshipService;
import domain.Parade;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		if (parade != null && parade.getMode().equals("FINAL")) {
			result = new ModelAndView("parade/display");
			result.addObject("parade", parade);

			final String imgbanner = this.sponsorshipService.findRandomSponsorship(paradeId).getBanner();
			result.addObject("imgbanner", imgbanner);
			result.addObject("lang", this.lang);
			final String targetpage = this.sponsorshipService.findRandomSponsorship(paradeId).getTargetPage();
			result.addObject("targetpage", targetpage);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAllFinalMode();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/list.do");

		return result;
	}

	@RequestMapping(value = "/listByBrotherhood", method = RequestMethod.GET)
	public ModelAndView listByBrotherhood(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAllFinalModeByBrotherhood(brotherhoodId);
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/list.do");

		return result;
	}
}
