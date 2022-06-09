package com.allioc.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
@Document("user")
@CompoundIndex(name="usr_emailid_status_index", def="{'emailId': 1 , 'status': 1}", background = true)
@CompoundIndex(name="usr_orgid_status_index", def="{'orgId': 1 , 'status': 1}", background = true)
@EqualsAndHashCode(callSuper=false)
@ToString
public class User extends BaseEntity{
    @Id
    private String id;

    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Indexed
    private String emailId;

    @NotBlank
    private String password;

    @NotBlank
    @Indexed
    private String orgId;

    @Builder.Default
    private String role = "user";

    private String status;

    @Transient
    @Builder.Default
    private String token = UUID.randomUUID().toString();

    public User clone(User usr) {
        return User.builder().id(usr.id).firstName(usr.firstName).lastName(usr.lastName).emailId(usr.emailId)
                .orgId(usr.orgId).role(usr.role).status(usr.status).token(null)
                .build();
    }

}
