
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

import repositories.PositionRepository;
import utilities.AbstractTest;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PositionServiceTest extends AbstractTest {

	//Services
	@Autowired
	private PositionService		positionService;

	//Repository
	@Autowired
	private PositionRepository	positionRepository;


	@Test
	public void testCreateStringString() {
		super.authenticate("admin1");
		final Position pos = this.positionService.create();
		pos.setNameEnglish("en");
		pos.setNameSpanish("sp");
		Assert.notNull(pos);
	}

	@Test
	public void testFindAll() {
		Assert.isTrue(this.positionService.findAll().size() > 0);
	}

	@Test
	public void testFindOne() {
		final List<Position> positions = new ArrayList<>(this.positionService.findAll());
		Assert.isTrue(positions.size() > 0);
		Assert.notNull(this.positionService.findOne(positions.get(0).getId()));
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Position pos = this.positionService.create();
		pos.setNameEnglish("en");
		pos.setNameSpanish("sp");
		final Position saved = this.positionService.save(pos);
		Assert.notNull(saved);
	}

	@Test
	public void testDelete() {
		super.authenticate("admin1");
		final List<Position> allPositions = (List<Position>) this.positionService.findAll();
		Assert.notEmpty(allPositions);
		final int id = allPositions.get(4).getId();
		final Position pos = this.positionService.findOne(id);
		Assert.notNull(pos);
		this.positionService.delete(pos);
		Position delete;
		delete = this.positionRepository.findOne(pos.getId());
		Assert.isTrue(delete == null);
	}

	// Jesus Garcia: Devuelve correctamente la lista de positions usadas, aunque este metodo no vaya bien
	@Test
	public void testAllPositionUsed() {
		super.authenticate("admin1");
		final Collection<Position> test = this.positionService.AllPositionUsed();
		Assert.notEmpty(test);
	}

}
