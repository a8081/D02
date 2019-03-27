
package controllers;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationParametersService;
import services.HistoryService;
import services.PeriodRecordService;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord")
public class PeriodRecordController extends AbstractController {

	@Autowired
	private PeriodRecordService				periodRecordService;
	@Autowired
	private HistoryService					historyService;
	@Autowired
	private HistoryController				historyController;
	@Autowired
	private BrotherhoodService				brotherhoodService;
	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.create();
		result = this.createEditModelAndView(periodRecord);
		return result;
	}

	//Updating
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		ModelAndView result;
		try {
			final PeriodRecord periodRecord;
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			periodRecord = this.periodRecordService.findOne(periodRecordId);
			Assert.isTrue(brotherhood.getHistory().getPeriodRecords().contains(periodRecord), "This period-record is not of your property");
			result = this.createEditModelAndView(periodRecord);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(periodRecord);
		else {
			final Date fechaActual = new Date();
			final Integer Año = fechaActual.getYear() + 1900;
			Assert.isTrue(Año <= periodRecord.getStartYear());
			Assert.isTrue(periodRecord.getStartYear() <= periodRecord.getEndYear(), "El año de inicio debe de ser anterior al año de fin.");
			if (periodRecord.getId() == 0) {
				final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
				final History history = brotherhood.getHistory();
				final Collection<PeriodRecord> pr = history.getPeriodRecords();
				pr.add(periodRecord);
				history.setPeriodRecords(pr);
				this.historyService.save(history);
			} else
				this.periodRecordService.save(periodRecord);
			result = this.historyController.list();
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int periodRecordId) {
		ModelAndView result;
		final PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);
		this.periodRecordService.delete(periodRecord);
		result = this.historyController.list();

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int periodRecordId) {

		ModelAndView res;

		final PeriodRecord periodRecord = this.periodRecordService.findOne(periodRecordId);

		if (periodRecord != null) {

			res = new ModelAndView("periodRecord/display");
			res.addObject("periodRecord", periodRecord);

			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(periodRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecord", periodRecord);
		result.addObject("message", message);

		return result;

	}

}
