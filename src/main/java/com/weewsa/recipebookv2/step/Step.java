package com.weewsa.recipebookv2.step;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Id
    @Column(name = "step_number")
    private Short stepNumber;
    @JsonIgnore
    @Id
    @Column(name = "recipe_id")
    private Long recipeId;
    private String description;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}

