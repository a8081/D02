
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorshipService;
import controllers.AbstractController;

@Controller
@RequestMapping(value = "/sponsorship/administrator")
public class SponsorshipAdministratorController extends AbstractController {

	@Autowired
	private SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/launchDeactivate", method = RequestMethod.GET)
	public ModelAndView launchDeactivate() {
		ModelAndView result;

		this.sponsorshipService.deactivateExpiredCreditCard();

		result = new ModelAndView("welcome/index");
		result.addObject("alert", "deactivated.sponsorhips");

		return result;

	}
}
