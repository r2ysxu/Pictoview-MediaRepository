package org.mrn.query.model;

import java.util.List;

public class PageItems<M extends QueryModel> {
	private List<M> items;
	private PageInfo pageInfo;
	
	public PageItems() {}
	
	public PageItems(List<M> items, PageInfo pageInfo) {
		this.items = items;
		this.pageInfo = pageInfo;
	}

	public List<M> getItems() {
		return items;
	}

	public void setItems(List<M> items) {
		this.items = items;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
}
