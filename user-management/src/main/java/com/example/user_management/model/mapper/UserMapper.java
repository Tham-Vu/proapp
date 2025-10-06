package com.example.user_management.model.mapper;

import com.example.user_management.entity.User;
import com.example.user_management.model.request.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserModel, User> {
    UserModel toDto(User user);
    User toEntity(UserModel userModel);
}
