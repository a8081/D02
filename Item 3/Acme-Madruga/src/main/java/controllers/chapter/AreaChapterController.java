
package controllers.chapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.ChapterService;
import controllers.AbstractController;
import domain.Area;
import domain.Chapter;

@Controller
@RequestMapping("/area/chapter")
public class AreaChapterController extends AbstractController {

	@Autowired
	private AreaService		areaService;

	@Autowired
	private ChapterService	chapterService;


	//Listing -----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;

		final List<Area> areas = (List<Area>) this.areaService.findAll();
		Assert.notNull(areas);

		result = new ModelAndView("area/list"); //lleva al list.jsp
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/chapter/list.do");

		return result;

	}

	//Asign

	@RequestMapping(value = "/asign", method = RequestMethod.GET)
	public ModelAndView asign(@RequestParam final int areaId) {
		ModelAndView result;
		final Area area = this.areaService.findOne(areaId);
		final Chapter principal = this.chapterService.findByPrincipal();
		if (area != null && principal.getArea() == null) {
			this.areaService.assignAreaToChapter(area, principal.getId());
			result = this.list();
		} else {
			result = new ModelAndView("problem/error");
			result.addObject("ok", "Already assigned an area");
		}

		return result;
	}

}
