
package org.mrn.service.builder;

import java.util.List;

import org.mrn.jpa.model.EntityModel;
import org.mrn.query.model.PageInfo;
import org.mrn.query.model.PageItems;
import org.mrn.query.model.QueryModel;
import org.springframework.data.domain.Page;

public class PageItemBuilder<M extends QueryModel, Entity extends EntityModel> {

	public PageItems<M> build(Page<Entity> pageItems, ModelBuilder<M, Entity> builder) {
		List<Entity> items = pageItems.getContent().stream().map(item -> (Entity)item).toList();
		return new PageItems<>(items.stream().map(item -> builder.build(item)).toList(),
				new PageInfo(pageItems.getPageable().getPageNumber(), pageItems.hasNext(), pageItems.getTotalElements()));
	}
}
