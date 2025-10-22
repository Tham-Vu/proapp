package com.example.user_management.service.serviceimpl;

import com.example.user_management.entity.Groups;
import com.example.user_management.entity.Permission;
import com.example.user_management.exception.BadRequestException;
import com.example.user_management.model.mapper.GroupsMapper;
import com.example.user_management.model.mapper.PermissionMapper;
import com.example.user_management.model.request.PermissionModel;
import com.example.user_management.repo.GroupsRepo;
import com.example.user_management.repo.PermissionRepo;
import com.example.user_management.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {
    private static final Logger LOGGER = Logger.getLogger(PermissionServiceImpl.class);
    private final PermissionRepo repo;
    private final GroupsRepo groupsRepo;
    private final PermissionMapper mapper;
    private final GroupsMapper groupsMapper;

    public PermissionServiceImpl(PermissionRepo repo, GroupsRepo groupsRepo, PermissionMapper mapper, GroupsMapper groupsMapper) {
        this.repo = repo;
        this.groupsRepo = groupsRepo;
        this.mapper = mapper;
        this.groupsMapper = groupsMapper;
    }

    @Override
    public PermissionModel savePermission(PermissionModel model) throws BadRequestException {
        Permission savedPermission = new Permission();
        if (model.getId() == null){
            Permission newPermission = mapper.toEntity(model);
            savedPermission = repo.save(newPermission);
        }else {
            //update old permission
            Permission existedPermission = repo.findById(model.getId()).orElseThrow(()->{
                LOGGER.warn(new LoggerInfo("savedPermission", new Date(), Consts.PERMISSION_NOT_FOUND_IN_DATABASE + model.getId()));
                return new BadRequestException(Consts.PERMISSION_NOT_FOUND_IN_DATABASE + model.getId());
            });
            existedPermission.setName(model.getName());
            existedPermission.setDescription(model.getDescription());
            existedPermission.setActive(model.getActive());
            existedPermission.setCreateDate(model.getCreateDate());
            existedPermission.setUpdateDate(model.getUpdateDate());
            List<Groups> groupsList = groupsRepo.findAllById(model.getGroupsIs());
            existedPermission.setListGroup(groupsList);
            savedPermission = repo.save(existedPermission);
        }
        if (savedPermission == null){
            LOGGER.warn(new LoggerInfo("savedPermission", new Date(), Consts.ERROR_UPDATE_DATABASE ));
            throw new InternalServerErrorException(Consts.ERROR_UPDATE_DATABASE + SqlStatementInspector.getLastSql());
        }
        return mapper.toDto(savedPermission);
    }

    @Override
    public List<PermissionModel> getAllPermission() {
        List<Permission>  list = repo.findAll();
        if (list == null  || list.isEmpty()){
            LOGGER.warn(new LoggerInfo("getAllPermission", new Date(), Consts.NO_CONTENT));
            return Collections.emptyList();
        }
        return mapper.toDto(list);
    }

    @Override
    public PermissionModel getPermissionById(long id) throws BadRequestException {
        Permission permission = repo.findById(id).orElseThrow(()->new BadRequestException(Consts.PERMISSION_NOT_FOUND_IN_DATABASE + id));
        return mapper.toDto(permission);
    }

    @Override
    public void deletePermission(long id) throws BadRequestException {
        Permission existedPermission = repo.findById(id).orElseThrow(()->{
            LOGGER.warn(new LoggerInfo("deletePermission", new Date(), Consts.PERMISSION_NOT_FOUND_IN_DATABASE + id));
            return new BadRequestException(Consts.PERMISSION_NOT_FOUND_IN_DATABASE + id);
        });
        repo.deleteById(id);
        repo.flush();
    }
}
