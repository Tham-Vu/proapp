package com.example.user_management.entity;

import jakarta.annotation.Nullable;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PERMISSION")
public class Permission implements Serializable {
    private final static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
//    @NonNull
    private Long id;
    @Column(name = "NAME")
    @Size(max = 200)
    @NotNull
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
    @ManyToMany(mappedBy = "listPermission")
    private List<Groups> listGroup;


}
