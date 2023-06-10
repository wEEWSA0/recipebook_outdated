package com.weewsa.recipebookv2.controller.users.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull
    @Size(min = 5, max = 24)
    private String login;
    @NotNull
    @Size(min = 3, max = 64)
    private String password;
}
