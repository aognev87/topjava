package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field

        Map<LocalDate, Integer> map = new HashMap<>();
        List<UserMeal> preResultList = new ArrayList<>();
        List<UserMealWithExceed> resultList = new ArrayList<>();

        mealList.stream()
                .filter(i -> {
                    if (map.containsKey(i.getDateTime().toLocalDate())) {
                        map.put(i.getDateTime().toLocalDate(), map.get(i.getDateTime().toLocalDate()) + i.getCalories());
                    } else
                        map.put(i.getDateTime().toLocalDate(), i.getCalories());
                    return i.getDateTime().toLocalTime().isAfter(startTime) && i.getDateTime().toLocalTime().isBefore(endTime);
                })
                .forEach(preResultList::add);

        preResultList.stream()
                .map(i -> new UserMealWithExceed(i.getDateTime(),
                        i.getDescription(),
                        i.getCalories(),
                        (map.get(i.getDateTime().toLocalDate()) > caloriesPerDay)))
                .forEach(resultList::add);

        return resultList;
    }
}
