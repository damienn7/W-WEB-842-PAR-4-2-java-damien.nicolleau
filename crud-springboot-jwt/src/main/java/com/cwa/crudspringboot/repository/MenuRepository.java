package com.cwa.crudspringboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cwa.crudspringboot.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
