
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import utilities.AbstractTest;
import domain.Procession;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// Services
	@Autowired
	private RequestService		requestService;
	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private MemberService		memberService;

	//Repositorys
	@Autowired
	private RequestRepository	requestRepository;


	@Test
	public void createTest() {
		final Request req = this.requestService.create();
		Assert.notNull(req);
	}

	@Test
	public void saveTest() {
		this.authenticate("member1");
		final Integer myId = this.actorService.findByPrincipal().getId();
		Request req = this.requestService.create();
		req.setStatus(Request.PENDING);
		final Date moment = new Date();
		req.setMoment(moment);
		final Procession procession = ((List<Procession>) this.processionService.findAll()).get(0);
		req.setProcession(procession);
		req.setMember(this.memberService.findOne(myId));

		req = this.requestService.save(req);
		Assert.isTrue(req.getId() != 0);

	}

	@Test
	public void deleteTest() {
		this.authenticate("member1");
		final Integer myId = this.actorService.findByPrincipal().getId();
		Request req = this.requestService.create();
		req.setStatus(Request.PENDING);
		final Procession procession = ((List<Procession>) this.processionService.findAll()).get(0);
		req.setProcession(procession);
		req.setMember(this.memberService.findOne(myId));
		final Date moment = new Date();
		req.setMoment(moment);

		req = this.requestRepository.saveAndFlush(req);
		final Integer idRequest = req.getId();
		Assert.isTrue(req.getId() != 0);
		this.requestService.delete(req);
		req = this.requestRepository.findOne(idRequest);
		Assert.isTrue(req == null);

	}

	@Test
	public void findAll() {
		this.authenticate("member1");
		Assert.isTrue(this.requestService.findAll().size() > 0);
	}

	@Test
	public void findOne() {
		this.authenticate("member1");
		final List<Request> list = new ArrayList<>(this.requestService.findAll());
		Assert.isTrue(list.size() > 0);
		Assert.notNull(this.requestService.findOne(list.get(0).getId()));
	}
}
