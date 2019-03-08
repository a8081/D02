
package controllers.brotherhood;

import java.util.Collection;
import java.util.Date;

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
import services.FloatService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Procession;
import domain.Request;
import forms.ProcessionForm;

@Controller
@RequestMapping("/procession/brotherhood")
public class ProcessionBrotherhoodController extends AbstractController {

	@Autowired
	private ProcessionService				processionService;

	@Autowired
	private RequestService					requestService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private FloatService					floatService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// CREATE

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Procession procession;

		// String lang = LocaleContextHolder.getLocale().getLanguage();

		procession = this.processionService.create();
		result = this.createEditModelAndView(procession);
		return result;
	}

	// DISPLAY

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int processionId) {
		final ModelAndView result;
		Procession procession;
		Collection<Request> requests;

		procession = this.processionService.findOne(processionId);
		requests = this.requestService.findAll();

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		final Brotherhood bro = this.brotherhoodService.findByPrincipal();

		if (procession != null && (procession.getMode().equals("FINAL") || procession.getBrotherhood() == bro)) {
			result = new ModelAndView("procession/display");
			result.addObject("procession", procession);
			result.addObject("lang", lang);
			result.addObject("rol", "brotherhood");
			result.addObject("requests", requests);

		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// LIST

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Procession> processions;
		String rol;

		processions = this.processionService.findAllByPrincipal();
		rol = "brotherhood";
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("procession/list");
		result.addObject("processions", processions);
		result.addObject("lang", lang);
		result.addObject("requetURI", "procession/brotherhood/list.do");
		result.addObject("rol", rol);

		return result;
	}

	// EDIT

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.findOne(processionId);

		final Brotherhood bro = this.brotherhoodService.findByPrincipal();

		if ((procession.getMode().equals("DRAFT") && procession.getBrotherhood() == bro))
			result = this.createEditModelAndView(procession);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Procession procession, final BindingResult binding) {
		ModelAndView result;

		// final Procession procession = this.processionService.reconstruct(pform, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				this.processionService.save(procession);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				final Date current = new Date(System.currentTimeMillis());
				if (procession.getMoment().before(current))
					result = this.createEditModelAndView(procession, "procession.date.error");
				else if (procession.getBrotherhood().getArea() == null)
					result = this.createEditModelAndView(procession, "procession.area.error");
				else if (procession.getMode().equals("FINAL"))
					result = this.createEditModelAndView(procession, "procession.mode.error");
				else if (procession.getFloats().isEmpty())
					result = this.createEditModelAndView(procession, "procession.float.error");
				else
					result = this.createEditModelAndView(procession, "procession.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/finalMode", method = RequestMethod.GET)
	public ModelAndView finalMode(@RequestParam final int processionId) {
		final ModelAndView result;
		if (this.brotherhoodService.findByPrincipal().getArea() != null) {
			this.processionService.toFinalMode(processionId);
			result = new ModelAndView("redirect:list.do");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	// DELETE

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Procession procession, final BindingResult binding) {
		ModelAndView result;

		try {
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.commit.error");
		}

		return result;

	}

	// ANCILLIARY METHODS

	protected ModelAndView createEditModelAndView(final Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Procession procession, final String messageCode) {
		Assert.notNull(procession);
		final ModelAndView result;

		result = new ModelAndView("procession/edit");
		result.addObject("procession", procession); // this.constructPruned(procession));

		result.addObject("floatsAvailable", this.floatService.findByBrotherhood(procession.getBrotherhood()));
		result.addObject("message", messageCode);

		return result;
	}

	// This method is not used because it doesn't make sense to have a pruned object in Procession
	private ProcessionForm constructPruned(final Procession procession) {
		final ProcessionForm pruned = new ProcessionForm();
		pruned.setId(procession.getId());
		pruned.setVersion(procession.getVersion());
		pruned.setTitle(procession.getTitle());
		pruned.setDescription(procession.getDescription());
		pruned.setMaxRows(procession.getMaxRows());
		pruned.setMaxColumns(procession.getMaxColumns());
		pruned.setMoment(procession.getMoment());
		pruned.setFloats(procession.getFloats());
		return pruned;
	}

}
