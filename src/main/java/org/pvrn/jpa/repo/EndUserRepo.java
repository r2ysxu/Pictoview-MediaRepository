package org.pvrn.jpa.repo;

import org.pvrn.jpa.model.user.EndUser;
import org.springframework.data.repository.CrudRepository;

public interface EndUserRepo extends CrudRepository<EndUser, Long> {
	public EndUser findByUsername(String username);
}
