
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

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	private ActorService	actorService;


	@Test
	public void testFindAll() {
		final Collection<Actor> actors = this.actorService.findAll();
		Assert.notEmpty(actors);
	}

	@Test
	public void testFindOne() {
		final Actor actor = (Actor) this.actorService.findAll().toArray()[0];
		final Actor retrieved = this.actorService.findOne(actor.getId());
		Assert.isTrue(actor.equals(retrieved));
	}

	@Test
	public void testFindByUserId() {
		final Actor retrieved = (Actor) this.actorService.findAll().toArray()[0];
		final int id = retrieved.getUserAccount().getId();
		final Actor a = this.actorService.findByUserId(id);
		Assert.isTrue(a.equals(retrieved));
	}

	@Test
	public void testFindByPrincipalActor() {
		super.authenticate("member1");
		final Actor res = this.actorService.findByPrincipal();
		Assert.notNull(res);
		Assert.isTrue(res.getUserAccount().getUsername().equals("member1"));
	}

	@Test
	public void testCheckAuthority() {
		final Actor retrieved = (Actor) this.actorService.findAll().toArray()[0];
		final UserAccount ua = retrieved.getUserAccount();
		final Authority auth = (Authority) ua.getAuthorities().toArray()[0];
		final boolean bool = this.actorService.checkAuthority(retrieved, auth.getAuthority());

		Assert.isTrue(bool);
	}

	@Test
	public void testFindSuspiciousActors() {
		super.authenticate("admin1");
		final Collection<Actor> actors = this.actorService.findAllSpammers();
		final Collection<Actor> spammers = new ArrayList<>();
		for (final Actor a : actors)
			if (a.getSpammer())
				spammers.add(a);
		Assert.isTrue(spammers.size() == actors.size());
	}
	@Test
	public void testBanActor() {
		super.authenticate("admin1");
		final Actor a = (Actor) this.actorService.findAllSpammers().toArray()[0];
		this.actorService.banActor(a);

		final UserAccount ua = a.getUserAccount();
		final Collection<Authority> auths = ua.getAuthorities();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.BANNED);

		Assert.isTrue(auths.contains(auth));
	}

	@Test
	public void testUnbanActor() {
		super.authenticate("admin1");
		final Authority auth = new Authority();
		auth.setAuthority(Authority.BANNED);
		int i = 0;
		Actor a = (Actor) this.actorService.findAllSpammers().toArray()[i];
		while (!a.getUserAccount().getAuthorities().contains(auth)) {
			a = (Actor) this.actorService.findAllSpammers().toArray()[i];
			i = i + 1;
		}

		this.actorService.unbanActor(a);
		final UserAccount ua = a.getUserAccount();
		final Collection<Authority> auths = ua.getAuthorities();

		Assert.isTrue(!auths.contains(auth));
	}

	@Test
	public void testComputeScore() {
		super.authenticate("admin1");
		int i = 0;
		Actor a = (Actor) this.actorService.findAll().toArray()[i];
		while (!(this.actorService.checkAuthority(a, Authority.MEMBER) || (this.actorService.checkAuthority(a, Authority.BROTHERHOOD)))) {
			a = (Actor) this.actorService.findAll().toArray()[i];
			i = i + 1;
		}
		final double res = this.actorService.computeScore(a);
		Assert.isTrue(res <= 1.0 && res >= -1.0);
	}
}
