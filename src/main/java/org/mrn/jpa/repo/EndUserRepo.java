package org.mrn.jpa.repo;

import org.mrn.jpa.model.user.EndUserEntity;
import org.springframework.data.repository.CrudRepository;

public interface EndUserRepo extends CrudRepository<EndUserEntity, Long> {
	public EndUserEntity findByUsername(String username);
}
