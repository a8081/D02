
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import security.UserAccount;
import services.auxiliary.RegisterService;
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

	@Autowired
	private RegisterService		registerService;

	@Autowired
	private UserAccountService	userAccountService;


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
	public void driverLoopSaveNew() {
		final Object testingData[][] = new Object[20][10];
		for (int i = 0; i < 20; i++) {
			testingData[i][0] = "test" + i;
			testingData[i][1] = "test" + i;
			testingData[i][2] = "name" + i;
			testingData[i][3] = "middlename" + i;
			testingData[i][4] = "surname" + i;
			testingData[i][5] = "";
			testingData[i][6] = "test" + i + "@gmail.com";
			testingData[i][7] = "+34654543432";
			testingData[i][8] = "avd test" + i;
			testingData[i][9] = null;
		}

		for (int i = 0; i < testingData.length; i++)
			this.templateSaveNew((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	/**
	 * 1
	 * **/
	@Test
	public void driverSaveNew() {
		final Object testingData[][] = {
			{
				"test1", "test1", "name test 1", "middlename test 1", "surname test 1", "http://phototest1.com", "testemail1@gmail.com", "+3465765", "avd test", javax.validation.ConstraintViolationException.class
			}, {
				"test2", "test2", "name test 2", "middlename test 2", "surname test 2", "http://phototest2.com", "testemail2.com", "+34657456234", "avd test", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				"test3", "test3", "name test 3", "middlename test 3", "surname test 3", "http://phototest3.com", "testemail3@gmail.com", "+34657651234", "avd test 3", org.springframework.dao.DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSaveNew((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void templateSaveNew(final String username, final String password, final String name, final String middlename, final String surname, final String photo, final String email, final String phone, final String address, final Class<?> expected) {
		Class<?> caught = null;

		try {
			final Member member = this.memberService.create();
			member.setName(name);
			member.setMiddleName(middlename);
			member.setSurname(surname);
			member.setPhoto(photo);
			member.setEmail(email);
			member.setPhone(phone);
			member.setAddress(address);
			member.setSpammer(false);

			System.out.println("holaa");
			final Member saved = this.memberService.save(member);
			final UserAccount ua = saved.getUserAccount();
			ua.setUsername(username);
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(password, null);
			ua.setPassword(hash);
			final UserAccount uaSaved = this.userAccountService.save(ua);
			System.out.println("adioos");
			final Collection<Member> members = this.memberService.findAll();

			Assert.isTrue(members.contains(saved));
			this.memberRepository.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
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
