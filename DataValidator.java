
/**
 * Performs data validation throughout the program.
 * 
 * Provides methods to validate strings, determine whether a string can be cast
 * to a double, validate dates, and validate lines of data read from CSV files.
 * 
 * @author Michel Préjet
 * @version 2025-08-29
 */

public class DataValidator {

    /**
     * @param s the string to be validated.
     * @return true if the given string is not null, empty, or only whitespace;
     *         false otherwise.
     */
    public static boolean validateString(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Checks whether a string is a valid double. To be valid, it must have
     * no more than one period, no more than one dash at the first index, and
     * no other non-digit characters.
     * 
     * @param s the string to be validated.
     * @return true if the given string is a valid double; false otherwise.
     */
    public static boolean isValidDouble(String s) {
        if (!validateString(s)) {
            return false;
        }
        s = s.trim();

        // Check that there is no more than one period in the string.
        if (s.indexOf(".") != s.lastIndexOf(".")) {
            return false;
        }

        // If there is a negative sign, check that it is the first character.
        if (s.indexOf("-") != -1) {
            if (s.lastIndexOf("-") != 0) {
                return false;
            }
        }

        // Check that all characters other than the period and the negative sign
        // are digits.
        s = s.replace(".", "");
        s = s.replace("-", "");
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether a string is a valid date of the form YYYY-MM-DD. To be valid,
     * the string must have a length of 10 and exactly two dashes at indices 4 and
     * 7, with all other characters being digits. The year, month, and day must also
     * be positive integers, the month must be between 1 and 12 (inclusive), and the
     * day must be valid for the given month (e.g. 31 days is invalid for June).
     * 
     * @param date the string to be validated.
     * @return true if the given string is a valid date; false otherwise.
     */
    public static boolean validateDate(String date) {
        if (date == null) {
            return false;
        }

        // Check that the date is formatted correctly. It must have a length of 10
        // and exactly two dashes at indices 4 and 7. All other characters must be
        // digits.
        date = date.trim();
        if (date.length() == 10 && date.charAt(4) == '-' && date.charAt(7) == '-') {
            date = date.replace("-", "");
            for (int i = 0; i < date.length(); i++) {
                if (!Character.isDigit(date.charAt(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }

        // Get the year, month, and day.
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));

        // Make sure that the year, month, and day are positive.
        if (year <= 0 || month <= 0 || day <= 0) {
            return false;
        }

        // Check that the month is between 1 and 12 (inclusive) and that it has
        // the correct number of days.
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return day <= 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return day <= 30;
            case 2:
                if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                    return day <= 29;
                } else {
                    return day <= 28;
                }
            default:
                return false;
        }
    }

    /**
     * Checks that a line of data read from a CSV file will not cause
     * exceptions or unexpected results. Ensures that there are either 5 or 8
     * tokens, that all tokens are valid strings, that tokens 4, 5, 6, and 7
     * can be safely cast to doubles, and that token 5 can safely be cast to a
     * boolean.
     * 
     * @param line the CSV line to be validated.
     * @return true if all required tokens exist and are valid; false otherwise.
     */
    public static boolean validateLine(String line) {
        if (!validateString(line)) {
            return false;
        }

        // Check that there is a correct number of tokens.
        String[] tokens = line.split(",");
        if (tokens.length != 5 && tokens.length != 8) {
            return false;
        }

        // Validate the first six tokens.
        if (!validateDate(tokens[0]) // Week anchor date
                || (Week.getDayIndex(tokens[1]) == -1 && !tokens[1].equals(MealPlanner.EMPTY_PLACEHOLDER)) // Day
                || (!validateString(tokens[2]) && !tokens[2].equals(MealPlanner.EMPTY_PLACEHOLDER)) // Meal name
                || (!validateString(tokens[3]) && !tokens[3].equals(MealPlanner.EMPTY_PLACEHOLDER)) // Ingredient name
                || (!isValidDouble(tokens[4]) && !tokens[4].equals(MealPlanner.EMPTY_PLACEHOLDER))) { // Quantity
            return false;
        } else if (tokens.length == 5) {
            return true;
        }

        // Validate the remaining tokens (for lines which contain nutritional
        // information).
        return (isValidDouble(tokens[5]) || tokens[5].equals(MealPlanner.EMPTY_PLACEHOLDER))
                && (isValidDouble(tokens[6]) || tokens[6].equals(MealPlanner.EMPTY_PLACEHOLDER))
                && (isValidDouble(tokens[7]) || tokens[7].equals(MealPlanner.EMPTY_PLACEHOLDER));
    }
}
