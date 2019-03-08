
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

import services.ConfigurationParametersService;
import services.PositionService;
import controllers.AbstractController;
import domain.ConfigurationParameters;
import domain.Position;

@Controller
@RequestMapping(value = "/position/administrator")
public class PositionAdministratorController extends AbstractController {

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private PositionService					positionService;


	//Listing -----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final ConfigurationParameters configurationParameters;

		//configurationParameters = this.configurationParametersService.findOne(1121);
		//Assert.notNull(configurationParameters);
		//final List<Position> positions = (List<Position>) configurationParameters.getPositionList();
		final List<Position> positions = (List<Position>) this.positionService.findAll();
		Assert.notNull(positions);

		result = new ModelAndView("position/list"); //lleva al list.jsp
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/administrator/list.do");

		return result;

	}
	//To open the view to edit-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		ModelAndView result;

		final Position position = this.positionService.findOne(positionId);
		Assert.notNull(position);

		result = this.createEditModelAndView(position);

		return result;

	}

	//To save what has been edited-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Position position, final BindingResult binding) {

		ModelAndView result = null;

		if (binding.hasErrors()) //errores de validación
			result = this.createEditModelAndView(position);
		else
			try {

				this.positionService.save(position);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				if (oops.getMessage().equals("This spanish name already exists"))
					result = this.createEditModelAndView(position, "position.duplicate.spanish");
				else if (oops.getMessage().equals("This english name already exists"))
					result = this.createEditModelAndView(position, "position.duplicate.english");
				else if (oops.getMessage().equals("Position cannot be a spam word"))
					result = this.createEditModelAndView(position, "position.spam");
				//else
				//result = this.createEditModelAndView(position, "position.commit.error");

			}
		return result;

	}

	// To create ----------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Position position;

		position = this.positionService.create();

		result = this.createEditModelAndView(position); //nos redirige al formulario de editar pero con el formulario vacio

		return result;

	}

	// To delete -------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Position position, final BindingResult binding) {

		ModelAndView result;

		try {
			this.positionService.delete(position);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("Not possible to delete a position in use"))
				result = this.createEditModelAndView(position, "position.used");
			else if (oops.getMessage().equals("Position id cannot be 0"))
				result = this.createEditModelAndView(position, "position.id.zero");
			else if (oops.getMessage().equals("Logged user is not an admin"))
				result = this.createEditModelAndView(position, "position.not.admin");
			else if (oops.getMessage().equals("Position cannot be null"))
				result = this.createEditModelAndView(position, "position.null");
			else
				result = this.createEditModelAndView(position, "position.commit.error");

		}

		return result;
	}

	//Ancillary methods------------------

	protected ModelAndView createEditModelAndView(final Position position) {

		Assert.notNull(position);
		ModelAndView result;
		result = this.createEditModelAndView(position, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Position position, final String messageCode) {
		Assert.notNull(position);

		ModelAndView result;

		result = new ModelAndView("position/edit");
		result.addObject("position", position);
		result.addObject("message", messageCode);
		result.addObject("RequestURI", "position/administrator/edit.do");

		return result;

	}
}
