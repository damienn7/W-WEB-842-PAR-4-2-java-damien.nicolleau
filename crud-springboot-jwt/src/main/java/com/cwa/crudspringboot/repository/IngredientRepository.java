package com.cwa.crudspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwa.crudspringboot.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
