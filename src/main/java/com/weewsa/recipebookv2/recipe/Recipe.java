package com.weewsa.recipebookv2.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weewsa.recipebookv2.ingredient.Ingredient;
import com.weewsa.recipebookv2.step.Step;
import com.weewsa.recipebookv2.tag.Tag;
import com.weewsa.recipebookv2.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@EqualsAndHashCode(exclude = {"ingredients", "steps"})
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Time cookingTime;
    private Short personsCount;
    private Long creatorId;
    private Date createDate;
    @JsonIgnore
    @ManyToMany
    @Lazy
    @JoinTable(name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "tag_id"})})
    private Set<Tag> recipeTags;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "step",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = {@JoinColumn(name = "step_number"), @JoinColumn(name = "recipe_id")}
    )
    @Lazy
    private Set<Step> steps;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = {@JoinColumn(name = "ingredient_number"), @JoinColumn(name = "recipe_id")}
    )
    @Lazy
    private Set<Ingredient> ingredients;

}
