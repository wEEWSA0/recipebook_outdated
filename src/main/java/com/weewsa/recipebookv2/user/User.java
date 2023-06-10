package com.weewsa.recipebookv2.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.weewsa.recipebookv2.recipe.Recipe;
import com.weewsa.recipebookv2.refreshToken.RefreshToken;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`users`")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique=true)
    @NotNull
    @Size(min = 5, max = 24)
    private String login;
    @NotNull
    @Size(min = 2, max = 40)
    private String name;
    @NotNull
    @Size(min = 3, max = 64)
    private String password;
    @NotNull
    private String aboutMe;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    @ManyToMany
    @Lazy
    @JoinTable(name = "liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "user_id"})})
    private Set<Recipe> likedRecipes;
    @JsonIgnore
    @ManyToMany
    @Lazy
    @JoinTable(name = "favourite",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "user_id"})})
    private Set<Recipe> favouriteRecipes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + getRole().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
