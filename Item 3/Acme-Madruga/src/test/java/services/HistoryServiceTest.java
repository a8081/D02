
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
	private InceptionRecordService	inceptionRecordService;
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
				"brotherhood1", null
			}, {
				"member1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateCreateSave(final String brotherhood, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(brotherhood);
			final History history = this.historyService.create();
			final History saved = this.historyService.save(history);
			Assert.isTrue(saved.getId() != 0);
			this.historyService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
