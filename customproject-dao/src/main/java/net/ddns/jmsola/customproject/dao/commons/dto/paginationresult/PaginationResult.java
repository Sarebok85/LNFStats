package net.ddns.jmsola.customproject.dao.commons.dto.paginationresult;

import java.io.Serializable;
import java.util.List;

public class PaginationResult<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> result;

	private int pageNumber;
	private int pageSize;
	private int offset;
	private long totalResult;

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public PaginationResult<T> result(List<T> result) {
		this.result = result;
		return this;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public PaginationResult<T> pageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public PaginationResult<T> pageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public PaginationResult<T> offset(int offset) {
		this.offset = offset;
		return this;
	}

	public long getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(long totalResult) {
		this.totalResult = totalResult;
	}

	public PaginationResult<T> totalResult(long totalResult) {
		this.totalResult = totalResult;
		return this;
	}

	public long getTotalPages() {
		return (this.getTotalResult() / this.getPageSize()) + 1;
	}
}
