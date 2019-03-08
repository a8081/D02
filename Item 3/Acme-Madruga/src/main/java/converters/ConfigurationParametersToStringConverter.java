
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.ConfigurationParameters;

@Component
@Transactional
public class ConfigurationParametersToStringConverter implements Converter<ConfigurationParameters, String> {

	@Override
	public String convert(final ConfigurationParameters configurationParameters) {

		String result;

		if (configurationParameters == null)
			result = null;
		else
			result = String.valueOf(configurationParameters.getId());

		return result;
	}

}
