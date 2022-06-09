package com.allioc.user.repository;

import com.allioc.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmailIdAndStatus(String emailId, String status);

    Optional<User> findByEmailIdAndPasswordAndStatus(String emailId, String pwd, String status);

    List<User> findByOrgIdAndStatus(String orgId, String status);

    User findByIdAndStatus(String id, String status);
}
