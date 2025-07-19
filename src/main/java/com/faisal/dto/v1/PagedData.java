package com.faisal.dto.v1;

import java.util.List;

public class PagedData<T> {
    private PageInfo pageInfo;
    private List<T> items;

    public PagedData() {}

    public PagedData(PageInfo pageInfo, List<T> items) {
        this.pageInfo = pageInfo;
        this.items = items;
    }

    // Getters and setters

    public PageInfo getPageInfo() { return pageInfo; }
    public void setPageInfo(PageInfo pageInfo) { this.pageInfo = pageInfo; }

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }
}
