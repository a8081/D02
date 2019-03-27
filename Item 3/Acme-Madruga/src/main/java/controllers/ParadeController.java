
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ParadeService;
import services.SegmentService;
import services.SponsorshipService;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;
import domain.Sponsorship;

@Controller
@RequestMapping("/parade/all")
public class ParadeController extends AbstractController {

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private SegmentService		segmentService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	final String				lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Parade parade;
		Collection<Segment> segments;

		parade = this.paradeService.findOne(paradeId);
		segments = this.segmentService.getPath(parade.getId());

		if (parade != null && parade.getMode().equals("FINAL")) {
			result = new ModelAndView("parade/display");
			result.addObject("parade", parade);
			result.addObject("segments", segments);
			result.addObject("rol", "all");
			result.addObject("lang", this.lang);
			final Sponsorship sp = this.sponsorshipService.findRandomSponsorship(paradeId);
			if (sp != null) {
				final String imgbanner = sp.getBanner();
				result.addObject("imgbanner", imgbanner);
				final String targetpage = sp.getTargetPage();
				result.addObject("targetpage", targetpage);
			}
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
		result.addObject("button", false);
		result.addObject("rol", "all");
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/all/list.do");

		return result;
	}

	@RequestMapping(value = "/listByBrotherhood", method = RequestMethod.GET)
	public ModelAndView listByBrotherhood(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		final Collection<Parade> parades;
		final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);

		parades = this.paradeService.findAllFinalModeByBrotherhood(brotherhoodId);
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("button", true);
		result.addObject("brotherhood", brotherhood);
		result.addObject("rol", "all");
		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/all/list.do");

		return result;
	}
}
