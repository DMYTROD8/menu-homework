package com.gmail.dmytrod8;

import com.gmail.dmytrod8.entity.Dish;
import com.gmail.dmytrod8.service.DishService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(final DishService dishService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                Dish dish;

                dish = new Dish("Fried Potatoes", 100, 50, 10);
                dishService.addDish(dish);

                dish = new Dish("Beef Stroganoff", 270, 250, 0);
                dishService.addDish(dish);

                dish = new Dish("Filet mignon", 250, 400, 7);
                dishService.addDish(dish);

                dish = new Dish("Chicken", 470, 200, 0);
                dishService.addDish(dish);

                dish = new Dish("Steak", 300, 500, 5);
                dishService.addDish(dish);

                dish = new Dish("Meatballs", 205, 120, 0);
                dishService.addDish(dish);

                dish = new Dish("Salmon carpaccio", 135, 220, 2);
                dishService.addDish(dish);

            }
        };
    }
}