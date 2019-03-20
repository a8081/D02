
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{		// Parade correcta
				"brotherhood1", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "SUBMITTED", null, null
			}, {		// Guardar Parade a otra brotherhood 
				"brotherhood2", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "SUBMITTED", null, IllegalArgumentException.class
			}

		//					{		// Parade correcta
		//						"brotherhood1", "brotherhood1", "", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "SUBMITTED", null, null
		//					}, {		// Parade correcta
		//						"brotherhood1", "brotherhood1", "Title1", "", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "SUBMITTED", null, null
		//					}, {		// Parade correcta
		//						"brotherhood1", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "", "DRAFT", 120, 120, "SUBMITTED", null, null
		//					}, {		// Parade correcta
		//						"brotherhood1", "brotherhood1", "Title1", "Description1", "2019-12-12 20:00", "djehfahwrlfhajsdhfashdflov", "DRAFT", 120, 120, "SUBMITTED", null, null
		//					}
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
}
