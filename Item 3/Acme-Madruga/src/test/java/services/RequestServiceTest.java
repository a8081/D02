
package services;

import java.sql.Date;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import utilities.AbstractTest;
import domain.Member;
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


	@Test
	public void test() {
		Assert.isNull(null);
	}

	@Test
	public void driverSaveByMember() {
		final Object testingData[][] = {
			{
				"member1", "PENDING", null, null, null, null, "parade11", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSaveByMember((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templateSaveByMember(final String member, final String status, final Date moment, final String explanation, final Integer row, final Integer column, final String parade, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(member);
			final Integer myId = this.actorService.findByPrincipal().getId();
			Request req = this.requestService.create();
			req.setStatus(status);
			req.setMoment(moment);
			req.setExplanation(explanation);
			req.setParade(this.paradeService.findOne(this.getEntityId(parade)));
			req.setMember(this.memberService.findOne(myId));
			req.setRow(row);
			req.setColumn(column);

			req = this.requestService.save(req);
			Assert.isTrue(req.getId() != 0);
			this.unauthenticate();

			this.requestRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/******************************************************************************************/

	@Test
	public void driverRequestToParade() {  //FUNCIONA
		final Object testingData[][] = {
			{
				"member1", "parade12", java.lang.IllegalArgumentException.class
			}, {
				"member1", "parade11", null
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
			System.out.println("HOLAAAAA");
			final Request req = this.requestService.requestToParade(idParade);
			Assert.isTrue(req.getId() != 0);
			this.unauthenticate();

			this.requestRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void loopRequestToParade() { //TO FIX
		final Collection<Member> members = this.memberService.findAll();
		final Object testingData[][] = new Object[members.size()][3];
		int x = 0;
		for (final Member m : members) {
			System.out.println(m);
			testingData[x][0] = m.getUserAccount().getUsername();
			testingData[x][1] = "parade11";
			testingData[x][2] = null;
			x++;
		}

		System.out.println(testingData);

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestToParade((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void requestToParade() {

		this.authenticate("member1");
		//final Integer myId = this.actorService.findByPrincipal().getId();
		System.out.println("hola");
		final int p = this.getEntityId("parade11");
		System.out.println(p);
		//final Parade parade = this.paradeService.findOne(this.getEntityId("parade11"));
		System.out.println("adios");
		final Request req = this.requestService.requestToParade(p);
		Assert.isTrue(req.getId() != 0);
		this.unauthenticate();

		this.requestRepository.flush();

	}

	/***
	 * UPDATE REQUEST: Use cases
	 * Brotherhood change status to rejected without giving an explanation -> Error
	 * Brotherhood change status to accepted without giving a position -> Error
	 * Brotherhood change status to rejected giving an explanation
	 * Brotherhood change status to accepted giving a position
	 * Member modify parade -> error
	 * **/

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
			System.out.println(req.getParade().getId());
			final Request result = this.requestService.save(req);
			Assert.isTrue(result.getId() != 0);
			this.unauthenticate();

			this.requestRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
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
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUpdateRequest((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/*
	 * @Test
	 * public void deleteTest() {
	 * this.authenticate("member1");
	 * final Integer myId = this.actorService.findByPrincipal().getId();
	 * Request req = this.requestService.create();
	 * req.setStatus(Request.PENDING);
	 * final Parade procession = ((List<Parade>) this.processionService.findAll()).get(0);
	 * req.setProcession(procession);
	 * req.setMember(this.memberService.findOne(myId));
	 * final Date moment = new Date();
	 * req.setMoment(moment);
	 * 
	 * req = this.requestRepository.saveAndFlush(req);
	 * final Integer idRequest = req.getId();
	 * Assert.isTrue(req.getId() != 0);
	 * this.requestService.delete(req);
	 * req = this.requestRepository.findOne(idRequest);
	 * Assert.isTrue(req == null);
	 * 
	 * }
	 * 
	 * @Test
	 * public void findAll() {
	 * this.authenticate("member1");
	 * Assert.isTrue(this.requestService.findAll().size() > 0);
	 * }
	 * 
	 * @Test
	 * public void findOne() {
	 * this.authenticate("member1");
	 * final List<Request> list = new ArrayList<>(this.requestService.findAll());
	 * Assert.isTrue(list.size() > 0);
	 * Assert.notNull(this.requestService.findOne(list.get(0).getId()));
	 * }
	 */
}
