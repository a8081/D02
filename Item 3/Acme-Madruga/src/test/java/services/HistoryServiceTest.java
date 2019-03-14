
package services;

import java.util.Date;

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
public class HistoryServiceTest extends AbstractTest {

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
	public void driverCreate() {
		final Object testingData[][] = {
			{
				"member1", "", new Date(), null, null, null, "parade12", IllegalArgumentException.class
			}, {
				"member1", "", new Date(), "", null, null, "parade11", IllegalArgumentException.class
			}, {
				"member1", "ACEPTED", new Date(), null, null, null, "parade11", IllegalArgumentException.class
			}, {
				"member1", "PENDING", new Date(), null, 3, 2, "parade11", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	protected void template(final String member, final String status, final Date moment, final String explanation, final Integer row, final Integer column, final String parade, final Class<?> expected) {

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
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
