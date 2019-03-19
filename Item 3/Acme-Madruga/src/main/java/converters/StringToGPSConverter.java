
package converters;

import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.GPS;

@Component
@Transactional
public class StringToGPSConverter implements Converter<String, GPS> {

	@Override
	public GPS convert(final String text) {
		GPS result;
		String parts[];

		if (text == null)
			result = null;
		else
			try {
				parts = text.split("\\|");
				result = new GPS();
				result.setLatitude(Double.valueOf(URLDecoder.decode(parts[0], "UTF-8")));
				result.setLongitude(Double.valueOf(URLDecoder.decode(parts[1], "UTF-8")));

			} catch (final Throwable oops) {
				throw new IllegalArgumentException(oops);
			}
		return result;
	}

}
