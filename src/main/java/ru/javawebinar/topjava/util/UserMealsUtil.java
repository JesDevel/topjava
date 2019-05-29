package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiFunction;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,11,0), "Завтрак", 100),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 28,14,0), "Обед", 1100),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29,21,0), "Ужин", 900),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,11,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 28,14,0), "Обед", 600),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 28,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 28,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> test = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        BiFunction<UserMeal, Integer, Boolean> bi = (UserMeal um, Integer cpd) -> {
            Integer sumCal = 0;
            for (int i = mealList.indexOf(um); i < mealList.size(); i++) {
                UserMeal current = mealList.get(i);
                if (!current.getDateTime().toLocalDate().equals(um.getDateTime().toLocalDate())) break;
                else {
                    sumCal += current.getCalories();
                }
            }
            return sumCal > cpd;
        };
        mealList.sort(Comparator.comparing(UserMeal::getDateTime));
        List<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, Boolean> exceedByDays = new HashMap<>();
        for (UserMeal meal : mealList) {
            LocalDate currentDay = meal.getDateTime().toLocalDate();
            if (!exceedByDays.containsKey(currentDay)) {
                exceedByDays.put(currentDay, bi.apply(meal, caloriesPerDay));
            }
            if (!TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) continue;
            UserMealWithExceed temp = new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceedByDays.get(currentDay));
            result.add(temp);
        }
        return result;
    }
}
