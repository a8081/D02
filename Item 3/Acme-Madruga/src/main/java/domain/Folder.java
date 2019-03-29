
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "actor, father, isSystemFolder")
})
public class Folder extends DomainEntity {

	private String				name;
	private Boolean				isSystemFolder;

	//Relational attributes
	private Actor				actor;
	private Collection<Message>	messages;
	private Folder				father;


	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	public Boolean getIsSystemFolder() {
		return this.isSystemFolder;
	}

	public void setIsSystemFolder(final Boolean isSystemFolder) {
		this.isSystemFolder = isSystemFolder;
	}

	//Relational methods

	@Valid
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

	@Valid
	@ManyToMany
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@Valid
	@ManyToOne(optional = true)
	public Folder getFather() {
		return this.father;
	}

	public void setFather(final Folder father) {
		this.father = father;
	}

}
