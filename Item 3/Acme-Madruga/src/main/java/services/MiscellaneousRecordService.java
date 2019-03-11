
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;

@Service
@Transactional
public class MiscellaneousRecordService {

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;


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
		this.miscellaneousRecordRepository.save(mR);
		return mR;
	}

	public void delete(final MiscellaneousRecord mR) {
		Assert.isTrue(mR.getId() != 0);
		Assert.notNull(mR);
		this.miscellaneousRecordRepository.delete(mR.getId());

	}

}
