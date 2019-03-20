
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import domain.GPS;
import domain.Parade;
import domain.Segment;
import forms.SegmentForm;

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
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		Segment segment;

		// String lang = LocaleContextHolder.getLocale().getLanguage();

		segment = this.segmentService.create();
		result = this.createEditModelAndView(segment, paradeId);
		result.addObject("paradeId", paradeId);
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
	public ModelAndView edit(@RequestParam final int segmentId, final int paradeId) {
		ModelAndView result;
		Segment segment;

		segment = this.segmentService.findOne(segmentId);

		if (segment != null)
			result = this.createEditModelAndView(segment, paradeId);

		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SegmentForm segmentForm, final BindingResult binding) {
		ModelAndView result;
		final int paradeId = segmentForm.getParadeId();

		final Segment segment = this.segmentService.reconstruct(segmentForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(segment, paradeId);
		else {
			//			try {
			this.segmentService.save(segment, paradeId);
			//				result = this.createEditModelAndView(segment, "segment.commit.congrat");
			result = this.paradeBrotherhoodController.display(paradeId);

			//			} catch (final Throwable oops) {
			//				result = this.createEditModelAndView(segment, "segment.commit.save.error");
			//
		}
		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

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

	protected ModelAndView createEditModelAndView(final Segment segment, final int paradeId) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, paradeId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final int paradeId, final String messageCode) {
		Assert.notNull(segment);
		final ModelAndView result;

		result = new ModelAndView("segment/edit");

		List<Segment> segments = new ArrayList<>();
		final Parade parade = this.paradeService.findOne(paradeId);
		segments = parade.getSegments();

		if (segment.getId() == 0 && !segments.isEmpty()) {
			final Segment lastSegment = segments.get(segments.size() - 1);
			final Date originTime = lastSegment.getDestinationTime();
			final GPS originCoordinates = lastSegment.getDestinationCoordinates();
			result.addObject("suggestOriginTime", originTime);
			result.addObject("suggestOriginCoordinates", originCoordinates);
		}

		result.addObject("segment", this.constructSegmentForm(segment, paradeId)); // this.constructPruned(parade));

		result.addObject("message", messageCode);

		return result;
	}

	public SegmentForm constructSegmentForm(final Segment segment, final int paradeId) {
		final SegmentForm segmentForm = new SegmentForm();
		segmentForm.setId(segment.getId());
		segmentForm.setVersion(segment.getVersion());
		segmentForm.setOriginTime(segment.getOriginTime());
		segmentForm.setDestinationTime(segment.getDestinationTime());
		segmentForm.setOriginCoordinates(segment.getOriginCoordinates());
		segmentForm.setDestinationCoordinates(segment.getDestinationCoordinates());
		segmentForm.setParadeId(paradeId);

		return segmentForm;

	}
}
