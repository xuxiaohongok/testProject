package com.zhidian.common.repository.support.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ASC = "asc";
	public static final String DESC = "desc";

	protected int pageNo = 1;
	protected int pageSize = 1;
	protected String orderBy = null;
	protected String order = null;
	protected boolean autoCount = true;

	protected List<T> result = Collections.emptyList();
	protected long totalCount = 0;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public Page(int pageSize,int pageNo) {
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", orderBy=" + orderBy + ", order="
				+ order + ", autoCount=" + autoCount + ", result=" + result + ", totalCount=" + totalCount
				+ "]";
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public Page<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	public String getOrder() {
		return order;
	}

	
	public void setOrder(final String order) {
		String[] orders = order.toLowerCase().split(",");
		for (String orderStr : orders) {
			if (!DESC.equals(orderStr) || !ASC.equals(orderStr))
				throw new IllegalArgumentException("order " + orderStr + " illegal");
		}

		this.order = order.toLowerCase();
	}

	public Page<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	public boolean isOrderBySetted() {
		return (orderBy!=null && !orderBy.trim().isEmpty()) && (order!=null && !order.trim().isEmpty());
	}

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	public Page<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	public List<T> getResult() {
		return result;
	}

	
	public void setResult(final List<T> result) {
		this.result = result;
	}
	
	public long getTotalCount() {
		return totalCount;
	}

	
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}
	
	public long getTotalPages() {
		if (totalCount <= 0)
			return 0;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}
}
