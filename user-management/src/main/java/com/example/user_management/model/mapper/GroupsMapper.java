package com.example.user_management.model.mapper;

import com.example.user_management.entity.Groups;
import com.example.user_management.entity.Permission;
import com.example.user_management.model.request.GroupModel;
import com.example.user_management.model.request.PermissionModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupsMapper extends EntityMapper<GroupModel, Groups> {
    GroupModel toDto(Groups group);

    Groups toEntity(GroupModel model);
    List<GroupModel> toDto(List<Groups> groups);
    List<Groups> toEntity(List<GroupModel> groupModels);
}
