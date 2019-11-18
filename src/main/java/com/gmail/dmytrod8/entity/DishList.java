package com.gmail.dmytrod8.entity;

import java.util.ArrayList;
import java.util.List;

public class DishList {
    private final List<Dish> list;

    public DishList(List<Dish> sourceList) {
        this.list = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++)
            list.add(sourceList.get(i));
    }
}