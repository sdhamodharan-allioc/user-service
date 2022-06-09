package com.allioc.user.service;

import com.allioc.user.model.Audit;
import com.allioc.user.model.UserGroup;
import com.allioc.user.repository.UserGroupRepository;
import com.allioc.user.util.GenericUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.allioc.user.util.AppConstants.*;

@Service
@Slf4j
public class UserGroupService {

    @Autowired
    private UserGroupRepository userGroupRepo;

    public UserGroup create(UserGroup userGrp, String hdrUsrId) {
        Optional<UserGroup> userGroup = userGroupRepo.findByNameAndOrgId(userGrp.getName(), userGrp.getOrgId());

        if (!userGroup.isPresent()) {
            userGrp.setId(GenericUtil.generateUrnWithPrefix(USERGROUP_URN_PREFIX));
            userGrp.setAudit(GenericUtil.getAudit(hdrUsrId));

            userGroupRepo.insert(userGrp);
        } else {
            throw new HttpClientErrorException(HttpStatus.FOUND, USERGROUP_EXISTS);
        }

        return userGrp;
    }

    public UserGroup update(UserGroup userGrp, String hdrUsrId) {
        Optional<UserGroup> userGroup = userGroupRepo.findById(userGrp.getId());

        if (userGroup.isPresent()) {
            Audit audit = userGroup.get().getAudit();
            if (audit != null) {
                audit.setModifiedBy(hdrUsrId);
                audit.setDateModified(Instant.now().toString());
            } else {
                audit = GenericUtil.getAudit(hdrUsrId);
            }

            userGroupRepo.save(userGrp);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USERGROUP_NOT_EXISTS);
        }

        return userGrp;
    }

    public UserGroup getWorkspaces(String userId, String orgId) {
        log.info("Fetching the workspaces");
        List<UserGroup> userGroups = userGroupRepo.findByUserIdAndOrgId(userId, orgId);
        Set<String> wspaceIds = new HashSet<>();

        //Get the Unique set of the workspaces which this user is part of.
        userGroups.stream().forEach(usrGrp -> {
            wspaceIds.addAll(usrGrp.getWorkspaces().stream().collect(Collectors.toSet()));
        });

        log.info("Workspaces for userId: {} is {}",userId, wspaceIds);
        UserGroup usrGrp = new UserGroup();
        usrGrp.setWorkspaces(wspaceIds.stream().collect(Collectors.toList()));
        usrGrp.setRole(getMyRole(userId));
        return usrGrp;
    }

    public Map<String, String> getRole(String userId) {
        Map<String, String> role = new HashMap<>();
        role.put("role", getMyRole(userId));
        return role;
    }

    private String getMyRole(String userId) {
        List<UserGroup> roles = userGroupRepo.findByUserId(userId);

        log.info("Roles {}", roles);
        Iterator<UserGroup> iter = roles.iterator();
        String role = "";
        while(iter.hasNext()) {
            role = iter.next().getRole();
            if (role.contains("admin")) {
                break;
            }
        }
        log.info("getRole for userId {} is {}" , userId, role);
        return role;
    }

    public List<UserGroup> getUserGroups(String orgId) {
        return userGroupRepo.findByOrgId(orgId);
    }

    public UserGroup getUserGroup(String userGroupId) {
        return userGroupRepo.findById(userGroupId).get();
    }

    /**
     * Find if there is an existing userGroup for this userId & orgId,
     * if not then create a new userGroup
     * @param orgId
     * @param userId
     * @param workspaceId
     * @param hdrUsrId
     * @return
     */
    public void assignWorkspace(String orgId, String userId, String workspaceId, String hdrUsrId) {
        // check if there is a workspace already exists for this user in the Org
        UserGroup userGroup = this.getWorkspaces(userId, orgId);
        //If there is no default userGroup then create new
        Optional<UserGroup> optnlUsrGrp = userGroupRepo.findByNameAndOrgId("Default-UserGroup", orgId);
        if (CollectionUtils.isEmpty(userGroup.getWorkspaces()) && !optnlUsrGrp.isPresent()) {
                UserGroup defaultUsrp = UserGroup.builder().workspaces(Arrays.asList(workspaceId))
                        .users(Arrays.asList(userId))
                        .orgId(orgId)
                        .name("Default-UserGroup")
                        .role("admin")
                        .build();
                log.info("Creating the defaultUserGroup {}", userGroup);

                userGroup = create(defaultUsrp, hdrUsrId);
        } else {
            List<UserGroup> usrGrps = userGroupRepo.findByUsersInAndWorkspacesInAndOrgId(userId, workspaceId, orgId);

            if (CollectionUtils.isEmpty(usrGrps)){
                UserGroup defUsrGrp = optnlUsrGrp.get();
                List<String> users = defUsrGrp.getUsers();
                if (!users.contains(userId)){
                    users.add(userId);
                    defUsrGrp.setUsers(users);
                }

                List<String> wspaces = defUsrGrp.getWorkspaces();
                if (!wspaces.contains(workspaceId)){
                    wspaces.add(workspaceId);
                    defUsrGrp.setWorkspaces(wspaces);
                }
                log.info("Added workspace to userGroup -> {}", defUsrGrp);
                userGroup = update(defUsrGrp, hdrUsrId);
            }
        }

        log.info("assignUserGroup {}", userGroup);

    }
}
