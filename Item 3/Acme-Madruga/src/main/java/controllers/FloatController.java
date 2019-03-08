
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FloatService;
import domain.Float;

@Controller
@RequestMapping("/float")
public class FloatController extends AbstractController {

	@Autowired
	private FloatService	floatService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("float/list");
		final Collection<domain.Float> floats = this.floatService.findByBrotherhoodPrincipal();
		result.addObject("floats", floats);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int floatId) {
		final ModelAndView result = new ModelAndView("float/display");
		final domain.Float f = this.floatService.findOne(floatId);
		result.addObject("f", f);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		final ModelAndView result = new ModelAndView("float/edit");
		final domain.Float f = this.floatService.findOne(floatId);
		result.addObject("f", f);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid Float f, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			/*
			 * final List<ObjectError> errors = new ArrayList<>();
			 * for (final ObjectError error : binding.getAllErrors())
			 * errors.add(new ObjectError("f", error.getDefaultMessage()));
			 */
			result = new ModelAndView("float/edit");
			result.addObject("errors", binding.getAllErrors());
		} else {
			result = new ModelAndView("float/display");
			f = this.floatService.save(f);
		}
		result.addObject("f", f);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result = new ModelAndView("float/edit");
		final Float f = this.floatService.create();
		result.addObject("f", f);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int floatId) {

		final Float f = this.floatService.findOne(floatId);
		this.floatService.delete(f);

		final ModelAndView result = new ModelAndView("float/list");
		final Collection<domain.Float> floats = this.floatService.findByBrotherhoodPrincipal();
		result.addObject("floats", floats);
		return result;
	}
}
