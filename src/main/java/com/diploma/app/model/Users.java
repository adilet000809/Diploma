package com.diploma.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "user_only"),
        @NamedEntityGraph(name = "user_with_roles", attributeNodes = @NamedAttributeNode("roles"))
})
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Users", description = "Class for soring information about user")
public class Users extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "password_last_changed_date")
    @JsonIgnore
    private Date passwordLastChangedDate = new Date();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
    @JsonIgnore
    private Set<Roles> roles;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_supermarkets",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "supermarket_id", referencedColumnName = "id") })
    private Set<Supermarket> supermarkets;

    public void addRole(Roles role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    public void removeRole(Roles role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.remove(role);
    }

    public void addSupermarket(Supermarket supermarket) {
        if (supermarkets == null) {
            supermarkets = new HashSet<>();
        }
        supermarkets.add(supermarket);
    }

    public void removeSupermarket(Supermarket supermarket) {
        if (supermarkets == null) {
            supermarkets = new HashSet<>();
        }
        supermarkets.remove(supermarket);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;
        if (!super.equals(o)) return false;
        Users users = (Users) o;
        return Objects.equals(getFirstName(), users.getFirstName()) && Objects.equals(getLastName(), users.getLastName()) && Objects.equals(getUserName(), users.getUserName()) && Objects.equals(getEmail(), users.getEmail()) && Objects.equals(getPassword(), users.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getLastName(), getUserName(), getEmail(), getPassword());
    }
}

