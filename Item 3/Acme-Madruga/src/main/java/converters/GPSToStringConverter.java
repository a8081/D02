
package converters;

import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.GPS;

@Component
@Transactional
public class GPSToStringConverter implements Converter<GPS, String> {

	@Override
	public String convert(final GPS gps) {
		String result;
		StringBuilder builder;

		if (gps == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(Double.toString(gps.getLatitude()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Double.toString(gps.getLongitude()), "UTF-8"));
				builder.append("|");

				result = builder.toString();
			} catch (final Throwable oops) {
				throw new IllegalArgumentException(oops);

			}
		return result;
	}
}
