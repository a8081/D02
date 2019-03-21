
package controllers;

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
import services.InceptionRecordService;
import domain.Brotherhood;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord")
public class InceptionRecordController extends AbstractController {

	@Autowired
	private InceptionRecordService	inceptionRecordService;
	@Autowired
	private HistoryService			historyService;
	@Autowired
	private HistoryController		historyController;
	@Autowired
	private BrotherhoodService		brotherhoodService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		result = this.createEditModelAndView(inceptionRecord);
		return result;
	}

	//Updating
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		try {
			final InceptionRecord inceptionRecord;
			final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);
			Assert.isTrue(brotherhood.getHistory().getInceptionRecord().equals(inceptionRecord), "This inception-record is not of your property");
			result = this.createEditModelAndView(inceptionRecord);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(inceptionRecord);
		else
			try {
				this.inceptionRecordService.save(inceptionRecord);
				result = this.historyController.list();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "general.commit.error");
			}

		return result;
	}
	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(inceptionRecord, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String message) {
		ModelAndView result;

		result = new ModelAndView("inceptionRecord/edit");
		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("message", message);

		return result;

	}

}
