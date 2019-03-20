
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import security.Authority;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	@Autowired
	private PeriodRecordRepository	periodRecordRepository;
	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private ActorService			actorService;


	//Metodos CRUD

	public PeriodRecord create() {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		return new PeriodRecord();
	}

	public Collection<PeriodRecord> findAll() {
		final Collection<PeriodRecord> res = this.periodRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PeriodRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final PeriodRecord res = this.periodRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public PeriodRecord save(final PeriodRecord pR) {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.notNull(pR);
		this.periodRecordRepository.save(pR);
		return pR;
	}

	public void delete(final PeriodRecord pR) {
		final Actor me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.isTrue(pR.getId() != 0);
		Assert.notNull(pR);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		final Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
		periodRecords.remove(pR);
		this.periodRecordRepository.delete(pR.getId());

	}

}
