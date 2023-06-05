package com.weewsa.recipebookv2.tag;

import com.weewsa.recipebookv2.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByName(String name);
//    Set<Tag> findAllByTagRecipesIsIn(Collection<Set<Recipe>> tagRecipes);
}
