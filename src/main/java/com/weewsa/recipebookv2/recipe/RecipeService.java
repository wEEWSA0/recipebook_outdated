package com.weewsa.recipebookv2.recipe;

import com.weewsa.recipebookv2.ingredient.Ingredient;
import com.weewsa.recipebookv2.ingredient.IngredientRepository;
import com.weewsa.recipebookv2.controller.recipes.dto.IngredientRequest;
import com.weewsa.recipebookv2.controller.recipes.dto.RecipeRequest;
import com.weewsa.recipebookv2.controller.recipes.dto.StepRequest;
import com.weewsa.recipebookv2.controller.recipes.dto.TagRequest;
import com.weewsa.recipebookv2.recipe.exception.RecipeNotFound;
import com.weewsa.recipebookv2.refreshToken.exception.NotEnoughRights;
import com.weewsa.recipebookv2.step.Step;
import com.weewsa.recipebookv2.step.StepRepository;
import com.weewsa.recipebookv2.tag.Tag;
import com.weewsa.recipebookv2.tag.TagRepository;
import com.weewsa.recipebookv2.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;
    private final StepRepository stepRepository;

    public Recipe getRecipeById(Long recipeId) throws RecipeNotFound {
        var foundRecipe = recipeRepository.findById(recipeId);

        if (foundRecipe.isEmpty()) {
            throw new RecipeNotFound("Recipe not found");
        }

        return foundRecipe.get();
    }

    public Set<Recipe> getRecipesWithName(String name) throws RecipeNotFound {
        var foundRecipes = recipeRepository.findAllByNameContaining(name);

        if (foundRecipes.isEmpty()) {
            throw new RecipeNotFound("Recipe not found");
        }

        return foundRecipes;
    }

    // todo RecipeModel ответы
    public Set<Recipe> getRecipesWithTags(Set<TagRequest> tagRequests) throws RecipeNotFound {
        Set<Tag> tags = getExistTags(tagRequests);

        var foundRecipes = recipeRepository.findAllByRecipeTagsIn(tags);

        if (foundRecipes.isEmpty()) {
            throw new RecipeNotFound("Recipes not found");
        }

        return foundRecipes;
    }

    public Set<Recipe> getRecipesWithTagsAndName(Set<TagRequest> tagRequests, String name) throws RecipeNotFound {
        Set<Tag> tags = getExistTags(tagRequests);

        var foundRecipes = recipeRepository.findAllByRecipeTagsInAndNameContaining(tags, name);

        if (foundRecipes.isEmpty()) {
            throw new RecipeNotFound("Recipes not found");
        }

        return foundRecipes;
    }

    @Transactional(rollbackOn = Exception.class)
    public void createRecipe(RecipeRequest recipeRequest, String login) {
        var userId = userRepository.findIdByLogin(login);

        // find or create new tags and add it's to recipe_tag table
        Set<Tag> tags = createOrFindTags(recipeRequest.getRecipeTags());

        // create recipe with tags
        var newRecipe = Recipe.builder()
                .name(recipeRequest.getName())
                .description(recipeRequest.getDescription())
                .imageUrl(recipeRequest.getImageUrl())
                .cookingTime(recipeRequest.getCookingTime())
                .personsCount(recipeRequest.getPersonsCount())
                .creatorId(userId)
                .createDate(new java.sql.Date(new Date().getTime()))
                .recipeTags(tags)
                .build();

        var recipeEntity = recipeRepository.save(newRecipe);

        // create recipe steps and ingredients
        createStepsAndIngredients(recipeEntity.getId(), recipeRequest.getSteps(), recipeRequest.getIngredients());
    }

    @Transactional(rollbackOn = Exception.class)
    public void editRecipe(Long recipeId, RecipeRequest recipeRequest, String login) throws RecipeNotFound, NotEnoughRights {
        Optional<Recipe> foundRecipe = recipeRepository.findById(recipeId);

        if (foundRecipe.isEmpty()) {
            throw new RecipeNotFound("Recipe not found");
        }

        var recipe = foundRecipe.get();

        var userId = userRepository.findIdByLogin(login);
        if (!recipe.getCreatorId().equals(userId)) {
            throw new NotEnoughRights("Has other creator");
        }

        // find or create new tags and add it's to recipe_tag table
        Set<Tag> tags = createOrFindTags(recipeRequest.getRecipeTags());

        if (recipe.getRecipeTags().size() > 0) {
            recipe.getRecipeTags().clear();
            recipe = recipeRepository.save(recipe);
        }

        recipe.setName(recipeRequest.getName());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setImageUrl(recipeRequest.getImageUrl());
        recipe.setCookingTime(recipeRequest.getCookingTime());
        recipe.setPersonsCount(recipeRequest.getPersonsCount());
        recipe.setRecipeTags(tags);

        recipeRepository.save(recipe);

        stepRepository.deleteAllByRecipeId(recipeId);
        ingredientRepository.deleteAllByRecipeId(recipeId);

        createStepsAndIngredients(recipeId, recipeRequest.getSteps(), recipeRequest.getIngredients());
    }

    public void deleteRecipe(Long recipeId, String login) throws RecipeNotFound, NotEnoughRights {
        var foundRecipe = recipeRepository.findById(recipeId);

        if (foundRecipe.isEmpty()) {
            throw new RecipeNotFound("Recipe not found");
        }

        var userId = userRepository.findIdByLogin(login);
        if (!foundRecipe.get().getCreatorId().equals(userId)) {
            throw new NotEnoughRights("Has other creator");
        }

        recipeRepository.delete(foundRecipe.get());
    }

    private Set<Tag> createOrFindTags(Set<TagRequest> tagsRequest) {
        Set<Tag> tags = new HashSet<>();

        for (TagRequest tagRequest : tagsRequest) {
            var foundTag = tagRepository.findTagByName(tagRequest.getName());

            if (foundTag.isPresent()) {
                tags.add(foundTag.get());
            }
            else {
                Tag newTag = Tag.builder()
                        .name(tagRequest.getName())
                        .build();
                var tagEntity = tagRepository.save(newTag);
                tags.add(tagEntity);
            }
        }

        return tags;
    }

    private void createStepsAndIngredients(Long recipeId, Set<StepRequest> stepRequests, Set<IngredientRequest> ingredientRequests) {
        // create steps
        short stepNumber = 1;
        for (StepRequest stepRequest : stepRequests) {
            var newStep = Step.builder()
                    .stepNumber(stepNumber)
                    .description(stepRequest.getDescription())
                    .recipeId(recipeId)
                    .build();
            stepRepository.save(newStep);
            stepNumber++;
        }

        // create ingredients
        short ingredientNumber = 1;
        for (IngredientRequest ingredientRequest : ingredientRequests) {
            var newIngredient = Ingredient.builder()
                    .title(ingredientRequest.getTitle())
                    .productsDescription(ingredientRequest.getProductsDescription())
                    .ingredientNumber(ingredientNumber)
                    .recipeId(recipeId)
                    .build();
            ingredientRepository.save(newIngredient);
            ingredientNumber++;
        }
    }

    private Set<Tag> getExistTags(Set<TagRequest> tagRequests) {
        Set<Tag> tags = new HashSet<>();

        for (TagRequest tagRequest : tagRequests) {
            Optional<Tag> foundTag = tagRepository.findTagByName(tagRequest.getName());

            if (foundTag.isPresent()) {
                tags.add(foundTag.get());
            }
        }

        if (tags.size() < 1) {
            return new HashSet<>();
        }

        return tags;
    }
}
