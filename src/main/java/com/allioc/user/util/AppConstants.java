package com.allioc.user.util;

public interface AppConstants {
    enum Status {
        ACTIVE, INACTIVE
    }

    String USER_URN_PREFIX = "urn:allioc:user:";
    String USERGROUP_URN_PREFIX = "urn:allioc:usergroup:";

    String FAILED_TO_AUTHENTICATE = "Invalid credentials. Failed to authenticate the user";
    String EMAIL_ALREADY_EXISTS = "Email id already registered";
    String USER_ID_NOT_EXISTS = "User id does not exists";
    String USERGROUP_EXISTS = "User Group with same name already exists in the given organization";
    String USERGROUP_NOT_EXISTS = "User Group does not exists for the given Id";

}
