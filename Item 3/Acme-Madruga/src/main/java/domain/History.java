
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class History extends DomainEntity {

	private InceptionRecord					inceptionRecord;
	private Collection<PeriodRecord>		periodRecords;
	private Collection<LegalRecord>			legalRecords;
	private Collection<LinkRecord>			linkRecords;
	private Collection<MiscellaneousRecord>	miscellaneousRecords;


	@NotNull
	@ManyToOne(optional = false)
	@OneToOne(cascade = CascadeType.ALL)
	public InceptionRecord getInceptionRecord() {
		return this.inceptionRecord;
	}

	public void setInceptionRecord(final InceptionRecord inceptionRecord) {
		this.inceptionRecord = inceptionRecord;
	}

	@ElementCollection
	@ManyToMany
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<PeriodRecord> getPeriodRecords() {
		return this.periodRecords;
	}

	public void setPeriodRecords(final Collection<PeriodRecord> periodRecords) {
		this.periodRecords = periodRecords;
	}

	@ElementCollection
	@ManyToMany
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<LegalRecord> getLegalRecords() {
		return this.legalRecords;
	}

	public void setLegalRecords(final Collection<LegalRecord> legalRecords) {
		this.legalRecords = legalRecords;
	}

	@ElementCollection
	@ManyToMany
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<LinkRecord> getLinkRecords() {
		return this.linkRecords;
	}

	public void setLinkRecords(final Collection<LinkRecord> linkRecords) {
		this.linkRecords = linkRecords;
	}

	@ElementCollection
	@ManyToMany
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}

	public void setMiscellaneousRecords(final Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

}
