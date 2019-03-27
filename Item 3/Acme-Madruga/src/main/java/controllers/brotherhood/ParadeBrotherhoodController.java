
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
import services.ParadeService;
import services.RequestService;
import services.SegmentService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Request;
import domain.Segment;
import domain.Sponsorship;
import forms.ParadeForm;

@Controller
@RequestMapping("/parade/brotherhood")
public class ParadeBrotherhoodController extends AbstractController {

	@Autowired
	private ParadeService					paradeService;

	@Autowired
	private RequestService					requestService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private FloatService					floatService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private SegmentService					segmentService;

	@Autowired
	private SponsorshipService				sponsorshipService;

	final String							lang	= LocaleContextHolder.getLocale().getLanguage();


	// CREATE

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Parade parade;

		// String lang = LocaleContextHolder.getLocale().getLanguage();

		parade = this.paradeService.create();
		result = this.createEditModelAndView(parade);
		return result;
	}

	// DISPLAY

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		final ModelAndView result;
		Parade parade;
		Collection<Request> requests;
		final Collection<Segment> segments;

		parade = this.paradeService.findOne(paradeId);
		requests = this.requestService.findAll();
		segments = this.segmentService.getPath(paradeId);

		final Brotherhood bro = this.brotherhoodService.findByPrincipal();

		if (parade != null && (parade.getMode().equals("FINAL") || parade.getBrotherhood() == bro)) {
			result = new ModelAndView("parade/display");
			result.addObject("parade", parade);
			result.addObject("lang", this.lang);
			result.addObject("rol", "brotherhood");
			result.addObject("requests", requests);
			result.addObject("segments", segments);
			final Sponsorship sp = this.sponsorshipService.findRandomSponsorship(paradeId);
			if (sp != null) {
				final String imgbanner = sp.getBanner();
				result.addObject("imgbanner", imgbanner);
				final String targetpage = sp.getTargetPage();
				result.addObject("targetpage", targetpage);
			}

		} else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// LIST ACCEPTED --------------------------------------------------------

	@RequestMapping(value = "/listAccepted", method = RequestMethod.GET)
	public ModelAndView listAccepted() {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAllAcceptedByBrotherhood();

		String listParades;
		String rol;

		listParades = "listAccepted";
		rol = "brotherhood";

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/brotherhood/listAccepted.do");
		result.addObject("listParades", listParades);
		result.addObject("principalID", this.brotherhoodService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST REJECTED --------------------------------------------------------

	@RequestMapping(value = "/listRejected", method = RequestMethod.GET)
	public ModelAndView listRejected() {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAllRejectedByBrotherhood();

		String listParades;
		String rol;

		listParades = "listRejected";
		rol = "brotherhood";

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/brotherhood/listRejected.do");
		result.addObject("listParades", listParades);
		result.addObject("principalID", this.brotherhoodService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST SUBMITTED --------------------------------------------------------

	@RequestMapping(value = "/listSubmitted", method = RequestMethod.GET)
	public ModelAndView listSubmitted() {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAllSubmittedByBrotherhood();

		String listParades;
		String rol;

		listParades = "listSubmitted";
		rol = "brotherhood";

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/brotherhood/listSubmitted.do");
		result.addObject("listParades", listParades);
		result.addObject("principalID", this.brotherhoodService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST DEFAULT --------------------------------------------------------

	@RequestMapping(value = "/listDefault", method = RequestMethod.GET)
	public ModelAndView listDefault() {
		final ModelAndView result;
		final Collection<Parade> parades;

		parades = this.paradeService.findAllDefaultByBrotherhood();

		String listParades;
		String rol;

		listParades = "listDefault";
		rol = "brotherhood";

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);

		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/brotherhood/listDefault.do");
		result.addObject("listParades", listParades);
		result.addObject("principalID", this.brotherhoodService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// LIST

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Parade> parades;
		String rol;
		String listParades;

		parades = this.paradeService.findAllByPrincipal();

		listParades = "list";
		rol = "brotherhood";

		result = new ModelAndView("parade/list");
		result.addObject("parades", parades);
		result.addObject("lang", this.lang);
		result.addObject("requetURI", "parade/brotherhood/list.do");
		result.addObject("listParades", listParades);
		result.addObject("principalID", this.brotherhoodService.findByPrincipal().getId());
		result.addObject("rol", rol);

		return result;
	}

	// MAKE COPY --------------------------------------------------------

	@RequestMapping(value = "/copyBrotherhoodParade", method = RequestMethod.GET)
	public ModelAndView copyBrotherhoodParade(@RequestParam final int paradeId) {
		final ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);

		if (parade == null) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
			result.addObject("ok", false);
		} else {
			this.paradeService.copyBrotherhoodParade(paradeId);
			result = this.listDefault();
		}

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// EDIT --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		final Brotherhood bro = this.brotherhoodService.findByPrincipal();

		if ((parade.getMode().equals("DRAFT") && parade.getBrotherhood() == bro))
			result = this.createEditModelAndView(parade);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Parade parade, final BindingResult binding) {
		ModelAndView result;

		// final Parade parade = this.paradeService.reconstruct(pform, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {
				this.paradeService.save(parade);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				final Date current = new Date(System.currentTimeMillis());
				if (parade.getMoment().before(current))
					result = this.createEditModelAndView(parade, "parade.date.error");
				else if (parade.getBrotherhood().getArea() == null)
					result = this.createEditModelAndView(parade, "parade.area.error");
				else if (parade.getMode().equals("FINAL"))
					result = this.createEditModelAndView(parade, "parade.mode.error");
				else if (parade.getFloats().isEmpty())
					result = this.createEditModelAndView(parade, "parade.float.error");
				else
					result = this.createEditModelAndView(parade, "parade.commit.error");
			}

		return result;
	}

	// TO FINAL MODE --------------------------------------------------------

	@RequestMapping(value = "/finalMode", method = RequestMethod.GET)
	public ModelAndView finalMode(@RequestParam final int paradeId) {
		final ModelAndView result;
		final Parade parade = this.paradeService.findOne(paradeId);
		if (this.brotherhoodService.findByPrincipal().getArea() != null && parade.getStatus().equals("DEFAULT")) {
			this.paradeService.toFinalMode(paradeId);
			result = new ModelAndView("redirect:listSubmitted.do");
		} else
			result = new ModelAndView("redirect:/misc/403.jsp");
		return result;
	}

	// DELETE

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Parade parade, final BindingResult binding) {
		ModelAndView result;

		try {
			this.paradeService.delete(parade);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.commit.error");
		}

		return result;

	}

	// ANCILLIARY METHODS --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String messageCode) {
		Assert.notNull(parade);
		final ModelAndView result;

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade); // this.constructPruned(parade));

		result.addObject("floatsAvailable", this.floatService.findByBrotherhood(parade.getBrotherhood()));
		result.addObject("message", messageCode);

		return result;
	}

	// This method is not used because it doesn't make sense to have a pruned object in parade
	private ParadeForm constructPruned(final Parade parade) {
		final ParadeForm pruned = new ParadeForm();
		pruned.setId(parade.getId());
		pruned.setVersion(parade.getVersion());
		pruned.setTitle(parade.getTitle());
		pruned.setDescription(parade.getDescription());
		pruned.setMaxRows(parade.getMaxRows());
		pruned.setMaxColumns(parade.getMaxColumns());
		pruned.setMoment(parade.getMoment());
		pruned.setFloats(parade.getFloats());
		return pruned;
	}

}
