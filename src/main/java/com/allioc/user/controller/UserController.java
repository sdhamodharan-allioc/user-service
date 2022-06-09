package com.allioc.user.controller;

import com.allioc.user.model.LoginModel;
import com.allioc.user.model.User;
import com.allioc.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1")
@Validated
public class UserController {

   @Autowired
   private UserService userService;

    @Value("${app.version}")
    private String version;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok().body("{ health: ok, version: " + version + " }");
    }

    /**
     * Authenticate the user credentials - This is a dummy implementation
     * @return User
     */
    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticate(@RequestBody @Valid LoginModel data) {
        return ResponseEntity.ok().body(userService.authenticate(data)) ;
    }


    /**
     * Create a new User
     * @param user
     * @param hdrUsrId
     * @return
     */
    @PostMapping("/user")
    public ResponseEntity<User> createNewUser(@RequestBody User user,
                                              @RequestHeader(name="x-user-id", required= true) String hdrUsrId) {
        return ResponseEntity.ok().body(userService.create(user, hdrUsrId));
    }

    /**
     * List of all active users in the given organization
     * @param orgId
     * @return
     */
    @GetMapping("/{orgId}/users")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable(name="orgId", required = true) String orgId) {
        return ResponseEntity.ok().body(userService.getUsers(orgId));
    }

    /**
     * Get the user details
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable(name="userId", required = true) String userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    /**
     *
     * @param user
     * @param hdrUsrId
     * @return
     */
    @PutMapping("/user/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user,
                                     @PathVariable(name="userId", required=true) String userId,
                                     @RequestHeader(name="x-user-id", required= true) String hdrUsrId) {
        user.setId(userId);
        return ResponseEntity.ok().body(userService.updateUser(user, hdrUsrId));
    }
}
