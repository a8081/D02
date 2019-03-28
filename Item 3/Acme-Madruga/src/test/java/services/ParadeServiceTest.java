
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Float;
import domain.Parade;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private FloatService		floatService;

	@Autowired
	private ChapterService		chapterService;

	@PersistenceContext
	private EntityManager		entityManager;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Negativo: Brotherhood crea Parade sin titutlo
				//C: Líneas totales de código = 90 ,Líneas recorrida = 89, Porcentaje = 98,88%.
				//D: Cobertura de datos = 11/36
				"brotherhood2", "brotherhood2", null, "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, ConstraintViolationException.class
			}, {
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Negativo: Brotherhood crea Parade con titulo nulo
				//C: Líneas totales de código = 90 ,Líneas recorrida = 89 , Porcentaje = 98,88%.
				//D: Cobertura de datos = 11/36
				"brotherhood2", "brotherhood2", null, "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, ConstraintViolationException.class
			}, {
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Negativo: Brotherhood crea Parade sin descripcion
				//C: Líneas totales de código = 90 ,Líneas recorrida = 89 , Porcentaje = 98,88%.
				//D: Cobertura de datos = 11/36
				"brotherhood2", "brotherhood2", "Title1", "", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, ConstraintViolationException.class
			}, {
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Positivo: Brotherhood crea Parade sin mode, status ni ticker.
				//C: Líneas totales de código = 90 ,Líneas recorrida = 90 , Porcentaje = 100%.
				//D: Cobertura de datos = 11/36
				"brotherhood2", "brotherhood2", "Title1", "Description1", "2019-12-12 20:00", "", "", 120, 120, "", null, null
			}, {
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Negativo: Brotherhood crea Parade nueva con rejection reason seteada
				//C: Líneas totales de código = 90 ,Líneas recorrida = 85 , Porcentaje = 94,44%.
				//D: Cobertura de datos = 11/36
				"brotherhood2", "brotherhood2", "Title1", "Description1", "2019-12-12 20:00", "", "", 120, 120, "", "RejectionReason1", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Positivo: Brotherhood crea Parade correcta
				//C: Líneas totales de código = 90 ,Líneas recorrida = 90 , Porcentaje = 100%.
				//D: Cobertura de datos = 11/36
				"brotherhood1", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, null
			}, {
				//A: Acme Parade Req -> Manage the parades that are published by the brotherhoods in the area that they co-ordinate.
				//B: Test Negativo: Brotherhood crea Parade a otra brotherhood
				//C: Líneas totales de código = 90 ,Líneas recorrida = 84, Porcentaje = 93,33%.
				//D: Cobertura de datos = 11/36
				"brotherhood2", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(int) testingData[i][7], (int) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void updateDriver() {
		final Object testingData[][] = {
			{
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Positivo: Brotherhood guarda Parade sin ningun cambio.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 85, Porcentaje = 100%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "This is a description of a parade13", "2022-04-26 00:00:00", "203486-QUODF", "DRAFT", 120, 300, "DEFAULT", null, "parade13", null
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade con titulo nulo.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 84, Porcentaje = 98,82%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", null, "This is a description of a parade13", "2022-04-26 00:00:00", "203486-QUODF", "DRAFT", 120, 300, "DEFAULT", null, "parade13", ConstraintViolationException.class
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade con descripcion vacia.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 84, Porcentaje = 98,82%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "", "2022-04-26 00:00:00", "203486-QUODF", "DRAFT", 120, 300, "DEFAULT", null, "parade13", ConstraintViolationException.class
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade sin status
				//C: Líneas totales de código = 85 ,Líneas recorrida = 78, Porcentaje = 91,76%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "This is a description of a parade13", "2022-04-26 00:00:00", "203486-QUODF", "DRAFT", 120, 300, "", null, "parade13", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade en draft mode con status accepted.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 79, Porcentaje = 92,94%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "This is a description of a parade13", "2022-04-26 00:00:00", "203486-QUODF", "DRAFT", 120, 300, "ACCEPTED", null, "parade13", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade sin mode.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 84, Porcentaje = 98,82%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "This is a description of a parade13", "2022-04-26 00:00:00", "203486-QUODF", "", 120, 300, "DEFAULT", null, "parade13", ConstraintViolationException.class
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade sin ticker.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 79, Porcentaje = 92,94%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "This is a description of a parade13", "2022-04-26 00:00:00", "", "DRAFT", 120, 300, "DEFAULT", null, "parade13", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> An actor who is authenticated as a brotherhood must be able to: 
				//Manage the paths of their parades, which includes updating.
				//B: Test Negativo: Brotherhood guarda Parade con ticker modificado.
				//C: Líneas totales de código = 85 ,Líneas recorrida = 78, Porcentaje = 91,76%.
				//D: Cobertura de datos = 12/36
				"brotherhood1", "brotherhood1", "Los Javieres", "This is a description of a parade13", "2022-04-26 00:00:00", "200006-QUADF", "DRAFT", 120, 300, "DEFAULT", null, "parade13", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.updateTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					(int) testingData[i][7], (int) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	@Test
	public void toFinalModeDriver() {
		final Object testingData[][] = {
			{
				//A: Acme Parade Req -> Manage the paths of their parades, which includes listing, showing, creating, updat-ing, and deleting them.
				//B: Test Positivo: Brotherhood tiene una Parade en draft mode con status accepted, to final mode correcto.
				//C: Líneas totales de código = 89 ,Líneas recorrida = 89, Porcentaje = 100%.
				//D: Cobertura de datos = 2/7
				"brotherhood1", "parade13", null
			}, {
				//A: Acme Parade Req -> Manage the paths of their parades, which includes listing, showing, creating, updat-ing, and deleting them.
				//B: Test Negativo: Brotherhood tiene una Parade en modo final una parade que tiene el estado rejected
				//C: Líneas totales de código = 89 ,Líneas recorrida = 89, Porcentaje = 100%.
				//D: Cobertura de datos = 2/7
				"brotherhood1", "parade2", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> Manage the paths of their parades, which includes listing, showing, creating, updat-ing, and deleting them.
				//B: Test Negativo: Brotherhood intenta editar una Parade to final mode cuando ya esta en final mode.
				//C: Líneas totales de código = 89 ,Líneas recorrida = 78, Porcentaje = 87,64%.
				//D: Cobertura de datos = 2/7
				"brotherhood1", "parade11", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> Manage the paths of their parades, which includes listing, showing, creating, updat-ing, and deleting them.
				//B: Test Negativo: Brotherhood intenta poner una Parade que no es suya en final mode
				//C: Líneas totales de código = 89 ,Líneas recorrida = 76, Porcentaje = 85,39%.
				//D: Cobertura de datos = 2/7
				"brotherhood2", "parade11", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.toFinalModeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void acceptParadeDriver() {
		final Object testingData[][] = {
			{
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Positivo: Un chapter acepta una parade en final mode con estado submitted, accion correcta
				//C: Líneas totales de código = 49 ,Líneas recorrida = 49, Porcentaje = 100%.
				//D: Cobertura de datos =
				"chapter1", "parade1", null
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un actor que no es chapter acepta una parade, accion incorrecta
				//C: Líneas totales de código = 49 ,Líneas recorrida = 29, Porcentaje = 59,18%.
				//D: Cobertura de datos =
				"brotherhood1", "parade1", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un chapter acepta una parade en draft mode con estado default, accion incorrecta
				//C: Líneas totales de código = 49 ,Líneas recorrida = 31, Porcentaje = 63,26%
				//D: Cobertura de datos =
				"chapter1", "parade13", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un chapter acepta una parade en final mode con estado accepted, accion incorrecta
				//C: Líneas totales de código = 49 ,Líneas recorrida = 29, Porcentaje = 59,18%.
				//D: Cobertura de datos =
				"chapter1", "parade2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.acceptParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void rejectParadeDriver() {
		final Object testingData[][] = {
			{
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Positivo: Un chapter rechaza una parade en final mode con estado submitted, accion correcta
				//C: Líneas totales de código = 52 ,Líneas recorrida = 52, Porcentaje = 100%.
				//D: Cobertura de datos = 3/9
				"chapter1", "parade1", "My Rejection Reason", null
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un actor que no es chapter rechaza una parade, accion incorrecta
				//C: Líneas totales de código = 52 ,Líneas recorrida = 33, Porcentaje = 63,46%.
				//D: Cobertura de datos = 3/9
				"brotherhood1", "parade1", "My Rejection Reason", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un chapter rechaza una parade en draft mode con estado default, accion incorrecta
				//C: Líneas totales de código = 52 ,Líneas recorrida = 28, Porcentaje = 53,84%.
				//D: Cobertura de datos = 3/9
				"chapter1", "parade13", "My Rejection Reason", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un chapter rechaza una parade en final mode con estado accepted, accion incorrecta
				//C: Líneas totales de código = 52 ,Líneas recorrida = 44, Porcentaje = 84,61%.
				//D: Cobertura de datos = 3/9
				"chapter1", "parade2", "My Rejection Reason", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un chapter rechaza una parade y no añade rejection reason, accion incorrecta
				//C: Líneas totales de código = 52 ,Líneas recorrida = 42, Porcentaje = 80,76%.
				//D: Cobertura de datos = 3/9
				"chapter1", "parade1", "", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req.6 -> When a parade is saved in final mode, it must be kept 
				//in status "submitted" until the corresponding chapter makes a decision on accepting or rejecting it.
				//B: Test Negativo: Un chapter rechaza una parade con rejection reason en null, accion incorrecta
				//C: Líneas totales de código = 52 ,Líneas recorrida = 37, Porcentaje = 71,15%.
				//D: Cobertura de datos = 3/9
				"chapter1", "parade1", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.rejectParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void copyBrotherhoodParadeDriver() {
		final Object testingData[][] = {
			{
				//A: Acme Parade Req -> Make a copy of one of their parades. 
				//B: Test Positivo: Una Brotherhood hace una copia de una Parade, accion correcta
				//C: Líneas totales de código = 83 ,Líneas recorrida = 83, Porcentaje = 100%.
				//D: Cobertura de datos = 2/8
				"brotherhood1", "parade1", null
			}, {
				//A: Acme Parade Req -> Make a copy of one of their parades. 
				//B: Test Negativo: Una Brotherhood hace una copia de una Parade que no es suya, accion incorrecta
				//C: Líneas totales de código = 83 ,Líneas recorrida = 51, Porcentaje = 61,44%.
				//D: Cobertura de datos = 2/8
				"brotherhood2", "parade1", IllegalArgumentException.class
			}, {
				//A: Acme Parade Req -> Make a copy of one of their parades. 
				//B: Test Negativo: Un chapter rechaza hace una copia de una Parade, accion incorrecta
				//C: Líneas totales de código = 83 ,Líneas recorrida = 45, Porcentaje = 54,22%.
				//D: Cobertura de datos = 2/8
				"chapter1", "parade1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.copyBrotherhoodParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	protected void createAndSaveTemplate(final String userName, final String brotherhoodBeanName, final String title, final String description, final String moment, final String ticker, final String mode, final int maxRows, final int maxColumns,
		final String status, final String rejectionReason, final Class<?> expected) {

		Class<?> caught;
		final List<Segment> segments = new ArrayList<>();
		final Collection<Float> floats = new ArrayList<>();
		final Parade parade;
		Brotherhood brotherhood;
		Date momentDate;
		int brotherhoodId;
		caught = null;
		try {

			this.authenticate(userName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);

			final Collection<Float> floatsBrotherhood = this.floatService.findByBrotherhood(brotherhood);

			if (!floatsBrotherhood.isEmpty()) {
				final Float f = (Float) floatsBrotherhood.toArray()[0];
				floats.add(f);
			}
			parade = new Parade();
			parade.setBrotherhood(brotherhood);
			parade.setRejectionReason(rejectionReason);
			parade.setTitle(title);
			parade.setDescription(description);
			parade.setTicker(ticker);
			parade.setStatus(status);
			parade.setMode(mode);
			parade.setMaxColumns(maxColumns);
			parade.setMaxRows(maxRows);
			parade.setFloats(floats);
			parade.setSegments(segments);

			if (moment != null)
				momentDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(moment);
			else
				momentDate = null;
			parade.setMoment(momentDate);

			this.paradeService.save(parade);
			this.unauthenticate();
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
	}

	protected void updateTemplate(final String userName, final String brotherhoodBeanName, final String title, final String description, final String moment, final String ticker, final String mode, final int maxRows, final int maxColumns,
		final String status, final String rejectionReason, final String paradeBeanName, final Class<?> expected) {

		Class<?> caught;
		Parade parade;
		Brotherhood brotherhood;
		Date momentDate;
		int brotherhoodId;
		int paradeId;
		caught = null;
		try {

			this.authenticate(userName);
			brotherhoodId = super.getEntityId(brotherhoodBeanName);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			paradeId = super.getEntityId(paradeBeanName);
			parade = (Parade) this.paradeService.findOne(paradeId).clone();

			parade.setBrotherhood(brotherhood);
			parade.setRejectionReason(rejectionReason);
			parade.setTitle(title);
			parade.setDescription(description);
			parade.setTicker(ticker);
			parade.setStatus(status);
			parade.setMode(mode);
			parade.setMaxColumns(maxColumns);
			parade.setMaxRows(maxRows);

			if (moment != null)
				momentDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(moment);
			else
				momentDate = null;
			parade.setMoment(momentDate);

			this.paradeService.save(parade);
			this.unauthenticate();
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
	}

	protected void acceptParadeTemplate(final String chapterUserName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		caught = null;
		try {

			this.authenticate(chapterUserName);
			paradeId = super.getEntityId(paradeBeanName);

			this.paradeService.acceptParade(paradeId);
			this.unauthenticate();
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
	}

	protected void rejectParadeTemplate(final String chapterUserName, final String paradeBeanName, final String rejectionReason, final Class<?> expected) {
		Class<?> caught;
		Parade parade;
		int paradeId;
		caught = null;
		try {

			this.authenticate(chapterUserName);
			paradeId = super.getEntityId(paradeBeanName);
			parade = this.paradeService.findOne(paradeId);
			parade.setRejectionReason(rejectionReason);

			this.paradeService.rejectParade(parade);
			this.unauthenticate();
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
	}

	protected void toFinalModeTemplate(final String brotherhoodUserName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		caught = null;
		try {

			this.authenticate(brotherhoodUserName);
			paradeId = super.getEntityId(paradeBeanName);

			this.paradeService.toFinalMode(paradeId);
			this.unauthenticate();
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
	}

	protected void copyBrotherhoodParadeTemplate(final String userName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		caught = null;
		try {
			this.authenticate(userName);
			paradeId = super.getEntityId(paradeBeanName);

			this.paradeService.copyBrotherhoodParade(paradeId);
			this.unauthenticate();
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}
	}
}
