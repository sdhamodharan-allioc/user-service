package com.allioc.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Audit {
    protected String dateCreated;

    protected String dateModified;

    protected String createdBy;

    protected String modifiedBy;
}
