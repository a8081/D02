
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class LinkRecord extends DomainEntity {

	private Brotherhood	linkedBrotherhood;
	private String		title;
	private String		description;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Valid
	@ManyToOne(optional = false)
	public Brotherhood getLinkedBrotherhood() {
		return this.linkedBrotherhood;
	}

	public void setLinkedBrotherhood(final Brotherhood linkedBrotherhood) {
		this.linkedBrotherhood = linkedBrotherhood;
	}

}
