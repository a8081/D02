
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import utilities.AbstractTest;
import domain.History;
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	// Services
	@Autowired
	private HistoryService			historyService;
	@Autowired
	private InceptionRecordService	inceotionRecordService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private BrotherhoodService		brotherhoodService;

	//Repositorys
	@Autowired
	private HistoryRepository		historyRepository;


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverCreateSave() {
		final Object testingData[][] = {
			{
				"brotherhood1", new InceptionRecord(), null
			}, {
				"member1", new InceptionRecord(), IllegalArgumentException.class
			}, {
				"brotherhood1", null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (InceptionRecord) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCreateSave(final String brotherhood, final InceptionRecord iR, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(brotherhood);
			if (iR != null) {
				iR.setDescription("DescriptionTest");
				iR.setTitle("TitleTest");
			}
			final History history = this.historyService.create();
			history.setInceptionRecord(iR);
			final History saved = this.historyService.save(history);
			Assert.isTrue(saved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final History history1 = this.historyService.findOne(2210);
		final History history2 = this.historyService.findOne(2214);
		final Object testingData[][] = {
			{
				"brotherhood1", history1, "InceptionTest", "DescriptionTest", null
			}, {
				"member1", history1, "InceptionTest", "DescriptionTest", IllegalArgumentException.class
			}, {
				"brotherhood1", history2, "InceptionTest", "DescriptionTest", IllegalArgumentException.class
			}, {
				"brotherhood1", history1, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (History) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateEdit(final String brotherhood, final History history, final String title, final String description, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(brotherhood);
			final InceptionRecord iR = history.getInceptionRecord();
			iR.setTitle(title);
			iR.setDescription(description);
			history.setInceptionRecord(iR);
			final History saved = this.historyService.save(history);
			Assert.isTrue(saved.getId() != 0);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
