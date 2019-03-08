
package controllers.administrator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping(value = "/ban/administrator")
public class BanAdministratorController extends AbstractController {

	@Autowired
	private ActorService	actorService;


	//Listing banned spammers -----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listBannedSpammers() {
		final ModelAndView result;

		final List<Actor> actors = (List<Actor>) this.actorService.findAll();
		Assert.notNull(actors);
		final List<Boolean> bannedList = this.actorService.getBannedList(actors);
		Assert.notNull(bannedList);

		result = new ModelAndView("ban/list");
		result.addObject("actors", actors);
		result.addObject("bannedList", bannedList);
		result.addObject("requestURI", "ban/administrator/list.do");

		return result;

	}

	//To ban an actor -----------------------------------
	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int actorId) {
		ModelAndView result;

		final Actor actor = this.actorService.findOne(actorId);
		Assert.notNull(actor);

		final List<Actor> actors = (List<Actor>) this.actorService.findAll();
		Assert.notNull(actors);
		final List<Boolean> bannedList = this.actorService.getBannedList(actors);
		Assert.notNull(bannedList);

		try {
			this.actorService.banActor(actor);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("ban/list");
			result.addObject("actors", actors);
			result.addObject("bannedList", bannedList);
			if (oops.getMessage().equals("Para banear un actor este debe ser spammer o tener una puntuación menor que -0.5"))
				result.addObject("message", "ban.error");
			//else
			//result.addObject("message", "ban.commit.error");
		}
		return result;

	}

	//To unban an actor -----------------------------------
	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam final int actorId) {
		final ModelAndView result;

		final Actor actor = this.actorService.findOne(actorId);
		Assert.notNull(actor);
		this.actorService.unbanActor(actor);

		result = new ModelAndView("redirect:list.do");

		return result;

	}
}
