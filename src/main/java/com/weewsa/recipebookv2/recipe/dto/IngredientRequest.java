package com.weewsa.recipebookv2.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {
    private String title;
    private String productsDescription;
}
