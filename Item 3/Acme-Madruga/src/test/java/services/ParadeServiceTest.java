
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	// Parade sin titulo
				"brotherhood2", "brotherhood1", "", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, ConstraintViolationException.class
			}, {	// Parade sin descripcion
				"brotherhood2", "brotherhood1", "Title1", "", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, ConstraintViolationException.class
			}, { 	// Parade sin mode, status ni ticker. No debe fallar ya que se setea en el save
				"brotherhood2", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "", "", 120, 120, "", null, null
			}, {		// Parade correcta
				"brotherhood1", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "DEFAULT", null, null
			}, {	// Guardar Parade a otra brotherhood 
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
	public void toFinalModeDriver() {
		final Object testingData[][] = {
			{		// Parade en draft mode con status accepted, to final mode correcto
				"brotherhood1", "parade13", null
			}, {		// Poner en modo final una parade que tiene el estado rejected
				"brotherhood1", "parade2", IllegalArgumentException.class
			}, {	// To final mode una parade que ya esta en final mode
				"brotherhood1", "parade11", IllegalArgumentException.class
			}, {	// No puedes poner en final mode una parade que no es tuya
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
			{		// Un chapter acepta una parade en final mode con estado submitted, accion correcta
				"chapter1", "parade1", null
			}, {		// Un actor que no es chapter acepta una parade, accion incorrecta
				"brotherhood1", "parade1", IllegalArgumentException.class
			}, {		// Un chapter acepta una parade en draft mode con estado default, accion incorrecta
				"chapter1", "parade13", IllegalArgumentException.class
			}, {		// Un chapter acepta una parade en final mode con estado accepted, accion incorrecta
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
			{		// Un chapter rechaza una parade en final mode con estado submitted, accion correcta
				"chapter1", "parade1", null
			}, {		// Un actor que no es chapter rechaza una parade, accion incorrecta
				"brotherhood1", "parade1", IllegalArgumentException.class
			}, {		// Un chapter rechaza una parade en draft mode con estado default, accion incorrecta
				"chapter1", "parade13", IllegalArgumentException.class
			}, {		// Un chapter rechaza una parade en final mode con estado accepted, accion incorrecta
				"chapter1", "parade2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.rejectParadeTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void copyBrotherhoodParadeDriver() {
		final Object testingData[][] = {
			{		// copia correcta
				"brotherhood1", "parade1", null
			}, {	// copia incorrecta, brotherhood 2 no es el propietario de la parade 1
				"brotherhood2", "parade1", IllegalArgumentException.class
			}, {		// copia incorrecta, actores con autoridad distinta a brotherhood no tienen privilegios para copiar parades
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
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
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
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void rejectParadeTemplate(final String chapterUserName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		caught = null;
		try {

			this.authenticate(chapterUserName);
			paradeId = super.getEntityId(paradeBeanName);

			this.paradeService.rejectParade(paradeId);
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
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
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
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
			this.paradeService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
	}
}
