package com.weewsa.recipebookv2.step;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Long> {
    void deleteAllByRecipeId(Long recipeId);
}
