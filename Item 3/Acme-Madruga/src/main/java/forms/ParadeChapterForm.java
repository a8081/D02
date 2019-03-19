
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class ParadeChapterForm extends DomainEntity {

	private String	status;
	private String	rejectionReason;


	@NotBlank
	@Pattern(regexp = "^(SUBMITTED|ACCEPTED|REJECTED)$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@SafeHtml
	public String getRejectionReason() {
		return this.rejectionReason;
	}

	public void setRejectionReason(final String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

}
