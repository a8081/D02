
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {
	
	private Date originTime;
	private Date destinationTime;oo
	
	//Relational attributes
	private Collection<Gps> gps;

	
	public Date getOriginTime() {
		return this.originTime;
	}

	
	public void setOriginTime(final Date originTime) {
		this.originTime = originTime;
	}

	
	public Date getDestinationTime() {
		return this.destinationTime;
	}

	
	public void setDestinationTime(final Date destinationTime) {
		this.destinationTime = destinationTime;
	}


	@Valid
	@ManyToMany
	public Collection<Gps> getGps() {
		return this.gps;
	}


	
	public void setGps(final Collection<Gps> gps) {
		this.gps = gps;
	}

	
	
	
	
	

}
