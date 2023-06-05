package com.weewsa.recipebookv2.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weewsa.recipebookv2.recipe.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "recipeTags")
//    @JsonIgnore
    @Lazy
    private Set<Recipe> tagRecipes;
}
