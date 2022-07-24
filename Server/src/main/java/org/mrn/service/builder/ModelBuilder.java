
package org.mrn.service.builder;

import org.mrn.jpa.model.EntityModel;
import org.mrn.query.model.QueryModel;

public interface ModelBuilder<M extends QueryModel, Entity extends EntityModel> {
	public M build(Entity entity);
}
