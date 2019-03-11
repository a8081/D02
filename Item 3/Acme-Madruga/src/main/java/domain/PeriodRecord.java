
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends Record {

	private Date				startYear;
	private Date				endYear;
	private Collection<String>	photos;


	@NotNull
	public Date getStartYear() {
		return this.startYear;
	}

	public void setStartYear(final Date startYear) {
		this.startYear = startYear;
	}

	@NotNull
	public Date getEndYear() {
		return this.endYear;
	}

	public void setEndYear(final Date endYear) {
		this.endYear = endYear;
	}

	@NotBlank
	@EachURL
	@ElementCollection
	public Collection<String> getPhotos() {
		return this.photos;
	}

	public void setPhotos(final Collection<String> photos) {
		this.photos = photos;
	}

}
