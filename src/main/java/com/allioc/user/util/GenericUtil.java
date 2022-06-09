package com.allioc.user.util;

import com.allioc.user.model.Audit;

import java.time.Instant;
import java.util.UUID;

public class GenericUtil {
    public static String generateUrnWithPrefix(String prefix) {
        return prefix + UUID.randomUUID().toString();
    }

    public static Audit getAudit(String hdrUsrId) {
        Audit audit = new Audit();
        audit.setDateCreated(Instant.now().toString());
        audit.setCreatedBy(hdrUsrId);
        return audit;
    }
}
