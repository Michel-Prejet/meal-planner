/**
 * Custom exception thrown when invalid data is entered into the program.
 * Includes a field name as well as specific codes for invalid strings, invalid
 * doubles, invalid dates, and non-positive values.
 * 
 * @author Michel Préjet
 * @version 2025-09-07
 */

public class ValidationException extends RuntimeException {
    private String field;
    private String code;

    public static final String NULL_ARGUMENT_CODE = "NULL_ARGUMENT";
    public static final String INVALID_STRING_CODE = "INVALID_STRING";
    public static final String INVALID_DOUBLE_CODE = "INVALID_DOUBLE";
    public static final String NON_POSITIVE_VALUE_CODE = "NON_POSITIVE_VALUE";
    public static final String NEGATIVE_VALUE_CODE = "NEGATIVE_VALUE";
    public static final String INVALID_DATE_CODE = "INVALID_DATE";
    public static final String ALREADY_EXISTS_CODE = "ALREADY_EXISTS";
    public static final String DOESNT_EXIST_CODE = "DOESNT_EXIST";
    public static final String INVALID_WEEKDAY_CODE = "INVALID_WEEKDAY";
    public static final String NO_CODE = "NONE";

    private static final String[] CODES = { NULL_ARGUMENT_CODE, INVALID_STRING_CODE, INVALID_DOUBLE_CODE,
            NON_POSITIVE_VALUE_CODE, NEGATIVE_VALUE_CODE, ALREADY_EXISTS_CODE, DOESNT_EXIST_CODE, INVALID_DATE_CODE,
            INVALID_WEEKDAY_CODE };

    private static final String NULL_ARGUMENT_MESSAGE = " cannot be null.";
    private static final String INVALID_STRING_MESSAGE = " cannot be null, empty, or only whitespace.";
    private static final String INVALID_DOUBLE_MESSAGE = " is not a valid double.";
    private static final String NON_POSITIVE_VALUE_MESSAGE = " cannot be zero or negative.";
    private static final String NEGATIVE_VALUE_MESSAGE = " cannot be negative.";
    private static final String INVALID_DATE_MESSAGE = " is not a valid date.";
    private static final String ALREADY_EXISTS_MESSAGE = " already exists.";
    public static final String DOESNT_EXIST_MESSAGE = " does not exist.";
    private static final String INVALID_WEEKDAY_MESSAGE = " is not a valid weekday.";
    private static final String NO_CODE_MESSAGE = ": no error message.";

    /**
     * Constructs a ValidationException with a field name and a code.
     * Checks if {@code code} exists in the list of codes, and assigns a code name
     * or "NONE" if no match was found. Assigns a message based on {@code code}.
     * 
     * @param field the name of the field that caused the exception.
     * @param code  the code to assign to the exception.
     */
    public ValidationException(String field, String code) {
        super(getMessage(field, code));

        this.field = field;

        // Assign code with a string in CODES, or "NONE" if no matching string is
        // found.
        code = code.toUpperCase();
        boolean found = false;
        for (int i = 0; i < CODES.length && !found; i++) {
            if (code.equals(CODES[i])) {
                this.code = CODES[i];
                found = true;
            }
        }
        if (!found) {
            this.code = NO_CODE;
        }
    }

    public String getField() {
        return this.field;
    }

    public String getCode() {
        return this.code;
    }

    /**
     * Constructs and returns an error message given a field and a code.
     * 
     * @param field the name of the field to use in the message.
     * @param code  the code to use to determine the format of the message.
     * @return a string containing an error message constructed from {@code field}
     *         and {@code code}.
     */
    private static String getMessage(String field, String code) {
        if (code.equals(INVALID_STRING_CODE)) {
            return field + INVALID_STRING_MESSAGE;
        } else if (code.equals(NULL_ARGUMENT_CODE)) {
            return field + NULL_ARGUMENT_MESSAGE;
        } else if (code.equals(INVALID_DOUBLE_CODE)) {
            return field + INVALID_DOUBLE_MESSAGE;
        } else if (code.equals(NON_POSITIVE_VALUE_CODE)) {
            return field + NON_POSITIVE_VALUE_MESSAGE;
        } else if (code.equals(NEGATIVE_VALUE_CODE)) {
            return field + NEGATIVE_VALUE_MESSAGE;
        } else if (code.equals(INVALID_DATE_CODE)) {
            return field + INVALID_DATE_MESSAGE;
        } else if (code.equals(ALREADY_EXISTS_CODE)) {
            return field + ALREADY_EXISTS_MESSAGE;
        } else if (code.equals(DOESNT_EXIST_CODE)) {
            return field + DOESNT_EXIST_MESSAGE;
        } else if (code.equals(INVALID_WEEKDAY_CODE)) {
            return field + INVALID_WEEKDAY_MESSAGE;
        } else {
            return field + NO_CODE_MESSAGE;
        }
    }
}
