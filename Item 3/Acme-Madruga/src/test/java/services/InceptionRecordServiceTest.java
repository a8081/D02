
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
import domain.Brotherhood;
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
	private BrotherhoodService		brotherhoodService;


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
		photos.add("http://tyniurl.com/dsfes3rfw45d.png");
		photosElementoVacio.add("");
		final Object testingData[][] = {
			{
				//Correcto
				"brotherhood1", "title inception recor 1", "description inception recor 1", null
			}, {
				//Crear con usuario distinto a brothethood
				"member1", "title inception record 1", "description inception recor 1", IllegalArgumentException.class
			}, {
				//Title cadena vacia
				"brotherhood1", "", "descriptionTest", IllegalArgumentException.class
			}, {
				//Title null
				"brotherhood1", null, "description", IllegalArgumentException.class
			}, {
				//Discription cadena vacia
				"brotherhood1", "InceptionTest", "", IllegalArgumentException.class
			}, {
				//Description null
				"brotherhood1", "InceptionTest", null, IllegalArgumentException.class
			},
		//					{
		//						//Photos null
		//						"brotherhood1", "InceptionTest", "descrptionTest", IllegalArgumentException.class
		//					}, {
		//						//Photos coleccion vacia
		//						"brotherhood1", "InceptionTest", "descrptionTest", photosVacio, IllegalArgumentException.class
		//					}, {
		//						//Photos con elemento con URL vac�a
		//						"brotherhood1", "InceptionTest", "descrptionTest", photosElementoVacio, IllegalArgumentException.class
		//					},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void templateCreateSave(final String user, final String title, final String description, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final InceptionRecord incRec = this.inceptionRecordService.create();
			incRec.setTitle(title);
			incRec.setDescription(description);
			//			incRec.setPhotos(photos);
			final InceptionRecord incRecSaved = this.inceptionRecordService.save(incRec);
			Assert.isTrue(incRecSaved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//CORRECTO
				"brotherhood1", "title inception recor test", "description inception recor test", null
			}, {
				//ERROR USER
				"member1", "title inception recor test", "description inception recor test", IllegalArgumentException.class
			}, {
				//TITLE VACIO
				"brotherhood1", "", "description inception recor test", IllegalArgumentException.class
			}, {
				//TITLE NULL
				"brotherhood1", null, "description inception recor test", IllegalArgumentException.class
			}, {
				//DESCRIPTION VACIO
				"brotherhood1", "title inception recor test", "", IllegalArgumentException.class
			}, {
				//DESCRIPTION NULL
				"brotherhood1", "title inception recor test", null, IllegalArgumentException.class
			},
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateEdit(final String actor, final String title, final String description, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final Brotherhood principal = this.brotherhoodService.findByPrincipal();
			final InceptionRecord broIncRec = principal.getHistory().getInceptionRecord();
			broIncRec.setTitle(title);
			broIncRec.setDescription(description);
			this.inceptionRecordService.save(broIncRec);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	//	@Test
	//	public void driverDelete() {
	//
	//		final Object testingData[][] = {
	//			{
	//				"brotherhood1", null, null
	//			}, {
	//				"brotherhood1", new InceptionRecord(), ConstraintViolationException.class
	//			}, {
	//				"member1", null, ConstraintViolationException.class
	//			},
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			this.templateDelete((String) testingData[i][0], (InceptionRecord) testingData[i][1], (Class<?>) testingData[i][2]);
	//	}
	//
	//	private void templateDelete(final String actor, final InceptionRecord inceptionRecord, final Class<?> expected) {
	//		Class<?> caught = null;
	//		try {
	//			this.authenticate(actor);
	//			InceptionRecord incRec = inceptionRecord;
	//			if (inceptionRecord == null) {
	//				final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
	//				incRec = brotherhood.getHistory().getInceptionRecord();
	//			}
	//			this.inceptionRecordService.delete(incRec);
	//			this.unauthenticate();
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//
	//		super.checkExceptions(expected, caught);
	//
	//	}
}