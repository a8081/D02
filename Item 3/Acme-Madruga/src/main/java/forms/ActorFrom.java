
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class ActorFrom extends DomainEntity {

	private String	name;
	private String	middleName;
	private String	surname;
	private String	photo;
	private String	email;
	private String	phone;
	private String	address;
	private Boolean	termsAndCondicions;


	@AssertTrue
	public Boolean getTermsAndCondicions() {
		return this.termsAndCondicions;
	}

	public void setTermsAndCondicions(final Boolean termsAndCondicions) {
		this.termsAndCondicions = termsAndCondicions;
	}


	//Relational attributes
	private String	userAccountuser;
	private String	userAccountpassword;


	@Size(min = 5, max = 32)
	public String getUserAccountuser() {
		return this.userAccountuser;
	}

	public void setUserAccountuser(final String userAccountuser) {
		this.userAccountuser = userAccountuser;
	}
	@Size(min = 5, max = 32)
	public String getUserAccountpassword() {
		return this.userAccountpassword;
	}

	public void setUserAccountpassword(final String userAccountpassword) {
		this.userAccountpassword = userAccountpassword;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	//TODO: Phone Pattern
	//@Pattern(regexp="^\+{1}[1-9]{0,3}\s\([1-9]{0,3}\)\s[0-9]{4,}|\+{1}[1-9]{0,3}\s[0-9]{4,}|[0-9]{4,}$")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

}
