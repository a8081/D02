
package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Procession;

@Controller
@RequestMapping("/procession/member")
public class ProcessionMemberController extends AbstractController {

	@Autowired
	private ProcessionService				processionService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int processionId) {
		final ModelAndView result;
		Procession procession;

		procession = this.processionService.findOne(processionId);
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		if (procession != null && procession.getMode().equals("FINAL")) {
			result = new ModelAndView("procession/display");
			result.addObject("procession", procession);
			result.addObject("rol", "member");
			result.addObject("lang", lang);

		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Procession> processions;
		Collection<Procession> memberProcessions;

		processions = this.processionService.findAllFinalMode();
		memberProcessions = this.processionService.findAllByPrincipal();
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("procession/list");
		result.addObject("processions", processions);
		result.addObject("lang", lang);
		result.addObject("rol", "member");
		result.addObject("memberProcessions", memberProcessions);
		result.addObject("requetURI", "procession/member/list.do");

		return result;
	}

}
