
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView res;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		final InceptionRecord inceptionRecord = history.getInceptionRecord();
		final Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
		final Collection<LegalRecord> legalRecords = history.getLegalRecords();
		final Collection<LinkRecord> linkRecords = history.getLinkRecords();
		final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();

		res = new ModelAndView("history/display");
		res.addObject("history", history);
		res.addObject("inceptionRecord", inceptionRecord);
		res.addObject("periodrecords", periodRecords);
		res.addObject("legalRecord", legalRecords);
		res.addObject("linkRecords", linkRecords);
		res.addObject("miscellaneousRecords", miscellaneousRecords);

		return res;
	}

	protected ModelAndView createListModelAndView(final History history) {
		ModelAndView result;

		result = this.createListModelAndView(history, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createListModelAndView(final History history, final String message) {
		ModelAndView result;

		result = new ModelAndView("history/display");
		result.addObject("history", history);
		result.addObject("message", message);

		return result;

	}

}
