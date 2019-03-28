
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
		final Parade parade = this.paradeService.findOne(paradeId);

		// String lang = LocaleContextHolder.getLocale().getLanguage();

		segment = this.segmentService.create();
		if (parade.getStatus().equals("DEFAULT")) {
			result = this.createEditModelAndView(segment, paradeId);
			result.addObject("segment", segment);
			result.addObject("paradeId", paradeId);

		} else {
			result = new ModelAndView("segment/error");
			result.addObject("ok", "Cannot create a new segment in parade whose status is not DEFAULT.");
		}
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
	public ModelAndView list(@RequestParam final int paradeId) {
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
	public ModelAndView edit(@RequestParam final int segmentId, @RequestParam final int paradeId) {
		ModelAndView result;
		Segment segment;

		segment = this.segmentService.findOne(segmentId);

		if (segment != null) {
			result = this.createEditModelAndView(segment, paradeId);
			result.addObject("paradeId", paradeId);
			result.addObject("segment", segment);

		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}
	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Segment segment, final HttpServletRequest request, final BindingResult binding) {
		ModelAndView result;
		String paramParadeId;
		Integer paradeId;

		paramParadeId = request.getParameter("paradeId");
		paradeId = paramParadeId.isEmpty() ? null : Integer.parseInt(paramParadeId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(segment, paradeId);
		else
			try {
				System.out.println(segment);
				this.segmentService.save(segment, paradeId);
				result = this.paradeBrotherhoodController.display(paradeId);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(segment, paradeId, "general.commit.error");
			}
		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// DELETE

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Segment segment, final int paradeId, final BindingResult binding) {
		ModelAndView result;

		this.segmentService.delete(segment, paradeId);
		result = this.paradeBrotherhoodController.display(paradeId);

		return result;

	}

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

		if (!segments.isEmpty())
			if (segment.getId() == 0) {
				final Segment lastSegment = segments.get(segments.size() - 1);
				final Date originTime = lastSegment.getDestinationTime();
				final GPS originCoordinates = lastSegment.getDestinationCoordinates();
				result.addObject("segment", segment);
				result.addObject("lastSegment", lastSegment);
				result.addObject("suggestOriginTime", originTime);
				result.addObject("suggestOriginCoordinates", originCoordinates);

			} else {
				final Segment lastSegment = segments.get(segments.size() - 1);
				final Date originTime = lastSegment.getOriginTime();
				final GPS originCoordinates = lastSegment.getOriginCoordinates();
				final Boolean isLastSegment = segment.equals(lastSegment);
				result.addObject("segment", segment);
				result.addObject("lastSegment", lastSegment);
				result.addObject("suggestOriginTime", originTime);
				result.addObject("suggestOriginCoordinates", originCoordinates);
				result.addObject("isLastSegment", isLastSegment);

			}

		result.addObject("segment", segment); // this.constructPruned(parade));

		result.addObject("message", messageCode);

		return result;
	}
}
