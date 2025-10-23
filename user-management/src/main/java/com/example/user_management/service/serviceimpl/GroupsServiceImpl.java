package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.Groups;
import com.example.user_management.entity.Permission;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.mapper.GroupsMapper;
import com.example.user_management.model.mapper.PermissionMapper;
import com.example.user_management.model.mapper.UserMapper;
import com.example.user_management.model.request.GroupModel;
import com.example.user_management.model.request.PermissionModel;
import com.example.user_management.repo.GroupsRepo;
import com.example.user_management.repo.PermissionRepo;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.GroupsService;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import jakarta.ws.rs.InternalServerErrorException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class GroupsServiceImpl implements GroupsService {
    private static  final Logger LOGGER = Logger.getLogger(GroupsServiceImpl.class);
    private final GroupsRepo repo;
    private final UserRepo userRepo;
    private final PermissionRepo permissionRepo;
    private final GroupsMapper mapper;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    public GroupsServiceImpl(GroupsRepo repo, UserRepo userRepo, PermissionRepo permissionRepo, GroupsMapper mapper, UserMapper userMapper, PermissionMapper permissionMapper) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.permissionRepo = permissionRepo;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public GroupModel saveGroups(GroupModel model) throws BadRequestException {
        Groups group;

        if (model.getId() == null) {
            // CREATE NEW GROUP
            group = new Groups();
        } else {
            // UPDATE EXISTING GROUP
            group = repo.findById(model.getId()).orElseThrow(() -> {
                LOGGER.warn(new LoggerInfo("saveGroups", new Date(),
                        Consts.GROUPS_NOT_FOUND_IN_DATABASE + model.getId()));
                return new BadRequestException(Consts.GROUPS_NOT_FOUND_IN_DATABASE + model.getId());
            });
        }

        group.setName(model.getName());
        group.setDescription(model.getDescription());
        group.setActive(model.isActive());
        group.setCreateDate(model.getCreateDate());
        group.setUpdateDate(model.getUpdateDate());

        if (model.getUserIds() != null && !model.getUserIds().isEmpty()) {
            group.setListUser(userRepo.findAllById(model.getUserIds()));
        }

        if (model.getPermissionModels() != null && !model.getPermissionModels().isEmpty()) {
            List<Long> permIds = model.getPermissionModels()
                    .stream()
                    .map(PermissionModel::getId)
                    .filter(Objects::nonNull)
                    .toList();

            List<Permission> permissions = permissionRepo.findAllById(permIds);
            group.setListPermission(permissions);
        } else {
            group.setListPermission(null);
        }

        Groups saved = repo.save(group);

        if (saved == null) {
            LOGGER.warn(new LoggerInfo("saveGroup", new Date(), Consts.ERROR_UPDATE_DATABASE));
            throw new InternalServerErrorException(Consts.ERROR_UPDATE_DATABASE);
        }

        return mapper.toDto(saved);
    }

    @Override
    public List<GroupModel> getAllGroups() {
        List<Groups>  list = repo.findAll();
        if (list == null  || list.isEmpty()){
            LOGGER.warn(new LoggerInfo("getAllGroups", new Date(), Consts.NO_CONTENT));
            return Collections.emptyList();
        }
        return mapper.toDto(list);
    }

    @Override
    public GroupModel getGroupsById(long id) throws BadRequestException {
        Groups groups = repo.findById(id).orElseThrow(()->new BadRequestException(Consts.GROUPS_NOT_FOUND_IN_DATABASE + id));
        return mapper.toDto(groups);
    }

    @Override
    public void deleteGroups(long id) throws BadRequestException {
        Groups existedGroups = repo.findById(id).orElseThrow(()->{
            LOGGER.warn(new LoggerInfo("deleteGroups", new Date(), Consts.GROUPS_NOT_FOUND_IN_DATABASE + id));
            return new BadRequestException(Consts.GROUPS_NOT_FOUND_IN_DATABASE + id);
        });
        repo.deleteById(id);
        repo.flush();
    }
}
