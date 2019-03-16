
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Enrolment;
import domain.GPS;
import domain.Member;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	@Test
	private void templateListEdit(final String title, final String description, final String moment, final String ticker, final String mode, final int maxRows, final int maxColumns, final String status, final String rejectionReason, final Class<?> expected) {
			// falta segments
		Rendezvouse rendezvouse;
		final Date organisedMomentDate;
		List<Rendezvouse> rendezvouses;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouses = new ArrayList<Rendezvouse>(this.rendezvouseService.findRendezvousesCreatedByUser());
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			Assert.isTrue(rendezvouses.contains(rendezvouse));
			rendezvouse.setName(name);
			rendezvouse.setDescription(description);
			if (organisedMoment != null)
				organisedMomentDate = (new SimpleDateFormat("yyyy/MM/dd")).parse(organisedMoment);
			else
				organisedMomentDate = null;
			rendezvouse.setOrganisedMoment(organisedMomentDate);
			rendezvouse.setPicture(picture);
			rendezvouse.setGps(gps);
			rendezvouse.setDraftMode(draftMode);
			rendezvouse.setDeleted(deleted);
			rendezvouse.setForAdult(forAdult);
			rendezvouse = this.rendezvouseService.save(rendezvouse);
			this.rendezvouseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	}
	// Test listEdit ----------------------------------------------------------------------------------
	// Se listan las rendezvouses creadas por el user logueado y de ellas se coge la pasada por parametro para cambiarles los valores
	//Caso de uso 5.2:Create a rendezvous, which he's implicitly assumed to attend. Note that a user may edit his or her rendezvouses as long as they aren’t saved them in final mode. Once a rendezvous is saved in final mode, it cannot be edited or deleted by the creator. (parte 1)
	@Test
	public void driverListEdit() {
		final Collection<GPS> listGPS = this.createAllGPSForTesting();
		final Iterator<GPS> iterator = listGPS.iterator();
		final GPS gpsOk = iterator.next();
		final Object testingData[][] = {
			{
				//Se edita un Rendezvouse correctamente
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", gpsOk, true, false, false, "rendezvouse1", null
			}, {
				//Se edita un Rendezvouse correctamente con Gps con latitude null
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", iterator.next(), true, false, false, "rendezvouse1", null
			}, {
				//Se edita un Rendezvouse correctamente con Gps con longitude null
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", iterator.next(), true, false, false, "rendezvouse1", null
			}, {
				//Se edita un Rendezvouse incorrectamente con Gps con OutOfRangeLatitudeMax
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", iterator.next(), true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse incorrectamente con Gps con OutOfRangeLatitudeMin
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", iterator.next(), true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse incorrectamente con Gps con OutOfRangeLongitudeMax
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", iterator.next(), true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse incorrectamente con Gps con OutOfRangeLongitudeMin
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", iterator.next(), true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse incorrectamente con title en blank
				"user1", "", "description", "2019/03/03", "http://www.test.com", gpsOk, true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse incorrectamente con description en blank
				"user1", "name test", "", "2019/03/03", "http://www.test.com", gpsOk, true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse incorrectamente con organisedMoment en null
				//Salta un NullPointerException en vez de javax.validation porque salta el Assert.isTrue que comprueba que la fecha introducida este en futuro
				"user1", "name test", "description", null, "http://www.test.com", gpsOk, true, false, false, "rendezvouse1", NullPointerException.class
			}, {
				//Se edita un Rendezvouse correctamente con picture en null
				"user1", "name test", "description", "2019/03/03", null, gpsOk, true, false, false, "rendezvouse1", null
			}, {
				//Se edita un Rendezvouse incorrectamente con picture con url malamente
				"user1", "name test", "description", "2019/03/03", "estoNoEsUnaURL", gpsOk, true, false, false, "rendezvouse1", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un Rendezvouse que esta en modo final (solo se puede editar la Rendezvous cuando no esta en modo final)
				"user1", "name test", "description", "2019/03/03", "estoNoEsUnaURL", gpsOk, true, false, false, "rendezvouse2", IllegalArgumentException.class
			}
		// Se contempla la opcion de que solo se puede editar una rendezvouse en modo no final en el controlador
		//Se contempla la opcion de que solo se puede editar un rendezvouse que ha creado el usuario de ese rendezvouse en el controlador
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (GPS) testingData[i][5], (boolean) testingData[i][6],
				(boolean) testingData[i][7], (boolean) testingData[i][8], super.getEntityId((String) testingData[i][9]), (Class<?>) testingData[i][10]);
	}
	private void templateListEdit(final String username, final String name, final String description, final String organisedMoment, final String picture, final GPS gps, final boolean draftMode, final boolean deleted, final boolean forAdult,
		final int rendezvouseId, final Class<?> expected) {
		Rendezvouse rendezvouse;
		final Date organisedMomentDate;
		List<Rendezvouse> rendezvouses;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouses = new ArrayList<Rendezvouse>(this.rendezvouseService.findRendezvousesCreatedByUser());
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			Assert.isTrue(rendezvouses.contains(rendezvouse));
			rendezvouse.setName(name);
			rendezvouse.setDescription(description);
			if (organisedMoment != null)
				organisedMomentDate = (new SimpleDateFormat("yyyy/MM/dd")).parse(organisedMoment);
			else
				organisedMomentDate = null;
			rendezvouse.setOrganisedMoment(organisedMomentDate);
			rendezvouse.setPicture(picture);
			rendezvouse.setGps(gps);
			rendezvouse.setDraftMode(draftMode);
			rendezvouse.setDeleted(deleted);
			rendezvouse.setForAdult(forAdult);
			rendezvouse = this.rendezvouseService.save(rendezvouse);
			this.rendezvouseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	// Test Create ----------------------------------------------------------------------------------
	// Se comprueba el metodo create de Rendezvouse
	//Caso de uso 5.2: Create a rendezvous, which he's implicitly assumed to attend. Note that a user may edit his or her rendezvouses as long as they aren’t saved them in final mode. Once a rendezvous is saved in final mode, it cannot be edited or deleted by the creator.(parte 2)
	@Test
	public void driverCreate() {
		final Collection<GPS> listGPS = this.createAllGPSForTesting();
		final Iterator<GPS> iterator = listGPS.iterator();
		final GPS gpsOk = iterator.next();
		final Object testingData[][] = {
			{
				//Se crea un Rendezvouse correctamente
				"user1", "name test", "description", "2019/03/03", "http://www.test.com", gpsOk, true, false, false, "rendezvouse1", null
			}, {
				//Un user menor de edad crea un Rendezvouse para mayores de edad
				"user5", "name test", "description", "2019/03/03", "http://www.test.com", gpsOk, true, false, true, "rendezvouse5", IllegalArgumentException.class
			}, {
				//Se crea un Rendezvouse incorrectamente porque lo intenta crear un admin
				"admin", "name test", "description", "2019/03/03", "http://www.test.com", gpsOk, true, false, false, "rendezvouse1", IllegalArgumentException.class
			}
		// Se contempla la opcion de que solo se puede editar una rendezvouse en modo no final en el controlador
		//Se contempla la opcion de que solo se puede editar un rendezvouse que ha creado el usuario de ese rendezvouse en el controlador
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (GPS) testingData[i][5], (boolean) testingData[i][6], (boolean) testingData[i][7],
				(boolean) testingData[i][8], super.getEntityId((String) testingData[i][9]), (Class<?>) testingData[i][10]);
	}
	private void templateCreate(final String username, final String name, final String description, final String organisedMoment, final String picture, final GPS gps, final boolean draftMode, final boolean deleted, final boolean forAdult,
		final int rendezvouseId, final Class<?> expected) {
		Rendezvouse rendezvouse;
		final Date organisedMomentDate;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouse = this.rendezvouseService.create();
			rendezvouse.setName(name);
			rendezvouse.setDescription(description);
			if (organisedMoment != null)
				organisedMomentDate = (new SimpleDateFormat("yyyy/MM/dd")).parse(organisedMoment);
			else
				organisedMomentDate = null;
			rendezvouse.setOrganisedMoment(organisedMomentDate);
			rendezvouse.setPicture(picture);
			rendezvouse.setGps(gps);
			rendezvouse.setDraftMode(draftMode);
			rendezvouse.setDeleted(deleted);
			rendezvouse.setForAdult(forAdult);
			rendezvouse = this.rendezvouseService.save(rendezvouse);
			this.rendezvouseService.flush();
			//Comprobamos que el user autentificado asiste a su rendezvous como nos dice el requisito
			Assert.isTrue(rendezvouse.getAssistants().contains(this.userService.findByPrincipal()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	// Test Delete Virtual----------------------------------------------------------------------------------
	// Se comprueba el metodo del delete virtual el cual solo puede realizar el user de sus rendezvouses y consiste en poner a 1 el atributo deleted
	// El hecho de no poder editar una Rendezvouse que esta borrada se contempla en el controlador debido a que se pueden "editar" cambiando solo las
	// rendezvouses que tiene similares, por tanto no lo ponemos en el metodo save la restriccion
	//Caso de uso 5.3:Update or delete the rendezvouses that he or she's created. Deletion is virtual, that is: the information is not removed from the database, but the rendezvous cannot be updated.
	@Test
	public void driverDeleteVirtual() {
		final Object testingData[][] = {
			{
				//Se elimina el rendezvouse4 por el user que la ha creado pero con draftMode en true
				"user1", "rendezvouse4", null
			}, {
				//Se elimina el rendezvouse1 por el user que la ha creado pero con draftMode en false
				"user2", "rendezvouse2", IllegalArgumentException.class
			}, {
				//Se elimina el rendezvouse1 por el user que NO la ha creado(Hacking get)
				"user5", "rendezvouse1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteVirtual((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDeleteVirtual(final String username, final int rendezvouseId, final Class<?> expected) {
		Rendezvouse rendezvouse;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			this.rendezvouseService.deletevirtual(rendezvouse);
			this.rendezvouseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();

	}

	// Test Delete ----------------------------------------------------------------------------------
	//Se comprueba el delete y que solo el admin puede eliminar las rendezvouses del sistema
	//Caso de uso 6.2: Remove a rendezvous that he or she thinks is inappropriate
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina el rendezvouse1 por el user que la ha creado (Solo puede eliminarlas el admin)
				"user1", "rendezvouse1", IllegalArgumentException.class
			}, {
				//Se elimina el rendezvouse1 por el admin
				"admin", "rendezvouse1", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int rendezvouseId, final Class<?> expected) {
		Rendezvouse rendezvouse;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			this.rendezvouseService.delete(rendezvouse);
			this.unauthenticate();
			this.rendezvouseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}

	// Test listAssist ------------------------------------------------------
	// Se comprueba el listar las Rendezvouses para poder asistir
	//Caso de uso 5.5: List the rendezvouses that he or she’s RSVPd. (parte 1)
	@Test
	public void driverListAssist() {
		final Object testingData[][] = {
			{
				//El user 1 lista las rendezvouses para asistir y aparece la rendezvous 3 porque puede asistir a ella
				"user1", "rendezvouse3", null
			}, {
				//El user 2 lista las rendezvouses para asistir y no aparece la rendezvous 1 porque ya asiste a ella
				"user2", "rendezvouse1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListAssist((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateListAssist(final String username, final int rendezvouseId, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		Collection<Rendezvouse> listAssists;
		User userPrincipal;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			userPrincipal = this.userService.findByPrincipal();
			listAssists = this.rendezvouseService.assistantToRendezvouse(userPrincipal);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			Assert.isTrue(listAssists.contains(rendezvouse));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	// Test listNotAssist ------------------------------------------------------
	// Se comprueba el listar las Rendezvouses para poder asistir
	//Caso de uso 5.5: List the rendezvouses that he or she’s RSVPd. (parte 2)
	@Test
	public void driverListNotAssist() {
		final Object testingData[][] = {
			{
				//El user 1 lista las rendezvouses para cancelar la asistencia y aparece la rendezvous 2 porque ya asiste a ella
				"user3", "rendezvouse3", null
			}, {
				//El user 2 lista las rendezvouses para cancelar la asistencia y no aparece la rendezvous 3 porque no asiste a ella
				"user2", "rendezvouse3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListNotAssist((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateListNotAssist(final String username, final int rendezvouseId, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		Collection<Rendezvouse> listNotAssists;
		User userPrincipal;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			userPrincipal = this.userService.findByPrincipal();
			listNotAssists = this.rendezvouseService.CancelMyassistantToRendezvouse(userPrincipal);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			Assert.isTrue(listNotAssists.contains(rendezvouse));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	// Test Assist ----------------------------------------------------------------------------------
	// Se comprueba la asistencia (RSPV) a una rendezvous
	//Caso de uso 5.4: RSVP a rendezvous or cancel it. (parte 1)
	@Test
	public void driverAssist() {
		final Object testingData[][] = {
			{
				//El user 1 asiste al rendezvouse3
				"user1", "rendezvouse3", null
			}, {
				//El user 2 mayor de edad asiste al rendezvouse2 para mayores de edad
				"user2", "rendezvouse2", null
			}, {
				//El user 5 menor de edad intenta asistir al rendezvouse2 de mayores de edad
				"user5", "rendezvouse2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAssist((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateAssist(final String username, final int rendezvouseId, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		User userPrincipal;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			userPrincipal = this.userService.findByPrincipal();
			this.rendezvouseService.assist(rendezvouseId);
			this.rendezvouseService.flush();
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			userPrincipal = this.userService.findByPrincipal();
			Assert.isTrue(rendezvouse.getAssistants().contains(userPrincipal));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		this.unauthenticate();

	}

	// Test Not-Assist ----------------------------------------------------------------------------------
	// Se comprueba la no asistencia (RSPV) a una rendezvous
	//Caso de uso 5.4: RSVP a rendezvous or cancel it. (parte 2)
	@Test
	public void driverNotAssist() {
		final Object testingData[][] = {
			{
				//El user 5 no asiste al rendezvouse1
				"user5", "rendezvouse1", null
			}, {
				//El user 2 no asiste al rendezvouse1
				"user2", "rendezvouse1", null
			}, {
				//El manager1 no asiste a la rendezvouse1 (ningun manager puede asistir o no asistir a ninguna rendezvouse)
				"manager1", "rendezvouse1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateNotAssist((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateNotAssist(final String username, final int rendezvouseId, final Class<?> expected) {
		Rendezvouse rendezvouse;
		User userPrincipal;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			this.rendezvouseService.unassist(rendezvouseId);
			this.rendezvouseService.flush();
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			userPrincipal = this.userService.findByPrincipal();
			Assert.isTrue(!rendezvouse.getAssistants().contains(userPrincipal));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		this.unauthenticate();

	}

	// Test LinkSimilar ----------------------------------------------------------------------------------
	// Caso de uso 16.4: Link one of the rendezvouses that he or she’s created to other similar rendezvouses. (Parte 1)
	@SuppressWarnings("unchecked")
	@Test
	public void driverLinkSimilar() {
		Collection<Rendezvouse> similarRendezvousesForTesting;

		similarRendezvousesForTesting = new ArrayList<Rendezvouse>();
		similarRendezvousesForTesting.addAll(this.rendezvouseService.findOne(super.getEntityId("rendezvouse2")).getSimilarRendezvouses());
		final Object testingData[][] = {
			{
				//El user1 que ha creado la Rendezvouse 1 cambia las similar rendezvouses
				"user1", "rendezvouse1", similarRendezvousesForTesting, null
			}, {
				//El user2 que ha creado la Rendezvouse 2 que se encuentra en modo final cambia las similar rendezvouses
				"user2", "rendezvouse2", similarRendezvousesForTesting, null
			}, {
				//El user2 que NO ha creado la Rendezvouse 1 cambia las similar rendezvouses
				"user2", "rendezvouse1", similarRendezvousesForTesting, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLinkSimilar((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Collection<Rendezvouse>) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateLinkSimilar(final String username, final int rendezvouseId, final Collection<Rendezvouse> similarRendezvousesForTesting, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			rendezvouse.setSimilarRendezvouses(similarRendezvousesForTesting);
			this.rendezvouseService.linkSimilar(rendezvouse);
			this.rendezvouseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		this.unauthenticate();

	}

	// Test UnLinkSimilar ----------------------------------------------------------------------------------
	// Caso de uso 16.4: Link one of the rendezvouses that he or she’s created to other similar rendezvouses. (Parte 2)
	@SuppressWarnings("unchecked")
	@Test
	public void driverUnLinkSimilar() {
		Collection<Rendezvouse> similarRendezvousesForTesting;

		similarRendezvousesForTesting = new ArrayList<Rendezvouse>();
		similarRendezvousesForTesting.addAll(this.rendezvouseService.findOne(super.getEntityId("rendezvouse2")).getSimilarRendezvouses());
		final Object testingData[][] = {
			{
				//El user1 que ha creado la Rendezvouse 1 cambia las similar rendezvouses
				"user1", "rendezvouse1", similarRendezvousesForTesting, null
			}, {
				//El user2 que NO ha creado la Rendezvouse 1 cambia las similar rendezvouses
				"user2", "rendezvouse1", similarRendezvousesForTesting, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateUnLinkSimilar((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Collection<Rendezvouse>) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateUnLinkSimilar(final String username, final int rendezvouseId, final Collection<Rendezvouse> similarRendezvousesForTesting, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			rendezvouse.setSimilarRendezvouses(similarRendezvousesForTesting);
			this.rendezvouseService.unlinkSimilar(rendezvouse);
			this.rendezvouseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		this.unauthenticate();

	}

	// Test listNonAutenticated
	// Se comprueba el metodo list para los usuarios no autentificados
	// Caso de uso 4.3: List the users of the system and navigate to their profiles, which include personal data and the list of rendezvouses that they’ve attended or are going to attend.
	@Test
	public void driverlistNonAutenticated() {
		final Object testingData[][] = {
			{
				//La rendezvouse 3 No es para mayores de edad y esta en modo final asi que debe aparecer para los usuarios no autentificados
				"rendezvouse3", null
			}, {
				//La rendezvouse 2 es para mayores de edad y esta en modo final asi que NO debe aparecer para los usuarios no autentificados
				"rendezvouse2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templatelistNonAutenticated((super.getEntityId((String) testingData[i][0])), (Class<?>) testingData[i][1]);
	}
	private void templatelistNonAutenticated(final int rendezvouseId, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		Collection<Rendezvouse> listNonAutenticated;
		Class<?> caught;

		caught = null;
		try {
			listNonAutenticated = this.rendezvouseService.findAllMinusAdult();
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			Assert.isTrue(listNonAutenticated.contains(rendezvouse));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	// Test listSimilar -----------------------------------------------------
	// Caso de uso 15.2: Navigate from a rendezvous to the rendezvouses that are similar to it.
	@Test
	public void driverListSimilar() {
		final Object testingData[][] = {
			{
				//La rendezvouse 5 Tiene como similar a la Rendezvous 1
				"rendezvouse5", "rendezvouse1", null
			}, {
				//La rendezvouse 5 NO tiene como similar a la Rendezvous 2
				"rendezvouse5", "rendezvouse2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListSimilar((super.getEntityId((String) testingData[i][0])), (super.getEntityId((String) testingData[i][1])), (Class<?>) testingData[i][2]);
	}
	private void templateListSimilar(final int rendezvouseId, final int similarRendezvouseId, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		final Rendezvouse similarRendezvouse;
		Collection<Rendezvouse> similarRendezvouses;
		Class<?> caught;

		caught = null;
		try {
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			similarRendezvouse = this.rendezvouseService.findOne(similarRendezvouseId);
			similarRendezvouses = rendezvouse.getSimilarRendezvouses();
			Assert.isTrue(similarRendezvouses.contains(similarRendezvouse));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	// Test listFindRendezvousByCategory ----------------------------------------------------
	// Caso de uso 10.1: List the rendezvouses in the system grouped by category
	@Test
	public void driverlistFindRendezvousByCategory() {
		final Object testingData[][] = {
			{
				//La rendezvouse1 solicita un servicio con la category1
				"rendezvouse1", "category1", null
			}, {
				//La rendezvouse1 solicita un servicio con la category2
				"rendezvouse1", "category1-1", null
			}, {
				//La rendezvouse2 NO solicita un servicio con la category1
				"rendezvouse2", "category1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templatelistFindRendezvousByCategory((super.getEntityId((String) testingData[i][0])), (super.getEntityId((String) testingData[i][1])), (Class<?>) testingData[i][2]);
	}
	private void templatelistFindRendezvousByCategory(final int rendezvouseId, final int categoryId, final Class<?> expected) {
		final Rendezvouse rendezvouse;
		Collection<Rendezvouse> listRendezvousesByCategoryId;
		Class<?> caught;

		caught = null;
		try {
			listRendezvousesByCategoryId = this.rendezvouseService.findRendezvousByCategory(categoryId);
			rendezvouse = this.rendezvouseService.findOne(rendezvouseId);
			Assert.isTrue(listRendezvousesByCategoryId.contains(rendezvouse));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	//	//Other Methods additionals---------------------------------------------------------------------------------------

	private Collection<GPS> createAllGPSForTesting() {
		final Collection<GPS> result;
		final GPS gpsOk;
		final GPS gpsNullLatitude;
		final GPS gpsNullLongitude;
		final GPS gpsOutOfRangeLatitudeMax;
		final GPS gpsOutOfRangeLatitudeMin;
		final GPS gpsOutOfRangeLongitudeMax;
		final GPS gpsOutOfRangeLongitudeMin;

		result = new ArrayList<GPS>();

		gpsOk = new GPS();
		gpsOk.setLatitude(-89.5);
		gpsOk.setLongitude(179.0);
		result.add(gpsOk);

		gpsNullLatitude = new GPS();
		gpsNullLatitude.setLatitude(null);
		gpsNullLatitude.setLongitude(-179.0);
		result.add(gpsNullLatitude);

		gpsNullLongitude = new GPS();
		gpsNullLongitude.setLatitude(89.0);
		gpsNullLongitude.setLongitude(null);
		result.add(gpsNullLongitude);

		gpsOutOfRangeLatitudeMax = new GPS();
		gpsOutOfRangeLatitudeMax.setLatitude(95.0);
		gpsOutOfRangeLatitudeMax.setLongitude(0.0);
		result.add(gpsOutOfRangeLatitudeMax);

		gpsOutOfRangeLatitudeMin = new GPS();
		gpsOutOfRangeLatitudeMin.setLatitude(-91.0);
		gpsOutOfRangeLatitudeMin.setLongitude(0.0);
		result.add(gpsOutOfRangeLatitudeMin);

		gpsOutOfRangeLongitudeMax = new GPS();
		gpsOutOfRangeLongitudeMax.setLatitude(0.0);
		gpsOutOfRangeLongitudeMax.setLongitude(181.0);
		result.add(gpsOutOfRangeLongitudeMax);

		gpsOutOfRangeLongitudeMin = new GPS();
		gpsOutOfRangeLongitudeMin.setLatitude(0.0);
		gpsOutOfRangeLongitudeMin.setLongitude(-181.0);
		result.add(gpsOutOfRangeLongitudeMin);

		return result;
	}
	
	
	protected void createAndSaveTemplate(final String userName, final String title, final String description, final String moment, final String ticker, final String mode, final int maxRows, final int maxColumns, final String status, final String rejectionReason, final Class<?> expected) {
		Class<?> caught;
		final Enrolment enrolment;
		Brotherhood brotherhood;
		Member member;
		Date momentDate;
		Date dropOutDate;
		Position position;
		int brotherhoodId;
		int memberId;
		int positionBeanNameId;
		caught = null;
		try {
			this.authenticate(userName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);
			memberId = super.getEntityId(memberBeanName);
			positionBeanNameId = super.getEntityId(positionBeanName);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			member = this.memberService.findOne(memberId);
			position = this.positionService.findOne(positionBeanNameId);

			enrolment = new Enrolment();
			enrolment.setBrotherhood(brotherhood);
			enrolment.setMember(member);
			enrolment.setEnrolled(enrolled);
			enrolment.setPosition(position);
			if (moment != null)
				momentDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(moment);
			else
				momentDate = null;
			enrolment.setMoment(momentDate);
			if (dropOut != null)
				dropOutDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(dropOut);
			else
				dropOutDate = null;
			enrolment.setDropOut(dropOutDate);

			this.enrolmentService.save(enrolment, brotherhoodId);
			this.enrolmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
