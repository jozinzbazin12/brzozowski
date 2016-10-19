package notes.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQuery(name = "Message.all", query = "SELECT m FROM Message m")
@Entity()
@Table(name = "message")
public class Message implements Serializable {
	private static final long serialVersionUID = -5118762503015592746L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private int messageId;

	@Column(name = "text")
	private String text;

	@JoinColumn(name = "owner")
	private User owner;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<User> editors = new HashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Message)) {
			return false;
		}
		Message m = (Message) obj;
		return m.messageId == messageId;
	}

	public Message() {
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<User> getEditors() {
		return editors;
	}

	public void setEditors(Set<User> editors) {
		this.editors = editors;
	}

}
