
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import security.Authority;
import domain.Brotherhood;
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
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.notNull(linkRecord);
		Assert.notNull(linkRecord.getTitle());
		Assert.notNull(linkRecord.getDescription());
		Assert.isTrue(linkRecord.getTitle() != "");
		Assert.isTrue(linkRecord.getDescription() != "");
		Assert.notNull(linkRecord.getLinkedBrotherhood());
		final LinkRecord res = this.linkRecordRepository.save(linkRecord);
		Assert.notNull(this.findOne(res.getId()));
		return res;
	}

	public void delete(final LinkRecord linkRecord) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.isTrue(this.actorService.checkAuthority(me, Authority.BROTHERHOOD), "You must be BROTHERHOO");
		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);
		final LinkRecord retrieved = this.findOne(linkRecord.getId());
		Assert.isTrue(me.getHistory().getLinkRecords().contains(retrieved));
		this.linkRecordRepository.delete(retrieved);
	}

}
