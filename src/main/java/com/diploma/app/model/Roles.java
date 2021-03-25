package com.diploma.app.model;

import io.swagger.annotations.ApiModel;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@ApiModel(value = "Roles", description = "Class for storing roles of users")
public class Roles extends BaseEntity implements GrantedAuthority {

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }

}

