
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import controllers.AbstractController;
import domain.ConfigurationParameters;

@Controller
@RequestMapping(value = "/configurationParameters/administrator")
public class ConfigurationParametersAdministratorController extends AbstractController {

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	//To open the view to edit-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		ModelAndView result;
		ConfigurationParameters configurationParameters;

		configurationParameters = this.configurationParametersService.find();

		Assert.notNull(configurationParameters);

		result = this.createEditModelAndView(configurationParameters);

		return result;

	}

	//To save what has been edited-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final domain.ConfigurationParameters configurationParameters, final BindingResult binding) {

		ModelAndView result = new ModelAndView("configurationParameters/edit");

		if (binding.hasErrors())
			//result = this.createEditModelAndView(configurationParameters);
			result.addObject("errors", binding.getFieldErrors());
		else
			try {
				this.configurationParametersService.save(configurationParameters);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(configurationParameters, "configurationSystem.commit.error");
			}

		return result;

	}

	//Ancillary methods------------------

	protected ModelAndView createEditModelAndView(final ConfigurationParameters cParameters) {

		Assert.notNull(cParameters);
		ModelAndView result;
		result = this.createEditModelAndView(cParameters, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ConfigurationParameters cParameters, final String messageCode) {
		Assert.notNull(cParameters);

		ModelAndView result;

		result = new ModelAndView("configurationParameters/edit");
		result.addObject("configurationParameters", cParameters);
		result.addObject("message", messageCode);
		result.addObject("RequestURI", "configurationParameters/administrator/edit.do");

		return result;

	}
}
