
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SegmentService;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping("/segment/all")
public class SegmentController extends AbstractController {

	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;


	// DISPLAY --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int segmentId) {
		final ModelAndView result;
		Segment segment;
		Parade parade;

		segment = this.segmentService.findOne(segmentId);
		parade = this.paradeService.findParadeBySegment(segmentId);

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("segment/display");

		result.addObject("segment", segment);
		result.addObject("lang", lang);
		result.addObject("parade", parade);
		result.addObject("rol", "all");

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
		result.addObject("requetURI", "segment/list.do");

		return result;
	}

}
