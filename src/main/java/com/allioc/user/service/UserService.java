package com.allioc.user.service;

import com.allioc.user.model.Audit;
import com.allioc.user.model.LoginModel;
import com.allioc.user.model.User;
import com.allioc.user.repository.UserRepository;
import com.allioc.user.util.AppConstants;
import com.allioc.user.util.TripleDes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.allioc.user.util.AppConstants.*;
import static com.allioc.user.util.AppConstants.Status.ACTIVE;
import static com.allioc.user.util.GenericUtil.*;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TripleDes encrypter;

    /**
     * Get all the ACTIVE tenants
     * @param data
     * @return User
     */
    public User authenticate(LoginModel data) {
        User user = null;
        String encryptedPwd = encrypter.encrypt(data.getPassword());
        log.info("Authenticate {} {}", data.getEmailId(), encryptedPwd);
        Optional<User> usr = userRepo.findByEmailIdAndPasswordAndStatus(data.getEmailId(), encryptedPwd, ACTIVE.name());
        if (usr.isPresent()) {
           user =  usr.get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, FAILED_TO_AUTHENTICATE);
        }
        return user;
    }

    /**
     * Create a new user iff the same name does not exists already
     * @param data - Request body
     * @param hdrUsrId - User Id passed in the header
     * @return User
     */
    public User create(User data, String hdrUsrId) {
        Audit audit = getAudit(hdrUsrId);

        // Generate the ID
        String id = generateUrnWithPrefix(AppConstants.USER_URN_PREFIX);
        data.setId(id);
        data.setStatus(ACTIVE.name());
        data.setAudit(audit);
        if (! userRepo.findByEmailIdAndStatus(data.getEmailId(), ACTIVE.name()).isPresent()) {
            log.info("Creating the new User", data);
            data.setPassword(encrypter.encrypt(data.getPassword()));
            userRepo.insert(data);
        } else {
            throw new HttpClientErrorException(HttpStatus.FOUND, EMAIL_ALREADY_EXISTS);
        }

        return data;
    }


    /**
     * Fetch all the users for the given orgId
     * @param orgId
     * @return
     */
    public List<User> getUsers(String orgId) {
        List<User> users = userRepo.findByOrgIdAndStatus(orgId, ACTIVE.name());
        List<User> usrLst = null;
        if (users != null && users.size() > 0 ) {
            usrLst = users.stream().map(usr -> (User.builder().build().clone(usr))).collect(Collectors.toList());
        }

        return usrLst;
    }

    /**
     * Get the userDetails for the given UserId
     * @param userId
     * @return
     */
    public User getUser(String userId) {
        User user = userRepo.findByIdAndStatus(userId, ACTIVE.name());
        if (user != null) {
            user.setPassword(null);
            user.setToken(null);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_NOT_EXISTS);
        }

        return user;
    }

    public User updateUser(User data, String hdrUsrId) {
        User user = userRepo.findByIdAndStatus(data.getId(), ACTIVE.name());
        if (user != null) {
            // Update only the status and the role
            user.setRole(data.getRole());
            user.setStatus(data.getStatus());

            Audit audit = user.getAudit();
            audit.setDateModified(Instant.now().toString());
            audit.setModifiedBy(hdrUsrId);

            userRepo.save(user);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, USER_ID_NOT_EXISTS);
        }
        return user;
    }

}
