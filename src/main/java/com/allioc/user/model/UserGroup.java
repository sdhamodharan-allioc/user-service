package com.allioc.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("usergroup")
@ToString
@EqualsAndHashCode(callSuper=false)
@CompoundIndex(name="usrgrp_orgid_index", def="{'orgId':1}", background = true)
@CompoundIndex(name="usrgrp_user_index", def="{'users.user':1 }", background = true)
public class UserGroup extends BaseEntity{

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String orgId;

    @NotBlank
    private List<String> users;

    @NotBlank
    private List<String> workspaces;

    @NotBlank
    private String role;

}
