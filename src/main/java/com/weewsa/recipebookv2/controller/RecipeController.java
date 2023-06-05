package com.weewsa.recipebookv2.controller;

import com.weewsa.recipebookv2.authenticate.JWTService;
import com.weewsa.recipebookv2.authenticate.exception.NotAuthorized;
import com.weewsa.recipebookv2.recipe.Recipe;
import com.weewsa.recipebookv2.recipe.RecipeResponseCreator;
import com.weewsa.recipebookv2.recipe.RecipeService;
import com.weewsa.recipebookv2.recipe.dto.NameRequest;
import com.weewsa.recipebookv2.recipe.dto.RecipeRequest;
import com.weewsa.recipebookv2.recipe.dto.NameAndTagsRequest;
import com.weewsa.recipebookv2.recipe.dto.TagRequest;
import com.weewsa.recipebookv2.recipe.exception.RecipeNotFound;
import com.weewsa.recipebookv2.refreshToken.exception.InvalidToken;
import com.weewsa.recipebookv2.refreshToken.exception.NotEnoughRights;
import com.weewsa.recipebookv2.tag.Tag;
import com.weewsa.recipebookv2.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final JWTService jwtService;
    private final RecipeService recipeService;
    private final RecipeResponseCreator recipeResponseCreator;
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            var recipe = recipeService.getRecipeById(id);

            return ResponseEntity.ok(recipe);
        } catch (RecipeNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody RecipeRequest recipeRequest, HttpServletRequest request) {
        try {
            var claims = jwtService.getClaimsFromRequest(request);
            if (!jwtService.hasAccess(claims, Role.USER)) {
                throw new NotEnoughRights("Not enough rights");
            }

            recipeService.createRecipe(recipeRequest, claims.getSubject());
        } catch (NotAuthorized | InvalidToken | NotEnoughRights e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }

        return ResponseEntity.ok("Created");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @RequestBody RecipeRequest recipeRequest, HttpServletRequest request) {
        try {
            var claims = jwtService.getClaimsFromRequest(request);
            if (!jwtService.hasAccess(claims, Role.USER)) {
                throw new NotEnoughRights("Not enough rights");
            }

            recipeService.editRecipe(id, recipeRequest, claims.getSubject());
        } catch (NotAuthorized | InvalidToken | NotEnoughRights | RecipeNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }

        return ResponseEntity.ok("Saved");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            var claims = jwtService.getClaimsFromRequest(request);
            if (!jwtService.hasAccess(claims, Role.USER)) {
                throw new NotEnoughRights("Not enough rights");
            }

            recipeService.deleteRecipe(id, claims.getSubject());
        } catch (NotAuthorized | InvalidToken | NotEnoughRights | RecipeNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }

        return ResponseEntity.ok("Saved");
    }

    @PostMapping("get-with-name")
    public ResponseEntity<?> getByName(@RequestBody NameRequest nameRequest) {
        try {
            var recipes = recipeService.getRecipesWithName(nameRequest.getName());

            return ResponseEntity.ok(recipeResponseCreator.createSetOfResponse(recipes));
        } catch (RecipeNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    @PostMapping("get-with-tags")
    public ResponseEntity<?> getByTags(@RequestBody Set<TagRequest> tags) {
        try {
            var recipes = recipeService.getRecipesWithTags(tags);

            return ResponseEntity.ok(recipes);
        } catch (RecipeNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    @PostMapping("get-with-name-and-tags")
    public ResponseEntity<?> getByNameAndTags(@RequestBody NameAndTagsRequest request) {
        try {
            var recipes = recipeService.getRecipesWithTagsAndName(request.getTags(), request.getName());

            return ResponseEntity.ok(recipes);
        } catch (RecipeNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }
}
