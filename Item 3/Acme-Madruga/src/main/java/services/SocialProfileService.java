
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods ----------------------------------------------------

	public SocialProfile create() {
		final SocialProfile profile = new SocialProfile();

		final Actor me = this.actorService.findByPrincipal();
		Assert.notNull(me);

		profile.setActor(me);

		return profile;
	}

	public SocialProfile save(final SocialProfile socialProfile) {
		Assert.notNull(socialProfile);

		final Actor me = this.actorService.findByPrincipal();
		Assert.notNull(me);

		Assert.isTrue(socialProfile.getActor().equals(me));

		SocialProfile result;
		result = this.socialProfileRepository.save(socialProfile);
		Assert.notNull(result);
		Assert.isTrue(result.getId() != 0);

		return result;

	}

	public SocialProfile findOne(final int idSocialProfile) {
		Assert.isTrue(idSocialProfile != 0);

		final SocialProfile result = this.socialProfileRepository.findOne(idSocialProfile);
		Assert.notNull(result);

		return result;
	}

	public Collection<SocialProfile> findAll() {

		final Collection<SocialProfile> result = this.socialProfileRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public void delete(final SocialProfile socialProfile) {
		final Actor me = this.actorService.findByPrincipal();
		Assert.notNull(me);
		Assert.isTrue(socialProfile.getActor().equals(me)); //solo puede borrar un socialProfile el actor de ese socialProfile

		Assert.notNull(socialProfile);
		Assert.notNull(this.socialProfileRepository.findOne(socialProfile.getId()));

		this.socialProfileRepository.delete(socialProfile);

	}

	// Other business methods -------------------------------------------------

	public Collection<SocialProfile> findByUserId(final int actorId) {
		return this.socialProfileRepository.findByActorId(actorId);
	}
	public Collection<SocialProfile> findAllByPrincipal() {
		return this.socialProfileRepository.findByActorId(this.actorService.findByPrincipal().getId());
	}

}
