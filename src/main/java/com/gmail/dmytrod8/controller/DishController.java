package com.gmail.dmytrod8.controller;

import com.gmail.dmytrod8.entity.Dish;
import com.gmail.dmytrod8.entity.DishList;
import com.gmail.dmytrod8.entity.ServerResponse;
import com.gmail.dmytrod8.service.DishService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class DishController {
    private static final int ITEMS_PER_PAGE = 5;

    @Autowired
    private DishService dishService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    void getDishInJSON(HttpServletResponse resp,
                       @RequestParam int page,
                       final @RequestParam boolean filter,
                       final @RequestParam(required = false, defaultValue = "0") double minPrice,
                       final @RequestParam(required = false, defaultValue = "9999999") double maxPrice,
                       final @RequestParam(required = false, defaultValue = "false") boolean discount) throws IOException {


        if (page < 0) page = 0;
        List<Dish> dishes;
        long pagesCount;

        if (filter) {
            dishes = getAllFilteredDishes(page, minPrice, maxPrice, discount);
            pagesCount = getFilteredPageCount(minPrice, maxPrice, discount);
        } else {
            dishes = getAllDishes(page);
            pagesCount = getPageCount();
        }

        final JsonElement jelement = toJSON(dishes);
        if (jelement != null) {
            ServerResponse.sendResponse(resp, jelement, "response:0", "pagesCount:" + pagesCount, "currentPage:" + page);
        }

    }


    @RequestMapping(value = "/add_dish", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    void addDish(HttpServletResponse resp,
                 final @RequestParam String title,
                 final @RequestParam int weight,
                 final @RequestParam double price,
                 final @RequestParam int discountAmount) throws IOException {

        final Dish dish = dishService.addDish(new Dish(title, weight, price, discountAmount));

        if (dish != null) {
            final long id = dish.getId();
            ServerResponse.sendResponse(resp, "response:0", "id:" + id);
        } else {
            ServerResponse.sendResponse(resp, "response:2");
        }

    }

    private JsonElement toJSON(List<Dish> dishesList) {
        return (JsonElement) new Gson().toJsonTree(new DishList(dishesList));
    }

    private long getPageCount() {
        long totalCount = dishService.count();
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }

    private long getFilteredPageCount(double minPrice, double maxPrice, boolean discount) {
        if (discount) {
            long totalCount = dishService.findByDiscount(minPrice, maxPrice, discount);
            return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
        }

        long totalCount = dishService.findByPrice(minPrice, maxPrice);
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }

    private List<Dish> getAllDishes(int page) {
        return dishService.findAll(PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));
    }

    private List<Dish> getAllFilteredDishes(int page, double minPrice, double maxPrice, boolean discount) {
        if (discount) {
            return dishService.findByDiscount(minPrice, maxPrice, discount,
                    PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));
        }

        return dishService.findByPrice(minPrice, maxPrice,
                PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));
    }

}



