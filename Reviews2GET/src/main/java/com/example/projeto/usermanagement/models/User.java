package com.example.projeto.usermanagement.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private boolean enabled = true;

    @Column (nullable = false, unique = true)

    private String username;

    @Column (nullable = false)
    private String password;

    @ElementCollection
    private Set<Role> authorities = new HashSet<>();

    protected User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("É obrigatório atribuir um username ao user.");
        }
        this.username = username;
    }
    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Tem de definir uma password. Não a pode deixar em branco");
        }
        this.password = password;
    }

    public void addAuthority(Role r) {
        authorities.add(r);
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }
}
