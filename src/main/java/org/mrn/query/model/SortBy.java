package org.mrn.query.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortBy {
	private String field;
	private Boolean ascending;

	public static Sort getSortField(SortBy sortBy) {
		return getSortField(sortBy.getField(), sortBy.getAscending());
	}

	public static Sort getSortField(String field, Boolean asc) {
		if (asc == null) asc = true;
		if (field == null || "unsorted".equalsIgnoreCase(field)) {
			return Sort.unsorted();
		} else if (asc) {
			return Sort.by(Order.asc(field));
		} else if (!asc) {
			return Sort.by(Order.desc(field));
		} else {
			return Sort.unsorted();
		}
	}

	public static SortBy instance(String field, Boolean asc) {
		if (asc == null) asc = true;
		return new SortBy(field, asc);
	}

	public SortBy() {
		this.field = "unsorted";
		this.ascending = true;
	}

	private SortBy(String field, Boolean asc) {
		this.field = field;
		this.ascending = asc;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Boolean getAscending() {
		return ascending;
	}

	public void setAscending(Boolean ascending) {
		this.ascending = ascending;
	}

}
