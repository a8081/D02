
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FolderRepository;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Service
@Transactional
public class FolderService {

	@Autowired
	private FolderRepository				folderRepository;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private MessageService					messageService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	public Folder create() {
		final Folder folder = new Folder();
		final Collection<Message> ms = new ArrayList<>();
		folder.setIsSystemFolder(false);
		folder.setMessages(ms);
		folder.setFather(null);
		return folder;
	}

	public Folder createInFolder(final Folder father) {
		final Folder folder = new Folder();
		final Collection<Message> ms = new ArrayList<>();
		folder.setIsSystemFolder(false);
		folder.setMessages(ms);
		folder.setFather(father);
		return folder;
	}

	public Collection<Folder> findAll() {
		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Folder> res = this.findAllByUserId(principal.getUserAccount().getId());
		Assert.notNull(res);

		return res;
	}

	public Folder findOne(final int id) {
		final Folder res = this.folderRepository.findOne(id);

		return res;
	}

	public void saveInFather(final Folder father, final Folder f, final Actor a) {
		Assert.notNull(father);
		Assert.notNull(f);
		Assert.notNull(a);
		Assert.isTrue(father.getId() != 0);
		Assert.isTrue(f.getId() != 0);
		Assert.isTrue(a.getId() != 0);

		f.setFather(father);
		f.setActor(a);
		this.folderRepository.save(f);

	}
	public Folder saveInFather2(final Folder father, final Folder f, final Actor a) {
		Assert.notNull(father);
		Assert.notNull(f);
		Assert.notNull(a);

		Folder saved;
		final boolean bool = this.checkForSpamWords(f);

		if (bool)
			a.setSpammer(true);

		if (f.getId() == 0 && father.getId() != 0) {
			Assert.isTrue(f.getFather() != null);
			f.setActor(a);
			f.setFather(father);
			saved = this.folderRepository.save(f);
		} else {
			final Collection<Folder> fs = this.findAllByUserId(a.getUserAccount().getId());
			Assert.isTrue(fs.contains(f));
			saved = this.folderRepository.save(f);
		}
		return saved;
	}

	public Folder save(final Folder f, final Actor a) {
		Assert.notNull(f);
		Assert.notNull(a);

		Folder saved;
		final boolean bool = this.checkForSpamWords(f);

		if (bool)
			a.setSpammer(true);

		if (f.getId() == 0) {
			Assert.isTrue(f.getFather() == null);
			f.setActor(a);
			saved = this.folderRepository.save(f);
		} else {
			final Collection<Folder> fs = this.findAllByUserId(a.getUserAccount().getId());
			Assert.isTrue(fs.contains(f));
			saved = this.folderRepository.save(f);
		}
		return saved;
	}
	private boolean checkForSpamWords(final Folder f) {
		final Collection<String> words = new ArrayList<>();
		words.add(f.getName());

		return this.configurationParametersService.checkForSpamWords(words);
	}

	public void delete(final Folder f) {
		Assert.notNull(f);
		Assert.isTrue(!f.getIsSystemFolder());
		Assert.isTrue(f.getId() != 0);

		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Folder> fs = this.findAllByUserId(principal.getUserAccount().getId());
		final Collection<Message> ms = this.messageService.findAllByFolderIdAndUserId(f.getId(), principal.getUserAccount().getId());
		final Collection<Folder> folders = this.findAllByFatherId(f.getId());
		Assert.isTrue(fs.contains(f));

		if (!ms.isEmpty() && !folders.isEmpty()) {
			this.messageService.deleteAll(ms, f);
			this.deleteAll(folders);
		} else if (!ms.isEmpty() && folders.isEmpty())
			this.messageService.deleteAll(ms, f);
		else if (ms.isEmpty() && !folders.isEmpty())
			this.deleteAll(folders);

		this.folderRepository.delete(f);

	}

	public void deleteAll(final Collection<Folder> fs) {
		Assert.notEmpty(fs);
		for (final Folder f : fs)
			this.delete(f);
	}

	public Collection<Folder> setFoldersByDefault(final Actor actor) {
		final Collection<Folder> folders = new ArrayList<Folder>();
		final Collection<Message> messages = new ArrayList<Message>();

		final Folder inbox = this.create();
		inbox.setName("In box");
		inbox.setIsSystemFolder(true);
		inbox.setActor(actor);
		inbox.setMessages(messages);
		inbox.setFather(null);
		folders.add(inbox);

		final Folder outbox = this.create();
		outbox.setName("Out box");
		outbox.setIsSystemFolder(true);
		outbox.setActor(actor);
		outbox.setMessages(messages);
		outbox.setFather(null);
		folders.add(outbox);

		final Folder trash = this.create();
		trash.setName("Trash box");
		trash.setIsSystemFolder(true);
		trash.setActor(actor);
		trash.setMessages(messages);
		trash.setFather(null);
		folders.add(trash);

		final Folder spam = this.create();
		spam.setName("Spam box");
		spam.setIsSystemFolder(true);
		spam.setActor(actor);
		spam.setMessages(messages);
		spam.setFather(null);
		folders.add(spam);

		final Folder notification = this.create();
		notification.setName("Notification box");
		notification.setIsSystemFolder(true);
		notification.setActor(actor);
		notification.setMessages(messages);
		notification.setFather(null);
		folders.add(notification);

		return folders;
	}

	public Collection<Folder> saveAll(final Collection<Folder> fs) {
		Assert.notEmpty(fs);
		return this.folderRepository.save(fs);
	}

	public Collection<Folder> findAllByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findAllByUserId(id);
	}

	public Folder findInboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findInboxByUserId(id);
	}

	public Folder findOutboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findOutboxByUserId(id);
	}

	public Folder findSpamboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findSpamboxByUserId(id);
	}

	public Folder findTrashboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findTrashboxByUserId(id);
	}

	public Folder findNotificationboxByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findNotificationboxByUserId(id);
	}

	public Collection<Folder> findAllByMessageIdAndUserId(final int mid, final int uid) {
		Assert.isTrue(mid != 0);
		Assert.isTrue(uid != 0);

		return this.folderRepository.findAllByMessageIdAndUserId(mid, uid);
	}

	public Collection<Folder> findAllByFatherId(final int fatherId) {
		Assert.isTrue(fatherId != 0);

		return this.folderRepository.findAllByFatherId(fatherId);
	}

	public Collection<Folder> findAllFolderFatherNullByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findAllFolderFatherNullByUserId(id);
	}

	public Collection<Folder> findAllSystemFolderByUserId(final int id) {
		Assert.isTrue(id != 0);

		return this.folderRepository.findAllSystemFolderByUserId(id);
	}

}
