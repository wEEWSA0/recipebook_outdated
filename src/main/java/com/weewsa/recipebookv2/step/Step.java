package com.weewsa.recipebookv2.step;

import com.weewsa.recipebookv2.recipe.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(StepId.class)
public class Step {
//    @Id
//    @Column(name = "recipe_id")
//    private Long recipeId;
    @Id
    private Short stepNumber;
    private String description;
    @Id
//    @Column(name = "recipe_id")
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}

