
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Folder;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FolderServiceTest extends AbstractTest {

	@Autowired
	private FolderService	folderService;

	@Autowired
	private ActorService	actorService;


	@Test
	public void testFindAll() {
		super.authenticate("member1");
		final Collection<Folder> folders = this.folderService.findAll();
		Assert.notEmpty(folders);
	}

	@Test
	public void testFindOne() {
		super.authenticate("member1");
		final Folder folder = (Folder) this.folderService.findAll().toArray()[0];
		final Folder retrieved = this.folderService.findOne(folder.getId());

		Assert.isTrue(folder.equals(retrieved));
	}

	@Test
	public void testFindAllByUserId() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();
		final Collection<Folder> retrieved = this.folderService.findAllByUserId(principal.getUserAccount().getId());
		Assert.notEmpty(retrieved);
		final Folder f = (Folder) retrieved.toArray()[0];
		Assert.isTrue(f.getActor().equals(principal));
	}

	@Test
	public void testFindInboxByUserId() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Folder> folders = this.folderService.findAllByUserId(principal.getUserAccount().getId());
		final Folder inbox = this.folderService.findInboxByUserId(principal.getUserAccount().getId());
		Assert.isTrue(inbox.getName().equals("In box"));
		Assert.isTrue(folders.contains(inbox));
	}

	@Test
	public void testFindOutboxByUserId() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Folder> folders = this.folderService.findAllByUserId(principal.getUserAccount().getId());
		final Folder outbox = this.folderService.findOutboxByUserId(principal.getUserAccount().getId());
		Assert.isTrue(outbox.getName().equals("Out box"));
		Assert.isTrue(folders.contains(outbox));
	}

	@Test
	public void testFindTrashByUserId() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Folder> folders = this.folderService.findAllByUserId(principal.getUserAccount().getId());
		final Folder trash = this.folderService.findTrashboxByUserId(principal.getUserAccount().getId());
		Assert.isTrue(trash.getName().equals("Trash box"));
		Assert.isTrue(folders.contains(trash));
	}

	@Test
	public void testFindSpamboxByUserId() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Folder> folders = this.folderService.findAllByUserId(principal.getUserAccount().getId());
		final Folder spam = this.folderService.findSpamboxByUserId(principal.getUserAccount().getId());
		Assert.isTrue(spam.getName().equals("Spam box"));
		Assert.isTrue(folders.contains(spam));
	}

	@Test
	public void testFindFoldersByMessageIdAndUserId() {
		super.authenticate("brotherhood1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Folder> fs = this.folderService.findAllByUserId(principal.getUserAccount().getId());
		final Folder folder = (Folder) fs.toArray()[0];
		final Message message = (Message) folder.getMessages().toArray()[0];

		final Collection<Folder> folders = this.folderService.findAllByMessageIdAndUserId(message.getId(), principal.getUserAccount().getId());
		Assert.isTrue(fs.containsAll(folders));

		for (final Folder f : folders)
			Assert.isTrue(f.getMessages().contains(message));
	}

	@Test
	public void testSave() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Folder folder = this.folderService.create();
		folder.setIsSystemFolder(false);
		folder.setName("test");

		final Folder saved = this.folderService.save(folder, principal);
		final Collection<Folder> folders = this.folderService.findAll();

		Assert.isTrue(folders.contains(saved));
	}

	@Test
	public void testSaveAll() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Message> messages = new ArrayList<>();
		final Folder folder1 = this.folderService.create();
		folder1.setName("test1");
		folder1.setIsSystemFolder(false);
		folder1.setMessages(messages);
		folder1.setActor(principal);
		final Folder folder2 = this.folderService.create();
		folder2.setName("test2");
		folder2.setIsSystemFolder(false);
		folder2.setMessages(messages);
		folder2.setActor(principal);
		final Folder folder3 = this.folderService.create();
		folder3.setName("test3");
		folder3.setIsSystemFolder(false);
		folder3.setMessages(messages);
		folder3.setActor(principal);

		final Collection<Folder> folders = new ArrayList<>();
		folders.add(folder1);
		folders.add(folder2);
		folders.add(folder3);

		final Collection<Folder> saved = this.folderService.saveAll(folders);

		final Collection<Folder> retrieved = this.folderService.findAll();

		Assert.isTrue(retrieved.containsAll(saved));
	}

	@Test
	public void testUpdate() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Folder folder = (Folder) this.folderService.findAllByUserId(principal.getUserAccount().getId()).toArray()[0];
		final String oldName = folder.getName();

		folder.setName("test");
		final Folder saved = this.folderService.save(folder, principal);

		Assert.isTrue(!oldName.equals(saved.getName()));
	}

	@Test
	public void testDelete() {
		super.authenticate("member1");
		final Actor principal = this.actorService.findByPrincipal();

		final Collection<Message> messages = new ArrayList<>();
		final Folder folder1 = this.folderService.create();
		folder1.setName("test1");
		folder1.setIsSystemFolder(false);
		folder1.setMessages(messages);
		folder1.setActor(principal);
		final Folder saved = this.folderService.save(folder1, principal);

		this.folderService.delete(saved);
		final Collection<Folder> updated = this.folderService.findAllByUserId(principal.getUserAccount().getId());

		Assert.isTrue(!updated.contains(saved));
	}
}
