package com.weewsa.recipebookv2.step;

import com.weewsa.recipebookv2.recipe.Recipe;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepId implements Serializable {
    private Long recipeId;
    private Short stepNumber;
}
