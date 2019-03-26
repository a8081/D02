
package controllers.chapter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ProclaimService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim/chapter")
public class ProclaimChapterController extends AbstractController {

	@Autowired
	private ProclaimService	proclaimService;

	@Autowired
	private ChapterService	chapterService;


	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Proclaim> proclaims;
		final Chapter chapter = this.chapterService.findByPrincipal();
		proclaims = this.proclaimService.getChapterProclaims(chapter.getUserAccount().getId());

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("proclaim/list");
		result.addObject("proclaims", proclaims);
		result.addObject("lang", lang);
		result.addObject("requestURI", "proclaim/chapter/list.do");

		return result;
	}

	// CONSTRUCTOR -----------------------------------------------------------

	public ProclaimChapterController() {
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
