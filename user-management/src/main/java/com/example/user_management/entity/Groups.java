package com.example.user_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Groups implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", unique = true)
    @Size(max = 50)
    private String name;
    @Column(name = "DESCRIPTION")
    @Size(max = 200)
    private String description;
    @Column(name = "ACTIVE")
    private boolean active;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(mappedBy = "groups")
    @JsonIgnore
    private Set<User> listUser;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinTable(name="GROUP_PERMISSION", joinColumns = {
           @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID")
    })
    private Set<Permission> listPermission;
}
