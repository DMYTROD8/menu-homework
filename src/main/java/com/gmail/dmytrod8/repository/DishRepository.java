package com.gmail.dmytrod8.repository;

import com.gmail.dmytrod8.entity.Dish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT c FROM Dish c WHERE c.price >= :minPrice AND c.price <= :maxPrice")
    List<Dish> findByPrice(
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            Pageable pegeable
    );

    @Query("SELECT c FROM Dish c WHERE c.price >= :minPrice AND c.price <= :maxPrice")
    List<Dish> findByPrice(
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice
    );

    @Query("SELECT c FROM Dish c WHERE c.price >= :minPrice AND c.price <= :maxPrice AND c.discount = :discount")
    List<Dish> findByDiscount(
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            @Param("discount") boolean discount,
            Pageable pageable
    );

    @Query("SELECT c FROM Dish c WHERE c.price >= :minPrice AND c.price <= :maxPrice AND c.discount = :discount")
    List<Dish> findByDiscount(
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            @Param("discount") boolean discount
    );

}
