package com.example.testrestfulwebservice.model.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {
    private RoleNameEnum name;

    public RoleEntity() {
    }

    @Enumerated(EnumType.STRING)
    public RoleNameEnum getRole() {
        return name;
    }

    public void setRole(RoleNameEnum name) {
        this.name = name;
    }
}
