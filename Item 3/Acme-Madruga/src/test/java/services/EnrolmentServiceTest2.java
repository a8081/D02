
package services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Enrolment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EnrolmentServiceTest2 extends AbstractTest {

	//Services
	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private PositionService		positionService;


	//Repository

	@Test
	public void testCreate() {
		super.authenticate("member1");
		final List<Brotherhood> allBro = (List<Brotherhood>) this.brotherhoodService.findAllMyBrotherHoodByMember();
		final Brotherhood bro = allBro.get(1);
		final Enrolment enrolment = this.enrolmentService.create();
		Assert.notNull(enrolment);
	}
	@Test
	public void testFindAll() {
		//		super.authenticate("member1");
		super.authenticate("brotherhood1");
		Assert.isTrue(this.enrolmentService.findAll().size() > 0);

	}

	@Test
	public void testFindOne() {
		//		super.authenticate("member1");
		super.authenticate("brotherhood1");
		final List<Enrolment> list = new ArrayList<>(this.enrolmentService.findAll());
		Assert.isTrue(list.size() > 0);
		Assert.notNull(this.enrolmentService.findOne(list.get(0).getId()));
	}

	@Test
	public void testSave() {
		super.authenticate("brotherhood1");
		final Brotherhood bro = this.brotherhoodService.findByPrincipal();
		final List<Enrolment> enrolments = (List<Enrolment>) this.enrolmentService.findAll();
		Assert.notEmpty(enrolments);
		final int id = enrolments.get(0).getId();
		final Enrolment enrol = this.enrolmentService.findOne(id);
		Assert.notNull(enrol);
		final Enrolment saved = this.enrolmentService.save(enrol, bro.getId());
	}

	@Test
	public void testDelete() {
		super.authenticate("member1");
		final List<Enrolment> enrolments = (List<Enrolment>) this.enrolmentService.findAll();
		Assert.notEmpty(enrolments);
		final int id = enrolments.get(1).getId();
		final Enrolment enrol = this.enrolmentService.findOne(id);
		Assert.notNull(enrol);
		this.enrolmentService.delete(enrol);
		Assert.isTrue(!this.enrolmentService.findAll().contains(enrol), "El enrol aun existe");
	}
}
