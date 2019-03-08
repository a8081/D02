
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ConfigurationParametersRepository;
import domain.ConfigurationParameters;

@Component
@Transactional
public class StringToConfigurationParametersConverter implements Converter<String, ConfigurationParameters> {

	@Autowired
	private ConfigurationParametersRepository	configurationParametersRepository;


	@Override
	public ConfigurationParameters convert(final String text) {

		final ConfigurationParameters result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.configurationParametersRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
