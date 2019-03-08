
package controllers.brotherhood;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationParametersService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Procession;
import domain.Request;

@Controller
@RequestMapping("/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	@Autowired
	private RequestService					requestService;

	@Autowired
	private ProcessionService				processionService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		final String rol = "brotherhood";

		requests = this.requestService.findAll();

		result = new ModelAndView("request/list");
		final String lang = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("lang", lang);
		result.addObject("requests", requests);
		result.addObject("rol", rol);

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	@RequestMapping(value = "/listApproved", method = RequestMethod.GET)
	public ModelAndView listApproved() {
		final ModelAndView result;
		final Collection<Request> requests;
		final String rol = "brotherhood";

		requests = this.requestService.findApprovedBrotherhood();

		result = new ModelAndView("request/list");
		final String lang = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("lang", lang);
		result.addObject("requests", requests);
		result.addObject("rol", rol);

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	@RequestMapping(value = "/listRejected", method = RequestMethod.GET)
	public ModelAndView listRejected() {
		final ModelAndView result;
		final Collection<Request> requests;
		final String rol = "brotherhood";

		requests = this.requestService.findRejectedBrotherhood();

		result = new ModelAndView("request/list");
		final String lang = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("lang", lang);
		result.addObject("requests", requests);
		result.addObject("rol", rol);

		return result;
	}

	@RequestMapping(value = "/listPending", method = RequestMethod.GET)
	public ModelAndView listPending() {
		final ModelAndView result;
		final Collection<Request> requests;
		final String rol = "brotherhood";

		requests = this.requestService.findPendingBrotherhood();

		result = new ModelAndView("request/list");
		final String lang = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("lang", lang);
		result.addObject("requests", requests);
		result.addObject("rol", rol);

		return result;
	}

	// Edition --------------------------------------------------------

	@RequestMapping(value = "/approve", method = RequestMethod.GET)
	public ModelAndView approve(@RequestParam final int requestId, @RequestParam final int processionId) {
		ModelAndView result;
		Request request;

		// access controlled in findOne method implemented by jmsx
		request = this.requestService.findOne(requestId);
		if (request == null || !request.getStatus().equals("PENDING") || this.requestService.processionRequested(processionId))
			result = new ModelAndView("redirect:/misc/403.jsp");
		else {
			result = this.createEditModelAndView(request);
			final List<Integer> ls = this.requestService.suggestPosition(this.processionService.findOne(processionId));
			result.addObject("setStatusTo", "APPROVED");
			result.addObject("suggestedRow", ls.get(0));
			result.addObject("suggestedColumn", ls.get(1));
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		// access controlled in findOne method implemented by jmsx
		request = this.requestService.findOne(requestId);

		if (request == null || !request.getStatus().equals("PENDING"))
			result = new ModelAndView("redirect:/misc/403.jsp");
		else {
			result = this.createEditModelAndView(request);
			result.addObject("setStatusTo", "REJECTED");
		}
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Request request, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(request, "request.commit.error");
		else
			try {
				result = new ModelAndView("redirect:/request/brotherhood/list.do");
				final Request retrieved = this.requestService.findOne(request.getId());
				if (!retrieved.getStatus().equals(request.getStatus()))
					this.requestService.save(request);
				else
					result = this.createEditModelAndView(request, "request.commit.error");
			} catch (final Throwable oops) {
				if (request.getStatus().equals("REJECTED") && (request.getExplanation() == null || request.getExplanation() == "")) {
					result = this.createEditModelAndView(request, "request.explanation.error");
					result.addObject("setStatusTo", "REJECTED");
				} else if (request.getStatus().equals("APPROVED") && (request.getRow() == null || request.getColumn() == null)) {
					result = this.createEditModelAndView(request, "request.rowcolumn.error");
					result.addObject("setStatusTo", "APPROVED");
				} else if (request.getStatus().equals("APPROVED") && ((request.getRow() > request.getProcession().getMaxRows()) || (request.getColumn() > request.getProcession().getMaxColumns()))) {
					result = this.createEditModelAndView(request, "request.max.error");
					result.addObject("setStatusTo", "APPROVED");
				} else if (!this.requestService.availableRowColumn(request)) {
					result = this.createEditModelAndView(request, "request.available.error");
					result.addObject("setStatusTo", "APPROVED");
				} else
					result = this.createEditModelAndView(request, "request.commit.error");
			}

		return result;
	}

	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOne(requestId);
		if (request == null)
			result = new ModelAndView("redirect:/misc/403.jsp");
		else {
			result = new ModelAndView("request/display");
			final String lang = LocaleContextHolder.getLocale().getLanguage();
			result.addObject("lang", lang);
			result.addObject("request", request);
			result.addObject("rol", "brotherhood");
		}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		return this.createEditModelAndView(request, null);
	}

	protected ModelAndView createEditModelAndView(final Request request, final String messageCode) {
		ModelAndView result;

		final Collection<Procession> processions;
		final String rol = "brotherhood";

		processions = this.processionService.findAll();

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("processions", processions);
		result.addObject("rol", rol);
		result.addObject("requestURI", "request/brotherhood/edit.do");
		result.addObject("message", messageCode);
		// the message code references an error message or null

		return result;
	}
}
