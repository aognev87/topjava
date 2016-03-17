package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Антон on 13.03.2016.
 */
public class UserUtil {
    public static final List<User> USER_LIST = Arrays.asList(
            new User(null, "User_0", "user_0@mail.ru", "user_0_password", Role.ROLE_USER, Role.ROLE_ADMIN),
            new User(null, "User_1", "user_1@mail.ru", "user_1_password", Role.ROLE_USER),
            new User(null, "User_2", "user_2@mail.ru", "user_2_password", Role.ROLE_USER),
            new User(null, "User_3", "user_3@mail.ru", "user_3_password", Role.ROLE_USER, Role.ROLE_ADMIN),
            new User(null, "User_4", "user_4@mail.ru", "user_4_password", Role.ROLE_USER)
    );

    public static void main(String[] args) {
        mealGenerator();

    }

    private static void mealGenerator(){
        for (int i = 0; i < 5; i++) {
            int month = (int) (Math.random() * 12 + 1);

            for (int j = 0; j < 2; j++) {
                int date = (int) (Math.random() * 12 + 1);
                int hour = (int) (Math.random() * 24);
                int minute = (int) (Math.random() * 59);
                int calories = (int) (Math.random() * 1501 + 500);

                System.out.format("\nnew UserMeal(LocalDateTime.of(2015, %d, %d, %d, %d), \"Завтрак\", %d, %d),", month, date, hour, minute, calories, i);

                hour = (int) (Math.random() * 24);
                minute = (int) (Math.random() * 59);
                calories = (int) (Math.random() * 1501 + 500);
                System.out.format("\nnew UserMeal(LocalDateTime.of(2015, %d, %d, %d, %d), \"Обед\", %d, %d),", month, date, hour, minute, calories, i);

                hour = (int) (Math.random() * 24);
                minute = (int) (Math.random() * 59);
                calories = (int) (Math.random() * 1501 + 500);
                System.out.format("\nnew UserMeal(LocalDateTime.of(2015, %d, %d, %d, %d), \"Ужин\", %d, %d),", month, date, hour, minute, calories, i);

            }

            System.out.println();
        }
    }
}
