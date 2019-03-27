
package controllers.administrator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationParametersService;
import services.FinderService;
import services.HistoryService;
import services.MemberService;
import services.MessageService;
import services.ParadeService;
import services.PositionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Parade;
import domain.Position;

@Controller
@RequestMapping(value = "/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	private PositionService					positionService;

	@Autowired
	private FinderService					finderService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private AreaService						areaService;

	@Autowired
	private ParadeService					paradeService;

	@Autowired
	private RequestService					requestService;

	@Autowired
	private MemberService					memberService;

	@Autowired
	private HistoryService					historyService;

	@Autowired
	private ChapterService					chapterService;

	@Autowired
	private MessageService					messageService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


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
		final Collection<Brotherhood> smallestBrotherhood = this.brotherhoodService.getSmallestBrotherhood();

		final List<String> smallestBrotherhoodUsername = new ArrayList<String>();
		if (smallestBrotherhood.size() > 0)
			for (final Brotherhood b : smallestBrotherhood)
				smallestBrotherhoodUsername.add(b.getUserAccount().getUsername());

		final Collection<Brotherhood> largestBrotherhood = this.brotherhoodService.getLargestBrotherhood();
		final List<String> largestBrotherhoodUsername = new ArrayList<String>();
		if (largestBrotherhood.size() > 0)
			for (final Brotherhood b : largestBrotherhood)
				largestBrotherhoodUsername.add(b.getUserAccount().getUsername());

		final List<String> soon = new ArrayList<String>();
		for (final Parade p : this.paradeService.getParadesThirtyDays())
			soon.add(p.getTitle());
		final Double requestApproved = this.requestService.findApprovedRequestRadio();
		final Double requestPending = this.requestService.findPendingRequestRadio();
		final Double requestRejected = this.requestService.findRejectedRequestRadio();
		final Double[] statisticsBrotherhoodsPerArea = this.areaService.getStatiticsBrotherhoodPerArea();
		final List<Member> membersTenPercent = this.memberService.getMembersTenPercent();
		final List<String> membersTenPercentUsername = new ArrayList<String>();
		if (membersTenPercent.size() > 0)
			for (final Member m : membersTenPercent)
				membersTenPercentUsername.add(m.getUserAccount().getUsername());

		final Collection<Parade> parades = this.paradeService.findAll();
		final Double ratioBrotherhoodsPerArea = this.areaService.getRatioBrotherhoodsPerArea();

		result = new ModelAndView("dashboard/statistics"); //lleva al list.jsp
		result.addObject("requestURI", "dashboard/admnistrator/statistics.do");

		result.addObject("averageMembers", statisticsMembersPerBrotherhood[0]);
		result.addObject("minMembers", statisticsMembersPerBrotherhood[2]);
		result.addObject("maxMembers", statisticsMembersPerBrotherhood[1]);
		result.addObject("desviationMembers", statisticsMembersPerBrotherhood[3]);
		result.addObject("largest", largestBrotherhoodUsername);
		result.addObject("smallest", smallestBrotherhoodUsername);

		result.addObject("soon", soon);
		result.addObject("requestsApproved", requestApproved);
		result.addObject("requestsPending", requestPending);
		result.addObject("requestsRejected", requestRejected);
		result.addObject("membersPercent", membersTenPercentUsername);
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

		result.addObject("parades", parades);

		if (id != null) {
			final Double requestParadeApproved = this.requestService.findApprovedRequestByParadeRadio(id);
			final Double requestParadePending = this.requestService.findPendingRequestByParadeRadio(id);
			final Double requestParadeRejected = this.requestService.findRejectedRequestByParadeRadio(id);
			result.addObject("requestsParadeApproved", requestParadeApproved);
			result.addObject("requestsParadePending", requestParadePending);
			result.addObject("requestsParadeRejected", requestParadeRejected);
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

		/*********************** D02 *********************************/

		final Double[] statisticsRecord = this.historyService.getStatisticsOfRecordsPerHistory();
		final Collection<Brotherhood> largestBrotherhoodPerHistory = this.historyService.getLargestBrotherhoodPerHistory();
		final List<String> largestBrotherhoodPerHistoryUsername = new ArrayList<String>();
		if (largestBrotherhoodPerHistory.size() > 0)
			for (final Brotherhood b : largestBrotherhoodPerHistory)
				largestBrotherhoodPerHistoryUsername.add(b.getUserAccount().getUsername());
		final Collection<Brotherhood> brotherhoodPerHistory = this.historyService.getBrotherhoodPerHistoryLargerThanStd();
		final List<String> brotherhoodPerHistoryUsername = new ArrayList<String>();
		if (brotherhoodPerHistory.size() > 0)
			for (final Brotherhood b : brotherhoodPerHistory)
				brotherhoodPerHistoryUsername.add(b.getUserAccount().getUsername());

		result.addObject("avgStatisticsRecord", statisticsRecord[0]);
		result.addObject("minAvgStatisticsRecord", statisticsRecord[1]);
		result.addObject("maxAvgStatisticsRecord", statisticsRecord[2]);
		result.addObject("stddevAvgStatisticsRecord", statisticsRecord[3]);
		result.addObject("largestBrotherhoodPerHistory", largestBrotherhoodPerHistoryUsername);
		result.addObject("brotherhoodPerHistory", brotherhoodPerHistoryUsername);

		final Double ratioNoCoordinatedArea = this.areaService.getRatioNoCoordinatedAreas();

		result.addObject("ratioNoCoordinatedArea", ratioNoCoordinatedArea);

		final Double[] statisticsParadesByChapter = this.chapterService.getStatisticsOfParadesPerChapter();
		final Collection<Chapter> chapterTenPercent = this.chapterService.findTenPerCentMoreParadesThanAverage();
		final List<String> chapterTenPercentUsername = new ArrayList<String>();
		if (chapterTenPercent.size() > 0)
			for (final Chapter c : chapterTenPercent)
				chapterTenPercentUsername.add(c.getUserAccount().getUsername());

		result.addObject("avgStatisticsParadesByChapter", statisticsParadesByChapter[0]);
		result.addObject("minStatisticsParadesByChapter", statisticsParadesByChapter[1]);
		result.addObject("maxStatisticsParadesByChapter", statisticsParadesByChapter[2]);
		result.addObject("stddevStatisticsParadesByChapter", statisticsParadesByChapter[3]);
		result.addObject("chapterTenPercent", chapterTenPercentUsername);

		final Double ratioDraftFinalParade = this.paradeService.findRatioDraftVsFinalParades();
		final Double submittedParadeRatio = this.paradeService.findSubmittedParadesRatio();
		final Double acceptedParadeRatio = this.paradeService.findAcceptedParadesRatio();
		final Double rejectedParadeRatio = this.paradeService.findRejectedParadesRatio();

		result.addObject("ratioDraftFinalParade", ratioDraftFinalParade);
		result.addObject("submittedParadeRatio", submittedParadeRatio);
		result.addObject("acceptedParadeRatio", acceptedParadeRatio);
		result.addObject("rejectedParadeRatio", rejectedParadeRatio);

		return result;

	}

	@RequestMapping(value = "/calculate", method = RequestMethod.GET)
	public ModelAndView calculate(@RequestParam final int id) {
		final Double requestApproved = this.requestService.findApprovedRequestByParadeRadio(id);
		final Double requestPending = this.requestService.findPendingRequestByParadeRadio(id);
		final Double requestRejected = this.requestService.findRejectedRequestByParadeRadio(id);
		final Collection<Parade> parades = this.paradeService.findAll();

		final ModelAndView result = new ModelAndView("dashboard/statistics");
		result.addObject("requestsParadeApproved", requestApproved);
		result.addObject("requestsParadePending", requestPending);
		result.addObject("requestsParadeRejected", requestRejected);
		result.addObject("parades", parades);

		return result;

	}

	@RequestMapping(value = "/dataBreach", method = RequestMethod.GET)
	public ModelAndView launchDeactivate() {
		ModelAndView result;

		this.messageService.dataBreachMessage();

		result = new ModelAndView("welcome/index");
		final String lang = LocaleContextHolder.getLocale().getLanguage();

		String mensaje = null;
		if (lang.equals("en"))
			mensaje = this.configurationParametersService.findWelcomeMessageEn();
		else if (lang.equals("es"))
			mensaje = this.configurationParametersService.findWelcomeMessageEsp();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		final String moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("moment", moment);
		result.addObject("mensaje", mensaje);

		result.addObject("alert", "data.breach.notified");

		return result;

	}

}
