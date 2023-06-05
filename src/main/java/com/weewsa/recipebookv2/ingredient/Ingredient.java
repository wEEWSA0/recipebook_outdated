package com.weewsa.recipebookv2.ingredient;

import com.weewsa.recipebookv2.recipe.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(IngredientId.class)
public class Ingredient {
//    @Id
//    @Column(name = "recipe_id")
//    private Long recipeId;
    @Id
    private Short ingredientNumber;
    private String title;
    private String productsDescription;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
