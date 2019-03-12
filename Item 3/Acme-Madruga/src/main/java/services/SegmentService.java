
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BrotherhoodRepository;
import repositories.SegmentRepository;
import domain.GPS;
import domain.Parade;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	@Autowired
	private SegmentRepository		segmentRepository;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;


	/**
	 * 
	 * los segmentos se guardan en una lista
	 * 
	 * RESTRICCIONES:
	 * Sólo se puede borrar el segmento del último
	 * Sólo se pueden agregar segmentos con un orden un nivel superior al nivel más alto que hay en ese momento
	 * Si se quiere modificar un segmento de nivel intermedio, la unica opcion es editarlo
	 * 
	 * En el jsp, al hacer click sobre ver el path de la parade:
	 * aparecerán los segmentos ordenador por su orden
	 * aparecerá el botón de borrar siempre al lado del último segmento
	 * en el modo edición no se podrá modificar el orden
	 * **/

	public Segment create() {
		final Segment result = new Segment();
		//TODO esto lo tengo que hacer aqui por el hecho de que tiene  que ser 2 si o si?
		final List<GPS> gps = new ArrayList<GPS>();
		final GPS origin = new GPS();
		origin.setLatitude(0.0);
		origin.setLongitude(0.0);
		final GPS destination = new GPS();
		destination.setLatitude(0.0);
		destination.setLongitude(0.0);
		gps.add(origin);
		gps.add(destination);
		result.setGps(gps);

		return result;
	}

	/**
	 * Es necesario pasarle el id de la parade a la que se le quiere añadir el segmento cuando
	 * se cree un segmento nuevo
	 **/
	public Segment save(final Segment segment, final int idParade) {
		Segment result;

		if (segment.getId() != 0) { //Sólo se puede modificar el último segment
			//Comprobamos que el usuario logueado es una brotherhood que 
			//tiene la parade a la que corresponde este segmento
			Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.segmentRepository.findBrotherhoodBySegment(segment.getId())), "El usuario logueado debe ser la hermandad que tiene la parade a la que corresponde ese segmento");

			//Comprobamos que es el último de la lista (del path)
			final Parade parade = this.segmentRepository.findParadeBySegment(segment.getId());
			final List<Segment> segments = parade.getSegments();
			final Segment lastSegment = segments.get(segments.size() - 1);
			Assert.isTrue(segment.equals(lastSegment), "No se puede editar un segmento si no es el último del path");

			//Comprobamos que solo se modifica el tiempo o la posicion final
			final Segment oldSegment = this.segmentRepository.findOne(segment.getId());
			final Date oldOriginDate = oldSegment.getOriginTime();
			final GPS oldOriginGPS = oldSegment.getGps().get(0);
			final Date newOriginDate = segment.getOriginTime();
			final GPS newOriginGPS = segment.getGps().get(0);
			Assert.isTrue(oldOriginDate.equals(newOriginDate) && oldOriginGPS.equals(newOriginGPS), "No se puede modificar ni la posicion ni la fecha de origen");

			result = this.segmentRepository.save(segment);
			Assert.notNull(result);

		} else {
			//En el caso de que el segmento se cree nuevo, hay que comprobar que
			//el usuario logueado sea una brotherhood

			this.brotherhoodService.findByPrincipal();
			Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.brotherhoodRepository.findBrotherhoodByParade(idParade)));

			//Comprobamos que, el caso de que no sea el primer segment del path, 
			//el instante y la posicion inicial coincidan con el instante y posicion final del segment
			//anterior
			final Parade parade = this.segmentRepository.findParadeBySegment(segment.getId());
			final List<Segment> segments = parade.getSegments();
			if (!segments.isEmpty()) {
				final Segment lastSegment = segments.get(segments.size() - 1);
				final Date initialDate = segment.getOriginTime();
				final GPS initialGPS = segment.getGps().get(0);
				final Date finalDate = lastSegment.getDestinationTime();
				final GPS finalGPS = lastSegment.getGps().get(1);
				Assert.isTrue(initialDate.equals(finalDate) && initialGPS.equals(finalGPS), "El instante y la posición de inicio del nuevo segmento no coincide con la fecha y posición final del segmnto precedente");

			}

			//Guardar el segment
			result = this.segmentRepository.save(segment);
			Assert.notNull(result);
			//Guardarlo en la última posición de la lista de segments del parade

			segments.add(result);
			parade.setSegments(segments);

		}

		return result;

	}

	public void delete(final Segment segment) {

		Assert.isTrue(this.segmentRepository.findOne(segment.getId()).equals(segment), "No se puede borrar un segmento que no existe");

		//Comprobamos que el usuario logueado es una brotherhood que 
		//tiene la parade a la que corresponde este segmento
		Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.segmentRepository.findBrotherhoodBySegment(segment.getId())), "El usuario logueado debe ser la hermandad que tiene la parade a la que corresponde ese segmento");

		//Comprobamos que es el último de la lista (del path)
		final Parade parade = this.segmentRepository.findParadeBySegment(segment.getId());
		final List<Segment> segments = parade.getSegments();
		final Segment lastSegment = segments.get(segments.size() - 1);
		Assert.isTrue(segment.equals(lastSegment), "No se puede borrar un segmento si no es el último del path");

	}

	public List<Segment> findAll() {
		final List<Segment> result = this.segmentRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Segment findOne(final int id) {
		//TODO solo puede ver los segments la brotherhood que tiene el parade?
		final Segment result = this.segmentRepository.findOne(id);
		Assert.notNull(result);
		return result;
	}

	public List<Segment> getPath(final int idParade) {
		//TODO solo puede ver los segments la brotherhood que tiene el parade?
		final List<Segment> result = this.segmentRepository.findSegmentsByParade(idParade);
		Assert.notNull(result);
		return result;
	}

}
