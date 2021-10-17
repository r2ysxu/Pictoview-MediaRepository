package org.mrn.jpa.repo;

import java.util.List;

import org.mrn.jpa.model.file.DirectoryAdded;
import org.springframework.data.repository.CrudRepository;

public interface DirectoryAddedRepo extends CrudRepository<DirectoryAdded, Long> {

	DirectoryAdded findFirstByAbsolutePath(String absolutePath);

	List<DirectoryAdded> findAllByAbsolutePathIn(List<String> absolutePaths);
}
