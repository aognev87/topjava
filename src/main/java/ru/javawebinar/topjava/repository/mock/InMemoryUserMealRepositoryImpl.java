package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {

    private Map<Integer, UserMeal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserMealsUtil.MEAL_LIST.forEach(this::save);
    }

    public InMemoryUserMealRepositoryImpl() {
    }

    @Override
    public UserMeal save(UserMeal userMeal) {
        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        repository.put(userMeal.getId(), userMeal);
        return userMeal;
    }

    @Override
    public void delete(int id, int userId) {
        if (repository.get(id).getUserId() == userId)
            repository.remove(id);
    }

    @Override
    public UserMeal get(int id, int userId) {

        UserMeal result = repository.get(id);

        if (result.getUserId() != userId)
            result = null;

        return result;
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        List<UserMeal> result = new ArrayList<>();

        repository.values().forEach(userMeal -> {
            if (userMeal.getUserId() == userId)
                result.add(userMeal);
        });

        if (!result.isEmpty())
            Collections.sort(result, new Comparator<UserMeal>() {
                @Override
                public int compare(UserMeal o1, UserMeal o2) {
                    return o1.getDateTime().compareTo(o2.getDateTime());
                }
            });

        return result.isEmpty() ? null : result;
    }
}