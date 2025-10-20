package com.example.user_management.service;

import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.request.GroupModel;

import java.util.List;

public interface GroupsService {
    List<GroupModel> getAllGroups();

    GroupModel getGroupsById(long id) throws BadRequestException;

    GroupModel saveGroups(GroupModel model) throws BadRequestException;

    void deleteGroups(long id) throws BadRequestException;
}
