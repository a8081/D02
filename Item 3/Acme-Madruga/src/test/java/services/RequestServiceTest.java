
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
	private ParadeService		paradeService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private MemberService		memberService;

	//Repositorys
	@Autowired
	private RequestRepository	requestRepository;


	/*
	 * @Test
	 * public void driverSaveByMember() {
	 * final Object testingData[][] = {
	 * {
	 * "member1", "PENDING", null, null, null, null, "parade11", null
	 * }
	 * };
	 * 
	 * for (int i = 0; i < testingData.length; i++)
	 * this.templateSaveByMember((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
	 * (Class<?>) testingData[i][7]);
	 * }
	 * 
	 * protected void templateSaveByMember(final String member, final String status, final Date moment, final String explanation, final Integer row, final Integer column, final String parade, final Class<?> expected) {
	 * 
	 * Class<?> caught = null;
	 * 
	 * try {
	 * this.authenticate(member);
	 * final Integer myId = this.actorService.findByPrincipal().getId();
	 * Request req = this.requestService.create();
	 * req.setStatus(status);
	 * req.setMoment(moment);
	 * req.setExplanation(explanation);
	 * req.setParade(this.paradeService.findOne(this.getEntityId(parade)));
	 * req.setMember(this.memberService.findOne(myId));
	 * req.setRow(row);
	 * req.setColumn(column);
	 * 
	 * req = this.requestService.save(req);
	 * Assert.isTrue(req.getId() != 0);
	 * this.unauthenticate();
	 * 
	 * this.requestRepository.flush();
	 * } catch (final Throwable oops) {
	 * caught = oops.getClass();
	 * }
	 * 
	 * super.checkExceptions(expected, caught);
	 * }
	 */

	/******************************************************************************************/

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
				"member1", "parade12", IllegalArgumentException.class
			}, {
				"member2", "parade11", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestToParade((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Acme Madruga - Req 7: a member may request to march in a procession
	 * Negative: a member cannot request twitce to the same parade
	 * % recorre 11 de las 28 líneas posibles
	 * cobertura de datos =
	 * **/
	@Test
	public void driverMemberRequestsTwice() {
		final Object testingData[][] = {
			{
				"member1", "parade11", null
			}, {
				"member1", "parade11", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestToParade((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
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

	/***
	 * UPDATE REQUEST: Use cases
	 * Brotherhood change status to rejected without giving an explanation -> Error
	 * Brotherhood change status to accepted without giving a position -> Error
	 * Brotherhood change status to rejected giving an explanation
	 * Brotherhood change status to accepted giving a position
	 * Member modify parade -> error
	 * **/

	//,{
	//	"brotherhood1", "request1", "APPROVED", null, 1, 1, null
	//}

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

	/**
	 * Acme Madruga - Req 7: a member may request to march in a procession
	 * Negative: a member cannot request twitce to the same parade
	 * % recorre 11 de las 28 líneas posibles
	 * cobertura de datos =
	 * **/
	//reuquests 1 y 3 son sobre la parade 1 que pertenece a la brotherhood 1
	@Test
	public void testGiveSamePositionTwice() {
		final Object testingData[][] = {
			{
				"brotherhood1", "request1", "APPROVED", null, 1, 1, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateRequest((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
	}

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
	 * DELETE REQUEST: use cases
	 * Member delete one of his/her request with status PENDING -> error
	 * Member delete request from other member with status PENDING -> error
	 * Member delete one of his/her request with status APPROVED -> error
	 * Brotherhood delete a request -> error
	 * **/
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
	 * FIND ONE REQUEST: use cases
	 * Member tries to see a request that does not belong to her/him -> error
	 * Member tries to see a request that does belong to her/him
	 * Brotherhood tries to see a request that does not belong to any of its parades -> error
	 * Brotherhood tries to see a request that does belong to any of its parades
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

}
