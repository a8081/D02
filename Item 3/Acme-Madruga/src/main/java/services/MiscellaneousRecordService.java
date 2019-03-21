
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Brotherhood;
import domain.History;
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
		Assert.notNull(mR);
		Assert.notNull(mR.getTitle());
		Assert.notNull(mR.getDescription());
		Assert.isTrue(mR.getTitle() != "");
		Assert.isTrue(mR.getDescription() != "");
		if (mR.getId() != 0)
			Assert.isTrue(this.findBrotherhoodByMiscellaneous(mR.getId()) == me);
		final MiscellaneousRecord res = this.miscellaneousRecordRepository.save(mR);
		Assert.notNull(me.getHistory().getMiscellaneousRecords().contains(res));
		return res;
	}

	public void delete(final MiscellaneousRecord mR) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.findBrotherhoodByMiscellaneous(mR.getId()) == me);
		Assert.notNull(mR);
		Assert.isTrue(mR.getId() != 0);
		final MiscellaneousRecord res = this.findOne(mR.getId());
		Assert.isTrue(me.getHistory().getMiscellaneousRecords().contains(res));
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();
		miscellaneousRecords.remove(res);
		this.miscellaneousRecordRepository.delete(res.getId());

	}

	public Brotherhood findBrotherhoodByMiscellaneous(final Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);
		final Brotherhood bro = this.miscellaneousRecordRepository.findBrotherhoodByMiscellaneous(id);
		Assert.notNull(bro);
		return bro;
	}
}
