package com.weewsa.recipebookv2.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    void deleteAllByRecipeId(Long recipeId);
}
