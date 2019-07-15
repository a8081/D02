
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import utilities.AbstractTest;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// Services
	@Autowired
	private RequestService		requestService;
	@Autowired
	private ActorService		actorService;

	//Repositories
	@Autowired
	private RequestRepository	requestRepository;


	/**
	 * 
	 * testingData[1]:
	 * Acme Madruga - Req 7: a member may request to march in a procession
	 * Negative: a member cannot request twitce to the a parade in DRAFT mode
	 * % recorre 4 de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[1]:
	 * Acme Madruga - Req 7: a member may request to march in a procession
	 * Positive
	 * % recorre 28 de las 28 líneas posibles
	 * cobertura de datos =
	 * **/
	@Test
	public void driverRequestToParade() {  //FUNCIONA
		final Object testingData[][] = {
			{
				"member1", "parade2", null
			}, {
				"member2", "parade2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestToParade((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * 
	 * testingData[0]:
	 * Acme Madruga - Req 10.6: A brotherhood decided on the request of its processions
	 * Positive: change status to rejected giving an explanation
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[1]:
	 * Acme Madruga - Req 10.6: A brotherhood decided on the request of its processions
	 * Negative: change status to accepted without giving a position
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[2]:
	 * Acme Madruga - Req 10.6: A brotherhood decided on the request of its processions
	 * Negative: change status to rejected without giving an explanation
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[3]:
	 * Acme Madruga - Req 11.1: A member mast be able to manage its requests (except updating them)
	 * Negative: a member cannot update its requests
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * **/
	@Test
	public void driverUpdateRequest() {
		final Object testingData[][] = {
			{
				"brotherhood1", "request1", "REJECTED", "explanation", null, null, null
			}, {
				"brotherhood1", "request1", "APPROVED", "explanation", null, null, IllegalArgumentException.class
			}, {
				"brotherhood1", "request1", "REJECTED", null, null, null, IllegalArgumentException.class
			}, {
				"member1", "request1", "PENDING", null, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateRequest((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * 
	 * testingData[0]:
	 * Acme Madruga - Req 11.1: A member is able to manage her/his requests
	 * Positive: Member deletes one of her/his requests with status PENDING
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[1]:
	 * Acme Madruga - Req 11.1: A member is able to manage her/his requests
	 * Negative: Member tries to delete request from other member with status PENDING
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[2]:
	 * Acme Madruga - Req 11.1: A member is able to manage her/his requests
	 * Negative: Member tries to delete one of his/her request with status APPROVED
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos = ***
	 * 
	 * testingData[3]:
	 * Acme Madruga - Req 10.6: A brotherhood decided on the request of its processions (but not deleting them)
	 * Negative: a brotherhood cannot delete its requests
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos = ***
	 * 
	 * **/
	@Test
	public void driverDeleteRequest() {
		final Object testingData[][] = {
			{
				"member1", "request1", null
			}, {
				"member2", "request1", IllegalArgumentException.class
			}, {
				"member2", "request2", IllegalArgumentException.class
			}, {
				"brotherhood1", "request2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * 
	 * testingData[0]:
	 * Acme Madruga - Req 11.1: A member is able to manage her/his requests
	 * Negative: Member tries to see a request that does not belong to her/him
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[1]:
	 * Acme Madruga - Req 11.1: A member is able to manage her/his requests
	 * Positive: Member tries to see a request that does belong to her/him
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos =
	 * 
	 * testingData[2]:
	 * Acme Madruga - Req 10.6: A brotherhood is able to manage its requets
	 * Negative: Brotherhood tries to see a request that does not belong to any of its parades
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos = ***
	 * 
	 * testingData[3]:
	 * Acme Madruga - Req 10.6: A brotherhood is able to manage its requets
	 * Positive: Brotherhood tries to see a request that does belong to any of its parades
	 * % recorre *** de las 28 líneas posibles
	 * cobertura de datos = ***
	 * 
	 * **/
	@Test
	public void driverFindOneRequest() {
		final Object testingData[][] = {
			{
				"member1", "request2", IllegalArgumentException.class
			}, {
				"member2", "request2", null
			}, {
				"brotherhood1", "request2", IllegalArgumentException.class
			}, {
				"brotherhood1", "request1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateFindOneRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	//********************************** TEMPLATES ****************************************************//

	protected void templateFindOneRequest(final String username, final String request, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final Integer myId = this.actorService.findByPrincipal().getId();
			final int idRequest = this.getEntityId(request);
			Assert.notNull(this.requestService.findOne(idRequest));
			this.unauthenticate();

			this.requestRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	protected void templateDeleteRequest(final String username, final String request, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final Integer myId = this.actorService.findByPrincipal().getId();
			final int idRequest = this.getEntityId(request);
			final Request req = this.requestService.findOne(idRequest);
			this.requestService.delete(req);
			this.unauthenticate();

			this.requestRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	protected void templateRequestToParade(final String member, final String parade, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(member);
			final Integer myId = this.actorService.findByPrincipal().getId();
			final int idParade = this.getEntityId(parade);
			final Request req = this.requestService.requestToParade(idParade);
			Assert.isTrue(req.getId() != 0);
			this.unauthenticate();

			this.requestRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	protected void templateUpdateRequest(final String username, final String request, final String status, final String explanation, final Integer row, final Integer column, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final Integer myId = this.actorService.findByPrincipal().getId();
			final int idRequest = this.getEntityId(request);
			final Request req = this.requestService.findOne(idRequest);
			req.setExplanation(explanation);
			req.setRow(row);
			req.setColumn(column);
			req.setStatus(status);
			final Request result = this.requestService.save(req);
			Assert.isTrue(result.getId() != 0);

			this.requestRepository.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
