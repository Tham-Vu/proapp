package com.example.user_management.model.request;

import com.example.user_management.entity.Groups;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserModel {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String email;
    private String publicKey;
    private String secretCode;
    private Date createDate;
    private Date updateDate;
    private boolean active;
    private Long groupsId;

    public UserModel(Long id, String username, String firstName, String lastName, String phoneNumber, Date dateOfBirth, String email, Date createDate, Date updateDate, boolean active, Long groupsId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.active = active;
        this.groupsId = groupsId;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", username='" + username +
                ", firstName='" + firstName +
                ", lastName='" + lastName +
                ", phoneNumber='" + phoneNumber +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", active=" + active +
                ", groupsId=" + groupsId +
                '}';
    }
}
