package com.allioc.user.controller;

import com.allioc.user.model.UserGroup;
import com.allioc.user.service.UserGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    /**
     * Create a new user group
     * @param userGrp
     * @param hdrUsrId
     * @return
     */
    @PostMapping("/usergroup")
    public ResponseEntity<UserGroup> create(@RequestBody UserGroup userGrp,
                                            @RequestHeader(name="x-user-id") String hdrUsrId) {
        return ResponseEntity.ok().body(userGroupService.create(userGrp, hdrUsrId));
    }

    /**
     * Update the existing user group
     * @param userGroup
     * @param hdrUsrId
     * @return
     */
    @PutMapping("/usergroup/{userGroupId}")
    public ResponseEntity<UserGroup> update(@RequestBody UserGroup userGroup,
                                            @PathVariable(name="userGroupId", required = true) String userGroupId,
                                            @RequestHeader(name="x-user-id") String hdrUsrId) {
        userGroup.setId(userGroupId);
        return ResponseEntity.ok().body(userGroupService.update(userGroup, hdrUsrId));
    }

    /**
     * Get all the workspaces that are mapped to the given user in the org
     * @param userId
     * @param orgId
     * @return
     */
    @GetMapping("/{orgId}/{userId}/workspaces")
    public ResponseEntity<UserGroup> getWorkspacesForUser(@PathVariable(name="userId", required = true) String userId,
                                                          @PathVariable(name="orgId", required = true) String orgId) {
        return ResponseEntity.ok().body(userGroupService.getWorkspaces(userId, orgId));
    }

    /**
     * Retrieve the role of the user from the list of all usergroups that has been assigned to the userId
     * If the user has multiple different roles in different userGroups, then max allowed role will be returned
     * @param userId
     * @return
     */
    @GetMapping("/usergroup/{userId}/role")
    public ResponseEntity<Object> getRole(@PathVariable(name="userId", required = true) String userId) {
        return ResponseEntity.ok().body(userGroupService.getRole(userId));
    }

    /**
     * Get all the usergroups that are mapped to the orgId
     * @param orgId
     * @return
     */
    @GetMapping("/{orgId}/usergroups")
    public ResponseEntity<List<UserGroup>> getUserGroups(@PathVariable(name="orgId", required=true) String orgId) {
        return ResponseEntity.ok().body(userGroupService.getUserGroups(orgId));
    }

    /**
     * Update the existing user group
     * @param userGroup
     * @return
     */
    @GetMapping("/usergroup/{userGroupId}")
    public ResponseEntity<UserGroup> getUserGroup(@PathVariable(name="userGroupId", required = true) String userGroupId) {
        return ResponseEntity.ok().body(userGroupService.getUserGroup(userGroupId));
    }

    @PutMapping("/{orgId}/{userId}/{workspaceId}/assign")
    public void assignUserGroup(@PathVariable(name="orgId", required = true) String orgId,
                                                     @PathVariable(name="userId", required = true) String userId,
                                                     @PathVariable(name="workspaceId", required = true) String workspaceId,
                                                     @RequestHeader("x-user-id") String hdrUsrId) {
        userGroupService.assignWorkspace(orgId, userId, workspaceId, hdrUsrId);
    }

}
