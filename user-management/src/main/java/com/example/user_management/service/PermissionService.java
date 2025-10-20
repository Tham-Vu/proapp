package com.example.user_management.service;

import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.PermissionModel;

import java.util.List;

public interface PermissionService {
    PermissionModel savePermission(PermissionModel model) throws BadRequestException;

    List<PermissionModel> getAllPermission();

    PermissionModel getPermissionById(long id) throws BadRequestException;

    void deletePermission(long id) throws BadRequestException;
}
