
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.Brotherhood;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;
	@Autowired
	private BrotherhoodService			brotherhoodService;
	@Autowired
	private ActorService				actorService;


	//Metodos CRUD

	public InceptionRecord create() {
		final InceptionRecord res = new InceptionRecord();
		res.setTitle("");
		res.setDescription("");
		res.setPhotos(new ArrayList<String>());
		return res;
	}

	public Collection<InceptionRecord> findAll() {
		final Collection<InceptionRecord> res = this.inceptionRecordRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public InceptionRecord findOne(final int id) {
		Assert.isTrue(id != 0);
		final InceptionRecord res = this.inceptionRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public InceptionRecord save(final InceptionRecord iR) {
		final Brotherhood me = this.brotherhoodService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(iR);
		Assert.notNull(iR.getTitle());
		Assert.notNull(iR.getDescription());
		if (iR.getId() != 0) {
			Assert.isTrue(iR.getTitle() != "");
			Assert.isTrue(iR.getDescription() != "");
			Assert.isTrue(this.findBrotherhoodByInception(iR.getId()) == me);
		}
		final InceptionRecord saved = this.inceptionRecordRepository.save(iR);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public Brotherhood findBrotherhoodByInception(final Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);
		final Brotherhood bro = this.inceptionRecordRepository.findBrotherhoodByInception(id);
		Assert.notNull(bro);
		return bro;
	}

}
