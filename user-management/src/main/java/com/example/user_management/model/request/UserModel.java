package com.example.user_management.model.request;

import com.example.user_management.entity.Groups;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class UserModel {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String email;
    private Date createDate;
    private Date updateDate;
    private boolean active;

    public UserModel(String username, String password, String firstName, String lastName, String phoneNumber, Date dateOfBirth, String email, Date createDate, Date updateDate, boolean active, Groups groups) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.active = active;
        this.groups = groups;
    }

    private Groups groups;

}
