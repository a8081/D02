
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import security.Authority;
import domain.Actor;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;
	@Autowired
	private BrotherhoodService			brotherhoodService;
	@Autowired
	private ActorService				actorService;


	//Metodos CRUD

	public InceptionRecord create() {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		return new InceptionRecord();
	}

	public Collection<InceptionRecord> findAll() {
		final Collection<InceptionRecord> res = this.inceptionRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public InceptionRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final InceptionRecord res = this.inceptionRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public InceptionRecord save(final InceptionRecord mR) {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.notNull(mR);
		this.inceptionRecordRepository.save(mR);
		return mR;
	}

	public void delete(final InceptionRecord mR) {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.isTrue(mR.getId() != 0);
		Assert.notNull(mR);
		this.inceptionRecordRepository.delete(mR.getId());

	}

}
