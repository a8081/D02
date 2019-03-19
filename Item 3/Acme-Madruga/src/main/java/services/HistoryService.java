
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Service
@Transactional
public class HistoryService {

	@Autowired
	private HistoryRepository		historyRepository;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private InceptionRecordService	inceptionRecordService;


	public History create() {
		final History history = new History();
		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		final InceptionRecord saved = this.inceptionRecordService.saveForNewHistory(inceptionRecord);
		history.setInceptionRecord(saved);
		final Collection<PeriodRecord> periodRecords = new ArrayList<PeriodRecord>();
		history.setPeriodRecords(periodRecords);

		final Collection<LegalRecord> legalRecords = new ArrayList<LegalRecord>();
		history.setLegalRecords(legalRecords);

		final Collection<LinkRecord> linkRecords = new ArrayList<LinkRecord>();
		history.setLinkRecords(linkRecords);

		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<MiscellaneousRecord>();
		history.setMiscellaneousRecords(miscellaneousRecords);

		return history;

	}

	public History createForNewBrotherhood() {
		final History history = new History();

		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		final InceptionRecord saved = this.inceptionRecordService.save(inceptionRecord);
		history.setInceptionRecord(saved);

		final Collection<PeriodRecord> periodRecords = new ArrayList<PeriodRecord>();
		history.setPeriodRecords(periodRecords);

		final Collection<LegalRecord> legalRecords = new ArrayList<LegalRecord>();
		history.setLegalRecords(legalRecords);

		final Collection<LinkRecord> linkRecords = new ArrayList<LinkRecord>();
		history.setLinkRecords(linkRecords);

		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<MiscellaneousRecord>();
		history.setMiscellaneousRecords(miscellaneousRecords);

		final History res = this.historyRepository.save(history);
		return res;
	}

	public Collection<History> findAll() {
		Collection<History> res = new ArrayList<>();
		res = this.historyRepository.findAll();
		Assert.notNull(res, "La lista total de Historias es nula.");
		return res;
	}

	public History findOne(final int historyId) {
		Assert.isTrue(historyId != 0);
		final History res = this.historyRepository.findOne(historyId);
		Assert.notNull(res);
		return res;
	}

	public History save(final History history) {
		Assert.notNull(history);
		final History res;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		if (history.getId() != 0) {
			final Brotherhood bro = this.brotherhoodService.findBrotherhoodByHistory(history.getId());
			Assert.isTrue(bro == brotherhood);
		}
		Assert.notNull(history.getInceptionRecord());
		res = this.historyRepository.save(history);
		return res;
	}

	public void delete(final History history) {
		Assert.notNull(history);
		Assert.isTrue(history.getId() != 0);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		final History retrieved = this.findOne(history.getId());
		Assert.isTrue(this.brotherhoodService.findBrotherhoodByHistory(retrieved.getId()) == brotherhood);
		this.historyRepository.delete(history);
	}

	/* ========================= OTHER METHODS =========================== */

}
