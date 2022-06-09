package com.allioc.user.repository;

import com.allioc.user.model.UserGroup;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserGroupRepository extends MongoRepository<UserGroup, String> {

    Optional<UserGroup> findByNameAndOrgId(String name, String orgId);

    @Query(value="{'users':{ '$in': [?0] }, 'orgId': ?1 }")
    List<UserGroup> findByUserIdAndOrgId(String userId, String orgId);

    @Query(value="{'users':{ '$in': [?0] } }", fields = "{'role': 1}")
    List<UserGroup> findByUserId(String userId);

    List<UserGroup> findByOrgId(String orgId);

    List<UserGroup> findByUsersInAndWorkspacesInAndOrgId(String userId, String wspaceId, String orgId);

}
