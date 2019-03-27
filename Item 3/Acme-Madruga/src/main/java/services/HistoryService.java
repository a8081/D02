
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
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
		inceptionRecord.setTitle("Title inception " + brotherhood.getName());
		inceptionRecord.setDescription("Decription inception " + brotherhood.getName());
		history.setInceptionRecord(inceptionRecord);

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

	//	public History createForNewBrotherhood() {
	//		final History history = new History();
	//
	//		final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
	//		final InceptionRecord saved = this.inceptionRecordService.save(inceptionRecord);
	//		history.setInceptionRecord(saved);
	//
	//		final Collection<PeriodRecord> periodRecords = new ArrayList<PeriodRecord>();
	//		history.setPeriodRecords(periodRecords);
	//
	//		final Collection<LegalRecord> legalRecords = new ArrayList<LegalRecord>();
	//		history.setLegalRecords(legalRecords);
	//
	//		final Collection<LinkRecord> linkRecords = new ArrayList<LinkRecord>();
	//		history.setLinkRecords(linkRecords);
	//
	//		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<MiscellaneousRecord>();
	//		history.setMiscellaneousRecords(miscellaneousRecords);
	//
	//		final History res = this.historyRepository.save(history);
	//		return res;
	//	}

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

		if (history.getId() != 0)
			Assert.isTrue(this.brotherhoodService.findBrotherhoodByHistory(history.getId()).equals(brotherhood));
		res = this.historyRepository.save(history);
		return res;
	}

	public void delete(final History history) {
		Assert.notNull(history);
		Assert.isTrue(history.getId() != 0);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History retrieved = this.findOne(history.getId());
		Assert.isTrue(this.brotherhoodService.findBrotherhoodByHistory(retrieved.getId()) == brotherhood, "No puede borrar la historia de otra brotherhood.");
		brotherhood.setHistory(null);
		this.historyRepository.delete(retrieved.getId());
	}

	/* ========================= OTHER METHODS =========================== */

	public void flush() {
		this.historyRepository.flush();
	}

	public Double[] getStatisticsOfRecordsPerHistory() {
		final Double[] result = this.historyRepository.getStatisticsOfRecordsPerHistory();
		Assert.notNull(result);
		return result;
	}

	public Collection<Brotherhood> getLargestBrotherhoodPerHistory() {
		final Collection<Brotherhood> result = this.historyRepository.getLargestBrotherhoodPerHistory();
		Assert.notNull(result);
		return result;
	}

	public Collection<Brotherhood> getBrotherhoodPerHistoryLargerThanStd() {
		final Collection<Brotherhood> result = this.historyRepository.getBrotherhoodPerHistoryLargerThanStd();
		Assert.notNull(result);
		return result;

	}

}
