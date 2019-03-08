package services;

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
import org.springframework.util.Assert;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Area;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest{
	
	@Autowired
	private BrotherhoodService brotherhoodService;
	
	@Autowired
	private EnrolmentService enrolmentService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private BrotherhoodRepository brotherhoodRepository;
	
	
//
//	@Test
//	public void testCreate() {
//		
//	}

//	@Test
//	public void testFindAll() {
//		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();
//		Assert.notEmpty(bros);
//	}

//	@Test
//	public void testFindOne() {
//		final Brotherhood brotherhood = (Brotherhood) this.brotherhoodService.findAll().toArray()[0];
//		final Brotherhood retrieved = this.brotherhoodService.findOne(brotherhood.getId());
//		Assert.isTrue(brotherhood.equals(retrieved));
//	}
//
//	@Test
//	public void testSave() {
//		final Brotherhood b = this.brotherhoodService.create();
//		final UserAccount user = this.userAccountService.create();
//		final Authority auth = new Authority();
//		auth.setAuthority(Authority.BROTHERHOOD);
//		user.addAuthority(auth);
//		b.setUserAccount(user);
//		b.setName("Juan");
//		b.setMiddleName("");
//		b.setSurname("Moron");
//		b.setPhoto("");
//		b.setEmail("juanmoron@gmail.com");
//		b.setPhone("650395733");
//		b.setAddress("Cervantes");
//		b.setSpammer(false);
//		b.setTitle("Brotherhood1");
//		Date date = new Date(2018/11/11);
//		b.setDate(date);
//		Collection<String> pictures = new ArrayList<String>();
//		b.setPictures(pictures);
//		
//		final Area area = this.areaService.create();
//		area.setName("Area5");
//		final Area areaSaved = this.areaService.save(area);
//		b.setArea(areaSaved);
//		final Brotherhood saved = this.brotherhoodService.save(b);
//		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();
//		
//		Assert.isTrue(bros.contains(saved));
//	}

//	@Test
//	public void testDelete() {
//		super.authenticate("brotherhood1");
//		final Brotherhood principal = this.brotherhoodService.findByPrincipal();
//		final Integer myId = principal.getId();
//		Assert.isTrue(myId != 0);
//		this.brotherhoodService.delete(principal);
//		Brotherhood bro;
//		bro = this.brotherhoodRepository.findOne(myId);
//		Assert.isTrue(bro != null);
//	}
//
//	@Test
//	public void testFindByPrincipal() {
//		super.authenticate("brotherhood1");
//		final Brotherhood res = this.brotherhoodService.findByPrincipal();
//		Assert.notNull(res);
//		Assert.isTrue(res.getUserAccount().getUsername().equals("brotherhood1"));
//	}

//	@Test
//	public void testFindByUserId() {
//		final Brotherhood retrieved = (Brotherhood) this.brotherhoodService.findAll().toArray()[0];
//		final int id = retrieved.getUserAccount().getId();
//		final Brotherhood a = this.brotherhoodService.findByUserId(id);
//		Assert.isTrue(a.equals(retrieved));
//	}

//	//TODO:testFindAllBrotherHoodByMember()
//	@Test
//	public void testFindAllBrotherHoodByMember() {
//		super.authenticate("member1");
//		final Member member = memberService.findByPrincipal();
//		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();
//		Enrolment enrol = null;
//		for(Brotherhood b: bros){
//			final Brotherhood bro = b;
//			if(bro.getUserAccount().getUsername().equals("brotherhood2"))
//				enrol = this.enrolmentService.create(bro.getId());
//		}
//		enrol.setMember(member);
//		final Position position = this.positionService.create();
//		position.setNameEnglish("Hello");
//		position.setNameSpanish("Hola");
//		final Position posSaved = this.positionService.save(position);
//		enrol.setPosition(posSaved);
//		final Enrolment saved = this.enrolmentService.save(enrol);
//		final Collection<Brotherhood> end = this.brotherhoodService.findAllBrotherHoodByMember();
//		
//		Assert.isTrue(end.contains(saved.getBrotherhood()));
//		
//	}
	
	@Test
	public void areaSet(){
		super.authenticate("brotherhood2");
		final List<Area> areas = (List<Area>) this.areaService.findAll();
		final Area area = areas.get(1);
		brotherhoodService.areaSet(area);
		final List<Area> areasSettled = (List<Area>) this.areaService.AllAreasSettled();
		Assert.isTrue(areasSettled.contains(area));
	}

}
