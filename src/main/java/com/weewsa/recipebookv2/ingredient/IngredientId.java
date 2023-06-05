package com.weewsa.recipebookv2.ingredient;

import com.weewsa.recipebookv2.recipe.Recipe;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientId implements Serializable {
    private Recipe recipe;
    private Short ingredientNumber;
}