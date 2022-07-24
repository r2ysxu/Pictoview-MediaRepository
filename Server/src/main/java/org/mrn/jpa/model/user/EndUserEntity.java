package org.mrn.jpa.model.user;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.mrn.jpa.model.EntityModel;

@Entity
@PrimaryKeyJoinColumn(name = "userid")
public class EndUserEntity extends UserEntity implements EntityModel {
	private static final long serialVersionUID = 1L;

	public static EndUserEntity create(String username, String password) {
		return new EndUserEntity(username, password);
	}

	public EndUserEntity() {
	}

	protected EndUserEntity(String username, String password) {
		super(username, password, Role.END_USER.ordinal());
	}
}
