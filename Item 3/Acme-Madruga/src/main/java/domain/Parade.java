
package domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title, description, ticker, mode, moment, brotherhood, status")
})
public class Parade extends DomainEntity implements Cloneable {

	private String				title;
	private String				description;
	private Date				moment;
	private String				ticker;
	private String				mode;
	private Integer				maxRows;
	private Integer				maxColumns;
	private String				status;
	private String				rejectionReason;

	//Relational atributes
	private Collection<Float>	floats;
	private Brotherhood			brotherhood;
	private List<Segment>		segments;


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
	@NotNull
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[0-9]{6}-[A-Z]{5}$")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	@Pattern(regexp = "^(DRAFT|FINAL)$")
	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	//Relational Methods
	@Valid
	@NotEmpty
	@ManyToMany
	public Collection<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final Collection<Float> floats) {
		this.floats = floats;
	}
	@Valid
	@ManyToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@Valid
	@ElementCollection
	@NotNull
	@OneToMany(cascade = {
		CascadeType.ALL
	})
	public List<Segment> getSegments() {
		return this.segments;
	}

	public void setSegments(final List<Segment> segments) {
		this.segments = segments;
	}

	@Min(1)
	public Integer getMaxRows() {
		return this.maxRows;
	}

	public void setMaxRows(final Integer maxRows) {
		this.maxRows = maxRows;
	}

	@Min(1)
	public Integer getMaxColumns() {
		return this.maxColumns;
	}

	public void setMaxColumns(final Integer maxColumns) {
		this.maxColumns = maxColumns;
	}

	@NotBlank
	@Pattern(regexp = "^(DEFAULT|SUBMITTED|ACCEPTED|REJECTED)$")
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

	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (final CloneNotSupportedException e) {
		}
		return o;
	}

}
