package com.diploma.app.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@ApiModel(value = "Roles", description = "Class for storing roles of users")
public class Roles implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<Users> users;

    @Override
    public String getAuthority() {
        return this.name;
    }

}

