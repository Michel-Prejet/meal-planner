
/**
 * Performs data validation throughout the program.
 * 
 * Provides methods to validate strings, determine whether a string can be cast
 * to a double, validate dates, and validate lines of data read from CSV files.
 * 
 * @author Michel Préjet
 * @version 2025-09-06
 */

public class DataValidator {

    /**
     * @param s         the string to validate.
     * @param fieldName the name of the field to validate.
     * @throws ValidationException if {@code s} is null, empty, or only
     *                             whitespace.
     */
    public static void validateString(String s, String fieldName) {
        if (s == null || s.trim().isEmpty()) {
            throw new ValidationException(fieldName, ValidationException.INVALID_STRING_CODE);
        }
    }

    /**
     * Checks whether a string is a valid double. To be valid, it must have
     * no more than one period, no more than one dash at the first index, and
     * no other non-digit characters.
     * 
     * @param s         the string to validate.
     * @param fieldName the name of the field to validate.
     * @throws ValidationException if {@code s} is not a valid double.
     */
    public static void requireDouble(String s, String fieldName) {
        validateString(s, fieldName);
        s = s.trim();

        // Check that there is no more than one period in the string.
        if (s.indexOf(".") != s.lastIndexOf(".")) {
            throw new ValidationException(fieldName, ValidationException.INVALID_DOUBLE_CODE);
        }

        // If there is a negative sign, check that it is the first character.
        if (s.indexOf("-") != -1) {
            if (s.lastIndexOf("-") != 0) {
                throw new ValidationException(fieldName, ValidationException.INVALID_DOUBLE_CODE);
            }
        }

        // Check that all characters other than the period and the negative sign
        // are digits.
        s = s.replace(".", "");
        s = s.replace("-", "");
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                throw new ValidationException(fieldName, ValidationException.INVALID_DOUBLE_CODE);
            }
        }
    }

    /**
     * Checks whether a string is a valid date of the form YYYY-MM-DD. To be valid,
     * the string must have a length of 10 and exactly two dashes at indices 4 and
     * 7, with all other characters being digits. The year, month, and day must also
     * be positive integers, the month must be between 1 and 12 (inclusive), and the
     * day must be valid for the given month (e.g. 31 days is invalid for June).
     * 
     * @param date      the string to validate.
     * @param fieldName the name of the field to validate.
     * @throws ValidationException if {@code date} is not a valid date.
     */
    public static void validateDate(String date, String fieldName) {
        validateString(date, fieldName);

        // Check that the date is formatted correctly. It must have a length of 10
        // and exactly two dashes at indices 4 and 7. All other characters must be
        // digits.
        date = date.trim();
        if (date.length() == 10 && date.charAt(4) == '-' && date.charAt(7) == '-') {
            date = date.replace("-", "");
            for (int i = 0; i < date.length(); i++) {
                if (!Character.isDigit(date.charAt(i))) {
                    throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
                }
            }
        } else {
            throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
        }

        // Get the year, month, and day.
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));

        // Make sure that the year, month, and day are positive.
        if (year <= 0 || month <= 0 || day <= 0) {
            throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
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
                if (day > 31) {
                    throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day > 30) {
                    throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
                }
                break;
            case 2:
                if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                    if (day > 29) {
                        throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
                    }
                } else {
                    if (day > 28) {
                        throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
                    }
                }
                break;
            default:
                throw new ValidationException(fieldName, ValidationException.INVALID_DATE_CODE);
        }
    }

    /**
     * Checks that a line of data read from a CSV file will not cause
     * exceptions or unexpected results. Ensures that there are either 5 or 8
     * tokens, that all tokens are valid strings, and that tokens 4, 5, 6, and 7
     * can be safely cast to doubles.
     * 
     * @param line the CSV line to be validated.
     * @return true if all required tokens exist and are valid; false otherwise.
     */
    public static boolean validateLine(String line) {
        try {
            validateString(line, "Line");
        } catch (ValidationException ve) {
            return false;
        }

        // Check that there is a correct number of tokens.
        String[] tokens = line.split(",");
        if (tokens.length != 5 && tokens.length != 8) {
            return false;
        }

        // Validate the first five tokens.
        // 1 - Day
        if (Week.getDayIndex(tokens[1]) == -1 && !tokens[1].equals(MealPlanner.EMPTY_PLACEHOLDER)) {
            return false;
        }
        try {
            // 0 - Week anchor date
            validateDate(tokens[0], "Week anchor date");
            // 2 - Meal name
            validateString(tokens[2], "Meal name");
            // 3 - Ingredient name
            validateString(tokens[3], "Ingredient name");
            // 4 - Ingredient quantity
            validateString(tokens[4], "Ingredient name");
            if (!tokens[4].equals(MealPlanner.EMPTY_PLACEHOLDER)) {
                requireDouble(tokens[4], "Ingredient quantity");
            }
        } catch (ValidationException ve) {
            return false;
        }

        if (tokens.length == 5) {
            return true;
        }

        // Validate the remaining tokens (for lines which contain nutritional
        // information).
        try {
            // 5 - carbs per 100 g
            requireDouble(tokens[5], "Carbohydrates per 100 grams");
            // 6 - fat per 100 g
            requireDouble(tokens[6], "Fat per 100 grams");
            // 7 - protein per 100 g
            requireDouble(tokens[7], "Protein per 100 grams");
        } catch (ValidationException ve) {
            return false;
        }
        return true;
    }
}
