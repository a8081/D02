
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import domain.DomainEntity;
import domain.Position;

@Entity
@Access(AccessType.PROPERTY)
public class EnrolmentForm extends DomainEntity {

	private Position	position;


	//Relational getters and setters

	@Valid
	@ManyToOne(optional = false)
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

}
