
package controllers.brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationParametersService;
import services.EnrolmentService;
import services.MemberService;
import services.PositionService;
import controllers.AbstractController;
import controllers.MemberController;
import domain.Actor;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;
import forms.EnrolmentForm;

@Controller
@RequestMapping("/enrolment/brotherhood")
public class EnrolmentBrotherhoodController extends AbstractController {

	@Autowired
	private EnrolmentService				enrolmentService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private MemberService					memberService;

	@Autowired
	private PositionService					positionService;

	@Autowired
	private MemberController				memberController;


	// CONSTRUCTOR -----------------------------------------------------------

	public EnrolmentBrotherhoodController() {
		super();
	}

	// DISPLAY PARA VISTA DE BROTHERHOOD  ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int memberId) {
		try {
			final ModelAndView result;

			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			final Member member = this.memberService.findOne(memberId);
			final Enrolment enrolment = this.enrolmentService.getEnrolment(brotherhood, member);

			final String lang = LocaleContextHolder.getLocale().getLanguage();

			if (brotherhood != null && enrolment != null) {
				result = new ModelAndView("enrolment/display");
				result.addObject("enrolment", enrolment);
				result.addObject("member", member);
				result.addObject("brotherhood", brotherhood);
				result.addObject("lang", lang);

				final String banner = this.configurationParametersService.findBanner();
				result.addObject("banner", banner);
			} else
				result = new ModelAndView("enrolment/error");
			result.addObject("ok", "No se encuentra ningún enrolment asociado");

			return result;
		} catch (final Throwable e) {
			return new ModelAndView("redirect:/misc/403.jsp");
		}
	}

	// EDIT  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int memberId) {
		ModelAndView result;
		final Actor principal = this.brotherhoodService.findByPrincipal();
		final Actor member = this.memberService.findByUserId(memberId);
		Enrolment enrolment;

		enrolment = this.enrolmentService.getEnrolment(principal, member);

		if (enrolment != null)
			result = this.createEditModelAndView(enrolment);
		else
			result = new ModelAndView("redirect:/misc/403.jsp");

		return result;
	}

	// SAVE  ---------------------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final EnrolmentForm enrolmentForm, final BindingResult binding) {
		ModelAndView result;

		final Enrolment enrolment = this.enrolmentService.reconstruct(enrolmentForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(enrolment);
		else
			try {
				this.enrolmentService.save(enrolment, enrolment.getBrotherhood().getId());
				result = this.memberController.list();
				final String banner = this.configurationParametersService.findBanner();
				result.addObject("banner", banner);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(enrolment, "enrolment.commit.error");
			}

		return result;
	}
	// ANCILLARY METHODS  ---------------------------------------------------------------		

	protected ModelAndView createEditModelAndView(final Enrolment enrolment) {
		ModelAndView result;

		result = this.createEditModelAndView(enrolment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final String messageCode) {
		final ModelAndView result;

		final Collection<Position> positions = this.positionService.findAll();
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("enrolment/edit");
		result.addObject("enrolment", this.constructPruned(enrolment));
		result.addObject("positions", positions);
		result.addObject("lang", lang);

		result.addObject("message", messageCode);

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	public EnrolmentForm constructPruned(final Enrolment enrolment) {
		final EnrolmentForm pruned = new EnrolmentForm();

		pruned.setId(enrolment.getId());
		pruned.setVersion(enrolment.getVersion());
		pruned.setPosition(enrolment.getPosition());

		return pruned;
	}
}
