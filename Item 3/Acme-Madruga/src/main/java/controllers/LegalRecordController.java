
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
import services.HistoryService;
import services.LegalRecordService;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord")
public class LegalRecordController extends AbstractController {

	@Autowired
	private LegalRecordService	legalRecordService;
	@Autowired
	private HistoryService		historyService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


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
			Assert.isTrue(brotherhood.getHistory().getPeriodRecords().equals(legalRecord), "This legal-record is not of your property");
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
				final LegalRecord legalRecord1 = this.legalRecordService.save(legalRecord);
				if (legalRecord.getVersion() == 0) {
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					final History history = brotherhood.getHistory();
					final Collection<LegalRecord> legalR = history.getLegalRecords();
					legalR.add(legalRecord1);
					history.setLegalRecords(legalR);
					this.historyService.save(history);
				}
				result = new ModelAndView("redirect:../history/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int legalRecordId) {
		ModelAndView result;
		final LegalRecord legalRecord = this.legalRecordService.findOne(legalRecordId);
		try {
			this.legalRecordService.delete(legalRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(legalRecord, "general.commit.error");
			result.addObject("id", legalRecord.getId());
		}
		return result;
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
