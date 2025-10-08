package com.example.user_management.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

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
    @Column(name = "PUBLIC_KEY")
    private String publicKey;
    @Column(name = "SECRET_CODE")
    @Size(max = 16)
    private String secretCode;
    @Column(name="CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name="UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "ACTIVE")
    private boolean active;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Groups groups;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        if (this.groups != null){
            this.getGroups().getListPermission().forEach(permission -> {
                list.add(new SimpleGrantedAuthority(permission.getName().toUpperCase()));
            });
        }
        return list;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
