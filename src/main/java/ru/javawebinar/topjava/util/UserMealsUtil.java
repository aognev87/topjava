package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static final List<UserMeal> MEAL_LIST = Arrays.asList(
            /*
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
            */

            new UserMeal(LocalDateTime.of(2015, 6, 7, 0, 4), "Завтрак", 848, 5),
            new UserMeal(LocalDateTime.of(2015, 6, 7, 23, 47), "Обед", 1296, 5),
            new UserMeal(LocalDateTime.of(2015, 6, 7, 9, 30), "Ужин", 1893, 5),
            new UserMeal(LocalDateTime.of(2015, 6, 8, 18, 2), "Завтрак", 1328, 5),
            new UserMeal(LocalDateTime.of(2015, 6, 8, 16, 58), "Обед", 657, 5),
            new UserMeal(LocalDateTime.of(2015, 6, 8, 18, 35), "Ужин", 782, 5),

            new UserMeal(LocalDateTime.of(2015, 2, 8, 3, 39), "Завтрак", 1085, 1),
            new UserMeal(LocalDateTime.of(2015, 2, 8, 21, 19), "Обед", 830, 1),
            new UserMeal(LocalDateTime.of(2015, 2, 8, 17, 4), "Ужин", 538, 1),
            new UserMeal(LocalDateTime.of(2015, 2, 9, 21, 30), "Завтрак", 775, 1),
            new UserMeal(LocalDateTime.of(2015, 2, 9, 3, 43), "Обед", 1241, 1),
            new UserMeal(LocalDateTime.of(2015, 2, 9, 6, 5), "Ужин", 702, 1),

            new UserMeal(LocalDateTime.of(2015, 11, 10, 8, 11), "Завтрак", 1431, 2),
            new UserMeal(LocalDateTime.of(2015, 11, 10, 21, 53), "Обед", 1133, 2),
            new UserMeal(LocalDateTime.of(2015, 11, 10, 10, 42), "Ужин", 1650, 2),
            new UserMeal(LocalDateTime.of(2015, 11, 3, 3, 6), "Завтрак", 541, 2),
            new UserMeal(LocalDateTime.of(2015, 11, 3, 4, 21), "Обед", 1550, 2),
            new UserMeal(LocalDateTime.of(2015, 11, 3, 20, 12), "Ужин", 1768, 2),

            new UserMeal(LocalDateTime.of(2015, 8, 4, 13, 2), "Завтрак", 628, 3),
            new UserMeal(LocalDateTime.of(2015, 8, 4, 17, 6), "Обед", 1351, 3),
            new UserMeal(LocalDateTime.of(2015, 8, 4, 22, 55), "Ужин", 793, 3),
            new UserMeal(LocalDateTime.of(2015, 8, 2, 13, 6), "Завтрак", 541, 3),
            new UserMeal(LocalDateTime.of(2015, 8, 2, 12, 7), "Обед", 1211, 3),
            new UserMeal(LocalDateTime.of(2015, 8, 2, 9, 33), "Ужин", 1574, 3),

            new UserMeal(LocalDateTime.of(2015, 9, 7, 13, 1), "Завтрак", 1487, 4),
            new UserMeal(LocalDateTime.of(2015, 9, 7, 2, 10), "Обед", 1202, 4),
            new UserMeal(LocalDateTime.of(2015, 9, 7, 20, 1), "Ужин", 1169, 4),
            new UserMeal(LocalDateTime.of(2015, 9, 11, 17, 26), "Завтрак", 974, 4),
            new UserMeal(LocalDateTime.of(2015, 9, 11, 1, 1), "Обед", 1581, 4),
            new UserMeal(LocalDateTime.of(2015, 9, 11, 7, 47), "Ужин", 1618, 4)


    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;


    public static void main(String[] args) {
        List<UserMealWithExceed> filteredMealsWithExceeded = getFilteredWithExceeded(MEAL_LIST, LocalTime.of(7, 0), LocalTime.of(12, 0), DEFAULT_CALORIES_PER_DAY);
        filteredMealsWithExceeded.forEach(System.out::println);

        System.out.println(getFilteredWithExceededByCycle(MEAL_LIST, LocalTime.of(7, 0), LocalTime.of(12, 0), DEFAULT_CALORIES_PER_DAY));
    }

    public static List<UserMealWithExceed> getWithExceeded(Collection<UserMeal> mealList, int caloriesPerDay) {
        return getFilteredWithExceeded(mealList, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(Collection<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream()
                .collect(
                        Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                                Collectors.summingInt(UserMeal::getCalories))
                );

        return mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> createWithExceed(um, caloriesSumByDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> caloriesSumPerDate = new HashMap<>();
        mealList.forEach(meal -> caloriesSumPerDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        final List<UserMealWithExceed> mealExceeded = new ArrayList<>();
        mealList.forEach(meal -> {
            final LocalDateTime dateTime = meal.getDateTime();
            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                mealExceeded.add(createWithExceed(meal, caloriesSumPerDate.get(dateTime.toLocalDate()) > caloriesPerDay));
            }
        });
        return mealExceeded;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededWithSearch(Collection<UserMeal> mealList,
                                                                             LocalDate startDate,
                                                                             LocalDate endDate,
                                                                             LocalTime startTime,
                                                                             LocalTime endTime,
                                                                             int caloriesPerDay,
                                                                             String textFilter) {


        if (startTime == null)
            startTime = LocalTime.MIN;
        if (endTime == null)
            endTime = LocalTime.MAX;

        final LocalTime lambdaStartTime = startTime;
        final LocalTime lambdaEndTime = endTime;

        List<UserMealWithExceed> result = new ArrayList<>();

        getFilteredWithExceeded(mealList, lambdaStartTime, lambdaEndTime, caloriesPerDay).stream()
                .filter(i -> {
                    if (startDate != null)
                        return i.getDateTime().toLocalDate().isAfter(startDate);

                    return true;
                })
                .filter(i -> {
                    if (endDate != null)
                        return i.getDateTime().toLocalDate().isBefore(endDate);

                    return true;
                })
                .forEach(i -> {
                            if (i.getDescription().toLowerCase().matches(".*" + textFilter.toLowerCase() + ".*"))
                                result.add(i);
                        }
                );

        return result;
    }

        public static UserMealWithExceed createWithExceed(UserMeal um, boolean exceeded) {
        return new UserMealWithExceed(um.getId(), um.getDateTime(), um.getDescription(), um.getCalories(), exceeded, um.getUserId());
    }
}
