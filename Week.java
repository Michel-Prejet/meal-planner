
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
 * @version 2025-09-06
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
     * @throws ValidationException if {@code weekAnchorDate} is not a valid date of
     *                             the form YYYY-MM-DD.
     */
    public Week(String weekAnchorDate) {
        DataValidator.validateDate(weekAnchorDate, "Week anchor date");

        this.weekAnchorDate = weekAnchorDate.trim();
        this.days = new Day[] { new Day(), new Day(), new Day(), new Day(), new Day(), new Day(), new Day() };
    }

    public String getAnchorDate() {
        return this.weekAnchorDate;
    }

    public static String[] getDaysOfWeek() {
        return DAYS_OF_THE_WEEK;
    }

    /**
     * @param index the index of the day of the week to be retrieved.
     * @return the day at the given index (e.g. 0 returns the Day object
     *         corresponding to Sunday), or null if no such day exists.
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
     * @throws ValidationException if the {@code dayOfWeek} is null or if it
     *                             doesn't correspond to a valid day of the week.
     */
    public Day getDay(String dayOfWeek) {
        int index = getDayIndex(dayOfWeek);
        if (index != -1) {
            return this.days[getDayIndex(dayOfWeek)];
        } else {
            throw new ValidationException("Day of week", ValidationException.INVALID_WEEKDAY_CODE);
        }
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
     * @return the index corresponding to {@code dayOfWeek} (e.g. 0 for
     *         "Sunday"), or -1 if {@code dayOfWeek} is invalid (not contained in
     *         DAYS_OF_THE_WEEK).
     * @throws ValidationException if {@code dayOfWeek} of the week is null, empty,
     *                             or only whitespace.
     */
    public static int getDayIndex(String dayOfWeek) {
        DataValidator.validateString(dayOfWeek, "Day of week");

        int dayIndex = -1;
        dayOfWeek = dayOfWeek.trim();
        for (int i = 0; i < DAYS_OF_THE_WEEK.length; i++) {
            if (dayOfWeek.equalsIgnoreCase(DAYS_OF_THE_WEEK[i])) {
                dayIndex = i;
                break;
            }
        }

        return dayIndex;
    }

    /**
     * @param weeks  the ArrayList of Week to be searched.
     * @param anchor the anchor date of the target.
     * @return the week with the given anchor date in a given ArrayList of Week,
     *         or null if no such week was found.
     * @throws ValidationException if {@code weeks} is null or if {@code anchor}
     *                             is not a valid date of the form YYYY-MM-DD.
     */
    public static Week getWeekByAnchor(ArrayList<Week> weeks, String anchor) {
        if (weeks == null) {
            throw new ValidationException("ArrayList of weeks", ValidationException.NULL_ARGUMENT_CODE);
        }
        DataValidator.validateDate(anchor, "Week anchor date");

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
     * becomes {"2025", "July", "01"}).
     * 
     * @param date the string to be converted into an array of String.
     * @return an array of String of the form {"Year", "Month name", "Day"}.
     * @throws ValidationException if {@code date} is invalid.
     */
    public static String[] getDateFromString(String date) {
        DataValidator.validateDate(date, "Date");

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

    /**
     * Compares this week with the given week by anchor date.
     * 
     * @param other the week to compare against.
     * @return a negative integer, zero, or a positive integer as this week's
     *         anchor date is lexicographically less than, equal to, or
     *         greater than that of {@code other}.
     * @throws ValidationException if {@code other} is null.
     */
    public int compareTo(Week other) {
        if (other == null) {
            throw new ValidationException("Week", ValidationException.NULL_ARGUMENT_CODE);
        }

        return this.getAnchorDate().compareTo(other.getAnchorDate());
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
}
