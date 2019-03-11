
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
import domain.Parade;

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
		final InceptionRecord saved = this.inceptionRecordService.save(inceptionRecord);

		history.setInceptionRecord(saved);
		return history;

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

		if (history.getId() == 0)
			Assert.isNull(brotherhood.getHistory());
		res = this.create();

	}
	public void delete(final Parade parade) {

	}

	/* ========================= OTHER METHODS =========================== */

}
