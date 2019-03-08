
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigurationParametersRepository;
import security.Authority;
import domain.Actor;
import domain.Administrator;
import domain.ConfigurationParameters;

@Service
@Transactional
public class ConfigurationParametersService {

	@Autowired
	private ConfigurationParametersRepository	configurationParametersRepository;

	@Autowired
	private PositionService						positionService;

	@Autowired
	private AdministratorService				administratorService;

	@Autowired
	private ActorService						actorService;


	public ConfigurationParameters create() {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		return new ConfigurationParameters();
	}

	public Collection<ConfigurationParameters> findAll() {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		final Collection<ConfigurationParameters> res = this.configurationParametersRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public ConfigurationParameters findOne(final int id) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.isTrue(id != 0);
		final ConfigurationParameters res = this.configurationParametersRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public boolean checkForSpamWords(final Collection<String> strings) {
		boolean res = false;
		final Collection<String> spamWords = this.findSpamWords();

		for (final String s : strings)
			for (final String spamWord : spamWords) {
				final boolean bool = s.matches(".*" + spamWord + ".*");
				if (s.contentEquals(spamWord)) {
					res = true;
					break;
				}
			}
		return res;
	}

	//======================================================================================================
	//======================================================================================================
	//======================================================================================================
	//================================  Ancilliary methods: a8081  =========================================
	//======================================================================================================
	//======================================================================================================
	//======================================================================================================

	public Collection<String> findNegativeWords() {
		final Collection<String> res = this.configurationParametersRepository.findNegativeWords();
		Assert.notNull(res);
		return res;
	}

	public Collection<String> findPositiveWords() {
		final Collection<String> res = this.configurationParametersRepository.findPositiveWords();
		Assert.notNull(res);
		return res;
	}

	public Collection<String> findSpamWords() {
		final Collection<String> res = this.configurationParametersRepository.findSpamWords();
		Assert.notNull(res);
		return res;
	}

	public String findWelcomeMessageEsp() {
		return this.configurationParametersRepository.findWelcomeMessageEsp();
	}

	public String findWelcomeMessageEn() {
		return this.configurationParametersRepository.findWelcomeMessageEn();
	}

	public String findBanner() {
		return this.configurationParametersRepository.findBanner();
	}

	public ConfigurationParameters find() {
		final ConfigurationParameters res = (ConfigurationParameters) this.configurationParametersRepository.findAll().toArray()[0];
		Assert.notNull(res);
		return res;
	}

	public ConfigurationParameters save(final ConfigurationParameters c) {

		Assert.notNull(c);
		final Administrator a = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(a, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		System.out.println(a.getUserAccount().getAuthorities());

		//		if (c.getId() == 0) {
		//			c.setBanner("https://tinyurl.com/acme-madruga");
		//			c.setCountryPhoneCode("+34");
		//
		//			this.positionService.createDefault("Presidente", "President");
		//			this.positionService.createDefault("Vicepresidente", "Vice President");
		//			this.positionService.createDefault("Secretario", "Secretary");
		//			this.positionService.createDefault("Tesorero", "Treasurer");
		//			this.positionService.createDefault("Historiador", "Historian");
		//			this.positionService.createDefault("Promotor", "Fundraiser");
		//			this.positionService.createDefault("Vocal", "Officer");
		//
		//			c.setMaxFinderResults(10);
		//			c.setFinderTime(1);
		//			c.setWelcomeMessageEn("Welcome to Acme Madrugá, the site to organise your processions.");
		//			c.setWelcomeMessageEsp("¡Bienvenidos a Acme Madrugá! Tu sitio para organizar procesiones.");
		//			final Collection<String> negativeWords = new ArrayList<>();
		//			negativeWords.add("not");
		//			negativeWords.add("bad");
		//			negativeWords.add("horrible");
		//			negativeWords.add("average");
		//			negativeWords.add("disaster");
		//			negativeWords.add("no");
		//			negativeWords.add("malo");
		//			negativeWords.add("mediocre");
		//			negativeWords.add("desastre");
		//			c.setNegativeWords(negativeWords);
		//
		//			final Collection<String> positiveWords = new ArrayList<>();
		//			positiveWords.add("good");
		//			positiveWords.add("fantastic");
		//			positiveWords.add("excellent");
		//			positiveWords.add("great");
		//			positiveWords.add("amazing");
		//			positiveWords.add("terrific");
		//			positiveWords.add("beautiful");
		//			positiveWords.add("bueno");
		//			positiveWords.add("fantastico");
		//			positiveWords.add("excelente");
		//			positiveWords.add("genial");
		//			positiveWords.add("increble");
		//			positiveWords.add("estupendo");
		//			positiveWords.add("bonito");
		//			c.setPositiveWords(positiveWords);
		//
		//			final Collection<String> spamWords = new ArrayList<>();
		//			spamWords.add("sex");
		//			spamWords.add("viagra");
		//			spamWords.add("cialis");
		//			spamWords.add("one million");
		//			spamWords.add("you've been selected");
		//			spamWords.add("nigeria");
		//			spamWords.add("sexo");
		//			spamWords.add("un millón");
		//			spamWords.add("has sido seleccionado");
		//			c.setSpamWords(spamWords);
		//
		//			c.setSysName("Acme Madrugá");
		//
		//		}

		final ConfigurationParameters result = this.configurationParametersRepository.save(c);
		Assert.notNull(result);
		System.out.println(result.getId());
		return result;

	}
	// Ancilliary methods

	public String createTicker() {
		final DateFormat df = new SimpleDateFormat("yymmdd");
		final Calendar cal = Calendar.getInstance();

		return df.format(cal.getTime()) + "-" + RandomStringUtils.randomAlphanumeric(5).toUpperCase();
	}

	public Collection<String> addNegativeWord(final String nword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(nword);
		final Collection<String> nwords = this.findNegativeWords();

		return this.addWord(nword, nwords);
	}

	public Collection<String> addPositiveWord(final String pword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(pword);
		final Collection<String> pwords = this.findPositiveWords();

		return this.addWord(pword, pwords);
	}

	public Collection<String> addSpamWord(final String sword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(sword);
		final Collection<String> swords = this.findSpamWords();

		return this.addWord(sword, swords);
	}

	public Collection<String> deleteNegativeWord(final String nword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(nword);
		final Collection<String> nwords = this.findNegativeWords();

		return this.deleteWord(nword, nwords);
	}

	public Collection<String> deletePositiveWord(final String pword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(pword);
		final Collection<String> pwords = this.findPositiveWords();

		return this.deleteWord(pword, pwords);
	}

	public Collection<String> deleteSpamWord(final String sword) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.notNull(sword);
		final Collection<String> swords = this.findSpamWords();

		return this.deleteWord(sword, swords);
	}

	private Collection<String> addWord(final String word, final Collection<String> words) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.isTrue(!words.contains(word));
		words.add(word);

		return words;
	}

	private Collection<String> deleteWord(final String word, final Collection<String> words) {
		final Actor principal = this.administratorService.findByPrincipal();
		final Boolean isAdmin = this.actorService.checkAuthority(principal, Authority.ADMIN);
		Assert.isTrue(isAdmin);
		Assert.isTrue(words.contains(word));
		words.remove(word);

		return words;
	}

}
