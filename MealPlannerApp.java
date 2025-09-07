
/**
 * Command line interface for the meal planner program. 
 * 
 * Allows the user to navigate across each level of the meal planner, adding/removing,
 * viewing, and updating elements. Handles exceptions and prints success/error
 * messages.
 * Persists data across program executions by loading previous data from a file,
 * then overwriting that file with the current data before the program terminates.
 * 
 * @author Michel Préjet
 * @version 2025-09-07
 */

import java.util.Scanner;

public class MealPlannerApp {
    private static Scanner in;
    private static MealPlanner planner;

    private static Week currWeek;
    private static int currDayIndex;
    private static Meal currMeal;
    private static Ingredient currIng;

    private static final String FILE_NAME = "data.csv";

    public static void main(String[] args) {
        // Initialize global variables.
        in = new Scanner(System.in);
        planner = new MealPlanner();
        currWeek = null;
        currDayIndex = -1;
        currMeal = null;
        currIng = null;

        // Load data.
        planner.loadDataFromCSV(FILE_NAME);

        printMainMenu();
        executeChoiceMain();

        // Overwrite file with current data.
        planner.writeDataToCSV(FILE_NAME);

        in.close();
    }

    /**
     * @return a string representing the context of the program in its current
     *         state, of the form
     *         mealplanner>weekName>dayOfWeek>mealName>ingredientName>, with
     *         a trailing space, where null fields are omitted.
     */
    public static String getContext() {
        String output = "mealplanner>";
        if (currWeek != null) {
            output += currWeek.getAnchorDate() + ">";

            if (currDayIndex >= 0 && currDayIndex < Week.getDaysOfWeek().length) {
                output += Week.getDaysOfWeek()[currDayIndex] + ">";

                if (currMeal != null) {
                    output += currMeal.getName() + ">";

                    if (currIng != null) {
                        output += currIng.getName() + ">";
                    }
                }
            }
        }
        return output + " ";
    }

    /**
     * Prints the main menu for the meal planner app, including the current
     * selection/context and all corresponding actions. Prompts the user to select
     * an option by entering its respective digit.
     */
    public static void printMainMenu() {
        System.out.println("\n------------ MAIN MENU ------------");
        System.out.println("Current selection: " + getContext());
        System.out.println("1. Change selection");

        // Remaining options depend on the current path.
        if (currIng != null) {
            System.out.println("2. View current ingredient");
            System.out.println("3. Change ingredient quantity");
        } else if (currMeal != null) {
            System.out.println("2. View current meal");
            System.out.println("3. Add ingredient");
            System.out.println("4. Remove ingredient");
        } else if (currDayIndex >= 0 && currDayIndex < Week.getDaysOfWeek().length) {
            System.out.println("2. View current day");
            System.out.println("3. Add meal");
            System.out.println("4. Remove meal");
        } else if (currWeek != null) {
            System.out.println("2. View current week");
            System.out.println("3. Get shopping list for the current week");
        } else {
            System.out.println("2. List all weeks");
            System.out.println("3. Add week");
            System.out.println("4. Remove week");
        }
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Processes and executes the user's selection from the main menu.
     */
    public static void executeChoiceMain() {
        boolean exit = false;
        while (!exit) {
            String userChoice = in.nextLine().trim();
            switch (userChoice) {
                case "1":
                    changeSelection();
                    break;
                case "2":
                    mainOption2();
                    break;
                case "3":
                    mainOption3();
                    break;
                case "4":
                    mainOption4();
                    break;
                case "0":
                    exit = true;
                    return;
                default:
                    System.out.println("[Error] Unrecognized input.");
            }
            printMainMenu();
        }
    }

    /**
     * Allows the user to navigate through each level of the meal planner
     * (Weeks, Days, Meals, and Ingredients) and add/remove, view, and modify
     * elements.
     */
    public static void changeSelection() {
        System.out.println("\n------------ CHANGE SELECTION ------------");
        System.out.println("Enter 'help' to view a list of commands");

        boolean exit = false;
        while (!exit) {
            System.out.print(getContext());
            String userChoice = in.nextLine().trim().toLowerCase();

            switch (userChoice) {
                case "help":
                case "h":
                case "commands":
                case "command list":
                case "list commands":
                    printCommandList();
                    break;
                case "clear":
                case "c":
                case "clr":
                    clearPath();
                    break;
                case "back":
                case "b":
                    backOneLevel();
                    break;
                case "main":
                case "quit":
                case "0":
                case "exit":
                    exit = true;
                    break;
                default:
                    nextLevel(userChoice);
            }
        }
    }

    // ~~~~~~~~~~ HELPER METHODS FOR executeChoiceMain() ~~~~~~~~~~ //

    public static void mainOption2() {
        // Ingredient level - print ingredient
        if (currIng != null) {
            planner.printIngredient(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex],
                    currMeal.getName(), currIng.getName());
        }
        // Meal level - print meal
        else if (currMeal != null) {
            planner.printMeal(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex],
                    currMeal.getName());
        }
        // Day level - print day
        else if (currDayIndex >= 0 && currDayIndex < Week.getDaysOfWeek().length) {
            planner.printDay(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex]);
        }
        // Week level - print week
        else if (currWeek != null) {
            planner.printWeek(currWeek.getAnchorDate());
        }
        // Meal planner level - list all weeks
        else {
            planner.printAllWeeks();
        }
    }

    public static void mainOption3() {
        // Ingredient level - change ingredient quantity
        if (currIng != null) {
            System.out.print("Enter new quantity: ");

            try {
                planner.changeIngredientQuantity(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex],
                        currMeal.getName(), currIng.getName(), in.nextLine().trim());
                System.out.println("[SUCCESS] Changed ingredient quantity.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Meal level - add ingredient
        else if (currMeal != null) {
            System.out.print("Enter the name of the ingredient: ");
            String ingName = in.nextLine().trim();

            System.out.print("Enter the quantity (in grams): ");
            String quantity = in.nextLine().trim();

            System.out.print("Do you want to include a nutritional profile (Y/N)? ");
            String nutritionOption = in.nextLine().trim();

            try {
                if (nutritionOption.equalsIgnoreCase("y") || nutritionOption.equalsIgnoreCase("yes")) {
                    System.out.print("Enter the amount of carbohydrates per 100 grams (in grams): ");
                    String carbs = in.nextLine().trim();

                    System.out.print("Enter the amount of fat per 100 grams (in grams): ");
                    String fat = in.nextLine().trim();

                    System.out.print("Enter the amount of protein per 100 grams (in grams): ");
                    String protein = in.nextLine().trim();

                    planner.addIngredient(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex],
                            currMeal.getName(), ingName, quantity, carbs, fat, protein);
                } else {
                    planner.addIngredient(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex],
                            currMeal.getName(), ingName, quantity);
                }
                currIng = currMeal.getIngredient(ingName);
                System.out.println("[SUCCESS] Added new ingredient.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Day level - add meal
        else if (currDayIndex >= 0 && currDayIndex < Week.getDaysOfWeek().length) {
            System.out.print("Enter the name of the meal: ");
            String mealName = in.nextLine().trim();

            try {
                currMeal = planner.addMeal(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex], mealName);
                System.out.println("[SUCCESS] Added new meal.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Week level - get shopping list
        else if (currWeek != null) {
            planner.printShoppingList(currWeek.getAnchorDate());
        }
        // Meal planner level - add week
        else {
            System.out.print("Enter the anchor date of the week (Sunday) in the form YYYY-MM-DD: ");
            String anchorDate = in.nextLine().trim();

            try {
                planner.addWeek(anchorDate);
                currWeek = planner.getWeek(anchorDate);
                System.out.println("[SUCCESS] Added new week.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
    }

    public static void mainOption4() {
        // Ingredient level - do nothing
        if (currIng != null) {
            System.out.println("[ERROR] Unrecognized input.");
        }
        // Meal level - remove ingredient
        else if (currMeal != null) {
            System.out.print("Enter the name of the ingredient: ");

            try {
                planner.removeIngredient(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex],
                        currMeal.getName(), in.nextLine().trim());
                System.out.println("[SUCCESS] Removed ingredient.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Day level - remove meal
        else if (currDayIndex >= 0 && currDayIndex < Week.getDaysOfWeek().length) {
            System.out.print("Enter the name of the meal: ");

            try {
                planner.removeMeal(currWeek.getAnchorDate(), Week.getDaysOfWeek()[currDayIndex], in.nextLine().trim());
                System.out.println("[SUCCESS] Removed meal.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Week level - do nothing
        else if (currWeek != null) {
            System.out.println("[ERROR] Unrecognized input.");
        }
        // Meal planner level - remove week
        else {
            System.out.print("Enter the anchor date of the week (Sunday) in the form YYYY-MM-DD: ");

            try {
                planner.removeWeek(in.nextLine().trim());
                System.out.println("[SUCCESS] Removed week.");
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
    }

    // ~~~~~~~~~~ HELPER METHODS FOR changeSelection() ~~~~~~~~~~ //

    /**
     * Takes in the user's input and navigates to the next level of the meal
     * planner accordingly (e.g. if the current level is Meal, navigates to
     * the ingredient with a name matching the user's input, or prints an
     * error if no such ingredient exists).
     * 
     * @param nextLevel the name of the element to be selected.
     */
    public static void nextLevel(String nextLevel) {
        // Ingredient level - do nothing
        if (currIng != null) {
            System.out.println("[ERROR] Unrecognized input.");
        }
        // Meal level - select ingredient
        else if (currMeal != null) {
            try {
                currIng = currMeal.getIngredient(nextLevel);
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Day level - select meal
        else if (currDayIndex >= 0 && currDayIndex < Week.getDaysOfWeek().length) {
            Day currDay = currWeek.getDay(currDayIndex);

            try {
                currMeal = currDay.getMeal(new Meal(nextLevel));
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
        // Week level - select day of week
        else if (currWeek != null) {
            switch (nextLevel) {
                case "sunday":
                case "sun":
                case "0":
                    currDayIndex = 0;
                    break;
                case "monday":
                case "mon":
                case "1":
                    currDayIndex = 1;
                    break;
                case "tuesday":
                case "tue":
                case "tues":
                case "2":
                    currDayIndex = 2;
                    break;
                case "wednesday":
                case "wed":
                case "3":
                    currDayIndex = 3;
                    break;
                case "thursday":
                case "thu":
                case "thurs":
                case "4":
                    currDayIndex = 4;
                    break;
                case "friday":
                case "fri":
                case "5":
                    currDayIndex = 5;
                    break;
                case "saturday":
                case "sat":
                case "6":
                    currDayIndex = 6;
                    break;
                default:
                    System.out.println("[ERROR] Invalid day of the week.");
            }
        }
        // Meal planner level - select week
        else {
            try {
                currWeek = planner.getWeek(nextLevel);
            } catch (ValidationException ve) {
                System.out.println("[ERROR] " + ve.getMessage());
            }
        }
    }

    public static void printCommandList() {
        System.out.println("\n------------ COMMANDS ------------");
        System.out.println("clear: Clears the current selection");
        System.out.println("back: Goes back one level");
        System.out.println("main: Returns to the main menu");
    }

    public static void backOneLevel() {
        if (currIng != null) {
            currIng = null;
        } else if (currMeal != null) {
            currMeal = null;
        } else if (currDayIndex != -1) {
            currDayIndex = -1;
        } else if (currWeek != null) {
            currWeek = null;
        }
    }

    public static void clearPath() {
        currIng = null;
        currMeal = null;
        currDayIndex = -1;
        currWeek = null;
    }

}
