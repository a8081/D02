
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "area, history")
})
public class Brotherhood extends Actor {

	private String				title;
	private Date				date;
	private Collection<String>	pictures;
	private Area				area;
	private History				history;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	// TODO: @ElementCollection
	@ElementCollection
	@EachURL
	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	@Valid
	@ManyToOne(optional = true)
	public Area getArea() {
		return this.area;
	}

	public void setArea(final Area area) {
		this.area = area;
	}

	@Valid
	@OneToOne(cascade = {
		CascadeType.ALL
	}, optional = true)
	public History getHistory() {
		return this.history;
	}

	public void setHistory(final History history) {
		this.history = history;
	}

}
