package com.weewsa.recipebookv2.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.weewsa.recipebookv2.ingredient.Ingredient;
import com.weewsa.recipebookv2.step.Step;
import com.weewsa.recipebookv2.tag.Tag;
import com.weewsa.recipebookv2.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Time cookingTime;
    private Short personsCount;
    private Long creatorId;
    private Date createDate;
    @ManyToMany
//    @JsonBackReference
    @Lazy
    @JoinTable(name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "tag_id"})})
    private Set<Tag> recipeTags;
    @ManyToMany
    @Lazy
    @JoinTable(name = "liked",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "user_id"})})
    private Set<User> likedUsers;
    @ManyToMany
    @Lazy
    @JoinTable(name = "favourite",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "user_id"})})
    private Set<User> favouriteUsers;
    @OneToMany(mappedBy = "recipe")
    @Lazy
    private Set<Step> steps;
    @OneToMany(mappedBy = "recipe")
    @Lazy
    private Set<Ingredient> ingredients;
}
