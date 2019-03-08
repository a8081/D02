
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AdministratorRepository;
import repositories.BrotherhoodRepository;
import repositories.MemberRepository;
import domain.Actor;

@Component
@Transactional
public class StringToActorConverter implements Converter<String, Actor> {

	@Autowired
	BrotherhoodRepository	brotherhoodRepository;

	@Autowired
	MemberRepository		memberRepository;

	@Autowired
	AdministratorRepository	administratorRepository;


	@Override
	public Actor convert(final String text) {
		Actor result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.brotherhoodRepository.findOne(id);
				if (result == null) {
					result = this.memberRepository.findOne(id);
					if (result == null)
						result = this.administratorRepository.findOne(id);
				}

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
