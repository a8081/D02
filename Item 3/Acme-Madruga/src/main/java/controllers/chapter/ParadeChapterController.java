
package controllers.chapter;

import java.util.Collection;

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
import services.ConfigurationParametersService;
import services.ParadeService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Parade;
import forms.ParadeChapterForm;

@Controller
@RequestMapping("/parade/chapter")
public class ParadeChapterController extends AbstractController {

	@Autowired
	private ParadeService					paradeService;

	@Autowired
	private ChapterService					chapterService;

	@Autowired
	private SponsorshipService				sponsorshipService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// LIST ALL --------------------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAll();

		String listParades;
		String rol;

		listParades = "listAll";
		rol = "chapter";

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/chapter/listAll.do");
		result.addObject("listParades", listParades);
		result.addObject("rol", rol);

		return result;
	}

	// LIST ACCEPTED --------------------------------------------------------

	@RequestMapping(value = "/listAccepted", method = RequestMethod.GET)
	public ModelAndView listAccepted() {
		final ModelAndView result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		final Collection<Parade> parades;

		parades = this.paradeService.findAllFinalModeAccepted(chapter.getArea().getId());

		String listParades;
		String rol;

		listParades = "listAccepted";
		rol = "chapter";

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/chapter/listAccepted.do");
		result.addObject("listParades", listParades);
		result.addObject("rol", rol);

		return result;
	}

	// LIST REJECTED --------------------------------------------------------

	@RequestMapping(value = "/listRejected", method = RequestMethod.GET)
	public ModelAndView listRejected() {
		final ModelAndView result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		final Collection<Parade> parades;

		parades = this.paradeService.findAllFinalModeRejected(chapter.getArea().getId());

		String listParades;
		String rol;

		listParades = "listRejected";
		rol = "chapter";

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/chapter/listRejected.do");
		result.addObject("listParades", listParades);
		result.addObject("rol", rol);

		return result;
	}

	// LIST SUBMITTED --------------------------------------------------------

	@RequestMapping(value = "/listSubmitted", method = RequestMethod.GET)
	public ModelAndView listSubmitted() {
		final ModelAndView result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		final Collection<Parade> parades;

		parades = this.paradeService.findAllFinalModeSubmitted(chapter.getArea().getId());

		String listParades;
		String rol;

		listParades = "listRejected";
		rol = "chapter";
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", lang);
		result.addObject("requetURI", "parade/chapter/listSubmitted.do");
		result.addObject("listParades", listParades);
		result.addObject("rol", rol);

		return result;
	}

	// ACCEPT PARADE --------------------------------------------------------

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int paradeId) {
		final ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);

		if (parade == null || !parade.getStatus().equals("SUBMITTED")) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
			result.addObject("ok", false);
		} else {
			this.paradeService.acceptParade(paradeId);
			result = this.listAccepted();
		}

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}
	// REJECT PARADE --------------------------------------------------------

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int paradeId) {
		ModelAndView result;
		final Chapter chapter = this.chapterService.findByPrincipal();
		Parade parade;

		parade = this.paradeService.findOne(paradeId);
		if (parade == null || !parade.getStatus().equals("SUBMITTED") || parade.getBrotherhood().getArea() != chapter.getArea()) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
			result.addObject("ok", false);
		} else
			result = this.createEditModelAndView(parade);
		return result;
	}

	// DISPLAY  --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		final Chapter chapter = this.chapterService.findByPrincipal();

		if (parade != null && (parade.getMode().equals("FINAL") || chapter.getArea() == parade.getBrotherhood().getArea())) {
			result = new ModelAndView("parade/display");
			result.addObject("parade", parade);
			result.addObject("lang", lang);
			result.addObject("rol", "chapter");
			final String imgbanner = this.sponsorshipService.findRandomSponsorship(paradeId).getBanner();
			result.addObject("imgbanner", imgbanner);
			final String targetpage = this.sponsorshipService.findRandomSponsorship(paradeId).getTargetPage();
			result.addObject("targetpage", targetpage);
		} else
			result = new ModelAndView("parade.commit.error");

		return result;
	}

	// EDIT  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		if (parade != null)
			result = this.createEditModelAndView(parade);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ParadeChapterForm paradeChapterForm, final BindingResult binding) {
		ModelAndView result;

		final Parade parade = this.paradeService.reconstruct2(paradeChapterForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {
				this.paradeService.save(parade);
				result = this.listRejected();
				final String banner = this.configurationParametersService.findBanner();
				result.addObject("banner", banner);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(parade, "parade.commit.save.error");
				result.addObject("ok", false);

			}

		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Parade parade) {
		return this.createEditModelAndView(parade, null);
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("parade/edit2");
		result.addObject("parade", this.constructPruned(parade));
		result.addObject("message", messageCode);
		result.addObject("ok", true);

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	public ParadeChapterForm constructPruned(final Parade parade) {
		final ParadeChapterForm pruned = new ParadeChapterForm();

		pruned.setId(parade.getId());
		pruned.setVersion(parade.getVersion());
		pruned.setStatus(parade.getStatus());
		pruned.setRejectionReason(parade.getRejectionReason());

		return pruned;
	}

}
