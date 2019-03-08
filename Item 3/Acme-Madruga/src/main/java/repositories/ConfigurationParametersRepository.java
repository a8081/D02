
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.ConfigurationParameters;

public interface ConfigurationParametersRepository extends JpaRepository<ConfigurationParameters, Integer> {

	@Query("select nw from ConfigurationParameters cfgp join cfgp.negativeWords nw")
	Collection<String> findNegativeWords();

	@Query("select pw from ConfigurationParameters cfgp join cfgp.positiveWords pw")
	Collection<String> findPositiveWords();

	@Query("select sw from ConfigurationParameters cfgp join cfgp.spamWords sw")
	Collection<String> findSpamWords();

	@Query("select c.welcomeMessageEsp from ConfigurationParameters c")
	String findWelcomeMessageEsp();

	@Query("select c.welcomeMessageEn from ConfigurationParameters c")
	String findWelcomeMessageEn();

	@Query("select c.banner from ConfigurationParameters c")
	String findBanner();

}
