package com.example.user_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NonNull
@NoArgsConstructor
@AllArgsConstructor
public class Groups implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
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
    private List<User> listUser;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="GROUP_PERMISSION", joinColumns = {
           @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    }, inverseJoinColumns = {
            @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID")
    })
    private List<Permission> listPermission;
}
