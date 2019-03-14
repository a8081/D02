
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

import repositories.RequestRepository;
import utilities.AbstractTest;
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	// Services
	@Autowired
	private InceptionRecordService	inceptionRecordService;
	@Autowired
	private ParadeService			paradeService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private MemberService			memberService;

	//Repositorys
	@Autowired
	private RequestRepository		requestRepository;


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Collection<String> photosVacio = new ArrayList<String>();
		final Collection<String> photosElementoVacio = new ArrayList<String>();
		final Collection<String> photos = new ArrayList<String>();
		photos.add("http://tyniurl.com/dsfrefd.png");
		final Object testingData[][] = {
			{
				"brotherhood1", "Inception1", "description", photos, null
			}, {
				"member1", "InceptionTest", "descriptionTest", photos, IllegalArgumentException.class
			}, {
				"brotherhood1", "", "descriptionTest", photos, IllegalArgumentException.class
			}, {
				"brotherhood1", null, "description", photos, NullPointerException.class
			}, {
				"brotherhood1", "InceptionTest", "", photos, IllegalArgumentException.class
			}, {
				"brotherhood1", "InceptionTest", null, photos, NullPointerException.class
			}, {
				"brotherhood1", "InceptionTest", "descrptionTest", null, NullPointerException.class
			}, {
				"brotherhood1", "InceptionTest", "descrptionTest", photosVacio, IllegalArgumentException.class
			}, {
				"brotherhood1", "InceptionTest", "descrptionTest", photosElementoVacio, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateCreateSave(final String user, final String title, final String description, final Collection<String> photos, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final InceptionRecord incRec = this.inceptionRecordService.create();
			incRec.setTitle(title);
			incRec.setDescription(description);
			incRec.setPhotos(photos);
			final InceptionRecord incRecSaved = this.inceptionRecordService.save(incRec);
			Assert.isTrue(incRecSaved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
