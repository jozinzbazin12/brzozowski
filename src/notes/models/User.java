package notes.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@NamedQueries({ @NamedQuery(name = "User.all", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.find", query = "SELECT u FROM User u where u.login=?1 and u.password=?2") })
@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = -3891445696703980893L;

	@Id
	@Column(name = "login")
	private String login;

	@Column(name = "name")
	private String name;

	@Column(name = "last_login")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@Column(name = "password")
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
	}

	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date date) {
		this.lastLogin = date;
	}

	public void vaildate() {
		validate(name, "imiê", false);
		validate(login, "login", true);
		validate(password, "has³o", true);
	}

	private void validate(String field, String label, boolean spaces) {
		if (field == null || field.length() < 3 || field.length() > 30) {
			throw new IllegalArgumentException("Nieprawid³owa wartoœæ: " + label);
		}
		if (spaces && field.contains(" ")) {
			throw new IllegalArgumentException("Spacje w " + label + " nie s¹ dozwolone");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof User)) {
			return false;
		}
		User u = (User) obj;
		return u.login.equals(login);
	}

	@Override
	public int hashCode() {
		return login.hashCode();
	}

	@Override
	public String toString() {
		return login + ", " + name;
	}
}
