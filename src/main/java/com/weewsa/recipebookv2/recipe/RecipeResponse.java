package com.weewsa.recipebookv2.recipe;

import com.weewsa.recipebookv2.ingredient.Ingredient;
import com.weewsa.recipebookv2.step.Step;
import com.weewsa.recipebookv2.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Time cookingTime;
    private Short personsCount;
    private String creatorLogin;
    private Set<Tag> recipeTags;
    private Set<Step> steps;
    private Set<Ingredient> ingredients;
    private Integer likedUsersCount;
    private Integer favouriteUsersCount;
}
