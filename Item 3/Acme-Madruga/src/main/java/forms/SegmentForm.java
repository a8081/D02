
package forms;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;

import domain.DomainEntity;
import domain.GPS;

@Entity
@Access(AccessType.PROPERTY)
public class SegmentForm extends DomainEntity {

	private Date	originTime;
	private Date	destinationTime;
	private GPS		originCoordinates;
	private GPS		destinationCoordinates;

	private int		paradeId;


	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getOriginTime() {
		return this.originTime;
	}

	public void setOriginTime(final Date originTime) {
		this.originTime = originTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getDestinationTime() {
		return this.destinationTime;
	}

	public void setDestinationTime(final Date destinationTime) {
		this.destinationTime = destinationTime;
	}

	@Valid
	public GPS getOriginCoordinates() {
		return this.originCoordinates;
	}

	public void setOriginCoordinates(final GPS originCoordinates) {
		this.originCoordinates = originCoordinates;
	}

	@Valid
	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "destinationLatitude")), @AttributeOverride(name = "longitude", column = @Column(name = "destinationLongitude"))
	})
	public GPS getDestinationCoordinates() {
		return this.destinationCoordinates;
	}

	public void setDestinationCoordinates(final GPS destinationCoordinates) {
		this.destinationCoordinates = destinationCoordinates;
	}

	public int getParadeId() {
		return this.paradeId;
	}

	public void setParadeId(final int paradeId) {
		this.paradeId = paradeId;
	}

}
