
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


	//Metodos CRUD

	public MiscellaneousRecord create() {
		return new MiscellaneousRecord();
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
		Assert.notNull(mR);
		return this.miscellaneousRecordRepository.save(mR);
	}

	public void delete(final MiscellaneousRecord mR) {
		Assert.isTrue(mR.getId() != 0);
		Assert.notNull(mR);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();
		miscellaneousRecords.remove(mR);
		this.miscellaneousRecordRepository.delete(mR.getId());

	}
}
