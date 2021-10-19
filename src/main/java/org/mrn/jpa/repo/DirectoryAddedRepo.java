package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.file.DirectoryAddedEntity;
import org.springframework.data.repository.CrudRepository;

public interface DirectoryAddedRepo extends CrudRepository<DirectoryAddedEntity, Long> {

	DirectoryAddedEntity findFirstByAbsolutePath(String absolutePath);

	List<DirectoryAddedEntity> findAllByAbsolutePathIn(List<String> absolutePaths);
}
