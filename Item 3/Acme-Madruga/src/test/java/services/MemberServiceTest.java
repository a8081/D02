
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import utilities.AbstractTest;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	//Services
	@Autowired
	private MemberService		memberService;

	//Repositorys
	@Autowired
	private MemberRepository	memberRepository;


	@Test
	public void testCreate() {
		final Member member = this.memberService.create();
		Assert.notNull(member);
	}

	@Test
	public void testFindAll() {
		Assert.isTrue(this.memberService.findAll().size() > 0);
	}

	@Test
	public void testFindOne() {
		final List<Member> members = new ArrayList<>(this.memberService.findAll());
		Assert.isTrue(members.size() > 0);
		Assert.notNull(this.memberService.findOne(members.get(0).getId()));
	}

	@Test
	public void testSave() {
		final Member member = this.memberService.create();
		//		final UserAccount user = this.userAccountService.create();
		//		final Authority auth = new Authority();
		//		auth.setAuthority(Authority.MEMBER);
		//		user.addAuthority(auth);

		// a8081: Jesus he a�adido esta funcionalidad diractamente en el save del servicio

		//		member.setUserAccount(user);
		member.setName("Jes�s Manuel");
		member.setMiddleName("");
		member.setSurname("Garc�a Lanzas");
		member.setPhoto("");
		member.setEmail("jmgl@gmail.com");
		member.setPhone("658 98 65 96");
		member.setAddress("Avda. Reina Mercedes");
		member.setSpammer(false);

		final Member saved = this.memberService.save(member);
		final Collection<Member> members = this.memberService.findAll();

		Assert.isTrue(members.contains(saved));

	}

	@Test
	public void testDelete() {
		super.authenticate("member1");
		final Member principal = this.memberService.findByPrincipal();
		final Integer myId = principal.getId();

		Assert.isTrue(myId != 0);
		this.memberService.delete(principal);
		Member member;
		member = this.memberRepository.findOne(myId);
		Assert.isTrue(member == null);

	}

	@Test
	public void testFindByPrincipal() {
		super.authenticate("member1");
		final Member res = this.memberService.findByPrincipal();
		Assert.notNull(res);
		Assert.isTrue(res.getUserAccount().getUsername().equals("member1"));
	}

	@Test
	public void testFindByUserId() {
		final Member retrieved = (Member) this.memberService.findAll().toArray()[0];
		final int id = retrieved.getUserAccount().getId();
		final Member member = this.memberService.findByUserId(id);
		Assert.isTrue(member.equals(retrieved));
	}

}
