
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import security.Authority;
import domain.Brotherhood;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private ActorService					actorService;


	//Metodos CRUD

	public MiscellaneousRecord create() {
		final MiscellaneousRecord mRecord = new MiscellaneousRecord();
		mRecord.setTitle("");
		mRecord.setDescription("");
		return mRecord;
	}

	public Collection<MiscellaneousRecord> findAll() {
		final Collection<MiscellaneousRecord> res = this.miscellaneousRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public MiscellaneousRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public MiscellaneousRecord save(final MiscellaneousRecord mR) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.notNull(mR);
		Assert.notNull(mR.getTitle());
		Assert.notNull(mR.getDescription());
		Assert.isTrue(mR.getTitle() != "");
		Assert.isTrue(mR.getDescription() != "");
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.save(mR);
		Assert.notNull(me.getHistory().getMiscellaneousRecords().contains(res));
		return res;
	}

	public void delete(final MiscellaneousRecord mR) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final MiscellaneousRecord res = this.findOne(mR.getId());
		Assert.isTrue(me.getHistory().getMiscellaneousRecords().contains(res));
		this.miscellaneousRecordRepository.delete(res);

	}

}
