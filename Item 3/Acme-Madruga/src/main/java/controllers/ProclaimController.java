
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ProclaimService;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim")
public class ProclaimController extends AbstractController {

	@Autowired
	private ProclaimService	proclaimService;

	@Autowired
	private ChapterService	chapterService;


	// DISPLAY  --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int proclaimId) {
		final ModelAndView result;
		Proclaim proclaim;

		proclaim = this.proclaimService.findOne(proclaimId);

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		if (proclaim != null) {
			result = new ModelAndView("proclaim/display");
			result.addObject("proclaim", proclaim);
			result.addObject("lang", lang);
			result.addObject("rol", "chapter");

		} else
			result = new ModelAndView("proclaim.commit.error");

		return result;
	}

	// CREATE -----------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Proclaim proclaim;
		proclaim = this.proclaimService.create();
		result = this.createEditModelAndView(proclaim);

		return result;
	}

	// SAVE -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Proclaim proclaim, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(proclaim);
		else
			try {
				this.proclaimService.save(proclaim);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable e) {
				result = this.createEditModelAndView(proclaim, "proclaim.commit.error");
			}
		return result;
	}

	// CONSTRUCTOR -----------------------------------------------------------

	public ProclaimController() {
		super();
	}

	// CREATEEDITMODELANDVIEW -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Proclaim proclaim) {
		ModelAndView result;

		result = this.createEditModelAndView(proclaim, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Proclaim proclaim, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("proclaim/edit");
		result.addObject("proclaim", proclaim);

		result.addObject("message", messageCode);

		return result;
	}

}
