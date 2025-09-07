/**
 * Represents a food ingredient with a name, a quantity (in grams) and optional
 * nutritional information (carbohydrates, fat, and protein).
 * 
 * Provides methods to retrieve the total Calories and nutrient content for the
 * stored quantity.
 * 
 * @author Michel Préjet
 * @version 2025-09-06
 */

public class Ingredient {
    private String name;
    private double quantity; // grams
    private boolean hasNutritionalFacts;
    private double carbsPer100Gram;
    private double fatPer100Gram;
    private double proteinPer100Gram;

    private static final double KCAL_PER_GRAM_CARB = 4;
    private static final double KCAL_PER_GRAM_FAT = 9;
    private static final double KCAL_PER_GRAM_PROTEIN = 4;

    /**
     * Constructs an Ingredient with a name and a quantity, but no nutritional
     * facts.
     * 
     * @param name     name of the ingredient.
     * @param quantity amount of the ingredient (in grams).
     * @throws ValidationException if {@code name} is null, empty, or only
     *                             whitespace, or if {@code quantity} is not
     *                             positive.
     */
    public Ingredient(String name, double quantity) {
        DataValidator.validateString(name, "Ingredient name");
        if (quantity <= 0) {
            throw new ValidationException("Ingredient quantity", ValidationException.NON_POSITIVE_VALUE_CODE);
        }

        this.name = name;
        this.quantity = quantity;
        this.hasNutritionalFacts = false;
    }

    /**
     * Constructs an Ingredient with a name, a quantity, and nutritional facts,
     * which include carbohydrates per 100 grams, fat per 100 grams, and protein per
     * 100 grams.
     * 
     * @param name              name of the ingredient.
     * @param quantity          amount of the ingredient (in grams).
     * @param carbsPer100Gram   grams of carbohydrates per 100 grams of ingredient.
     * @param fatPer100Gram     grams of fat per 100 grams of ingredient.
     * @param proteinPer100Gram grams of protein per 100 grams of ingredient.
     * @throws ValidationException if {@code name} is null, empty, or only
     *                             whitespace, if {@code quantity} is not
     *                             positive, or if the given nutritional values
     *                             are negative.
     */
    public Ingredient(String name, double quantity, double carbsPer100Gram, double fatPer100Gram,
            double proteinPer100Gram) {
        DataValidator.validateString(name, "Ingredient name");
        if (quantity <= 0) {
            throw new ValidationException("Ingredient quantity", ValidationException.NON_POSITIVE_VALUE_CODE);
        } else if (carbsPer100Gram < 0 || fatPer100Gram < 0 || proteinPer100Gram < 0) {
            throw new ValidationException("Macronutrient quantity", ValidationException.NEGATIVE_VALUE_CODE);
        }

        this.name = name.trim();
        this.quantity = quantity;
        this.carbsPer100Gram = carbsPer100Gram;
        this.fatPer100Gram = fatPer100Gram;
        this.proteinPer100Gram = proteinPer100Gram;
        this.hasNutritionalFacts = true;
    }

    public String getName() {
        return this.name;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public boolean hasNutrition() {
        return this.hasNutritionalFacts;
    }

    /**
     * @return grams of carbohydrates per 100 grams of the ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getCarbsPer100Grams() {
        if (this.hasNutritionalFacts) {
            return this.carbsPer100Gram;
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @return grams of fat per 100 grams of the ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getFatPer100Grams() {
        if (this.hasNutritionalFacts) {
            return this.fatPer100Gram;
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @return grams of protein per 100 grams of the ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getProteinPer100Grams() {
        if (this.hasNutritionalFacts) {
            return this.proteinPer100Gram;
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @return grams of carbohydrates for the stored quantity of the ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getCarbsTotal() {
        if (this.hasNutritionalFacts) {
            return (this.carbsPer100Gram / 100.0) * this.quantity;
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @return grams of fat for the stored quantity of the ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getFatTotal() {
        if (this.hasNutritionalFacts) {
            return (this.fatPer100Gram / 100.0) * this.quantity;
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @return grams of protein for the stored quantity of the ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getProteinTotal() {
        if (this.hasNutritionalFacts) {
            return (this.proteinPer100Gram / 100.0) * this.quantity;
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @return the number of Calories (kilocalories) contained in the stored
     *         quantity of the current ingredient.
     * @throws IllegalStateException if no nutritional profile is available.
     */
    public double getCalories() {
        if (this.hasNutritionalFacts) {
            return (this.getProteinTotal() * KCAL_PER_GRAM_PROTEIN + this.getFatTotal() * KCAL_PER_GRAM_FAT
                    + this.getCarbsTotal() * KCAL_PER_GRAM_CARB);
        } else {
            throw new IllegalStateException("Missing nutrition profile for ingredient: " + this.name + ".");
        }
    }

    /**
     * @param newQuantity the new amount for the ingredient.
     * @throws ValidationException if {@code newQuantity} is not positive.
     */
    public void setQuantity(double newQuantity) {
        if (newQuantity <= 0) {
            throw new ValidationException("Ingredient quantity", ValidationException.NON_POSITIVE_VALUE_CODE);
        }

        this.quantity = newQuantity;
    }

    public Ingredient clone() {
        if (this.hasNutritionalFacts) {
            return new Ingredient(this.name, this.quantity, this.carbsPer100Gram, this.fatPer100Gram,
                    this.proteinPer100Gram);
        } else {
            return new Ingredient(this.name, this.quantity);
        }
    }

    /**
     * @param o the object to compare against.
     * @return true if the given object is an ingredient with the same name as the
     *         current instance (case-insensitive); false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Ingredient) {
            Ingredient other = (Ingredient) o;
            return other.getName().trim().equalsIgnoreCase(this.name.trim());
        }
        return false;
    }

    /**
     * Compares this ingredient with {@code other} by name (case-
     * insensitive).
     * 
     * @param other the ingredient to compare against.
     * @return a negative integer, zero, or a positive integer as this
     *         ingredient's name is lexicographically less than, equal to, or
     *         greater than other's.
     * @throws ValidationException if {@code other} is null.
     */
    public int compareTo(Ingredient other) {
        if (other == null) {
            throw new ValidationException("Ingredient", ValidationException.NULL_ARGUMENT_CODE);
        }

        return this.getName().toLowerCase().compareTo(other.getName().toLowerCase());
    }

    @Override
    public String toString() {
        String output = String.format("\n%s (%.2f g)\n", this.name, this.quantity);
        if (this.hasNutritionalFacts) {
            output += String.format("\t%.2f g carbohydrates\n", this.getCarbsTotal());
            output += String.format("\t%.2f g fat\n", this.getFatTotal());
            output += String.format("\t%.2f g protein\n", this.getProteinTotal());
            output += String.format("\t%.2f kcal", this.getCalories());
        } else {
            output += "\tNo nutritional profile";
        }
        return output;
    }
}