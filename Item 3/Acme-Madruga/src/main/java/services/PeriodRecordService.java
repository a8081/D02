
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.Brotherhood;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	@Autowired
	private PeriodRecordRepository	periodRecordRepository;
	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Metodos CRUD

	public PeriodRecord create() {
		final PeriodRecord res = new PeriodRecord();
		res.setTitle("");
		res.setDescription("");
		res.setPhotos(new ArrayList<String>());
		res.setStartYear(null);
		res.setEndYear(null);
		return res;
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
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(pR);
		Assert.notNull(pR.getTitle());
		Assert.notNull(pR.getDescription());
		Assert.isTrue(pR.getTitle() != "");
		Assert.isTrue(pR.getDescription() != "");
		if (pR.getId() != 0)
			Assert.isTrue(this.findBrotherhoodByPeriod(pR.getId()) == me);
		final PeriodRecord saved = this.periodRecordRepository.save(pR);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public void delete(final PeriodRecord pR) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(pR);
		Assert.isTrue(pR.getId() != 0);
		Assert.isTrue(this.findBrotherhoodByPeriod(pR.getId()) == me);
		final PeriodRecord retrieved = this.findOne(pR.getId());
		Assert.isTrue(me.getHistory().getPeriodRecords().contains(retrieved));
		this.periodRecordRepository.delete(retrieved);

	}

	public Brotherhood findBrotherhoodByPeriod(final Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);
		final Brotherhood bro = this.periodRecordRepository.findBrotherhoodByPeriod(id);
		Assert.notNull(bro);
		return bro;
	}

}
