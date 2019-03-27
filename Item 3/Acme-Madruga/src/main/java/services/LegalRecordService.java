
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

	@Autowired
	private ActorService			actorService;


	public LegalRecord create() {
		final LegalRecord legalRecord = new LegalRecord();
		legalRecord.setTitle("");
		legalRecord.setDescription("");
		legalRecord.setLegalName("");
		legalRecord.setVat(null);
		legalRecord.setLaws(new ArrayList<String>());
		return legalRecord;

	}

	public Collection<LegalRecord> findAll() {
		final Collection<LegalRecord> res = this.legalRecordRepository.findAll();
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
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(legalRecord);
		Assert.notNull(legalRecord.getTitle());
		Assert.notNull(legalRecord.getDescription());
		Assert.isTrue(legalRecord.getTitle() != "");
		Assert.isTrue(legalRecord.getDescription() != "");
		if (legalRecord.getId() != 0)
			Assert.isTrue(this.findBrotherhoodByLegal(legalRecord.getId()) == me);
		final LegalRecord res;
		res = this.legalRecordRepository.save(legalRecord);
		Assert.notNull(this.findOne(res.getId()));
		return res;
	}

	public void delete(final LegalRecord legalRecord) {
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood, "You must be logged in the system");
		Assert.isTrue(this.findBrotherhoodByLegal(legalRecord.getId()) == brotherhood, "No puede borrar un legalRecord que no pertenezca a su historia.");
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);
		final LegalRecord retrieved = this.findOne(legalRecord.getId());
		Assert.isTrue(brotherhood.getHistory().getLegalRecords().contains(retrieved));
		final History history = brotherhood.getHistory();
		final Collection<LegalRecord> legalRecords = history.getLegalRecords();
		legalRecords.remove(retrieved);
		this.legalRecordRepository.delete(retrieved.getId());
	}

	/* ========================= OTHER METHODS =========================== */

	public Brotherhood findBrotherhoodByLegal(final Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);
		final Brotherhood bro = this.legalRecordRepository.findBrotherhoodByLegal(id);
		Assert.notNull(bro);
		return bro;
	}

	public void flush() {
		this.legalRecordRepository.flush();
	}
}
