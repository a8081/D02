
package controllers.chapter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Parade;

@Controller
@RequestMapping("/parade/chapter")
public class ParadeChapterController extends AbstractController {

	@Autowired
	private ParadeService	paradeService;

	@Autowired
	private ChapterService	chapterService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		final Collection<Parade> paradesAccepted;
		final Collection<Parade> paradesRejected;
		final Collection<Parade> paradesSubmitted;

		paradesAccepted = this.paradeService.findAllFinalModeAccepted(chapter.getArea().getId());
		paradesRejected = this.paradeService.findAllFinalModeRejected(chapter.getArea().getId());
		paradesSubmitted = this.paradeService.findAllFinalModeSubmitted(chapter.getArea().getId());

		String rol;

		rol = "chapter";
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("paradesAccepted", paradesAccepted);
		result.addObject("paradesRejected", paradesRejected);
		result.addObject("paradesSubmitted", paradesSubmitted);

		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/brotherhood/list.do");
		result.addObject("rol", rol);

		return result;
	}

}
