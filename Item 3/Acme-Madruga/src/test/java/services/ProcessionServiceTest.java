
package services;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Procession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"

})
@Transactional
public class ProcessionServiceTest extends AbstractTest {

	@Autowired
	private ProcessionService	processionService;
	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void create() {
		super.authenticate("brotherhood1");
		final Procession p = this.processionService.create();
		Assert.notNull(p);
	}

	@Test
	public void save() {
		super.authenticate("brotherhood1");
		final Procession p = this.processionService.create();
		p.setBrotherhood(this.brotherhoodService.findByPrincipal());
		p.setDescription("The description");
		p.setMode("DRAFT");
		Assert.notNull(p);
		this.processionService.save(p);
	}

	@Test
	public void findAll() {
		super.authenticate("brotherhood1");
		final List<Procession> lista = (List<Procession>) this.processionService.findAll();
		Assert.notNull(lista);
		Assert.isTrue(lista.size() > 0);
	}

	@Test
	public void findOne() {
		super.authenticate("brotherhood1");
		final List<Procession> lista = (List<Procession>) this.processionService.findAll();
		Assert.notNull(lista);
		Assert.isTrue(lista.size() > 0);
		final int id = lista.get(0).getId();
		final Procession p = this.processionService.findOne(id);
		Assert.notNull(p);
	}

	@Test
	public void delete() {
		super.authenticate("brotherhood1");
		final List<Procession> lista = (List<Procession>) this.processionService.findAll();
		Assert.notNull(lista);
		Assert.isTrue(lista.size() > 0);
		final int id = lista.get(0).getId();
		Procession p = this.processionService.findOne(id);
		Assert.notNull(p);
		this.processionService.delete(p);
		p = this.processionService.findOne(id);
		Assert.isNull(p);
	}
}
