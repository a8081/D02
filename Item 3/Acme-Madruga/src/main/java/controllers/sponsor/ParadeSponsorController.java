
package controllers.sponsor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SponsorService;
import controllers.AbstractController;
import domain.Parade;
import domain.Sponsor;

@Controller
@RequestMapping("/parade/sponsor")
public class ParadeSponsorController extends AbstractController {

	@Autowired
	private SponsorService	sponsorService;

	@Autowired
	private ParadeService	paradeService;

	final String			lang	= LocaleContextHolder.getLocale().getLanguage();


	@RequestMapping(value = "/listParadeNoSponsors", method = RequestMethod.GET)
	public ModelAndView listParadeNoSponsors() {
		ModelAndView result;
		Collection<Parade> parades;

		parades = this.paradeService.paradesAvailableSponsor();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/sponsor/listParadeNoSponsors.do");

		return result;

	}

	@RequestMapping(value = "/listSponsorParade", method = RequestMethod.GET)
	public ModelAndView listSponsorParade() {
		ModelAndView result;
		Collection<Parade> myParades;
		final Sponsor s = this.sponsorService.findByPrincipal();

		myParades = this.paradeService.findAllParadeBySponsor(s);

		result = new ModelAndView("parade/list");
		result.addObject("parades", myParades);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/sponsor/listSponsorParade.do");

		return result;

	}

}
