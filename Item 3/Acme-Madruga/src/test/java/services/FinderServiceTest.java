
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

import utilities.AbstractTest;
import domain.Finder;
import domain.Procession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService		finderService;

	@Autowired
	private ProcessionService	processionService;


	@Test
	public void testCreate() {
		final Finder finder = this.finderService.create();
		final Collection<Procession> processions = this.processionService.findAll();
		finder.setProcessions(processions);
		Assert.notNull(finder);
	}

	@Test
	public void testFindAll() {
		super.authenticate("member1");
		Assert.isTrue(this.finderService.findAll().size() > 0);

	}

	@Test
	public void testFindOne() {
		super.authenticate("member1");
		final List<Finder> finders = new ArrayList<>(this.finderService.findAll());
		Assert.isTrue(finders.size() > 0);
		Assert.notNull(this.finderService.findOne(finders.get(0).getId()));
	}

	@Test
	public void testSave() {
		this.authenticate("member1");
		final List<Finder> finders = new ArrayList<>(this.finderService.findAll());
		Assert.isTrue(finders.size() > 0);
		Finder finder = this.finderService.findOne(finders.get(0).getId());
		finder.setKeyword("This");
		final Date moment = new Date();
		finder.setMaxDate(moment);
		finder.setMinDate(moment);
		finder.setAreaName("area1");
		finder = this.finderService.save(finder);
		Assert.isTrue(finder.getId() != 0);
	}

	@Test
	public void testDelete() {
		this.authenticate("member1");
		final List<Finder> finders = new ArrayList<>(this.finderService.findAll());
		Assert.isTrue(finders.size() > 0);
		final Finder finder = this.finderService.findOne(finders.get(0).getId());
		finder.setKeyword("This");
		final Date moment = new Date();
		finder.setMaxDate(moment);
		finder.setMinDate(moment);
		finder.setAreaName("area1");
		this.finderService.delete(finder);
		Assert.isTrue(finder.getId() != 0);
	}

	//		@Test
	//		public void testFind() {
	//			
	//		}

}
