
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
import domain.Brotherhood;
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
	public void driver() {
		final Object testingData[][] = {
			//				A: Acme Parade Req. 3 -> Brotherhoods can manage their history
			//				B: Test Positivo: Brotherhood crea history
			//				C: 100% Recorre 80 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"brotherhood1", false, null
			},
			//				A: Acme Parade Req. 3 -> Brotherhoods can manage their history
			//				B: Test Negativo: Un member intenta borrar una history
			//				C: 10% Recorre 8 de las 80 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"member1", false, IllegalArgumentException.class
			},
			//				A: Acme Parade Req. 3 -> Brotherhoods can manage their history
			//				B: Test Positivo: Brotherhood borra su history
			//				C: 100% Recorre 64 de las 64 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"brotherhood2", true, null
			},
			//				A: Acme Parade Req. 3 -> Brotherhoods can manage their history
			//				B: Test Negativo: Un member intenta borrar una history
			//				C: 12,5% Recorre 8 de las 64 lineas posibles
			//				D: cobertura de datos=1/3
			{
				"member1", true, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSave((String) testingData[i][0], (Boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCreateSave(final String brotherhood, final Boolean delete, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(brotherhood);
			if (delete) {
				final Brotherhood bro = this.brotherhoodService.findByPrincipal();
				this.historyService.delete(bro.getHistory());
			} else {
				final History history = this.historyService.create();
				final History saved = this.historyService.save(history);
				Assert.isTrue(saved.getId() != 0);
			}
			this.historyService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
