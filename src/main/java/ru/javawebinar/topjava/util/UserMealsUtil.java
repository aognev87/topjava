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
    private final static Map<Integer, UserMeal> MEALS_MAP = initMap();
    private static int maxId;

    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(0, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredMealsWithExceeded = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredMealsWithExceeded.forEach(System.out::println);

        System.out.println(getFilteredMealsWithExceededByCycle(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
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

    public static List<UserMealWithExceed> getFilteredMealsWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

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

    public static UserMealWithExceed createWithExceed(UserMeal um, boolean exceeded) {
        return new UserMealWithExceed(um.getId(), um.getDateTime(), um.getDescription(), um.getCalories(), exceeded);
    }

    public static List<UserMealWithExceed> cachedMapMeals(){
        return getFilteredMealsWithExceeded(new ArrayList<>(MEALS_MAP.values()), LocalTime.of(0, 0), LocalTime.of(23, 0), 2000);
    }

    public static void deleteMealFromList(String strId){
        try {
            MEALS_MAP.remove(Integer.parseInt(strId));
        } catch (NumberFormatException e){}
    }

    public static void addMealToMap(String strDate, String strTime, String description, String calories){
        LocalDateTime dateTime = getDateTimeFromString(strDate + " " + strTime);

        if (dateTime != null){
            maxId++;
            try {
                MEALS_MAP.put(maxId, new UserMeal(maxId, dateTime, description, Integer.parseInt(calories)));
            } catch (NumberFormatException e) {}
        }

    }

    public static UserMeal getUserMealFromMapById(String strId){
        UserMeal result = null;

        try {
            result = MEALS_MAP.get(Integer.parseInt(strId));
        }catch (NumberFormatException e){}

        return result;
    }

    public static void updateMealInMap(String strId, String strDate, String strTime, String description, String calories){
        LocalDateTime dateTime = getDateTimeFromString(strDate + " " + strTime);

        if (dateTime != null){

            try {
                MEALS_MAP.put(Integer.parseInt(strId), new UserMeal(Integer.parseInt(strId), dateTime, description, Integer.parseInt(calories)));
            } catch (NumberFormatException e) {}

        }

    }

    private static LocalDateTime getDateTimeFromString(String strDateTime){
        LocalDateTime dateTime = null;

        try {
            String date = strDateTime.split(" ")[0];
            String time = strDateTime.split(" ")[1];
            dateTime = LocalDateTime.of(
                    Integer.parseInt(date.split("-")[0]),
                    Integer.parseInt(date.split("-")[1]),
                    Integer.parseInt(date.split("-")[2]),
                    Integer.parseInt(time.split(":")[0]),
                    Integer.parseInt(time.split(":")[1])
            );
        } catch (NumberFormatException e) {}

        return dateTime;
    }

    private static Map<Integer, UserMeal> initMap(){
        Map<Integer, UserMeal> map = new HashMap<>();
        map.put(0, new UserMeal(0, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        map.put(1, new UserMeal(1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        map.put(2, new UserMeal(2, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        map.put(3, new UserMeal(3, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        map.put(4, new UserMeal(4, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        map.put(5, new UserMeal(5, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        maxId = 5;
        return map;
    }
}