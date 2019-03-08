
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Float;

@Component
@Transactional
public class FloatToStringConverter implements Converter<Float, String> {

	@Override
	public String convert(final Float fProcession) {

		String result;

		if (fProcession == null)
			result = null;
		else
			result = String.valueOf(fProcession.getId());

		return result;
	}

}
