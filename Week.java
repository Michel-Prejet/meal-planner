
/**
 * Represents a week with an anchor date and a list of days (each of which contain
 * meals).
 * 
 * Provides methods to add/remove meals from specific days of the week, to retrieve 
 * a specific week from an ArrayList by its anchor date, to get a list of all 
 * ingredients needed to prepare the week's meals, to get the average amount 
 * consumed per day of a specific nutrient, and to get the average number of 
 * Calories consumed per day.
 * 
 * @author Michel Préjet
 * @version 2025-08-25
 */

import java.util.ArrayList;

public class Week {
    private String weekAnchorDate; // YYYY-MM-DD
    private Day[] days;
    private static final String[] DAYS_OF_THE_WEEK = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday" };

    /**
     * Constructs a Week with a given anchor date (meant to be the Sunday, but could
     * refer to any day in that week) and an empty array of Day of length 7.
     * 
     * @param weekAnchorDate the anchor date for the week, in YYYY-MM-DD format.
     * @throws IllegalArgumentException if the given date is not a valid date of the
     *                                  form YYYY-MM-DD.
     */
    public Week(String weekAnchorDate) {
        if (!DataValidator.validateDate(weekAnchorDate)) {
            throw new IllegalArgumentException("The date must be a valid date of the form YYYY-MM-DD");
        }

        this.weekAnchorDate = weekAnchorDate;
        this.days = new Day[] { new Day(), new Day(), new Day(), new Day(), new Day(), new Day(), new Day() };
    }

    public String getAnchorDate() {
        return this.weekAnchorDate;
    }

    /**
     * @param index the index of the day of the week to be retrieved.
     * @return the day at a given index (e.g. 0 returns the Day object corresponding
     *         to Sunday).
     */
    public Day getDay(int index) {
        if (index >= 0 && index < this.days.length) {
            return this.days[index];
        }
        return null;
    }

    /**
     * @param dayOfWeek the name of the day of the week to be retrieved.
     * @return the day with the given name.
     */
    public Day getDay(String dayOfWeek) {
        int index = getDayIndex(dayOfWeek);
        if (index != -1) {
            return this.days[index];
        }
        return null;
    }

    /**
     * Returns an ArrayList of Ingredient containing all ingredients used to
     * prepare meals for the current week. Duplicate ingredients are merged
     * (their quantities are added together). All Ingredient objects in the
     * returned list are clones.
     * 
     * @return an ArrayList of Ingredient containing all ingredients used to
     *         prepare meals for the current week.
     */
    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (Day day : this.days) {
            for (Meal meal : day.getMeals()) {
                for (Ingredient ing : meal.getIngredients()) {
                    int index = ingredients.indexOf(ing);
                    if (index != -1) {
                        Ingredient other = ingredients.get(index);
                        other.setQuantity(ing.getQuantity() + other.getQuantity());
                    } else {
                        ingredients.add(ing.clone());
                    }
                }
            }
        }

        return ingredients;
    }

    /**
     * @return the average amount of carbohydrates consumed per day for the week (in
     *         grams per day).
     */
    public double getAvgCarbsPerDay() {
        double totalCarbs = 0;
        for (Day day : this.days) {
            totalCarbs += day.getCarbsTotal();
        }
        return totalCarbs / 7;
    }

    /**
     * @return the average amount of fat consumed per day for the week (in grams per
     *         day).
     */
    public double getAvgFatPerDay() {
        double totalFat = 0;
        for (Day day : this.days) {
            totalFat += day.getFatTotal();
        }
        return totalFat / 7;
    }

    /**
     * @return the average amount of protein consumed per day for the week (in grams
     *         per day).
     */
    public double getAvgProteinPerDay() {
        double totalProtein = 0;
        for (Day day : this.days) {
            totalProtein += day.getProteinTotal();
        }
        return totalProtein / 7;
    }

    /**
     * @return the average number of Calories consumed per day for the week.
     */
    public double getAvgCaloriesPerDay() {
        double totalCalories = 0;
        for (Day day : this.days) {
            totalCalories += day.getCalories();
        }
        return totalCalories / 7;
    }

    /**
     * @param dayOfWeek the day of the week (as a string) for which the index
     *                  should be retrieved.
     * @return the index corresponding to the given day of the week (e.g. 0 for
     *         "Sunday"), or -1 if the given day is invalid (not contained in
     *         DAYS_OF_THE_WEEK).
     * @throws IllegalArgumentException if the given day of the week is null.
     */
    public static int getDayIndex(String dayOfWeek) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null.");
        }

        int dayIndex = -1;
        dayOfWeek = dayOfWeek.trim().toLowerCase();
        for (int i = 0; i < DAYS_OF_THE_WEEK.length; i++) {
            if (dayOfWeek.equals(DAYS_OF_THE_WEEK[i].toLowerCase())) {
                dayIndex = i;
                break;
            }
        }

        return dayIndex;
    }

    /**
     * @return true if the given object is a week with the same anchor date as the
     *         current instance; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Week) {
            return ((Week) o).getAnchorDate().equals(this.weekAnchorDate);
        }
        return false;
    }

    @Override
    public String toString() {
        String[] date = getDateFromString(this.weekAnchorDate);
        String output = String.format("--- Week of %s %d, %d ---", date[1], Integer.parseInt(date[2]),
                Integer.parseInt(date[0]));
        for (int i = 0; i < DAYS_OF_THE_WEEK.length; i++) {
            output += String.format("\n%s: ", DAYS_OF_THE_WEEK[i]);

            // Build a string containing all meals for the current day.
            String meals = "";
            for (Meal meal : this.days[i].getMeals()) {
                meals += meal.getName() + ", ";
            }

            // Remove trailing comma.
            if (meals.lastIndexOf(",") != -1) {
                output += meals.substring(0, meals.length() - 2);
            } else {
                output += "No meals";
            }
        }

        return output;
    }

    /**
     * @param weeks  the ArrayList of Week to be searched.
     * @param anchor the anchor date of the target.
     * @return the week with a given anchor date in a given ArrayList of Week,
     *         or null if no such week was found.
     */
    public static Week getWeekByAnchor(ArrayList<Week> weeks, String anchor) {
        for (Week week : weeks) {
            if (week.getAnchorDate().equals(anchor)) {
                return week;
            }
        }
        return null;
    }

    /**
     * Converts a valid date of the form YYYY-MM-DD into an array of String
     * of length 3 containing the year, month name, and day (e.g. 2025-07-01
     * becomes {"2025", "July", "01"}). Assumes that the given string has
     * already been checked by validateDate(), but returns null if it is
     * invalid.
     * 
     * @param date the string to be converted into an array of String.
     * @return an array of String of the form {"Year", "Month name", "Day"}, or
     *         null if the given string is invalid.
     */
    public static String[] getDateFromString(String date) {
        if (!DataValidator.validateDate(date)) {
            return null;
        }

        String[] formattedDate = new String[3];

        // Get the year and the day.
        formattedDate[0] = date.substring(0, 4);
        formattedDate[2] = date.substring(8, 10);

        // Get the month.
        int monthNum = Integer.parseInt(date.substring(5, 7));
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        formattedDate[1] = monthNames[monthNum - 1];

        return formattedDate;
    }
}
