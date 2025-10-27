package com.example.user_management.model.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GroupModel {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private Date createDate;
    private Date updateDate;
    private List<Long> userIds;
    private List<Long> permissionIds;

    @Override
    public String toString() {
        return "GroupModel{" +
                "id=" + id +
                ", name='" + name +
                ", description='" + description +
                ", active=" + active +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", userIds=" + userIds +
                ", permissionIds=" + permissionIds +
                '}';
    }
}
