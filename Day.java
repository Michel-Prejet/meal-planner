
/**
 * Represents a specific day with a list of meals.
 * 
 * Provides methods to add/remove meals and to retrieve the total Calories 
 * and nutrient amounts for the day.
 * 
 * @author Michel Préjet
 * @version 2025-08-21
 */

import java.util.ArrayList;

public class Day {
    private ArrayList<Meal> meals;

    /**
     * Constructs a Day with an empty meal list.
     */
    public Day() {
        this.meals = new ArrayList<>();
    }

    /**
     * @param meal the meal to add to the meals ArrayList.
     * @throws IllegalArgumentException if the given meal is null.
     */
    public void addMeal(Meal meal) {
        if (meal == null) {
            throw new IllegalArgumentException("Meal cannot be null.");
        }
        this.meals.add(meal);
    }

    /**
     * @param meal the meal to remove from the meals ArrayList.
     * @return true if the meal was removed successfully; false otherwise (if
     *         it wasn't found in the list).
     * @throws IllegalArgumentException if the given meal is null.
     */
    public boolean removeMeal(Meal meal) {
        if (meal == null) {
            throw new IllegalArgumentException("Meal cannot be null.");
        }
        return this.meals.remove(meal);
    }

    /**
     * Retrieves the first meal in the current day which is equal to a given
     * meal (but may not have the same memory address).
     * 
     * @param other the meal to be searched for in the current day.
     * @return the meal in the current day matching a given meal.
     */
    public Meal getMeal(Meal other) {
        for (Meal meal : this.meals) {
            if (meal.equals(other)) {
                return meal;
            }
        }
        return null;
    }

    /**
     * @return a copy of the meal list.
     */
    public ArrayList<Meal> getMeals() {
        return new ArrayList<>(this.meals);
    }

    /**
     * @return the total amount of carbohydrates for the current day (in grams).
     */
    public double getCarbsTotal() {
        double carbs = 0;
        for (Meal meal : this.meals) {
            carbs += meal.getCarbsTotal();
        }
        return carbs;
    }

    /**
     * @return the total amount of fat for the current day (in grams).
     */
    public double getFatTotal() {
        double fat = 0;
        for (Meal meal : this.meals) {
            fat += meal.getFatTotal();
        }
        return fat;
    }

    /**
     * @return the total amount of protein for the current day (in grams).
     */
    public double getProteinTotal() {
        double protein = 0;
        for (Meal meal : this.meals) {
            protein += meal.getProteinTotal();
        }
        return protein;
    }

    /**
     * @return the total number of Calories for the current day.
     */
    public double getCalories() {
        double calories = 0;
        for (Meal meal : this.meals) {
            calories += meal.getCalories();
        }
        return calories;
    }

    @Override
    public String toString() {
        int arrSize = this.meals.size();

        String output = "";
        for (int i = 0; i < arrSize - 1; i++) {
            output += this.meals.get(i).getName() + ", ";
        }
        if (arrSize > 0) {
            output += this.meals.get(arrSize - 1);
        }

        return output;
    }
}
