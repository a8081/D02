
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FloatRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Service
@Transactional
public class FloatService {

	@Autowired
	private FloatRepository					floatRepository;

	@Autowired
	private ProcessionService				processionService;

	@Autowired
	private ConfigurationParametersService	configuracionParametersService;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private ActorService					actorService;


	//Métodos CRUD

	public Float create() {
		final Float fProcession = new Float();
		final Collection<String> pictures = new ArrayList<>();
		final Brotherhood bhood = this.brotherhoodService.findByPrincipal();
		fProcession.setBrotherhood(bhood);
		fProcession.setPictures(pictures);
		Assert.notNull(fProcession);
		return fProcession;
	}
	public Collection<Float> findAll() {
		final Collection<Float> res = this.floatRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Float findOne(final int floatId) {
		Assert.notNull(floatId);
		Assert.isTrue(floatId != 0);
		final Float res = this.floatRepository.findOne(floatId);
		Assert.notNull(res);
		return res;
	}

	public Float save(Float f) {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		if (f.getId() == 0) {
			f.setBrotherhood(this.brotherhoodService.findOne(me.getId()));
			f = this.floatRepository.save(f);
		} else {
			final Float old = this.floatRepository.findOne(f.getId());
			Assert.isTrue(me.getId() == old.getBrotherhood().getId(), "You must be the owner of the float");
			f = this.floatRepository.save(f);
		}

		return f;
	}

	public void delete(final Float f) {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		if (f.getId() != 0) {
			final Float old = this.floatRepository.findOne(f.getId());
			Assert.isTrue(me.getId() == old.getBrotherhood().getId(), "You must be the owner of the float");
			this.floatRepository.delete(f);
		}
	}

	public Collection<Procession> find(final String fProcession) {
		final Collection<Procession> res;

		try {
			final Collection<Procession> aux = this.processionService.findAll();
			aux.retainAll(this.floatRepository.findForFloat(fProcession));
			//			if (aux.size() > numberOfElementInList)
			//				res = new ArrayList<>(aux.subList(0, numberOfElementInList));
			//			else
			res = aux;
			return res;
		} catch (final Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * This method returns all floats associated to a brotherhood
	 * 
	 * @param b
	 *            Brotherhood
	 * 
	 * @author a8081
	 * */
	public Collection<Float> findByBrotherhood(final Brotherhood b) {
		Assert.notNull(b);
		Assert.isTrue(b.getId() != 0);
		final Collection<Float> res = this.floatRepository.findByBrotherhood(b.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Float> findByBrotherhoodPrincipal() {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.BROTHERHOOD), "The principal actor must be a BROTHEHOOD");
		final Collection<Float> res = this.floatRepository.findByBrotherhood(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}
}
