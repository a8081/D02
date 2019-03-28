
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.GPS;
import domain.Parade;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SegmentServiceTest extends AbstractTest {

	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;


	/* ========================= Test Create Segment =========================== */

	@Test
	public void driverCreateSegment() {

		final Object testingData[][] = {
			{
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Positivo: Creacion correcta de un segment
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos
				"brotherhood1", "parade1", "2020/03/16 15:20", "2020/03/16 15:40", 0.0, 0.0, 0.0, 0.0, null
			//			}
			//			, {
			// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
			//				showing, creating, updating, and deleting them.
			//				B: Test Negativo: Creacion incorrecta de un segment con fecha vacía
			//				C: % Recorre 8 de la 23 lineas posibles
			//				D: cobertura de datos=Combinaciones con sentido/numero atributos
			//				"brotherhood1", "parade1", "2020/03/16 15:20", "", ParseException.class
			//			}, {
			// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
			//				showing, creating, updating, and deleting them.
			//				B: Test Negativo: Creacion incorrecta de un segment con parametro a null
			//				C: % Recorre 8 de la 23 lineas posibles
			//				D: cobertura de datos=Combinaciones con sentido/numero atributos
			//				"brotherhood1", "parade1", null, "2020/03/16 15:40", NullPointerException.class
			//			}, {
			// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
			//				showing, creating, updating, and deleting them.
			//				B: Test Negativo: Creacion incorrecta de un segment con brotherhood vacia
			//				C: % Recorre 8 de la 23 lineas posibles
			//				D: cobertura de datos=Combinaciones con sentido/numero atributos
			//				"", "parade1", "2020/03/16 15:20", "2020/03/16 15:40", IllegalArgumentException.class
			//			}, {
			// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
			//				showing, creating, updating, and deleting them.
			//				B: Test Negativo: Creacion incorrecta de un segment con parade vacía
			//				C: % Recorre 8 de la 23 lineas posibles
			//				D: cobertura de datos=Combinaciones con sentido/numero atributos
			//				"brotherhood1", "", "2020/03/16 15:20", "2020/03/16 15:40", AssertionError.class
			//			}, {
			// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
			//				showing, creating, updating, and deleting them.
			//				B: Test Negativo: Creacion incorrecta de un segment con parade invalida
			//				C: % Recorre 8 de la 23 lineas posibles
			//				D: cobertura de datos=Combinaciones con sentido/numero atributos
			//				"brotherhood1", "parade2", "2020/03/16 15:20", "2020/03/16 15:40", IllegalArgumentException.class
			//			}, {
			// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
			//				showing, creating, updating, and deleting them.
			//				B: Test Negativo: Creacion incorrecta de un segment con fecha destino anterior a fecha origen
			//				C: % Recorre 8 de la 23 lineas posibles
			//				D: cobertura de datos=Combinaciones con sentido/numero atributos
			//				"brotherhood1", "parade1", "2020/03/16 15:40", "2020/03/16 15:00", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSegment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Double) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	private void templateCreateSegment(final String brotherhood, final String parade, final String originTime, final Double originLatitude, final Double originLongitude, final Double destinationLatitude, final Double destinationLongitude,
		final String destinationTime, final Class<?> expected) {

		Class<?> caught;

		Date timeOrigin;
		Date timeDestination;

		caught = null;

		try {

			final Parade p = this.paradeService.findOne(this.getEntityId(parade));
			this.authenticate(brotherhood);

			final Segment segment = this.segmentService.create();

			timeOrigin = (new SimpleDateFormat("yyyy/MM/dd HH:mm")).parse(originTime);
			timeDestination = (new SimpleDateFormat("yyyy/MM/dd HH:mm")).parse(destinationTime);

			segment.setOriginTime(timeOrigin);
			segment.setDestinationTime(timeDestination);

			final Segment saved = this.segmentService.save(segment, p.getId());
			p.getSegments().add(segment);
			this.segmentService.flush();

			Assert.notNull(saved);
			Assert.isTrue(saved.getId() != 0);
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}

	/* ========================= Test Save Segment =========================== */

	@Test
	public void driverSaveSegment() {

		final Object testingData[][] = {
			{
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Positivo: Guardado correcto de un segment
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos				
				"brotherhood1", "parade1", "2020/03/16 15:20", "2020/03/16 15:40", 40.89654, 10.23568, 41.89654, 11.23568, null
			}, {
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Negativo: Guardado incorrecto de un segment con fecha vacia
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos	
				"brotherhood1", "parade1", "2020/03/16 15:20", "", 42.89654, 12.23568, 41.89654, 11.23568, ParseException.class
			}, {
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Negativo: Guardado incorrecto de un segment con parametro null
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos				
				"brotherhood1", "parade1", "2020/03/16 15:20", null, 41.89654, 11.23568, 42.89654, 12.23568, NullPointerException.class
			}, {
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Negativo: Guardado incorrecto de un segment con brotherhood vacia
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos
				"", "parade1", "2020/03/16 15:20", "2020/03/16 15:40", 43.89654, 13.23568, 44.89654, 14.23568, IllegalArgumentException.class
			}, {
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Negativo: Guardado incorrecto de un segment con parade vacia
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos
				"brotherhood1", "", "2020/03/16 15:20", "2020/03/16 15:40", 45.89654, 15.23568, 46.89654, 16.23568, AssertionError.class
			}, {
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Negativo: Guardado incorrecto de un segment con parade no correspondiente a esa brotherhood
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos
				"brotherhood1", "parade2", "2020/03/16 15:20", "2020/03/16 15:40", 47.89654, 17.23568, 48.89654, 18.23568, IllegalArgumentException.class
			}, {
				// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
				//				showing, creating, updating, and deleting them.
				//				B: Test Negativo: Guardado incorrecto de un segment con fecha destino anterior a fecha origen
				//				C: % Recorre 8 de la 23 lineas posibles
				//				D: cobertura de datos=Combinaciones con sentido/numero atributos
				"brotherhood1", "parade1", "2020/03/16 15:40", "2020/03/16 15:00", 49.89654, 19.23568, 40.89654, 10.23568, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSaveSegment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Double) testingData[i][6],
				(Double) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	private void templateSaveSegment(final String brotherhood, final String parade, final String originTime, final String destinationTime, final Double originLatitude, final Double originLongitude, final Double destinationLatitude,
		final Double destinationLongitude, final Class<?> expected) {

		Class<?> caught;
		Segment segment;

		GPS origin;
		GPS destination;

		final Date timeOrigin;
		final Date timeDestination;

		caught = null;

		try {
			final Parade p = this.paradeService.findOne(this.getEntityId(parade));

			this.authenticate(brotherhood);

			origin = new GPS();
			origin.setLatitude(originLatitude);
			origin.setLongitude(originLongitude);

			destination = new GPS();
			destination.setLatitude(destinationLatitude);
			destination.setLongitude(destinationLongitude);

			timeOrigin = (new SimpleDateFormat("yyyy/MM/dd HH:mm")).parse(originTime);

			timeDestination = (new SimpleDateFormat("yyyy/MM/dd HH:mm")).parse(destinationTime);

			segment = new Segment();
			segment.setOriginTime(timeOrigin);
			segment.setDestinationTime(timeDestination);

			segment.setOriginCoordinates(origin);
			segment.setDestinationCoordinates(destination);

			segment = this.segmentService.save(segment, p.getId());
			this.segmentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);
		this.unauthenticate();
	}

	/* ========================= Test Delete Segment =========================== */

	// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
	//				showing, creating, updating, and deleting them.
	//				B: Test Positivo: Borrado correcto de un segment
	//				C: % Recorre 8 de la 23 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos
	@Test
	public void testDeleteSegment() {
		Segment segment;
		Parade parade;

		segment = this.segmentService.findOne(this.getEntityId("segment2"));
		parade = this.paradeService.findOne(this.getEntityId("parade1"));

		super.authenticate("brotherhood1");
		this.segmentService.delete(segment, parade.getId());
		super.unauthenticate();

	}
	// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
	//				showing, creating, updating, and deleting them.
	//				B: Test Negativo: Guardado incorrecto de un segment, No se puede borrar un segmento si no es el ultimo del path
	//				C: % Recorre 8 de la 23 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteSegmentInvalid() {
		Segment segment;
		Parade parade;

		segment = this.segmentService.findOne(this.getEntityId("segment1"));
		parade = this.paradeService.findOne(this.getEntityId("parade1"));

		super.authenticate("brotherhood1");
		this.segmentService.delete(segment, parade.getId());
		super.unauthenticate();

	}

	// 				A: Acme Parade - Functional Req. 3.3 Manage the paths of their parades, which includes listing,
	//				showing, creating, updating, and deleting them.
	//				B: Test Negativo: Guardado incorrecto de un segment, El usuario logueado debe ser la hermandad que tiene la parade a la que corresponde ese segmento
	//				C: % Recorre 8 de la 23 lineas posibles
	//				D: cobertura de datos=Combinaciones con sentido/numero atributos	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteLogInvalid() {
		Segment segment;
		Parade parade;

		segment = this.segmentService.findOne(this.getEntityId("segment3"));
		parade = this.paradeService.findOne(this.getEntityId("parade1"));

		super.authenticate("brotherhood1");
		this.segmentService.delete(segment, parade.getId());
		super.unauthenticate();

	}
}
