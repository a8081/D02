
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import security.Authority;
import domain.Actor;
import domain.Position;

@Service
@Transactional
public class PositionService {

	@Autowired
	private PositionRepository				positionRepository;

	@Autowired
	private AdministratorService			administratorService;

	@Autowired
	private ActorService					actorService;

	@Autowired
	private ConfigurationParametersService	configurationParametersService;


	public Position create() {
		final Position res = new Position();
		res.setNameEnglish("");
		res.setNameSpanish("");
		return res;
	}

	public Position createDefault(final String nameEn, final String nameEs) {
		final Position res = new Position();
		res.setNameEnglish(nameEn);
		res.setNameSpanish(nameEs);
		final Position retrieved = this.positionRepository.save(res);
		return retrieved;
	}

	public Collection<Position> findAll() {
		final Collection<Position> res = this.positionRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Position findOne(final int positionId) {
		Assert.notNull(positionId);
		Assert.isTrue(positionId != 0);
		final Position res = this.positionRepository.findOne(positionId);
		Assert.notNull(res);
		return res;
	}

	public Position save(final Position position) {
		Assert.notNull(position);
		if (position.getId() != 0)
			Assert.isTrue(!(this.configurationParametersService.findSpamWords().contains(position.getNameEnglish()) || this.configurationParametersService.findSpamWords().contains(position.getNameSpanish())), "Position cannot be a spam word");
		for (final Position p : this.positionRepository.findAll()) {
			Assert.isTrue(!p.getNameEnglish().equals(position.getNameEnglish()), "This english name already exists");
			Assert.isTrue(!p.getNameSpanish().equals(position.getNameSpanish()), "This spanish name already exists");
		}

		final Position res = this.positionRepository.save(position);
		return res;
	}

	public void delete(final Position position) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.ADMIN), "Logged user is not an admin");
		Assert.notNull(position, "Position cannot be null");
		Assert.isTrue(position.getId() != 0, "Position id cannot be 0");
		Assert.isTrue(!this.AllPositionUsed().contains(position), "Not possible to delete a position in use");
		this.positionRepository.delete(position);
	}

	/* ========================= OTHER METHODS =========================== */

	public Collection<Position> AllPositionUsed() {
		final Collection<Position> res = this.positionRepository.AllPositionUsed();
		Assert.notNull(res);
		return res;
	}

	public Map<Position, Long> getPositionsFrequency() {
		/*
		 * final List<Map<Position, Integer>> positionsFrequency = this.positionRepository.getPositionFrequency();
		 * Assert.notNull(positionsFrequency);
		 * final Map<Position, Integer> result = new HashMap<Position, Integer>();
		 * System.out.println("AQUIIIIIIIIII" + positionsFrequency.get(0));
		 * for (final Map<Position, Integer> pf : positionsFrequency)
		 * for (final Map.Entry<Position, Integer> entry : pf.entrySet())
		 * result.put(entry.getKey(), entry.getValue());
		 * 
		 * /*
		 * final Map<String, String> result = steps
		 * .stream()
		 * .collect(Collectors.toMap(s -> (String) s.get("key"), s -> (String) s.get("value")));
		 * result.entrySet().forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
		 */
		final Map<Position, Long> result = new HashMap<Position, Long>();
		final List<Object[]> posFreq = this.positionRepository.getPositionFrequency();
		for (final Object[] entry : posFreq)
			result.put((Position) entry[0], (Long) entry[1]);
		return result;
	}
}
