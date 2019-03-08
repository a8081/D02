
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

import utilities.AbstractTest;
import domain.ConfigurationParameters;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ConfigurationParametersServiceTest extends AbstractTest {

	@Autowired
	private ConfigurationParametersService	configurationParametersService;

	@Autowired
	private PositionService					positionService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		final ConfigurationParameters conf = this.configurationParametersService.create();
		Assert.notNull(conf);
	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<ConfigurationParameters> confs = this.configurationParametersService.findAll();
		Assert.notEmpty(confs);
	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final List<ConfigurationParameters> confs = (List<ConfigurationParameters>) this.configurationParametersService.findAll();
		final int id = confs.get(0).getId();
		final ConfigurationParameters conf = this.configurationParametersService.findOne(id);
		Assert.notNull(conf);
	}

	@Test
	public void testCheckForSpamWords() {
		super.authenticate("admin1");
		final Collection<String> spamWords = new ArrayList<>();
		spamWords.add("sex");
		spamWords.add("viagra");
		final Boolean res = this.configurationParametersService.checkForSpamWords(spamWords);
		Assert.isTrue(res);
	}

	@Test
	public void testFindNegativeWords() {
		super.authenticate("admin1");
		final Collection<String> res = this.configurationParametersService.findNegativeWords();
		Assert.notEmpty(res);
	}

	@Test
	public void testFindPositiveWords() {
		super.authenticate("admin1");
		final Collection<String> res = this.configurationParametersService.findPositiveWords();
		Assert.notEmpty(res);
	}

	@Test
	public void testFindSpamWords() {
		super.authenticate("admin1");
		final Collection<String> res = this.configurationParametersService.findSpamWords();
		Assert.notEmpty(res);
	}

	@Test
	public void testFindWelcomeMessageEsp() {
		super.authenticate("admin1");
		final String res = this.configurationParametersService.findWelcomeMessageEsp();
		Assert.notNull(res);
	}

	@Test
	public void testFindWelcomeMessageEn() {
		super.authenticate("admin1");
		final String res = this.configurationParametersService.findWelcomeMessageEn();
		Assert.notNull(res);
	}

	@Test
	public void testFindBanner() {
		super.authenticate("admin1");
		final String res = this.configurationParametersService.findBanner();
		Assert.notNull(res);
	}

	@Test
	public void testFind() {
		super.authenticate("admin1");
		final ConfigurationParameters res = this.configurationParametersService.find();
		Assert.notNull(res);
	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final ConfigurationParameters conf = this.configurationParametersService.create();
		final ConfigurationParameters confSaved = this.configurationParametersService.save(conf);
		Assert.notNull(confSaved);
		Assert.notNull(confSaved.getBanner());
		Assert.notNull(confSaved.getCountryPhoneCode());
		Assert.notEmpty(confSaved.getPositiveWords());
		Assert.notEmpty(confSaved.getNegativeWords());
		Assert.notEmpty(confSaved.getSpamWords());
		Assert.notNull(confSaved.getMaxFinderResults());
		Assert.notNull(confSaved.getFinderTime());
		Assert.notNull(confSaved.getWelcomeMessageEn());
		Assert.notNull(confSaved.getWelcomeMessageEsp());
		Assert.notNull(confSaved.getSysName());

	}

	@Test
	public void testCreateTicker() {
		super.authenticate("admin1");
		final String res = this.configurationParametersService.createTicker();
		Assert.notNull(res);
	}

	@Test
	public void testAddNegativeWord() {
		super.authenticate("admin1");
		final String nword = "hola";
		final Collection<String> res = this.configurationParametersService.addNegativeWord(nword);
		Assert.isTrue(res.contains(nword));
	}

	@Test
	public void testAddPositiveWord() {
		super.authenticate("admin1");
		final String nword = "hola";
		final Collection<String> res = this.configurationParametersService.addPositiveWord(nword);
		Assert.isTrue(res.contains(nword));
	}

	@Test
	public void testAddSpamWord() {
		super.authenticate("admin1");
		final String nword = "hola";
		final Collection<String> res = this.configurationParametersService.addSpamWord(nword);
		Assert.isTrue(res.contains(nword));
	}

	@Test
	public void testDeleteNegativeWord() {
		super.authenticate("admin1");
		final List<String> words = (List<String>) this.configurationParametersService.findNegativeWords();
		final String w = words.get(0);
		final List<String> res = (List<String>) this.configurationParametersService.deleteNegativeWord(w);
		Assert.isTrue(!res.contains(w));
	}

	@Test
	public void testDeletePositiveWord() {
		super.authenticate("admin1");
		final List<String> words = (List<String>) this.configurationParametersService.findPositiveWords();
		final String w = words.get(0);
		final List<String> res = (List<String>) this.configurationParametersService.deletePositiveWord(w);
		Assert.isTrue(!res.contains(w));
	}

	@Test
	public void testDeleteSpamWord() {
		super.authenticate("admin1");
		final List<String> words = (List<String>) this.configurationParametersService.findSpamWords();
		final String w = words.get(0);
		final List<String> res = (List<String>) this.configurationParametersService.deleteSpamWord(w);
		Assert.isTrue(!res.contains(w));
	}

}
