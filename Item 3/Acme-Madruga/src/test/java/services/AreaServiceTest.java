
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Area;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AreaServiceTest extends AbstractTest {

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		final Area area = this.areaService.create();
		Assert.notNull(area);
	}

	//	@Test
	//	public void testFindAll() {
	//		super.authenticate("admin1");
	//		final Collection<Area> areas = this.areaService.findAll();
	//		Assert.notEmpty(areas);
	//	}
	//
	//	@Test
	//	public void testFindOne() {
	//		super.authenticate("admin1");
	//		final List<Area> areas = new ArrayList<Area>(this.areaService.findAll());
	//		Assert.notEmpty(areas);
	//		final int id = areas.get(0).getId();
	//		final Area a = this.areaService.findOne(id);
	//		Assert.notNull(a);
	//	}
	//
	//	@Test
	//	public void testSave() {
	//		super.authenticate("admin1");
	//		final Area area = this.areaService.create();
	//		area.setName("Area3");
	//		final Collection<String> pictures = new ArrayList<String>();
	//		area.setPictures(pictures);
	//		final Area saved = this.areaService.save(area);
	//		Assert.notNull(saved);
	//	}
	//
	//	@Test
	//	public void testDelete() {
	//		super.authenticate("admin1");
	//		final List<Area> areas = (List<Area>) this.areaService.findAll();
	//		Assert.notEmpty(areas);
	//		final int id = areas.get(1).getId();
	//		Area a = this.areaService.findOne(id);
	//		Assert.notNull(a);
	//		this.areaService.delete(a);
	//		Assert.isTrue(!this.areaService.findAll().contains(a),"El area aun existe");
	//	}
	//
	//	@Test
	//	public void testAllAreasSettled() {
	//		super.authenticate("brotherhood2");
	//		final List<Area> areas = (List<Area>) this.areaService.findAll();
	//		final List<Area> areasSettled = (List<Area>) this.areaService.AllAreasSettled();
	//		Area a = null;
	//		for(Area area: areas){
	//			if(!areasSettled.contains(area)){
	//				a = area;
	//				break;
	//			}
	//		}
	//		this.brotherhoodService.areaSet(a);
	//		final List<Area> areasSettledFin = (List<Area>) this.areaService.AllAreasSettled();
	//		Assert.isTrue(areasSettledFin.contains(a));
	//	}

}
