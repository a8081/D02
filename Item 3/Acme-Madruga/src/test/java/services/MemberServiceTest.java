
package services;

import java.util.Collection;

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
import utilities.AbstractTest;
import domain.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MemberServiceTest extends AbstractTest {

	//Services
	@Autowired
	private MemberService		memberService;

	@Autowired
	private MemberRepository	memberRepository;

	@Autowired
	private UserAccountService	userAccountService;


	/**
	 * Acme Madruga - Req 8: an actor who is not authenticated must be able register to the system as a member
	 * Postive
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos = 1
	 * **/
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
	 * Acme Madruga - Req 8: an actor who is not authenticated must be able register to the system as a member
	 * Negative: incorrect email pattern
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos = 1
	 * **/
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void saveNew() {
		final Member member = this.memberService.create();
		member.setName("name test1");
		member.setMiddleName("middlename test1");
		member.setSurname("surname test1");
		member.setPhoto("http://phototest1.com");
		member.setEmail("emailtil.com");
		member.setPhone("+34675874321");
		member.setAddress("avd test 1");
		member.setSpammer(false);

		final Member saved = this.memberService.save(member);
		final UserAccount ua = member.getUserAccount();
		ua.setUsername("test1");
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword("test1", null);
		ua.setPassword(hash);
		final UserAccount uaSaved = this.userAccountService.save(ua);

		Assert.isTrue(this.memberService.findAll().contains(saved));
		this.memberRepository.flush();

	}

	/**
	 * Acme Madruga - Req 8: an actor who is not authenticated must be able register to the system as a member
	 * Negative: a user not authenticated try to register with a existing username
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos =
	 * **/
	@Test
	public void registerExistingUsername() {
		final Object testingData[][] = {

			{
				"test2", "test2", "name test 2", "middlename test 2", "surname test 2", "http://phototest2.com", "testem2@gmail.com", "+34654654654", "avd test 2", null
			}, {
				"test2", "test2", "name test 3", "middlename test 3", "surname test 3", "http://phototest3.com", "testemail3@gmail.com", "+34654321234", "avd test 3", org.springframework.dao.DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSaveNew((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	/**
	 * Acme Madruga - Req 8: An actor who is not authenticated must be able to delete his/her perdonal data
	 * Positive: logged member deletes his/her data
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos = 1
	 * **/
	@Test
	public void testDeletePostive() {
		super.authenticate("member1");
		final Member principal = this.memberService.findByPrincipal();
		final Integer myId = principal.getId();

		Assert.isTrue(myId != 0);
		this.memberService.delete(principal);
		Member member;
		member = this.memberRepository.findOne(myId);
		Assert.isTrue(member == null);

	}

	/**
	 * Acme Madruga - Req 8: An actor who is not authenticated must be able to delete his/her perdonal data
	 * Negative: the logged member tries to delete another member's data
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos = 1
	 * **/
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testDeleteNegative() {
		super.authenticate("member1");
		final Member principal = this.memberService.findOne(this.getEntityId("member2"));

		this.memberService.delete(principal);
		final Member member = this.memberRepository.findOne(this.getEntityId("member2"));
		Assert.isTrue(member == null);

	}

	/**
	 * Acme Madruga - Req 8: An actor who is authenticated must be able to edit her/his personal data
	 * Positive: the logged member edits his/her data
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos = 1
	 * **/
	@Test()
	public void updatePostive() {
		this.authenticate("member1");
		final Member member = this.memberService.findOne(this.getEntityId("member1"));
		member.setAddress("avd test 1 modificada");
		final Member saved = this.memberService.save(member);
		Assert.isTrue(this.memberService.findOne(this.getEntityId("member1")).getAddress().equals("avd test 1 modificada"));
		this.unauthenticate();
		this.memberRepository.flush();

	}

	/**
	 * Acme Madruga - Req 9: An actor who is authenticated must be able to edit her/his personal data
	 * Positive: the logged member tries to delete another member's data
	 * % recorre 8 de las 12 líneas posibles
	 * cobertura de datos = 1
	 * **/
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void updateNegative() {
		this.authenticate("member1");
		final Member member = this.memberService.findOne(this.getEntityId("member2"));
		member.setAddress("avd test 1 modificada");

		final Member saved = this.memberService.save(member);

		Assert.isTrue(this.memberService.findOne(this.getEntityId("member2")).getAddress().equals("avd test 1 modificada"));
		this.memberRepository.flush();
		this.unauthenticate();

	}

	//********************************* TEMPLATES ********************************************//

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

			final Member saved = this.memberService.save(member);
			//final UserAccount ua = saved.getUserAccount();
			final UserAccount ua = member.getUserAccount();
			ua.setUsername(username);
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(password, null);
			ua.setPassword(hash);
			final UserAccount uaSaved = this.userAccountService.save(ua);
			final Collection<Member> members = this.memberService.findAll();

			Assert.isTrue(members.contains(saved));
			this.memberRepository.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
