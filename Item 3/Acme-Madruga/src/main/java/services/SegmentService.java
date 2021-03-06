
package services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import repositories.SegmentRepository;
import domain.GPS;
import domain.Parade;
import domain.Segment;
import forms.SegmentForm;

@Service
@Transactional
public class SegmentService {

	@Autowired
	private SegmentRepository		segmentRepository;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	@Autowired
	private ParadeService			paradeService;

	@org.springframework.beans.factory.annotation.Autowired(required = true)
	private Validator				validator;


	/**
	 * 
	 * los segmentos se guardan en una lista
	 * 
	 * RESTRICCIONES:
	 * S�lo se puede borrar el segmento del �ltimo
	 * S�lo se pueden agregar segmentos con un orden un nivel superior al nivel m�s alto que hay en ese momento
	 * Si se quiere modificar un segmento de nivel intermedio, la unica opcion es editarlo
	 * 
	 * En el jsp, al hacer click sobre ver el path de la parade:
	 * aparecer�n los segmentos ordenador por su orden
	 * aparecer� el bot�n de borrar siempre al lado del �ltimo segmento
	 * en el modo edici�n no se podr� modificar el orden
	 * **/

	public Segment create() {
		final Segment result = new Segment();
		//final List<GPS> gps = new ArrayList<GPS>();
		final GPS origin = new GPS();
		origin.setLatitude(0.0);
		origin.setLongitude(0.0);
		final GPS destination = new GPS();
		destination.setLatitude(0.0);
		destination.setLongitude(0.0);
		result.setOriginCoordinates(origin);
		result.setDestinationCoordinates(destination);

		return result;
	}

	/**
	 * Es necesario pasarle el id de la parade a la que se le quiere a�adir el segmento cuando
	 * se cree un segmento nuevo
	 **/
	public Segment save(final Segment segment, final int idParade) {
		Assert.notNull(segment);
		Segment result;

		if (segment.getId() != 0) { //S�lo se puede modificar el �ltimo segment
			//Comprobamos que el usuario logueado es una brotherhood que 
			//tiene la parade a la que corresponde este segmento
			Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.segmentRepository.findBrotherhoodBySegment(segment.getId())), "El usuario logueado debe ser la hermandad que tiene la parade a la que corresponde ese segmento");

			final Parade parade = this.paradeService.findOne(idParade);

			//Comprobamos que el status de la parade tiene que ser DEFAULT para poder modificar un segment.
			Assert.isTrue(parade.getStatus().equals("DEFAULT"), "No puede modificar un segment de un desfile que tenga su estado distinto a DEFAULT.");

			//Comprobamos que es el �ltimo de la lista (del path)
			final List<Segment> segments = parade.getSegments();
			final Segment lastSegment = segments.get(segments.size() - 1);
			Assert.isTrue(segment.equals(lastSegment), "No se puede editar un segmento si no es el �ltimo del path");

			segment.setOriginTime(lastSegment.getOriginTime());
			segment.setOriginCoordinates(lastSegment.getOriginCoordinates());

			Assert.isTrue(segment.getOriginTime().before(segment.getDestinationTime()), "El horario de llegada no puede ser anterior al horario de salida.");
			Assert.isTrue(this.correctRangeCoordinates(segment));
			result = this.segmentRepository.save(segment);
			Assert.notNull(result, "El segmento guardado es nulo");

		} else {
			//En el caso de que el segmento se cree nuevo, hay que comprobar que
			//el usuario logueado sea una brotherhood

			this.brotherhoodService.findByPrincipal();
			Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.brotherhoodRepository.findBrotherhoodByParade(idParade)), "No puede crear un segment en un desfile que no pertenece a su hermandad.");

			//Comprobamos que, el caso de que no sea el primer segment del path, 
			//el instante y la posicion inicial coincidan con el instante y posicion final del segment
			//anterior
			final Parade parade = this.paradeService.findOne(idParade);
			final List<Segment> segments = parade.getSegments();
			if (!segments.isEmpty()) {
				final Segment lastSegment = segments.get(segments.size() - 1);
				segment.setOriginTime(lastSegment.getDestinationTime());
				segment.setOriginCoordinates(lastSegment.getDestinationCoordinates());
			}
			Assert.isTrue(segment.getOriginTime().before(segment.getDestinationTime()), "El horario de llegada no puede ser anterior al horario de salida.");
			Assert.isTrue(this.correctRangeCoordinates(segment));

			//Guardar el segment
			result = this.segmentRepository.save(segment);
			Assert.notNull(result);
			//Guardarlo en la �ltima posici�n de la lista de segments del parade

			segments.add(result);
			parade.setSegments(segments);

		}

		return result;

	}

	public boolean correctRangeCoordinates(final Segment segment) {
		final boolean rangeOriginLatitude = segment.getOriginCoordinates().getLatitude() < 90.0 && segment.getOriginCoordinates().getLatitude() > (-90.0);
		final boolean rangeOriginLongitude = segment.getOriginCoordinates().getLongitude() < (180.0) && segment.getOriginCoordinates().getLongitude() > (-180.0);
		final boolean rangeDestinationLatitude = segment.getDestinationCoordinates().getLatitude() < 90.0 && segment.getDestinationCoordinates().getLatitude() > (-90.0);
		final boolean rangeDestinationLongitude = segment.getDestinationCoordinates().getLongitude() < 180.0 && segment.getDestinationCoordinates().getLongitude() > (-180.0);
		return (rangeOriginLatitude && rangeOriginLongitude && rangeDestinationLatitude && rangeDestinationLongitude);
	}
	public void delete(final Segment segment, final int paradeId) {

		Assert.isTrue(this.segmentRepository.findOne(segment.getId()).equals(segment), "No se puede borrar un segmento que no existe");

		//Comprobamos que el usuario logueado es una brotherhood que 
		//tiene la parade a la que corresponde este segmento
		Assert.isTrue(this.brotherhoodService.findByPrincipal().equals(this.segmentRepository.findBrotherhoodBySegment(segment.getId())), "El usuario logueado debe ser la hermandad que tiene la parade a la que corresponde ese segmento");

		//Comprobamos que es el �ltimo de la lista (del path)
		final Parade parade = this.paradeService.findOne(paradeId);
		final List<Segment> segments = parade.getSegments();
		final Segment lastSegment = segments.get(segments.size() - 1);
		Assert.isTrue(segment.equals(lastSegment), "No se puede borrar un segmento si no es el �ltimo del path");

		segments.remove(segment);
		this.segmentRepository.delete(segment);
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

	public List<Segment> copyPath(final List<Segment> path) {
		final List<Segment> result = new ArrayList<>();
		Segment retrieved;
		final Segment copia = new Segment();
		for (final Segment segment : path) {
			copia.setOriginTime(segment.getOriginTime());
			copia.setOriginCoordinates(segment.getOriginCoordinates());
			copia.setDestinationTime(segment.getDestinationTime());
			copia.setDestinationCoordinates(segment.getDestinationCoordinates());
			retrieved = this.segmentRepository.save(copia);
			result.add(retrieved);
		}
		return result;
	}

	public void flush() {
		this.segmentRepository.flush();
	}

	public Segment reconstruct(final SegmentForm segmentForm, final BindingResult binding) {
		Segment result;

		Assert.isTrue(segmentForm.getId() != 0);

		result = this.findOne(segmentForm.getId());

		result.setId(segmentForm.getId());
		result.setVersion(segmentForm.getVersion());
		result.setOriginTime(segmentForm.getOriginTime());
		result.setDestinationTime(segmentForm.getDestinationTime());
		result.setOriginCoordinates(segmentForm.getOriginCoordinates());
		result.setDestinationCoordinates(segmentForm.getDestinationCoordinates());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
}
