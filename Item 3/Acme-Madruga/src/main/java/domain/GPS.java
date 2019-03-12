
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPS {

	private Double	orginLatitude;
	private Double	orginLongitude;
	private Double	destinationLatitude;
	private Double	destinationLongitude;


	@NotNull
	public Double getOrginLatitude() {
		return this.orginLatitude;
	}

	public void setOrginLatitude(final Double orginLatitude) {
		this.orginLatitude = orginLatitude;
	}

	@NotNull
	public Double getOrginLongitude() {
		return this.orginLongitude;
	}

	public void setOrginLongitude(final Double orginLongitude) {
		this.orginLongitude = orginLongitude;
	}

	@NotNull
	public Double getDestinationLatitude() {
		return this.destinationLatitude;
	}

	public void setDestinationLatitude(final Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	@NotNull
	public Double getDestinationLongitude() {
		return this.destinationLongitude;
	}

	public void setDestinationLongitude(final Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}
}
