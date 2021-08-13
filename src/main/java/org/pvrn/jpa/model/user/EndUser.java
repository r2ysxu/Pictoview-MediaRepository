package org.pvrn.jpa.model.user;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "userid")
public class EndUser extends User {
	private static final long serialVersionUID = 1L;

	public static EndUser create(String username, String password) {
		return new EndUser(username, password);
	}

	public EndUser() {
	}

	protected EndUser(String username, String password) {
		super(username, password, Role.END_USER.ordinal());
	}
}
