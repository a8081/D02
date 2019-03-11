
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.GPS;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	@Autowired
	private SegmentRepository	segmentRepository;


	public Segment create() {
		final Segment result = new Segment();
		final Collection<GPS> gps = new ArrayList<GPS>();
		return result;
	}

	public Segment save(final Segment segment) {
		Segment result;

		//Comprobamos que el usuario logueado es una brotherhood que 
		//tiene la parade a la que corresponde este segmento

		result = this.segmentRepository.save(segment);
		Assert.notNull(result);
		return result;

	}

}
