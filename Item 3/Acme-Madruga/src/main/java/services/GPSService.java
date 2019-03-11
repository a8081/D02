
package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.GPSRepository;
import repositories.SegmentRepository;
import domain.GPS;
import domain.Segment;

@Service
@Transactional
public class GPSService {

	@Autowired
	private GPSRepository		GPSRepository;

	@Autowired
	private SegmentService		segmentService;

	@Autowired
	private SegmentRepository	segmentRepository;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	public GPS create() {
		final GPS result = new GPS();
		final double latitude = 0.0;
		final double longitude = 0.0;
		result.setLatitude(latitude);
		result.setLongitude(longitude);
		return result;
	}

	public GPS save(final GPS gps) {
		GPS result;

		//Comprobamos que pertenece a la brotherhood logueada
		Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.GPSRepository.findBrotherhoodByGPS(gps.getId())), "Este GPS no pertenece al brotherhood logueado");

		result = this.GPSRepository.save(gps);
		Assert.notNull(result);

		if (gps.getId() == 0) {
			final Segment segment = this.GPSRepository.findSegmentByGPS(gps.getId());
			final List<GPS> gpsList = segment.getGps();
			Assert.isTrue(gpsList.size() < 2, "Un segmento no puede tener mas de dos GPSs");
			gpsList.add(result);
			segment.setGps(gpsList);
			this.segmentRepository.save(segment);

		}

		return result;
	}
}
