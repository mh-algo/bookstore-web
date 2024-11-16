package com.bookshelf.bookproject.security.service;

import com.bookshelf.bookproject.security.mapper.PathRoleMapper;

import java.util.Map;

public class DynamicAuthorizationService {
    private final PathRoleMapper delegate;

    public DynamicAuthorizationService(PathRoleMapper delegate) {
        this.delegate = delegate;
    }

    public Map<String, String> getPathRoleMappings() {
        return delegate.getPathRoleMappings();
    }
}
