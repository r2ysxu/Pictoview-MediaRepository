package org.mrn.query.model;

public class PageInfo {
	private Boolean hasNext;
	private Long total;
	private Integer page;

	public PageInfo() {}

	public PageInfo(Integer page, Boolean hasNext, Long total) {
		this.total = total;
		this.page = page;
		this.hasNext = hasNext;
	}

	public Boolean getHasNext() {
		return hasNext;
	}
	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
}
