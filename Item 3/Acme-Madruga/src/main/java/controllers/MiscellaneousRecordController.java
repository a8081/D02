
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
import services.MiscellaneousRecordService;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord")
public class MiscellaneousRecordController extends AbstractController {

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;
	@Autowired
	private HistoryService				historyService;
	@Autowired
	private BrotherhoodService			brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.create();
		result = this.createEditModelAndView(miscellaneousRecord);
		return result;
	}

	//Updating
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		try {
			final MiscellaneousRecord miscellaneousRecord;
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			Assert.isTrue(brotherhood.getHistory().getMiscellaneousRecords().equals(miscellaneousRecord), "This miscellaneous-record is not of your property");
			result = this.createEditModelAndView(miscellaneousRecord);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(miscellaneousRecord);
		else
			try {
				final MiscellaneousRecord miscellaneousRecord1 = this.miscellaneousRecordService.save(miscellaneousRecord);
				if (miscellaneousRecord.getVersion() == 0) {
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					final History history = brotherhood.getHistory();
					final Collection<MiscellaneousRecord> mr = history.getMiscellaneousRecords();
					mr.add(miscellaneousRecord1);
					history.setMiscellaneousRecords(mr);
					this.historyService.save(history);
				}
				result = new ModelAndView("redirect:../history/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "general.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("message", message);

		return result;

	}

}
