package com.example.user_management.model.mapper;

import com.example.user_management.entity.Permission;
import com.example.user_management.model.request.PermissionModel;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionModel, Permission>{
    PermissionModel toDto(Permission permission);
    Permission toEntity(PermissionModel permissionModel);
    List<PermissionModel> toDto(List<Permission> permissions);
}
