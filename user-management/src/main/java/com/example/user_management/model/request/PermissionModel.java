package com.example.user_management.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PermissionModel {
    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Date createDate;
    private Date updateDate;
    @JsonIgnore
    private List<Long> groupsIs;

    @Override
    public String toString() {
        return "PermissionModel{" +
                "id=" + id +
                ", name='" + name +
                ", description='" + description +
                ", active=" + active +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", groupsIs=" + groupsIs +
                '}';
    }
}
