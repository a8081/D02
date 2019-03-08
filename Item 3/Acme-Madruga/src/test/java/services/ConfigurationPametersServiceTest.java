
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
import domain.ConfigurationParameters;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ConfigurationPametersServiceTest extends AbstractTest {

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	@Test
	public void testFindOne() {

		final List<ConfigurationParameters> cp = (List<ConfigurationParameters>) this.configurationParametersService.findAll();
		final ConfigurationParameters configurationParameters = cp.get(0);

		Assert.notNull(configurationParameters);
		System.out.println(configurationParameters);
	}
}
