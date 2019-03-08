
package controllers.administrator;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import controllers.AbstractController;
import domain.Area;

@Controller
@RequestMapping(value = "/area/administrator")
public class AreaAdministratorController extends AbstractController {

	@Autowired
	private AreaService	areaService;


	//Listing -----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;

		final List<Area> areas = (List<Area>) this.areaService.findAll();
		Assert.notNull(areas);

		result = new ModelAndView("area/list"); //lleva al list.jsp
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/administrator/list.do");

		return result;

	}
	//To open the view to edit-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId) {
		ModelAndView result;

		final Area area = this.areaService.findOne(areaId);
		Assert.notNull(area);

		result = this.createEditModelAndView(area);

		return result;

	}

	//To save what has been edited-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Area area, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(area);
		else
			try {

				this.areaService.save(area);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				if (oops.getMessage().equals("The logged actor is not an admin"))
					result = this.createEditModelAndView(area, "area.not.admin");
				else
					result = this.createEditModelAndView(area, "area.commit.error");

			}
		return result;

	}

	// To create ----------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Area area;

		area = this.areaService.create();

		result = this.createEditModelAndView(area); //nos redirige al formulario de editar pero con el formulario vacio

		return result;

	}

	// To delete -------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Area area, final BindingResult binding) {

		ModelAndView result;

		try {
			this.areaService.delete(area);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("Not possible to delete a settled area"))
				result = this.createEditModelAndView(area, "area.settled");
			else
				result = this.createEditModelAndView(area, "area.commit.error");

		}

		return result;
	}

	//To display -----------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int areaId) {
		ModelAndView result;

		final Area area = this.areaService.findOne(areaId);
		Assert.notNull(area);

		result = new ModelAndView("area/display");
		result.addObject("area", area);

		return result;
	}

	//Ancillary methods------------------

	protected ModelAndView createEditModelAndView(final Area area) {

		Assert.notNull(area);
		ModelAndView result;
		result = this.createEditModelAndView(area, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area, final String messageCode) {
		Assert.notNull(area);

		ModelAndView result;

		result = new ModelAndView("area/edit");
		result.addObject("area", area);
		result.addObject("message", messageCode);
		result.addObject("RequestURI", "area/administrator/edit.do");

		return result;

	}
}
