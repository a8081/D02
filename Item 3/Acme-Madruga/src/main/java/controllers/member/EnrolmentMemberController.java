
package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationParametersService;
import services.EnrolmentService;
import services.MemberService;
import controllers.AbstractController;
import controllers.BrotherhoodController;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment/member")
public class EnrolmentMemberController extends AbstractController {

	@Autowired
	private EnrolmentService				enrolmentService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private MemberService					memberService;

	@Autowired
	private BrotherhoodController			brotherhoodController;


	// CONSTRUCTOR -----------------------------------------------------------

	public EnrolmentMemberController() {
		super();
	}

	// CREATEEDITMODELANDVIEW -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Enrolment enrolment) {
		ModelAndView result;

		result = this.createEditModelAndView(enrolment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("enrolment/edit");
		result.addObject("enrolment", enrolment);

		result.addObject("message", messageCode);
		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}

	// CREATE  ---------------------------------------------------------------		

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int brotherhoodId) {
		ModelAndView result = new ModelAndView();
		final Member member = this.memberService.findByPrincipal();
		final Brotherhood brotherhood = this.brotherhoodService.findByUserId(brotherhoodId);
		final Enrolment enrolment = this.enrolmentService.enrole(brotherhoodId);
		result = this.brotherhoodController.allBrotherhoodsFree();
		result.addObject("enrolment", enrolment);
		result.addObject("brotherhood", brotherhood);
		result.addObject("member", member);
		return result;
	}

	// DISPLAY ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int brotherhoodId) {
		try {
			final ModelAndView result;
			final Member member = this.memberService.findByPrincipal();
			final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			final Enrolment enrolment = this.enrolmentService.getEnrolment(brotherhood, member);

			final String lang = LocaleContextHolder.getLocale().getLanguage();

			if (member != null && enrolment != null) {
				result = new ModelAndView("enrolment/display");
				result.addObject("enrolment", enrolment);
				result.addObject("brotherhood", brotherhood);
				result.addObject("member", member);
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

	// LIST MY ENROLMENTS  ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Member member = this.memberService.findByPrincipal();
		final Collection<Enrolment> enrolments;

		enrolments = this.enrolmentService.findAllByMemberId(member.getUserAccount().getId());

		final String lang = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("enrolment/list");
		result.addObject("lang", lang);
		result.addObject("enrolments", enrolments);
		result.addObject("member", member);
		result.addObject("ok", false);
		result.addObject("leave", true);
		result.addObject("requetURI", "enrolment/list.do");

		final String banner = this.configurationParametersService.findBanner();
		result.addObject("banner", banner);

		return result;
	}
}
