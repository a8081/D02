
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationParametersService;
import services.FolderService;
import services.MessageService;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService					messageService;

	@Autowired
	private FolderService					folderService;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	//	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//	public ModelAndView list2(@RequestParam final int folderId) {
	//		final ModelAndView res;
	//		final Collection<Message> messages;
	//		final Folder father = this.folderService.findOne(folderId);
	//		final Collection<Folder> folders = this.folderService.findAll();
	//		final Collection<Folder> foldersFinal = new ArrayList<>();
	//		for (final Folder f : folders)
	//			if (f.getFather() != null && f.getFather().equals(father))
	//				foldersFinal.add(f);
	//
	//		folders.retainAll(foldersFinal);
	//
	//		if (folder != null) {
	//			final int userId = this.actorService.findByPrincipal().getUserAccount().getId();
	//
	//			messages = this.messageService.findAllByFolderIdAndUserId(folderId, userId);
	//
	//			res = new ModelAndView("message/list");
	//			res.addObject("m", messages);
	//			res.addObject("folder", folder);
	//			res.addObject("father", father);
	//			res.addObject("folders", foldersFinal);
	//			res.addObject("requestURI", "message/list.do?folderId=" + folderId);
	//			final String banner = this.configurationParametersService.find().getBanner();
	//			res.addObject("banner", banner);
	//		} else
	//			res = new ModelAndView("redirect:/misc/403.jsp");
	//
	//		return res;
	//
	//	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int folderId) {
		final ModelAndView res;
		final Collection<Message> messages;
		final Folder folder = this.folderService.findOne(folderId);

		if (folder != null && folder.getFather() == null) {
			final int userId = this.actorService.findByPrincipal().getUserAccount().getId();
			final Folder father = this.folderService.findOne(folderId);
			final Collection<Folder> folders = this.folderService.findAll();
			folders.retainAll(this.folderService.findAllByFatherId(father.getId()));

			messages = this.messageService.findAllByFolderIdAndUserId(folderId, userId);

			res = new ModelAndView("message/list");
			res.addObject("m", messages);
			res.addObject("folder", folder);
			res.addObject("folders", folders);
			res.addObject("requestURI", "message/list.do?folderId=" + folderId);
			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else if (folder != null && folder.getFather() != null) {
			final Collection<Folder> folders = this.folderService.findAllByFatherId(folderId);

			final int userId = this.actorService.findByPrincipal().getUserAccount().getId();

			messages = this.messageService.findAllByFolderIdAndUserId(folderId, userId);

			res = new ModelAndView("message/list");
			res.addObject("m", messages);
			res.addObject("folder", folder);
			res.addObject("folders", folders);
			res.addObject("requestURI", "message/list.do?folderId=" + folderId);
			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView res;
		Message message;

		message = this.messageService.create();
		res = this.createEditModelAndView(message);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "send")
	public ModelAndView save(@ModelAttribute("m") @Valid final Message m, final BindingResult binding) {
		ModelAndView res;
		final Actor principal = this.actorService.findByPrincipal();
		final Folder outbox = this.folderService.findOutboxByUserId(principal.getUserAccount().getId());

		if (binding.hasErrors())
			res = this.createEditModelAndView(m);
		else
			try {
				final Message sent = this.messageService.send(m);
				res = new ModelAndView("redirect:display.do?messageId=" + sent.getId() + "&folderId=" + outbox.getId());
				final String banner = this.configurationParametersService.find().getBanner();
				res.addObject("banner", banner);
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(m, "message.commit.error");
			}
		return res;

	}

	@RequestMapping(value = "/broadcast", method = RequestMethod.GET)
	public ModelAndView broadcast() {

		ModelAndView res;
		Message message;

		message = this.messageService.create();
		final Collection<Actor> actors = this.actorService.findAll();
		message.setRecipients(actors);
		final Collection<String> priorities = new ArrayList<>();

		priorities.add("HIGH");
		priorities.add("NEUTRAL");
		priorities.add("LOW");

		res = new ModelAndView("message/broadcast");
		res.addObject("m", message);
		res.addObject("priorities", priorities);
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "broadcast")
	public ModelAndView saveBroadcast(@ModelAttribute("m") @Valid final Message m, final BindingResult binding) {

		ModelAndView res;
		//		final Actor principal = this.actorService.findByPrincipal();
		//		final Folder outbox = this.folderService.findOutboxByUserId(principal.getUserAccount().getId());

		if (binding.hasErrors())
			res = this.createEditModelAndViewBroadcast(m);
		else
			try {
				this.messageService.broadcast(m);
				res = new ModelAndView("administrator/congratulation");
				final String banner = this.configurationParametersService.find().getBanner();
				res.addObject("banner", banner);
			} catch (final Throwable oops) {
				res = this.createEditModelAndViewBroadcast(m, "message.commit.error");
			}
		return res;

	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId, @RequestParam final int folderId) {

		ModelAndView res;
		final Message toDelete = this.messageService.findOne(messageId);
		final Folder folder = this.folderService.findOne(folderId);

		try {
			this.messageService.deleteFromFolder(toDelete, folder);
			res = new ModelAndView("redirect: list.do?folderId=" + folderId);
			res.addObject("folder", folder);
			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect: display.do?messageId=" + messageId + "&folderId=" + folderId);
			final String error = "Cannot delete this message";
			res.addObject("error", error);
		}
		return res;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId, @RequestParam final int folderId) {

		ModelAndView res;
		Message message;
		Folder folder;
		boolean ownMessage;

		final Actor principal = this.actorService.findByPrincipal();

		message = this.messageService.findOne(messageId);
		folder = this.folderService.findOne(folderId);
		Assert.isTrue(principal.equals(folder.getActor()), "No puedes ver mensajes de otros actores.");

		if (folder != null && message != null) {
			ownMessage = message.getSender().equals(principal);

			res = new ModelAndView("message/display");
			res.addObject("m", message);
			res.addObject("folder", folder);
			res.addObject("ownMessage", ownMessage);

			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam final int messageId, @RequestParam final int folderId) {
		ModelAndView res;
		Message message;
		final Collection<Actor> recipients = new ArrayList<>();

		final Folder folder = this.folderService.findOne(folderId);
		message = this.messageService.findOne(messageId);

		if (folder != null && message != null) {
			final Actor recipient = message.getSender();
			recipients.add(recipient);
			final Message newMessage = this.messageService.create();
			newMessage.setRecipients(recipients);

			res = new ModelAndView("message/reply");
			res.addObject("m", newMessage);
			res.addObject("previousMessageId", messageId);
			res.addObject("folder", folder);

			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "reply")
	public ModelAndView saveReply(@ModelAttribute("m") @Valid final Message m, final BindingResult binding) {
		ModelAndView res;
		final Actor principal = this.actorService.findByPrincipal();
		final Folder outbox = this.folderService.findOutboxByUserId(principal.getUserAccount().getId());

		if (binding.hasErrors())
			res = this.createEditModelAndViewReply(m);
		else
			try {
				final Message sent = this.messageService.send(m);
				res = new ModelAndView("redirect:display.do?messageId=" + sent.getId() + "&folderId=" + outbox.getId());
				final String banner = this.configurationParametersService.find().getBanner();
				res.addObject("banner", banner);
			} catch (final Throwable oops) {
				res = this.createEditModelAndViewReply(m, "message.commit.error");
			}
		return res;

	}

	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId, @RequestParam final int folderId) {
		final ModelAndView res;
		final Message message = this.messageService.findOne(messageId);
		final Folder actualFolder = this.folderService.findOne(folderId);
		final Actor actor = this.actorService.findByPrincipal();
		final Folder trashBox = this.folderService.findTrashboxByUserId(actor.getUserAccount().getId());

		if (message != null && actualFolder != null) {
			final Collection<Folder> allFolders = this.folderService.findAllByUserId(actor.getUserAccount().getId());
			allFolders.remove(trashBox);
			res = new ModelAndView("message/move");
			res.addObject("m", message);
			res.addObject("folder", actualFolder);
			res.addObject("folders", allFolders);
			res.addObject("requestURI", "message/move.do");

			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;
	}
	@RequestMapping(value = "/saveMove", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam(required = true) final int messageId, @RequestParam(required = true) final int folderId, @RequestParam(required = true) final int choosedFolderId) {
		ModelAndView res;
		final Message message = this.messageService.findOne(messageId);
		final Folder actualFolder = this.folderService.findOne(folderId);
		final Folder choosedFolder = this.folderService.findOne(choosedFolderId);

		if (message != null && actualFolder != null && choosedFolder != null)
			try {

				this.messageService.moveFromAToB(message, actualFolder, choosedFolder);
				res = new ModelAndView("administrator/congratulationMove");
				res.addObject("actualFolder", actualFolder);
				res.addObject("choosedFolder", choosedFolder);

				final String banner = this.configurationParametersService.find().getBanner();
				res.addObject("banner", banner);

			} catch (final Throwable oops) {

				final Collection<Folder> folders = this.folderService.findAll();
				res = new ModelAndView("message/move");
				res.addObject("m", message);
				res.addObject("message", "ms.commit.error");
				res.addObject("folders", folders);

				final String banner = this.configurationParametersService.find().getBanner();
				res.addObject("banner", banner);

			}
		else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;
	}

	@RequestMapping(value = "/copy", method = RequestMethod.GET)
	public ModelAndView copy(@RequestParam final int messageId) {
		final ModelAndView res;
		final Message message = this.messageService.findOne(messageId);
		final Actor actor = this.actorService.findByPrincipal();
		final Folder trashBox = this.folderService.findTrashboxByUserId(actor.getUserAccount().getId());

		if (message != null) {
			final Collection<Folder> allFolders = this.folderService.findAllByUserId(actor.getUserAccount().getId());
			allFolders.remove(trashBox);

			res = new ModelAndView("message/copy");
			res.addObject("m", message);
			res.addObject("folders", allFolders);
			res.addObject("requestURI", "message/copy.do");

			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);
		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}

	@RequestMapping(value = "/saveCopy", method = RequestMethod.GET)
	public ModelAndView copy(@RequestParam(required = true) final int messageId, @RequestParam(required = true) final int choosedFolderId) {

		ModelAndView res;
		final Message message = this.messageService.findOne(messageId);
		final Folder choosedFolder = this.folderService.findOne(choosedFolderId);

		try {

			this.messageService.copyToFolder(message, choosedFolder);
			res = new ModelAndView("administrator/congratulationCopy");
			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);

		} catch (final Throwable oops) {

			final Collection<Folder> folders = this.folderService.findAll();
			res = new ModelAndView("message/move");
			res.addObject("m", message);
			res.addObject("message", "ms.commit.error");
			res.addObject("folders", folders);
			final String banner = this.configurationParametersService.find().getBanner();
			res.addObject("banner", banner);

		}

		return res;
	}

	protected ModelAndView createEditModelAndView(final Message message) {

		ModelAndView res;

		res = this.createEditModelAndView(message, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {

		final ModelAndView res;
		final Collection<Actor> recipients = this.actorService.findAll();
		final Collection<String> priorities = new ArrayList<>();

		priorities.add("HIGH");
		priorities.add("NEUTRAL");
		priorities.add("LOW");

		res = new ModelAndView("message/edit");
		res.addObject("m", message);
		res.addObject("recipients", recipients);
		res.addObject("priorities", priorities);

		res.addObject("message", messageCode);
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;

	}

	protected ModelAndView createEditModelAndViewBroadcast(final Message message) {

		ModelAndView res;

		res = this.createEditModelAndViewBroadcast(message, null);

		return res;
	}

	protected ModelAndView createEditModelAndViewBroadcast(final Message message, final String messageCode) {

		final ModelAndView res;
		final Collection<String> priorities = new ArrayList<>();

		priorities.add("HIGH");
		priorities.add("NEUTRAL");
		priorities.add("LOW");

		res = new ModelAndView("message/broadcast");
		res.addObject("m", message);
		res.addObject("priorities", priorities);

		res.addObject("message", messageCode);
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;

	}

	protected ModelAndView createEditModelAndViewReply(final Message message) {

		ModelAndView res;

		res = this.createEditModelAndViewReply(message, null);

		return res;
	}

	protected ModelAndView createEditModelAndViewReply(final Message message, final String messageCode) {

		final ModelAndView res;
		final Collection<String> priorities = new ArrayList<>();

		priorities.add("HIGH");
		priorities.add("NEUTRAL");
		priorities.add("LOW");

		res = new ModelAndView("message/reply");
		res.addObject("m", message);
		res.addObject("priorities", priorities);

		res.addObject("message", messageCode);
		final String banner = this.configurationParametersService.find().getBanner();
		res.addObject("banner", banner);

		return res;

	}

}
