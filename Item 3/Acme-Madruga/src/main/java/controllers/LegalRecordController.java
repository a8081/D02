
package controllers;

import java.util.Collection;

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
import services.LegalRecordService;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord")
public class LegalRecordController extends AbstractController {

	@Autowired
	private LegalRecordService				legalRecordService;
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
		final LegalRecord legalRecord = this.legalRecordService.create();
		result = this.createEditModelAndView(legalRecord);
		return result;
	}

	//Updating
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		try {
			final LegalRecord legalRecord;
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			legalRecord = this.legalRecordService.findOne(legalRecordId);
			Assert.isTrue(brotherhood.getHistory().getLegalRecords().contains(legalRecord), "This legal-record is not of your property");
			result = this.createEditModelAndView(legalRecord);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(legalRecord);
		else
			try {
				if (legalRecord.getId() == 0) {
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					final History history = brotherhood.getHistory();
					final Collection<LegalRecord> legalRecords = history.getLegalRecords();
					legalRecords.add(legalRecord);
					history.setLegalRecords(legalRecords);
					this.historyService.save(history);
				} else
					this.legalRecordService.save(legalRecord);
				result = this.historyController.list();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int legalRecordId) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		this.legalRecordService.delete(legalRecord);
		result = this.historyController.list();
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int legalRecordId) {

		ModelAndView res;

		final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);

		if (legalRecord != null) {

			res = new ModelAndView("legalRecord/display");
			res.addObject("legalRecord", legalRecord);
			res.addObject("buttons", false);

			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(legalRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecord", legalRecord);
		result.addObject("message", message);

		return result;

	}

}
