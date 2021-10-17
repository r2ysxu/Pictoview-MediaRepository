package org.mrn.jpa.repo;

import org.mrn.jpa.model.user.EndUser;
import org.springframework.data.repository.CrudRepository;

public interface EndUserRepo extends CrudRepository<EndUser, Long> {
	public EndUser findByUsername(String username);
}
