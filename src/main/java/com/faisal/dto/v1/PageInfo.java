package com.faisal.dto.v1;

public class PageInfo {
    private long totalCount;
    private int rowsPerPage;
    private int currentPage;
    private int totalPageCount;
    private boolean hasNextPage;

    public PageInfo() {}

    public PageInfo(long totalCount, int rowsPerPage, int currentPage, int totalPageCount, boolean hasNextPage) {
        this.totalCount = totalCount;
        this.rowsPerPage = rowsPerPage;
        this.currentPage = currentPage;
        this.totalPageCount = totalPageCount;
        this.hasNextPage = hasNextPage;
    }

    // Getters and setters

    public long getTotalCount() { return totalCount; }
    public void setTotalCount(long totalCount) { this.totalCount = totalCount; }

    public int getRowsPerPage() { return rowsPerPage; }
    public void setRowsPerPage(int rowsPerPage) { this.rowsPerPage = rowsPerPage; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalPageCount() { return totalPageCount; }
    public void setTotalPageCount(int totalPageCount) { this.totalPageCount = totalPageCount; }

    public boolean isHasNextPage() { return hasNextPage; }
    public void setHasNextPage(boolean hasNextPage) { this.hasNextPage = hasNextPage; }
}
