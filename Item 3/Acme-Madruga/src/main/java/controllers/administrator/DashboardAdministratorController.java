
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import services.FinderService;
import services.MemberService;
import services.PositionService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Member;
import domain.Position;
import domain.Procession;

@Controller
@RequestMapping(value = "/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	private PositionService		positionService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private MemberService		memberService;


	@RequestMapping(value = "/chart", method = RequestMethod.GET)
	public ModelAndView chart() {
		final ModelAndView result;

		final Map<Position, Long> positionsFrequency = this.positionService.getPositionsFrequency();
		Assert.notNull(positionsFrequency);
		final List<String> positions = new ArrayList<String>();
		final List<Long> frequencies = new ArrayList<Long>();
		for (final Map.Entry<Position, Long> entry : positionsFrequency.entrySet()) {
			positions.add(entry.getKey().getNameEnglish() + "/" + entry.getKey().getNameSpanish());
			frequencies.add(entry.getValue());
		}

		result = new ModelAndView("dashboard/chart"); //lleva al list.jsp
		result.addObject("positions2", positions);
		result.addObject("frequencies2", frequencies);
		result.addObject("requestURI", "dashboard/admnistrator/chart.do");

		return result;

	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ModelAndView statistics(@RequestParam(value = "id", required = false) final Integer id) {
		final ModelAndView result;

		final Double averageResults = this.finderService.getAverageFinderResults();
		final Integer maxResults = this.finderService.getMaxFinderResults();
		final Integer minResults = this.finderService.getMinFinderResults();
		final Double desviationResults = this.finderService.getDesviationFinderResults();
		final Double ratioFinders = this.finderService.getRatioEmptyFinders();
		final Double[] statisticsMembersPerBrotherhood = this.brotherhoodService.getStatisticsOfMembersPerBrotherhood();
		final List<String> smallestBrotherhood = new ArrayList<String>();
		for (final Brotherhood b : this.brotherhoodService.getSmallestBrotherhood())
			smallestBrotherhood.add(b.getName());
		final List<String> largestBrotherhood = new ArrayList<String>();
		for (final Brotherhood b : this.brotherhoodService.getLargestBrotherhood())
			largestBrotherhood.add(b.getName());
		final List<String> soon = new ArrayList<String>();
		for (final Procession p : this.processionService.getProcessionsThirtyDays())
			soon.add(p.getTitle());
		final Double requestApproved = this.requestService.findApprovedRequestRadio();
		final Double requestPending = this.requestService.findPendingRequestRadio();
		final Double requestRejected = this.requestService.findRejectedRequestRadio();
		final Double[] statisticsBrotherhoodsPerArea = this.areaService.getStatiticsBrotherhoodPerArea();
		final List<String> membersTenPercent = new ArrayList<String>();
		for (final Member m : this.memberService.getMembersTenPercent())
			membersTenPercent.add(m.getName());
		final Collection<Procession> processions = this.processionService.findAll();
		final Double ratioBrotherhoodsPerArea = this.areaService.getRatioBrotherhoodsPerArea();

		result = new ModelAndView("dashboard/statistics"); //lleva al list.jsp
		result.addObject("requestURI", "dashboard/admnistrator/statistics.do");

		result.addObject("averageMembers", statisticsMembersPerBrotherhood[0]);
		result.addObject("minMembers", statisticsMembersPerBrotherhood[2]);
		result.addObject("maxMembers", statisticsMembersPerBrotherhood[1]);
		result.addObject("desviationMembers", statisticsMembersPerBrotherhood[3]);
		result.addObject("largest", largestBrotherhood);
		result.addObject("smallest", smallestBrotherhood);

		result.addObject("soon", soon);
		result.addObject("requestsApproved", requestApproved);
		result.addObject("requestsPending", requestPending);
		result.addObject("requestsRejected", requestRejected);
		result.addObject("membersPercent", membersTenPercent);
		result.addObject("minBrotherhoods", statisticsBrotherhoodsPerArea[2]);
		result.addObject("averageBrotherhoods", statisticsBrotherhoodsPerArea[0]);
		result.addObject("maxBrotherhoods", statisticsBrotherhoodsPerArea[1]);
		result.addObject("desviationBrotherhoods", statisticsBrotherhoodsPerArea[3]);
		result.addObject("ratioBrotherhoods", ratioBrotherhoodsPerArea);
		result.addObject("averageResults", averageResults);
		result.addObject("minResults", minResults);
		result.addObject("maxResults", maxResults);
		result.addObject("desviationResults", desviationResults);
		result.addObject("ratioFinders", ratioFinders);
		result.addObject("processions", processions);

		if (id != null) {
			final Double requestProcessionApproved = this.requestService.findApprovedRequestByProcessionRadio(id);
			final Double requestProcessionPending = this.requestService.findPendingRequestByProcessionRadio(id);
			final Double requestProcessionRejected = this.requestService.findRejectedRequestByProcessionRadio(id);
			result.addObject("requestsProcessionApproved", requestProcessionApproved);
			result.addObject("requestsProcessionPending", requestProcessionPending);
			result.addObject("requestsProcessionRejected", requestProcessionRejected);
		}

		final Map<Position, Long> positionsFrequency = this.positionService.getPositionsFrequency();
		Assert.notNull(positionsFrequency);
		final List<String> positions = new ArrayList<String>();
		final List<Long> frequencies = new ArrayList<Long>();
		for (final Map.Entry<Position, Long> entry : positionsFrequency.entrySet()) {
			positions.add(entry.getKey().getNameEnglish() + "/" + entry.getKey().getNameSpanish());
			frequencies.add(entry.getValue());
		}

		result.addObject("positions2", positions);
		result.addObject("frequencies2", frequencies);

		return result;

	}

	@RequestMapping(value = "/calculate", method = RequestMethod.GET)
	public ModelAndView calculate(@RequestParam final int id) {
		final Double requestApproved = this.requestService.findApprovedRequestByProcessionRadio(id);
		final Double requestPending = this.requestService.findPendingRequestByProcessionRadio(id);
		final Double requestRejected = this.requestService.findRejectedRequestByProcessionRadio(id);
		final Collection<Procession> processions = this.processionService.findAll();

		final ModelAndView result = new ModelAndView("dashboard/statistics");
		result.addObject("requestsProcessionApproved", requestApproved);
		result.addObject("requestsProcessionPending", requestPending);
		result.addObject("requestsProcessionRejected", requestRejected);
		result.addObject("processions", processions);

		return result;

	}

}
