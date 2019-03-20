
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	@Autowired
	private LinkRecordRepository	linkRecordRepository;
	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Metodos CRUD

	public LinkRecord create() {
		return new LinkRecord();
	}

	public Collection<LinkRecord> findAll() {
		final Collection<LinkRecord> res = this.linkRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public LinkRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final LinkRecord res = this.linkRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public LinkRecord save(LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		if (linkRecord.getLinkedBrotherhood() != null)
			linkRecord = this.linkRecordRepository.save(linkRecord);
		return linkRecord;
	}

	public void delete(final LinkRecord linkRecord) {
		Assert.isTrue(linkRecord.getId() != 0);
		Assert.notNull(linkRecord.getLinkedBrotherhood());
		Assert.notNull(linkRecord);
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final History history = brotherhood.getHistory();
		final Collection<LinkRecord> linkRecords = history.getLinkRecords();
		linkRecords.remove(linkRecord);
		this.linkRecordRepository.delete(linkRecord.getId());
	}

}
