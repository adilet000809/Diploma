package com.diploma.app.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "password_token")
@Data
@ApiModel(value = "Password token", description = "PasswordReset token class for storing token")
public class PasswordResetToken {

    private static final int TOKEN_LIFE = 10;
    private static final int MAX_ATTEMPTS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "attempts", nullable = false, unique = true)
    private Integer attempts = MAX_ATTEMPTS;

    @OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "users_id")
    private Users user;

    @Column(name = "expiration")
    private Date expiration;

    public void setExpiration() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, TOKEN_LIFE);
        this.expiration = now.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expiration);
    }

}
