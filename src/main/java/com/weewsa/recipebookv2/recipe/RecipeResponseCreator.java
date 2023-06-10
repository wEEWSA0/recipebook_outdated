package com.weewsa.recipebookv2.recipe;

import com.weewsa.recipebookv2.user.UserRepository;
import com.weewsa.recipebookv2.user.exception.UserNotFound;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RecipeResponseCreator {
    private final UserRepository userRepository;

    public RecipeResponse createResponse(Recipe recipe) throws UserNotFound {
        var userFound = userRepository.findById(recipe.getCreatorId());

        if (userFound.isEmpty()) {
            throw new UserNotFound("User not found");
        }

        var user = userFound.get();

        RecipeResponse recipeResponse = RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .imageUrl(recipe.getImageUrl())
                .cookingTime(recipe.getCookingTime())
                .personsCount(recipe.getPersonsCount())
                .creatorLogin(user.getLogin())
                .recipeTags(recipe.getRecipeTags())
                .ingredients(recipe.getIngredients())
                .steps(recipe.getSteps())
                .likedUsersCount(user.getLikedRecipes().size())
                .favouriteUsersCount(user.getFavouriteRecipes().size())
                .build();

        return recipeResponse;
    }

    public Set<RecipeResponse> createSetOfResponse(Set<Recipe> recipes) throws UserNotFound {
        Set<RecipeResponse> recipeResponses = new HashSet<>();

        for (Recipe recipe: recipes) {
            recipeResponses.add(createResponse(recipe));
        }

        return recipeResponses;
    }
}
