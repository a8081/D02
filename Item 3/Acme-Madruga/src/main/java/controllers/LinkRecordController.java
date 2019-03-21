
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
import services.LinkRecordService;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord")
public class LinkRecordController extends AbstractController {

	@Autowired
	private LinkRecordService	linkRecordService;
	@Autowired
	private HistoryService		historyService;
	@Autowired
	private HistoryController	historyController;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.create();
		final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findAll();
		result = this.createEditModelAndView(linkRecord);
		result.addObject("brotherhoods", brotherhoods);
		return result;
	}

	//Updating
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		try {
			final LinkRecord linkRecord;
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			linkRecord = this.linkRecordService.findOne(linkRecordId);
			Assert.isTrue(brotherhood.getHistory().getLinkRecords().contains(linkRecord), "This link-record is not of your property");
			final Collection<Brotherhood> brotherhoods = this.brotherhoodService.findAll();
			result = this.createEditModelAndView(linkRecord);
			result.addObject("brotherhoods", brotherhoods);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecord linkRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(linkRecord);
		else
			try {
				if (linkRecord.getId() == 0) {
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
					final History history = brotherhood.getHistory();
					final Collection<LinkRecord> linkRecords = history.getLinkRecords();
					linkRecords.add(linkRecord);
					history.setLinkRecords(linkRecords);
					this.historyService.save(history);
				} else
					this.linkRecordService.save(linkRecord);
				result = this.historyController.list();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecord, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int linkRecordId) {
		ModelAndView result;
		final LinkRecord linkRecord = this.linkRecordService.findOne(linkRecordId);
		try {
			this.linkRecordService.delete(linkRecord);
			result = this.historyController.list();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(linkRecord, "general.commit.error");
			result.addObject("id", linkRecord.getId());
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(linkRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecord", linkRecord);
		result.addObject("message", message);

		return result;

	}

}
