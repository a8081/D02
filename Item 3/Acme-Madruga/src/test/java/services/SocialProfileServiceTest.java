
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	// Service under test ---------------------------------
	@Autowired
	private SocialProfileService	socialProfileService;


	// Tests ----------------------------------------------

	@Test
	public void testCreate() {
		super.authenticate("member1");

		final SocialProfile socialProfile = this.socialProfileService.create();
		Assert.notNull(socialProfile);

		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileMember1"));
		Assert.notNull(socialProfile);
	}

	@Test
	public void testSave() {
		super.authenticate("member1");

		final SocialProfile socialProfile = this.socialProfileService.create();

		socialProfile.setProfileLink("http://www.facebook.com/member1");
		socialProfile.setSocialNetwork("Facebook");
		socialProfile.setNick("nickMember1");

		final SocialProfile result = this.socialProfileService.save(socialProfile);
		Assert.notNull(result);

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("member1");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileMember1"));
		this.socialProfileService.delete(socialProfile);

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAll();
		Assert.notNull(socialProfiles);
	}

	@Test
	public void testModify() {
		super.authenticate("member1");

		final SocialProfile socialProfile = this.socialProfileService.findOne(super.getEntityId("socialProfileMember1"));
		socialProfile.setNick("nickMember1Modified");
		this.socialProfileService.save(socialProfile);

		final SocialProfile socialProfileModified = this.socialProfileService.findOne(super.getEntityId("socialProfileMember1"));
		Assert.isTrue(socialProfileModified.getNick().equals("nickMember1Modified"));

		super.unauthenticate();

	}
}
