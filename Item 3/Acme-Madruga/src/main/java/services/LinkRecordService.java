
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
	@Autowired
	private ActorService			actorService;


	//Metodos CRUD

	public LinkRecord create() {
		final LinkRecord res = new LinkRecord();
		res.setTitle("");
		res.setDescription("");
		res.setLinkedBrotherhood(null);
		return res;

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

	public LinkRecord save(final LinkRecord linkRecord) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(linkRecord);
		Assert.notNull(linkRecord.getTitle());
		Assert.notNull(linkRecord.getDescription());
		Assert.isTrue(linkRecord.getTitle() != "");
		Assert.isTrue(linkRecord.getDescription() != "");
		Assert.notNull(linkRecord.getLinkedBrotherhood());
		if (linkRecord.getId() != 0)
			Assert.isTrue(this.findBrotherhoodByLink(linkRecord.getId()) == me);
		final LinkRecord res = this.linkRecordRepository.save(linkRecord);
		Assert.notNull(this.findOne(res.getId()));
		return res;
	}

	public void delete(final LinkRecord linkRecord) {
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		Assert.notNull(brotherhood, "You must be logged in the system");
		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);
		Assert.isTrue(this.findBrotherhoodByLink(linkRecord.getId()) == brotherhood);
		final LinkRecord retrieved = this.findOne(linkRecord.getId());
		final History history = brotherhood.getHistory();
		final Collection<LinkRecord> linkRecords = history.getLinkRecords();
		Assert.isTrue(linkRecords.contains(retrieved));
		linkRecords.remove(retrieved);
		this.linkRecordRepository.delete(retrieved.getId());
	}

	public Brotherhood findBrotherhoodByLink(final Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);
		final Brotherhood bro = this.linkRecordRepository.findBrotherhoodByLink(id);
		Assert.notNull(bro);
		return bro;

	}

}
