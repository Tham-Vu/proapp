package com.example.user_management.model.mapper;

import com.example.user_management.entity.User;
import com.example.user_management.model.request.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserModel, User> {
    UserModel toDto(User user);
    User toEntity(UserModel userModel);
    List<UserModel> toDto(List<User> users);
    List<User> toEntity(List<UserModel> models);
}
