package com.gmail.dmytrod8.service;

import com.gmail.dmytrod8.entity.Dish;
import com.gmail.dmytrod8.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;

    @Transactional
    public Dish addDish(Dish tempDish) {
        Dish dish = dishRepository.save(tempDish);
        if(tempDish == dish){
            return dish;
        }
        return null;
    }

    @Transactional(readOnly=true)
    public List<Dish> findAll(Pageable pageable) {
        return dishRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public long count() {
        return dishRepository.count();
    }

    @Transactional(readOnly=true)
    public List<Dish> findByPrice(double minPrice, double maxPrice, Pageable pageable) {
        return dishRepository.findByPrice(minPrice, maxPrice, pageable);
    }

    @Transactional(readOnly=true)
    public long findByPrice(double minPrice, double maxPrice) {
        return dishRepository.findByPrice(minPrice, maxPrice).size();
    }

    @Transactional(readOnly=true)
    public List<Dish> findByDiscount(double minPrice, double maxPrice, boolean discount, Pageable pageable) {
        return dishRepository.findByDiscount(minPrice, maxPrice, discount, pageable);
    }

    @Transactional(readOnly=true)
    public long findByDiscount(double minPrice, double maxPrice, boolean discount) {
        return dishRepository.findByDiscount(minPrice, maxPrice, discount).size();
    }

}

