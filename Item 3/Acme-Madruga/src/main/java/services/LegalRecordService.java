
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	@Autowired
	private LegalRecordRepository	legalRecordRepository;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	public LegalRecord create() {
		final LegalRecord legalRecord = new LegalRecord();
		return legalRecord;

	}

	public Collection<LegalRecord> findAll() {
		Collection<LegalRecord> res = new ArrayList<>();
		res = this.legalRecordRepository.findAll();
		Assert.notNull(res, "La lista total de LegalRecords es nula.");
		return res;
	}

	public LegalRecord findOne(final int legalRecordId) {
		Assert.isTrue(legalRecordId != 0);
		final LegalRecord res = this.legalRecordRepository.findOne(legalRecordId);
		Assert.notNull(res);
		return res;
	}

	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		final LegalRecord res;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		if (legalRecord.getId() == 0)
			brotherhood.getHistory().getLegalRecords().add(legalRecord);
		else
			Assert.isTrue(brotherhood.getHistory().getLegalRecords().contains(legalRecord));

		res = this.legalRecordRepository.save(legalRecord);
		return res;
	}

	public void delete(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final LegalRecord retrieved = this.findOne(legalRecord.getId());
		Assert.isTrue(brotherhood.getHistory().getLegalRecords().contains(retrieved));
		final History history = brotherhood.getHistory();
		final Collection<LegalRecord> legalRecords = history.getLegalRecords();
		legalRecords.remove(legalRecord);
		this.legalRecordRepository.delete(legalRecord);

	}

	/* ========================= OTHER METHODS =========================== */

}
