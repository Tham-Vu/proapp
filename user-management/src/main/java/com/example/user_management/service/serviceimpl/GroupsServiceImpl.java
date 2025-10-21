package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.Groups;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.mapper.GroupsMapper;
import com.example.user_management.model.mapper.PermissionMapper;
import com.example.user_management.model.mapper.UserMapper;
import com.example.user_management.model.request.GroupModel;
import com.example.user_management.repo.GroupsRepo;
import com.example.user_management.service.GroupsService;
import com.example.user_management.utils.Consts;
import com.example.user_management.utils.LoggerInfo;
import com.example.user_management.utils.SqlStatementInspector;
import jakarta.ws.rs.InternalServerErrorException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GroupsServiceImpl implements GroupsService {
    private static  final Logger LOGGER = Logger.getLogger(GroupsServiceImpl.class);
    private final GroupsRepo repo;
    private final GroupsMapper mapper;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    public GroupsServiceImpl(GroupsRepo repo, GroupsMapper mapper, UserMapper userMapper, PermissionMapper permissionMapper) {
        this.repo = repo;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public GroupModel saveGroups(GroupModel model) throws BadRequestException {
        Groups savedGroup = new Groups();
        if (model.getId() == null){
            Groups newGroups = mapper.toEntity(model);
            savedGroup = repo.save(newGroups);
        }else {
            //update old permission
            Groups existedGroups = repo.findById(model.getId()).orElseThrow(()->{
                LOGGER.warn(new LoggerInfo("saveGroups", new Date(), Consts.GROUPS_NOT_FOUND_IN_DATABASE + model.getId()));
                return new BadRequestException(Consts.GROUPS_NOT_FOUND_IN_DATABASE + model.getId());
            });
            existedGroups.setName(model.getName());
            existedGroups.setDescription(model.getDescription());
            existedGroups.setActive(model.isActive());
            existedGroups.setCreateDate(model.getCreateDate());
            existedGroups.setUpdateDate(model.getUpdateDate());
            existedGroups.setListUser(userMapper.toEntity(model.getUserModelList()));
            existedGroups.setListPermission(permissionMapper.toEntity(model.getPermissionModelList()));
            savedGroup = repo.save(mapper.toEntity(model));
        }
        if (savedGroup == null){
            LOGGER.warn(new LoggerInfo("saveGroup", new Date(), Consts.ERROR_UPDATE_DATABASE ));
            throw new InternalServerErrorException(Consts.ERROR_UPDATE_DATABASE + SqlStatementInspector.getLastSql());
        }
        return mapper.toDto(savedGroup);
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
