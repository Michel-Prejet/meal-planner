# Meal Planner Program

Java CLI meal planner with an object-oriented model (Week -> Day -> Meal -> Ingredient). Plan weekly meals, get nutritional information (including Calorie counts and macronutrient amounts), generate weekly shopping lists, and persist data in a CSV file.

---

## General Overview

As a user, you can:

- Add, remove, and/or modify elements
  - Add/remove a week with a given anchor date
  - Add/remove a meal with a given name to a day of a week
  - Add/remove an ingredient (with an optional nutritional profile) to a meal
  - Modify the quantity of an ingredient
- Navigate through each level of the meal planner (weeks -> days -> meals -> ingredients)
- Print a detailed summary of any element
  - Print a list of all weeks in the meal planner
  - Print a weekly summary in tabular form, including meals and weekly nutritional averages
  - Print a daily summary which includes nutritional totals and a detailed summary of all meals
  - Print individual meals, including nutritional totals and a list of ingredients
  - Print individual ingredients, including a nutritional profile (if applicable)
- Print an alphabetized shopping list (a list of all ingredients) for any given week

---

## Technologies

- Built with Java (JDK 23).
- No external libraries.

---

## How To Run

Navigate to the project directory in the command line, then type the following:

```bash
javac *.java
java MealPlannerApp
```

On startup, the program attempts to load `data.csv`. On exit, it **overwrites** the contents of `data.csv`.

---

## Main Menu
```
------------ MAIN MENU ------------
Current selection: mealplanner>
1. Change selection
2. List all weeks
3. Add week
4. Remove week
0. Exit
Enter choice:
```

Menu options change depending on context (the path shown by `Current selection`):
- **At the root level**: `2. List all weeks`, `3. Add week`, `4. Remove week`
- **At week level**: `2. View current week`, `3. Get shopping list for the current week`
- **At day level**: `2. View current day`, `3. Add meal`, `4. Remove meal`
- **At meal level**: `2. View current meal`, `3. Add ingredient`, `4. Remove ingredient`
- **At ingredient level**: `2. View current ingredient`, `3. Change ingredient quantity`

---

## Change Selection Menu

When `1. Change selection` is selected from the main menu, the program enters
"change selection" mode, which allows you to navigate across levels of 
the program (Week -> Day -> Meal -> Ingredient). All inputs are case-insensitive.

Navigation inputs:

- **At the root level**: enter a valid week anchor date of the form YYYY-MM-DD to select a week
- **At the week level**: enter a valid day of the week (e.g. Monday) to select a day
  - Three-letter abbreviations (e.g. mon) and indices (e.g. 1) are also accepted, where index 0 is Sunday
- **At the day level**: enter a valid meal name to select a meal
- **At the meal level**: enter a valid ingredient name to select an ingredient

Additional commands:

- `help`, `h`, `commands`, `command list`, `list commands` - print a list of available commands
- `clear`, `c`, `clr` - clear the current selection
- `back`, `b` - go back one level (e.g. from Meal to Day)
- `main`, `quit`, `0`, `exit` - return to the main menu (exit "change selection" mode)

---

## File Structure

The program consists of 7 files:

- `MealPlannerApp.java` - CLI: menus, navigation, prompts
- `MealPlanner.java` - core functions: CSV load/write, add/remove/sort/print elements, print shopping list
- `Week.java` - week anchor date, 7 days; get anchor date/day/ingredients, get nutrient averages
- `Day.java` - list of meals; add/remove/get meals, get nutrient totals
- `Meal.java` - name, list of ingredients; add/remove/get ingredients, get nutrient totals
- `Ingredient.java` - name, quantity (g), optional nutrient profile; get nutrient densities and totals
- `DataValidator.java` - utility class: string/double/line/date validation

---

## CSV Formatting

Place one CSV file with the name `data.csv` in the project directory. The file must contain the following header:
```
WeekAnchorDate,DayOfWeek,MealName,IngredientName,Quantity,CarbsPer100g,FatPer100g,ProteinPer100g
```

The CSV file is not intended for manual editing, although data validation has been implemented in the event that this should occur. On startup, the program loads `data.csv`, and overwrites it with the current state upon exit.

To use a CSV file with a different name, update the value of `FILE_NAME` in `MealPlannerApp.java`. 

## Sample Output
```
------------ MAIN MENU ------------
Current selection: mealplanner> 
1. Change selection
2. List all weeks
3. Add week
4. Remove week
0. Exit
Enter choice: 3
Enter the anchor date of the week (Sunday) in the form YYYY-MM-DD: 2025-09-14
[SUCCESS] Added new week.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14> 
1. Change selection
2. View current week
3. Get shopping list for the current week
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14> help

------------ COMMANDS ------------
clear: Clears the current selection
back: Goes back one level
main: Returns to the main menu
mealplanner>2025-09-14> monday
mealplanner>2025-09-14>Monday> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday> 
1. Change selection
2. View current day
3. Add meal
4. Remove meal
0. Exit
Enter choice: 3
Enter the name of the meal: Hamburgers
[SUCCESS] Added new meal.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>
1. Change selection
2. View current meal
3. Add ingredient
4. Remove ingredient
0. Exit
Enter choice: 3
Enter the name of the ingredient: Hamburger Buns
Enter the quantity (in grams): 150.0
Do you want to include a nutritional profile (Y/N)? y
Enter the amount of carbohydrates per 100 grams (in grams): 50.0
Enter the amount of fat per 100 grams (in grams): 4.5
Enter the amount of protein per 100 grams (in grams): 9
[SUCCESS] Added new ingredient.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>Hamburger Buns>
1. Change selection
2. View current ingredient
3. Change ingredient quantity
0. Exit
Enter choice: 2

Hamburger Buns (150.00 g)
        75.00 g carbohydrates
        6.75 g fat
        13.50 g protein
        414.75 kcal

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>Hamburger Buns>
1. Change selection
2. View current ingredient
3. Change ingredient quantity
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14>Monday>Hamburgers>Hamburger Buns> back
mealplanner>2025-09-14>Monday>Hamburgers> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>
1. Change selection
2. View current meal
3. Add ingredient
4. Remove ingredient
0. Exit
Enter choice: 3
Enter the name of the ingredient: Burgers
Enter the quantity (in grams): 50
Do you want to include a nutritional profile (Y/N)? y
Enter the amount of carbohydrates per 100 grams (in grams): 0
Enter the amount of fat per 100 grams (in grams): 17
Enter the amount of protein per 100 grams (in grams): 26.5
[SUCCESS] Added new ingredient.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>Burgers>
1. Change selection
2. View current ingredient
3. Change ingredient quantity
0. Exit
Enter choice: 3
Enter new quantity: 75
[SUCCESS] Changed ingredient quantity.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>Burgers>
1. Change selection
2. View current ingredient
3. Change ingredient quantity
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14>Monday>Hamburgers>Burgers> back
mealplanner>2025-09-14>Monday>Hamburgers> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>
1. Change selection
2. View current meal
3. Add ingredient
4. Remove ingredient
0. Exit
Enter choice: 2

Hamburgers (609.00 kcal)
        Ingredients:
        - Hamburger Buns (150.00 g)
        - Burgers (75.00 g)

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Hamburgers>
1. Change selection
2. View current meal
3. Add ingredient
4. Remove ingredient
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14>Monday>Hamburgers> b
mealplanner>2025-09-14>Monday> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>
1. Change selection
2. View current day
3. Add meal
4. Remove meal
0. Exit
Enter choice: 2

Monday (week of September 14, 2025)
609.00 kcal | 75.00 g carbs | 19.50 g fat | 33.38 g protein

        Hamburgers (609.00 kcal, 75.00 g carbs, 19.50 g fat, 33.38 g protein)
                - Hamburger Buns (150.00 g)
                - Burgers (75.00 g)

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>
1. Change selection
2. View current day
3. Add meal
4. Remove meal
0. Exit
Enter choice: 3
Enter the name of the meal: Pancakes
[SUCCESS] Added new meal.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Pancakes>
1. Change selection
2. View current meal
3. Add ingredient
4. Remove ingredient
0. Exit
Enter choice: 3
Enter the name of the ingredient: Syrup
Enter the quantity (in grams): 0
Do you want to include a nutritional profile (Y/N)? n
[ERROR] Ingredient quantity cannot be zero or negative.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Pancakes>
1. Change selection
2. View current meal
3. Add ingredient
4. Remove ingredient
0. Exit
Enter choice: 3
Enter the name of the ingredient: Syrup
Enter the quantity (in grams): 5
Do you want to include a nutritional profile (Y/N)? n
[SUCCESS] Added new ingredient.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>Pancakes>Syrup>
1. Change selection
2. View current ingredient
3. Change ingredient quantity
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14>Monday>Pancakes>Syrup> b
mealplanner>2025-09-14>Monday>Pancakes> b
mealplanner>2025-09-14>Monday> b
mealplanner>2025-09-14> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>
1. Change selection
2. View current week
3. Get shopping list for the current week
0. Exit
Enter choice: 2

Week of September 14, 2025
Sunday          |Monday          |Tuesday         |Wednesday       |Thursday        |Friday          |Saturday        |
-----------------------------------------------------------------------------------------------------------------------
                |Hamburgers      |                |                |                |                |                |
                |609.00 kcal     |                |                |                |                |                |
                |                |                |                |                |                |                |
                |Pancakes        |                |                |                |                |                |
                |0.00 kcal       |                |                |                |                |                |
                |                |                |                |                |                |                |
-----------------------------------------------------------------------------------------------------------------------
Average daily Calorie intake: 87.00 kcal
Average daily carbohydrate consumption: 10.71 g
Average daily fat consumption: 2.79 g
Average daily protein consumption: 4.77 g

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14> 
1. Change selection
2. View current week
3. Get shopping list for the current week
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14> mon
mealplanner>2025-09-14>Monday> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>
1. Change selection
2. View current day
3. Add meal
4. Remove meal
0. Exit
Enter choice: 4
Enter the name of the meal: Hotdogs
[ERROR] Meal does not exist.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>
1. Change selection
2. View current day
3. Add meal
4. Remove meal
0. Exit
Enter choice: 4
Enter the name of the meal: Pancakes
[SUCCESS] Removed meal.

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>Monday>
1. Change selection
2. View current day
3. Add meal
4. Remove meal
0. Exit
Enter choice: 1

------------ CHANGE SELECTION ------------
Enter 'help' to view a list of commands
mealplanner>2025-09-14>Monday> back
mealplanner>2025-09-14> main

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>
1. Change selection
2. View current week
3. Get shopping list for the current week
0. Exit
Enter choice: 2

Week of September 14, 2025
Sunday          |Monday          |Tuesday         |Wednesday       |Thursday        |Friday          |Saturday        |
-----------------------------------------------------------------------------------------------------------------------
                |Hamburgers      |                |                |                |                |                |
                |609.00 kcal     |                |                |                |                |                |
                |                |                |                |                |                |                |
-----------------------------------------------------------------------------------------------------------------------
Average daily Calorie intake: 87.00 kcal
Average daily carbohydrate consumption: 10.71 g
Average daily fat consumption: 2.79 g
Average daily protein consumption: 4.77 g

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>
1. Change selection
2. View current week
3. Get shopping list for the current week
0. Exit
Enter choice: 3

Shopping list for the week of September 14, 2025:
        - Burgers (75.00 g)
        - Hamburger Buns (150.00 g)

------------ MAIN MENU ------------
Current selection: mealplanner>2025-09-14>
1. Change selection
2. View current week
3. Get shopping list for the current week
0. Exit
Enter choice: 0
```

---

## License

All code is licensed under the MIT license.

---

## Author

Michel Préjet
Computer Science Student, University of Manitoba
[prejetm@myumanitoba.ca](mailto:prejetm@myumanitoba.ca)

_Last updated: September 7, 2025_


