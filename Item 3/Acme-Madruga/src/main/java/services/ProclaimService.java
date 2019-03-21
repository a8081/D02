
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ProclaimRepository;
import domain.Chapter;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	@Autowired
	private ProclaimRepository	proclaimRepository;
	@Autowired
	private ChapterService		chapterService;


	//Metodos CRUD

	public Proclaim create() {
		final Proclaim res = new Proclaim();
		res.setMoment(null);
		res.setText("");
		res.setChapter(new Chapter());
		return res;
	}

	public Collection<Proclaim> findAll() {
		final Collection<Proclaim> res = this.proclaimRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Proclaim findOne(final int id) {
		Assert.isTrue(id != 0);
		final Proclaim res = this.proclaimRepository.findOne(id);
		Assert.notNull(res);
		return res;
	}

	public Proclaim save(final Proclaim p) {
		final Chapter me = this.chapterService.findByPrincipal();
		Assert.notNull(me, "You must be logged in the system");
		Assert.notNull(p);
		final Proclaim saved = this.proclaimRepository.save(p);
		Assert.notNull(this.findOne(saved.getId()));
		return saved;
	}

	public Collection<Proclaim> getChapterProclaims(final Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);
		return this.proclaimRepository.getChapterProclaims(id);
	}

}
