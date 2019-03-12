
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	private Date	originTime;
	private Date	destinationTime;
	private GPS		originCoordinates;
	private GPS		destinationCoordinates;


	@Temporal(TemporalType.TIMESTAMP)
	//@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getOriginTime() {
		return this.originTime;
	}

	public void setOriginTime(final Date originTime) {
		this.originTime = originTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	//@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
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
	public GPS getDestinationCoordinates() {
		return this.destinationCoordinates;
	}

	public void setDestinationCoordinates(final GPS destinationCoordinates) {
		this.destinationCoordinates = destinationCoordinates;
	}

}
