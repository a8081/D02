
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;


	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Administrator administrator = this.administratorService.create();

		administrator.setName("Antonio");
		administrator.setMiddleName("");
		administrator.setSurname("M");
		administrator.setPhoto("");
		administrator.setEmail("antM@gmail.com");
//		administrator.setPhone("");
//		administrator.setAddress("");
		administrator.setSpammer(false);

		final Administrator saved = this.administratorService.save(administrator);
		final UserAccount userAccount = saved.getUserAccount();
		userAccount.setUsername("admin");
		userAccount.setPassword("admin");

		final Collection<Actor> actors = this.actorService.findAll();

		Assert.isTrue(actors.contains(saved));
	}

}
