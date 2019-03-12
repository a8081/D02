
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class LinkRecord extends Record {

	private Brotherhood	linkedBrotherhood;


	@Valid
	@ManyToOne(optional = false)
	public Brotherhood getLinkedBrotherhood() {
		return this.linkedBrotherhood;
	}

	public void setLinkedBrotherhood(final Brotherhood linkedBrotherhood) {
		this.linkedBrotherhood = linkedBrotherhood;
	}

}
