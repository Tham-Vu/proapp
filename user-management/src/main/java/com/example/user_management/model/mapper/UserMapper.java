package com.example.user_management.model.mapper;

import com.example.user_management.entity.User;
import com.example.user_management.model.request.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserModel, User> {
//    @Mapping(target = "id", source = "id")
    @Mapping(target = "groups", ignore  = true)
    UserModel toDto(User user);
    @Mapping(target = "groups", ignore  = true)
    User toEntity(UserModel userModel);
    List<UserModel> toDto(List<User> users);
}
