package com.weewsa.recipebookv2.controller.recipes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReactionToRecipeRequest {
    @JsonProperty
    private Long recipeId;
    @JsonProperty
    private boolean isActive;
}
