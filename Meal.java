
/**
 * Represents a meal with a name and an ingredient list.
 * 
 * Provides methods to add/remove ingredients and to retrieve the total Calories 
 * and nutrient content for the whole meal.
 * 
 * @author Michel Préjet
 * @version 2025-08-21
 */

import java.util.ArrayList;

public class Meal {
    private String name;
    private ArrayList<Ingredient> ingredients;

    /**
     * Constructs a Meal with a given name and an empty ingredient list.
     * 
     * @throws IllegalArgumentException if the given name is null, empty, or
     *                                  only whitespace.
     */
    public Meal(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Meal name cannot be null, empty, or only whitespace.");
        }

        this.name = name.trim();
        this.ingredients = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    /**
     * @param ingredientName the name of the ingredient to be returned.
     * @return the ingredient with the given name in the meal, or null if no
     *         such ingredient exists.
     */
    public Ingredient getIngredient(String ingredientName) {
        for (Ingredient ing : this.ingredients) {
            if (ing.getName().toLowerCase().equals(ingredientName.toLowerCase())) {
                return ing;
            }
        }
        return null;
    }

    /**
     * @return a copy of the ingredient list.
     */
    public ArrayList<Ingredient> getIngredients() {
        return new ArrayList<>(this.ingredients);
    }

    /**
     * @param ing the ingredient to add to the meal.
     * @return true if the given ingredient was added successfully; false
     *         otherwise (if it was null or already in the ingredients ArrayList).
     */
    public boolean addIngredient(Ingredient ing) {
        if (ing != null && !this.ingredients.contains(ing)) {
            this.ingredients.add(ing);
            return true;
        }
        return false;
    }

    /**
     * @param ing the ingredient to remove from the meal.
     * @return true if the given ingredient was removed successfully; false
     *         otherwise (if it was null or not in the ingredients ArrayList).
     */
    public boolean removeIngredient(Ingredient ing) {
        if (ing != null) {
            return this.ingredients.remove(ing);
        }
        return false;
    }

    /**
     * @return the total amount of carbohydrates in the meal (in grams).
     */
    public double getCarbsTotal() {
        double carbs = 0;
        for (Ingredient ing : this.ingredients) {
            if (ing.hasNutrition()) {
                carbs += ing.getCarbsTotal();
            }
        }
        return carbs;
    }

    /**
     * @return the total amount of fat in the meal (in grams).
     */
    public double getFatTotal() {
        double fat = 0;
        for (Ingredient ing : this.ingredients) {
            if (ing.hasNutrition()) {
                fat += ing.getFatTotal();
            }
        }
        return fat;
    }

    /**
     * @return the total amount of protein in the meal (in grams).
     */
    public double getProteinTotal() {
        double protein = 0;
        for (Ingredient ing : this.ingredients) {
            if (ing.hasNutrition()) {
                protein += ing.getProteinTotal();
            }
        }
        return protein;
    }

    /**
     * @return the total number of Calories in the meal.
     */
    public double getCalories() {
        double cals = 0;
        for (Ingredient ing : this.ingredients) {
            if (ing.hasNutrition()) {
                cals += ing.getCalories();
            }
        }
        return cals;
    }

    /**
     * @return true if the given object is a meal with the same name
     *         (case-insensitive) as the current instance.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Meal) {
            return ((Meal) o).getName().equalsIgnoreCase(this.name);
        }
        return false;
    }

    @Override
    public String toString() {
        String output = String.format("%s (%.2f Calories)", this.name, this.getCalories());

        if (this.ingredients.size() > 0) {
            output += "\n\tIngredients:";
        } else {
            output += "\n\tNo ingredients.";
        }

        for (Ingredient ing : this.ingredients) {
            output += String.format("\n\t- %s (%.2f g)", ing.getName(), ing.getQuantity());
        }

        return output;
    }
}
