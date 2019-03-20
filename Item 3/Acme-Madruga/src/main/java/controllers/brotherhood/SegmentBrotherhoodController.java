
package controllers.brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationParametersService;
import services.ParadeService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping("/segment/brotherhood")
public class SegmentBrotherhoodController extends AbstractController {

	@Autowired
	private SegmentService					segmentService;

	@Autowired
	private ParadeService					paradeService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private ParadeBrotherhoodController		paradeBrotherhoodController;


	// CREATE --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Segment segment;

		// String lang = LocaleContextHolder.getLocale().getLanguage();

		segment = this.segmentService.create();
		result = this.createEditModelAndView(segment);
		return result;
	}

	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int segmentId) {
		final ModelAndView result;
		Segment segment;
		Parade parade;

		segment = this.segmentService.findOne(segmentId);
		parade = this.paradeService.findParadeBySegment(segmentId);

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		final Brotherhood bro = this.brotherhoodService.findByPrincipal();

		result = new ModelAndView("segment/display");
		result.addObject("segment", segment);
		result.addObject("lang", lang);
		result.addObject("brotherhood", bro);
		result.addObject("parade", parade);
		result.addObject("rol", "brotherhood");

		return result;
	}

	// LIST --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final int paradeId) {
		final ModelAndView result;
		final Collection<Segment> segments;
		final Parade parade = this.paradeService.findOne(paradeId);

		if (parade != null) {
			segments = this.segmentService.getPath(paradeId);
			result = new ModelAndView("segment/list");
			result.addObject("segments", segments);
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result.addObject("lang", lang);
		result.addObject("requetURI", "segment/brotherhood/list.do");

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int segmentId) {
		ModelAndView result;
		Segment segment;

		segment = this.segmentService.findOne(segmentId);

		if (segment != null)
			result = this.createEditModelAndView(segment);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Segment segment, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(segment);
		else
			try {
				final Parade parade = this.paradeService.findParadeBySegment(segment.getId());
				this.segmentService.save(segment, parade.getId());
				result = this.createEditModelAndView(segment, "segment.commit.congrat");
				//				result = this.paradeBrotherhoodController.display(parade.getId());
				final String banner = this.configurationParametersService.findBanner();
				result.addObject("banner", banner);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(segment, "segment.commit.save.error");

			}
		return result;
	}

	//	// DELETE
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(final Parade parade, final BindingResult binding) {
	//		ModelAndView result;
	//
	//		try {
	//			this.paradeService.delete(parade);
	//			result = new ModelAndView("redirect:list.do");
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(parade, "parade.commit.error");
	//		}
	//
	//		return result;
	//
	//	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Segment segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final String messageCode) {
		Assert.notNull(segment);
		final ModelAndView result;

		result = new ModelAndView("segment/edit");
		result.addObject("segment", segment); // this.constructPruned(parade));

		result.addObject("message", messageCode);

		return result;
	}

}
