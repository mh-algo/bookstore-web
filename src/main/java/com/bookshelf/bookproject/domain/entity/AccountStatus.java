package com.bookshelf.bookproject.domain.entity;

public enum AccountStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    DELETED("deleted");

    private String status;

    AccountStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
