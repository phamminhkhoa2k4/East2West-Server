package com.east2west.models.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "roleid")
    private Long roleid;
     @Enumerated(EnumType.STRING)
    @Column(name = "rolename")
     private ERole roleName;

     public ERole getRoleName() {
         return this.roleName;
     }

     public void setRoleName(ERole roleName) {
         this.roleName = roleName;
     }

    // Getters and Setters
    public Long getRoleId() {
        return roleid;
    }

    public void setRoleId(Long roleId) {
        this.roleid = roleId;
    }

    
}
