/**
 * Lab 3: An inventory control program that implements stacks, queues, and iterable lists in order to allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date March 15th, 2024
 * @version 1.2
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main implements InventoryMenu {

    public static void main(String[] args) {

        showHeader(3, "Lists", "TV Inventory Control Program");

        boolean loop = true;

        // initiates stack of TV objects by calling static method to open file
        Stack<TV> tvStack = openTVFile();

        // initiates CustomerData object by calling static method to open file
        CustomerData customerDataList = openCustFile();

        // initiates queue of customer objects
        Queue<Customer> customerQueue = new LinkedList<>();

        //  counter for something secret
        int extraCredit = 0;

        // keeps track of number of items in stack for purpose of creating new IDs
        int highest_ID = tvStack.size() - 1;

        // main loop
        while (loop) {
            switch (displayMainMenu()) {
                case InventoryMenu.STOCK_SHELVES -> {
                    if (tvStack.size() >= InventoryMenu.STOCKING_QUANTITY) {

                        System.out.printf("The following TV's have been placed on the floor for sale:%n");

                        // pops specific number of TV's off top of stack to be stocked on floor (five, in this assignment)
                        for (int i = 0; i < InventoryMenu.STOCKING_QUANTITY; i++) {
                            System.out.println(tvStack.pop());
                        }

                        System.out.printf("There are %d TV's left in inventory%n%n", tvStack.size());

                    } else {
                        System.out.printf("Not enough TVs in inventory!%n%n");
                    }
                }
                case InventoryMenu.FILL_ORDER -> {
                    if (tvStack.size() > 0) {

                        System.out.printf("%nThe following TV's have been shipped:%n");
                        System.out.println(tvStack.pop());
                        System.out.printf("There are %d TV's left in inventory%n%n", tvStack.size());

                    } else {
                        System.out.printf("%nNot enough TVs in inventory!%n%n");
                    }
                }
                case InventoryMenu.RESTOCK_RETURN -> {
                    // creates a TV with a new ID using the number of TV's in the stack +1
                    highest_ID += 1;
                    tvStack.push(new TV("ABC123-" + highest_ID));
                    System.out.printf("Returned TV added to inventory%n");
                    System.out.printf("The following %d TV's are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
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
                        tvStack.push(new TV("ABC123-" + highest_ID));
                    }
                    System.out.printf("The following %d TV's are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
                        System.out.print(tv);
                        System.out.println();
                    }
                    System.out.println();
                }

                case InventoryMenu.CUSTOMER_PURCHASE -> {
                    if (tvStack.size() > 0) {

                        customerDataList.displayList();

                        Scanner input = new Scanner(System.in);
                        System.out.print("Please enter account number or 'NONE': ");
                        String accountNumberInput = input.nextLine();

                        if(accountNumberInput.equalsIgnoreCase("none")){
                            System.out.println("*** ADD CUSTOMER ***");
                            customerDataList.addCustomer();
                            extraCredit++;
                        } else if (customerDataList.findCustomer(accountNumberInput)==null){
                            System.out.println("*** NO MATCH FOUND - ADD CUSTOMER ***");
                            customerDataList.addCustomer(accountNumberInput);
                            extraCredit++;
                        }

                        Customer customer = customerDataList.findCustomer(accountNumberInput);

                        if(customer!=null) {
                            boolean purchase_loop = true;
                            int quantity = 0;

                            while (purchase_loop) {

                                System.out.print("Please enter the number of TV's purchased: ");

                                try {
                                    quantity = Integer.parseInt(input.nextLine());

                                    if (quantity > tvStack.size()) {
                                        System.out.print("Error - Not enough TV's left in inventory!");
                                        System.out.printf("%nTV's left in inventory: %d. You purchased: %d. Try again.%n", tvStack.size(), quantity);
                                    } else if (quantity > 0) {
                                        purchase_loop = false;
                                    } else {
                                        throw new NumberFormatException();
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.printf("Error - Input must be positive integer! Try again.%n");

                                }

                            }

                            if(customer.getId_purchased()==null) {
                                customer.setId_purchased(new ArrayList<>());
                                customerQueue.add(customer);
                            }
                            System.out.printf("%nCustomer %s purchased the following TV's:%n", customer.getName());

                            for (int i = 0; i < quantity; i++) {
                                System.out.println(tvStack.peek());
                                customer.setNumber_purchased(customer.getNumber_purchased() + 1);
                                customer.getId_purchased().add(tvStack.pop());
                            }

                            System.out.printf("There are %d TV's left in inventory.%n%n", tvStack.size());
                        }
                    } else {
                        System.out.printf("There are no TV's left in inventory!%n%n");
                    }

                }

                case InventoryMenu.CUSTOMER_CHECKOUT -> {
                    if (customerQueue.isEmpty()) {
                        System.out.printf("There are no customers left to check out!%n%n");
                    } else {
                        Customer current_customer = customerQueue.poll();
                        System.out.printf(current_customer.toString());

                        System.out.printf("%n%nThere are %d customers left to check out.%n%n", customerQueue.size());

                    }

                }

                case InventoryMenu.CUSTOMER_UPDATE -> {

                    boolean innerLoop = true;
                    int changeCount = 0;

                    while(innerLoop){

                        switch(displayCustomerMenu()){

                            case InventoryMenu.ADD_CUSTOMER -> {
                                System.out.println("*** ADD CUSTOMER ***");
                                customerDataList.addCustomer();
                                changeCount++;
                            }

                            case InventoryMenu.DELETE_CUSTOMER -> {
                                System.out.println("*** DELETE CUSTOMER ***");
                                customerDataList.removeCustomer();
                                changeCount++;
                            }

                            case InventoryMenu.UPDATE_CUSTOMER -> {
                                System.out.println("*** UPDATE CUSTOMER ***");
                                customerDataList.updateName();
                                changeCount++;
                            }

                            case InventoryMenu.SAVE_TO_FILE -> {

                                Scanner input = new Scanner(System.in);

                                System.out.println("*** SAVE CUSTOMER DATA ***");
                                System.out.print("Enter file name (w/ .txt): ");
                                String newName = input.nextLine();
                                System.out.println("Saving output file...");

                                saveCustFile(customerDataList, newName);

                                extraCredit = 0;
                                changeCount = 0;
                            }

                            case InventoryMenu.DISPLAY_LIST -> {
                                customerDataList.displayList();
                            }
                            case InventoryMenu.RETURN_TO_MAIN -> {

                                if (changeCount > 0) {

                                    System.out.print("Unsaved changes to customer file. Exit without saving? Y/N: ");

                                    boolean yesNoLoop = true;

                                    while (yesNoLoop) {

                                        Scanner input = new Scanner(System.in);
                                        String response = input.nextLine();

                                        if (response.equalsIgnoreCase("y")) {
                                            System.out.println();
                                            yesNoLoop = false;
                                        } else if (response.equalsIgnoreCase("n")) {
                                            System.out.printf("Changes not saved.%n%n");
                                            yesNoLoop = false;
                                            innerLoop = false;
                                        } else {
                                            System.out.print("Invalid input. Exit without saving? Y/N: ");
                                        }

                                    }

                                } else {
                                    innerLoop = false;
                                }

                            }

                        }
                    }

                }

                case InventoryMenu.DISPLAY_INVENTORY -> {
                    // uses the inherent toString() method to show TV's left in stack
                    System.out.printf("The following %d TV's are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
                        System.out.print(tv);
                        System.out.println();
                    }
                    System.out.println();
                }
                case InventoryMenu.END -> {

                    if (customerQueue.isEmpty()) {

                        loop = false;

                        if (extraCredit > 0) {

                            System.out.print("Unsaved changes to customer file. Exit without saving? Y/N: ");

                            boolean yesNoLoop = true;
                            while (yesNoLoop) {

                                Scanner input = new Scanner(System.in);
                                String response = input.nextLine();

                                if (response.equalsIgnoreCase("y")) {
                                    System.out.println();
                                    yesNoLoop = false;
                                    loop = true;
                                } else if (response.equalsIgnoreCase("n")) {
                                    System.out.printf("Changes not saved.%n%n");
                                    yesNoLoop = false;
                                } else {
                                    System.out.print("Invalid input. Save before leaving? Y/N: ");
                                }

                            }

                        }

                    } else {
                        System.out.printf("There are still %d customer(s) who have not checked out.%nPlease make sure all customers are processed before ending the program.%n%n", customerQueue.size());
                    }
                }
            }

        }

        System.out.println("Saving inventory file...");
        saveTVFile(tvStack);
        System.out.println("Thank you for using ACME inventory control.");

    }

    public static int displayMainMenu() throws NumberFormatException {

        System.out.printf("*** MAIN MENU ***" +
                "%n%d - Stock Shelves" +
                "%n%d - Fill Web Order" +
                "%n%d - Restock Return" +
                "%n%d - Restock Inventory" +
                "%n%d - Customer Update" +
                "%n%d - Customer Purchase" +
                "%n%d - Customer Checkout" +
                "%n%d - Display Inventory" +
                "%n%d - End Program",
                InventoryMenu.STOCK_SHELVES,
                InventoryMenu.FILL_ORDER,
                InventoryMenu.RESTOCK_RETURN,
                InventoryMenu.RESTOCK_INVENTORY,
                InventoryMenu.CUSTOMER_UPDATE,
                InventoryMenu.CUSTOMER_PURCHASE,
                InventoryMenu.CUSTOMER_CHECKOUT,
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

    public static int displayCustomerMenu() throws NumberFormatException {

        System.out.printf("*** CUSTOMER MENU ***" +
                        "%n%d - Add a Customer" +
                        "%n%d - Delete a Customer" +
                        "%n%d - Update Customer Name" +
                        "%n%d - Save Customer Data to File" +
                        "%n%d - Display Customer List" +
                        "%n%d - Return to Main Menu",
                InventoryMenu.ADD_CUSTOMER,
                InventoryMenu.DELETE_CUSTOMER,
                InventoryMenu.UPDATE_CUSTOMER,
                InventoryMenu.SAVE_TO_FILE,
                InventoryMenu.DISPLAY_LIST,
                InventoryMenu.RETURN_TO_MAIN);

        Scanner input = new Scanner(System.in);
        int result = 0;
        boolean invalidInput = true;

        System.out.printf("%nPlease enter the menu choice: ");

        while (invalidInput) {

            // try/catch block that makes sure input can be parsed as an integer and is between menu values (in this assignment, 1 and 6)
            try {
                result = Integer.parseInt(input.nextLine());

                if (result >= InventoryMenu.ADD_CUSTOMER && result <= InventoryMenu.RETURN_TO_MAIN) {
                    invalidInput = false;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.printf("Input must be valid integer between %d and %d! Try again: ", InventoryMenu.ADD_CUSTOMER, InventoryMenu.RETURN_TO_MAIN);

            }

        }

        System.out.println();
        return result;

    }

    public static Stack<TV> openTVFile(){
        String filePath = "stack.txt";
        Path path = Paths.get(filePath);

        Stack<TV> stack = new Stack<>();

        // copies TV IDs from .txt file onto stack
        try (Scanner input_file = new Scanner(path)) {

            while (input_file.hasNext()) {

                TV nextTV = new TV(input_file.nextLine());
                stack.push(nextTV);

            }

        } catch (IOException error) {
            System.out.printf("Error: File '%s' not found in local directory!%n", filePath);
        }

        return stack;
    }
    public static void saveTVFile(Stack<TV> tvStack){
        String filePath = "stack.txt";
        Path path = Paths.get(filePath);

        // creates stringbuilder object as basis for output file
        StringBuilder output = new StringBuilder();

        // adds ID numbers of remaining TVs in stack to file
        for (TV tv : tvStack) {
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

    }


    public static CustomerData openCustFile(){
        String filePath = "CustFile.txt";
        Path path = Paths.get(filePath);

        CustomerData customerDataList = new CustomerData();

        LinkedList<Customer> list = customerDataList.getList();

        // creates customer objects based on using odd lines for names, even lines for account numbers
        try (Scanner input_file = new Scanner(path)) {

            while (input_file.hasNext()) {

                Customer customer = new Customer(input_file.nextLine(), input_file.nextLine());
                list.add(customer);

            }

        } catch (IOException error) {
            System.out.printf("Error: File '%s' not found in local directory!%n", filePath);
        }

        customerDataList.setList(list);

        return customerDataList;
    }

    public static void saveCustFile(CustomerData customerDataList, String filePath){

        Path path = Paths.get(filePath);

        // creates stringbuilder object as basis for output file
        StringBuilder output = new StringBuilder();

        // adds ID numbers of remaining TVs in stack to file
        for (Customer customer : customerDataList) {
            output.append(customer.getName());
            output.append("\n");
            output.append(customer.getAccount_number());
            output.append("\n");
        }

        // removes trailing newline character from file
        if (output.length() > 0 && output.charAt(output.length() - 1) == '\n') {
            output.deleteCharAt(output.length() - 1);
        }


        // saves output file
        try (PrintWriter outputFile = new PrintWriter(filePath)) {
            outputFile.print(output);
            System.out.printf("File saved.%n%n");
        } catch (IOException e) {
            System.out.println("Failed to write file!");
        }


    }

    public static void showHeader(int labNum, String labName, String labTitle) {

        System.out.printf("Lab %d: %s%n", labNum, labName);
        System.out.println("Copyright ©2024 - Howard Community College. All rights reserved; Unauthorized duplication prohibited");
        System.out.printf("CMSY 265 %s%n%n", labTitle);

    }

}