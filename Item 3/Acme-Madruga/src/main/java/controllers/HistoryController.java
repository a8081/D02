
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
		if (!(history == null)) {
			final InceptionRecord inceptionRecord = history.getInceptionRecord();
			final Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
			final Collection<LegalRecord> legalRecords = history.getLegalRecords();
			final Collection<LinkRecord> linkRecords = history.getLinkRecords();
			final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();

			res = new ModelAndView("history/display");
			res.addObject("history", history);
			res.addObject("buttons", true);
			res.addObject("inceptionRecord", inceptionRecord);
			res.addObject("periodrecords", periodRecords);
			res.addObject("legalRecord", legalRecords);
			res.addObject("linkRecords", linkRecords);
			res.addObject("miscellaneousRecords", miscellaneousRecords);
		} else {
			res = new ModelAndView("history/create");
			res.addObject("history", history);
		}

		return res;
	}

	@RequestMapping(value = "/listForAnonymous", method = RequestMethod.GET)
	public ModelAndView listForAnonymous(@RequestParam final int brotherhoodId) {
		final ModelAndView res;
		final Brotherhood brotherhood = this.brotherhoodService.findOne(brotherhoodId);
		final History history = brotherhood.getHistory();
		final InceptionRecord inceptionRecord = history.getInceptionRecord();
		final Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
		final Collection<LegalRecord> legalRecords = history.getLegalRecords();
		final Collection<LinkRecord> linkRecords = history.getLinkRecords();
		final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();

		res = new ModelAndView("history/display");
		res.addObject("brotherhood", brotherhood);
		res.addObject("buttons", false);
		res.addObject("history", history);
		res.addObject("inceptionRecord", inceptionRecord);
		res.addObject("periodrecords", periodRecords);
		res.addObject("legalRecord", legalRecords);
		res.addObject("linkRecords", linkRecords);
		res.addObject("miscellaneousRecords", miscellaneousRecords);

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		if (brotherhood.getHistory() == null) {
			final History history = this.historyService.create();
			brotherhood.setHistory(history);
			this.historyService.save(history);
			result = this.list();
			result.addObject("history", history);
		}
		result = this.list();
		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final History history, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(history);
		else
			try {
				if (history.getVersion() == 0) {
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					brotherhood.setHistory(history);
					this.historyService.save(history);
				}
				result = this.list();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(history, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int historyId) {
		ModelAndView result;
		//		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = this.historyService.findOne(historyId);
		this.historyService.delete(history);
		result = new ModelAndView("history/create");
		result.addObject("history", history);
		return result;
	}
	protected ModelAndView createEditModelAndView(final History history) {
		ModelAndView result;

		result = this.createEditModelAndView(history, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final History history, final String message) {
		ModelAndView result;

		result = new ModelAndView("history/display");
		result.addObject("history", history);
		result.addObject("message", message);

		return result;

	}

}
