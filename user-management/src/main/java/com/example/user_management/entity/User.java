package com.example.user_management.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jdk.jfr.Timestamp;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class User implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @Column(name = "ID")
    private Long id;
    @Column(name="USER_NAME")
    @Size(max=100)
    private String username;
    @Column(name="PASSWORD")
    private String password;
    @Column(name="FIRST_NAME")
    @Size(max=50)
    private String firstName;
    @Column(name="LAST_NAME")
    @Size(max=50)
    private String lastName;
    @Column(name="PHONE_NUMBER")
    @Size(max=20)
    private String phoneNumber;
    @Column(name="DOB")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Column(name="EMAIL")
    @Size(max=100)
    private String email;
    @Column(name="CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name="UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }
}
