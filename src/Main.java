/**
 * Lab 1: An inventory control program that implements stacks to allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date February 15th, 2024
 * @version 1.0
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;

public class Main implements InventoryMenu {

    public static void main(String[] args) {

        showHeader(1, "Stacks", "TV Inventory Control Program");

        boolean loop = true;

        // initiates stack of TV objects
        Stack<TV> stack = new Stack<>();

        String filePath = "stack.txt";
        Path path = Paths.get(filePath);

        // copies TV IDs from .txt file onto stack
        try (Scanner input = new Scanner(path)) {

            while (input.hasNext()) {

                TV nextTV = new TV(input.nextLine());
                stack.push(nextTV);

            }

        } catch (IOException error) {
            System.out.printf("Error: File '%s' not found in local directory!", filePath);
            loop = false;
        }

        // keeps track of number of items in stack for purpose of creating new IDs
        int highest_ID = stack.size() - 1;

        TV addTV;

        // main loop
        while (loop) {
            switch (displayMenu()) {
                case InventoryMenu.STOCK_SHELVES -> {
                    if (stack.size() >= InventoryMenu.STOCKING_QUANTITY) {

                        System.out.printf("The following TV's have been placed on the floor for sale:%n");

                        // pops specific number of TV's off top of stack to be stocked on floor (five, in this assignment)
                        for (int i = 0; i < InventoryMenu.STOCKING_QUANTITY; i++) {
                            System.out.println(stack.pop());
                        }

                        System.out.printf("There are %d TV's left in inventory%n%n", stack.size());

                    } else {
                        System.out.printf("Not enough TVs in inventory!%n%n");
                    }
                }
                case InventoryMenu.FILL_ORDER -> {
                    if (stack.size() > 0) {

                        System.out.printf("%nThe following TV's have been shipped:%n");
                        System.out.println(stack.pop());
                        System.out.printf("There are %d TV's left in inventory%n%n", stack.size());

                    } else {
                        System.out.printf("%nNot enough TVs in inventory!%n%n");
                    }
                }
                case InventoryMenu.RESTOCK_RETURN -> {
                    // creates a TV with a new ID using the number of TV's in the stack +1
                    highest_ID += 1;
                    stack.push(new TV("ABC123-" + highest_ID));
                    System.out.printf("Returned TV added to inventory%n");
                    System.out.printf("The following %d TV's are left in inventory:%n", stack.size());
                    for (TV tv : stack) {
                        System.out.print(tv);
                        System.out.println();
                    }
                    System.out.println();
                }
                case InventoryMenu.RESTOCK_INVENTORY -> {
                    // creates specific number of TV's (five, in this assignment) with a new ID using the number of TV's in the stack +1 for each new ID
                    System.out.printf("%d TV's have been added to inventory%n", InventoryMenu.STOCKING_QUANTITY);
                    for (int i = 0; i < InventoryMenu.STOCKING_QUANTITY; i++) {
                        highest_ID += 1;
                        stack.push(new TV("ABC123-" + highest_ID));
                    }
                    System.out.printf("The following %d TV's are left in inventory:%n", stack.size());
                    for (TV tv : stack) {
                        System.out.print(tv);
                        System.out.println();
                    }
                    System.out.println();
                }
                case InventoryMenu.DISPLAY_INVENTORY -> {
                    // uses the inherent toString() method to show TV's left in stack
                    System.out.printf("The following %d TV's are left in inventory:%n", stack.size());
                    for (TV tv : stack) {
                        System.out.print(tv);
                        System.out.println();
                    }
                    System.out.println();
                }
                case InventoryMenu.END -> {
                    System.out.println("Saving output file...");

                    // creates string as basis for output file
                    StringBuilder output = new StringBuilder();

                    // adds ID numbers of remaining TVs in stack to file
                    for (TV tv : stack) {
                        output.append(tv.getId_number());
                        output.append("\n");
                    }

                    // removes trailing newline character from file
                    if (output.length() > 0 && output.charAt(output.length() - 1) == '\n') {
                        output.deleteCharAt(output.length() - 1);
                    }

                    // saves output file
                    try (PrintWriter outputFile = new PrintWriter(filePath)) {
                        outputFile.print(output);
                        System.out.println("File saved.");
                    } catch (IOException e) {
                        System.out.println("Failed to write file!");
                    }

                    System.out.println("Thank you for using ACME inventory control.");
                    loop = false;
                }
            }

        }


    }

    public static int displayMenu() throws NumberFormatException {

        System.out.printf("Menu Options" +
                        "%n%d - Stock Shelves" +
                        "%n%d - Fill Web Order" +
                        "%n%d - Restock Return" +
                        "%n%d - Restock Inventory" +
                        "%n%d - Display Inventory" +
                        "%n%d - End Program",
                InventoryMenu.STOCK_SHELVES,
                InventoryMenu.FILL_ORDER,
                InventoryMenu.RESTOCK_RETURN,
                InventoryMenu.RESTOCK_INVENTORY,
                InventoryMenu.DISPLAY_INVENTORY,
                InventoryMenu.END);

        Scanner input = new Scanner(System.in);
        int result = 0;
        boolean invalidInput = true;

        System.out.printf("%nPlease enter the menu choice: ");

        while (invalidInput) {

            // try/catch block that makes sure input can be parsed as an integer and is between menu values (in this assignment, 1 and 6)
            try {
                result = Integer.parseInt(input.nextLine());

                if (result >= InventoryMenu.STOCK_SHELVES && result <= InventoryMenu.END) {
                    invalidInput = false;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.printf("Input must be valid integer between %d and %d! Try again: ", InventoryMenu.STOCK_SHELVES, InventoryMenu.END);

            }

        }

        System.out.println();
        return result;

    }

    public static void showHeader(int labNum, String labName, String labTitle) {

        System.out.printf("Lab %d: %s%n", labNum, labName);
        System.out.println("Copyright ©2024 - Howard Community College. All rights reserved; Unauthorized duplication prohibited");
        System.out.printf("%nCMSY 265 %s%n%n", labTitle);

    }


}
