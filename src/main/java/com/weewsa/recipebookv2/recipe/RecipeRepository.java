package com.weewsa.recipebookv2.recipe;

import com.weewsa.recipebookv2.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
//    Set<Recipe> findAllByName(String name);
    Set<Recipe> findAllByNameContaining(String name);
    Set<Recipe> findAllByRecipeTagsIn(Collection<Tag> recipeTags);
//    Set<Tag> findRecipeTagsByRecipe(Recipe recipe);
    Set<Recipe> findAllByRecipeTagsInAndNameContaining(Collection<Tag> recipeTags, String name);
//    Set<Recipe> findAllByRecipeTagsContains(Set<Tag> recipeTags);
//    Set<Tag> findRecipeTagsInById(Long id);
}
