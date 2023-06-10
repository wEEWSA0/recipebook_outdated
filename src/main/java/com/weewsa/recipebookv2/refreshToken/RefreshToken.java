package com.weewsa.recipebookv2.refreshToken;

import com.fasterxml.jackson.annotation.*;
import com.weewsa.recipebookv2.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {
    @JsonIgnore
    @Id
    @Lazy
    private Long userId;
    @NotNull
    private String token;
    @JsonIgnore
    @Lazy
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
