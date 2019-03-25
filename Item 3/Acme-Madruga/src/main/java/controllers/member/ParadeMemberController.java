
package controllers.member;

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
import controllers.AbstractController;
import domain.Parade;

@Controller
@RequestMapping("/parade/member")
public class ParadeMemberController extends AbstractController {

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		if (parade != null && parade.getMode().equals("FINAL")) {
			result = new ModelAndView("parade/display");
			result.addObject("parade", parade);
			result.addObject("rol", "member");
			result.addObject("lang", lang);
			final String imgbanner = this.sponsorshipService.findRandomSponsorship(paradeId).getBanner();
			result.addObject("imgbanner", imgbanner);
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
		Collection<Parade> memberparades;

		parades = this.paradeService.findAllFinalMode();
		memberparades = this.paradeService.findAllByPrincipal();
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("lang", lang);
		result.addObject("rol", "member");
		result.addObject("memberparades", memberparades);
		result.addObject("requetURI", "parade/member/list.do");

		return result;
	}

}
