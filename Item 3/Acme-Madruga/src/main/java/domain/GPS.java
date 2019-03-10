
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class GPS extends DomainEntity {

	double	latitude;
	double	longitude;


	@NotNull
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	@NotNull
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

}
