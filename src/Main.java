/**
 * Lab 4: An inventory control program that implements stacks, queues, iterable lists, and recursion in order to allow
 * the user
 * to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date March 18th, 2024
 * @version 1.3
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main implements InventoryMenu {

    //TODO add method that takes CustomerData list as parameter and returns it sorted. first, take list, then call
    // the .toArray function from CustomerData class. sort the array using a recursive method (insertion sort?) check
    // old assignments. convert back to list. add sorting function to appropriate menu items

    public static void main(String[] args) {

        showHeader(4, "Recursion", "TV Inventory Control Program");

        boolean loop = true;

        // initiates stack of TV objects by calling static method to open file
        Stack<TV> tvStack = openTVFile("stack.txt");

        // looks through IDs in TV stack to determine highest ID number as starting point for new IDs
        int highest_ID=0;
        for(TV tv: tvStack){
            // initializes integer of ID to be compared
            int compare_ID;
            // isolates substring in ID number following the "-" symbol, assumed to be an integer
            String currentID = tv.getId_number().substring(tv.getId_number().lastIndexOf("-") + 1);
            try{
                // attempts to parse substring as integer
                compare_ID = Integer.parseInt(currentID);
                // if successfully parsed AND number is higher than current highest, update highest ID number
                if(compare_ID>highest_ID){
                    highest_ID = compare_ID;
                }
            } catch (NumberFormatException e){
                // if unable to parse substring as integer, defaults highest ID number to number of TVs
                highest_ID = tvStack.size() - 1;
            }
        }

        // initiates CustomerData object by calling static method to open file
        CustomerData customerDataList = openCustFile("custfile.txt");

        // initiates queue of customer objects
        Queue<Customer> customerQueue = new LinkedList<>();

        //  flag for when a customer specific action is initiated from main menu
        int globalChangeCount = 0;

        // main loop
        while (loop) {
            switch (displayMainMenu()) {
                // STOCK_SHELVES case: stocks given number of TVs (i.e. 5) on shelves
                case InventoryMenu.STOCK_SHELVES -> {
                    if (tvStack.size() >= InventoryMenu.STOCKING_QUANTITY) {

                        System.out.printf("The following TVs have been placed on the floor for sale:%n");

                        // pops TVs off top of stack to be shelved
                        for (int i = 0; i < InventoryMenu.STOCKING_QUANTITY; i++) {
                            System.out.printf("\t" + tvStack.pop() + "%n");
                        }

                        System.out.printf("\tThere are %d TVs left in inventory.%n%n", tvStack.size());

                        // displays error if not enough TVs (i.e. 5) are available
                    } else {
                        System.out.printf("Not enough TVs in inventory!%n%n");
                    }
                }
                // FILL_ORDER case: pops 1 TV off stack
                case InventoryMenu.FILL_ORDER -> {
                    if (tvStack.size() > 0) {

                        System.out.printf("%nThe following TVs have been shipped:%n\t" + tvStack.pop() + "%n");
                        System.out.printf("\tThere are %d TVs left in inventory.%n%n", tvStack.size());

                        // displays error if not enough TVs (i.e. 1) are available
                    } else {
                        System.out.printf("%nNot enough TVs in inventory!%n%n");
                    }
                }
                // RESTOCK_RETURN case: adds 1 TV to inventory with new ID
                case InventoryMenu.RESTOCK_RETURN -> {
                    // creates a TV with a new ID using the number of TV's in the stack +1
                    highest_ID += 1;
                    tvStack.push(new TV("ABC123-" + highest_ID));
                    System.out.printf("Returned TV added to inventory.%n");
                    System.out.printf("The following %d TVs are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
                        System.out.printf("\t" + tv + "%n");
                    }
                    System.out.println();
                }
                // RESTOCK_INVENTORY case: adds given number of TVs (i.e. 5) to inventory
                case InventoryMenu.RESTOCK_INVENTORY -> {
                    System.out.printf("%d TVs have been added to inventory%n", InventoryMenu.STOCKING_QUANTITY);
                    for (int i = 0; i < InventoryMenu.STOCKING_QUANTITY; i++) {
                        // increments value of highest value from inventory file, ensuring no duplicate ID numbers
                        highest_ID += 1;
                        tvStack.push(new TV("ABC123-" + highest_ID));
                    }
                    System.out.printf("The following %d TVs are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
                        System.out.printf("\t" + tv + "%n");
                    }
                    System.out.println();
                }

                // CUSTOMER_PURCHASE case: allows selection (or creation) of customer account and allows TV purchase
                case InventoryMenu.CUSTOMER_PURCHASE -> {
                    if (tvStack.size() > 0) {

                        customerDataList.displayList();

                        Scanner input = new Scanner(System.in);
                        System.out.print("Please enter account number or 'NONE': ");
                        String accountNumberInput = input.nextLine();

                        if (accountNumberInput.equalsIgnoreCase("none")) {
                            System.out.println("*** ADD CUSTOMER ***");
                            accountNumberInput = customerDataList.addCustomer();
                            globalChangeCount++;
                        } else if (customerDataList.findCustomer(accountNumberInput) == null) {
                            System.out.println("*** NO MATCH FOUND - ADD CUSTOMER ***");
                            customerDataList.addCustomer(accountNumberInput);
                            globalChangeCount++;
                        }

                        Customer customer = customerDataList.findCustomer(accountNumberInput);

                        if (customer != null) {
                            boolean purchase_loop = true;
                            int quantity = 0;

                            while (purchase_loop) {

                                System.out.print("Please enter the number of TVs purchased: ");

                                try {
                                    quantity = Integer.parseInt(input.nextLine());

                                    if (quantity > tvStack.size()) {
                                        System.out.print("Error - Not enough TVs left in inventory!");
                                        System.out.printf("%nTVs left in inventory: %d. Your purchase: %d TVs. Try again.%n", tvStack.size(), quantity);
                                    } else if (quantity > 0) {
                                        purchase_loop = false;
                                    } else {
                                        throw new NumberFormatException();
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.printf("Error - Input must be positive integer! Try again.%n");

                                }

                            }

                            System.out.printf("%nCustomer %s purchased the following TVs:%n", customer.getName());

                            for (int i = 0; i < quantity; i++) {
                                System.out.printf("\t" + tvStack.peek() + "%n");
                                customer.setNumber_purchased(customer.getNumber_purchased() + 1);
                                customer.getId_purchased().add(tvStack.pop());
                            }

                            customerQueue.add(customer);
                            System.out.printf("\tThere are %d TVs left in inventory.%n%n", tvStack.size());
                        }
                    } else {
                        System.out.printf("There are no TVs left in inventory!%n%n");
                    }
                }

                case InventoryMenu.CUSTOMER_CHECKOUT -> {
                    if (customerQueue.isEmpty()) {
                        System.out.printf("There are no customers left to check out!%n%n");
                    } else {
                        System.out.printf(customerQueue.poll().toString());
                        System.out.printf("%n%nThere are %d customers left to check out.%n%n", customerQueue.size());
                    }
                }

                case InventoryMenu.CUSTOMER_UPDATE -> {

                    boolean innerLoop = true;
                    int localChangeCount = 0;

                    while (innerLoop) {

                        switch (displayCustomerMenu()) {

                            case InventoryMenu.ADD_CUSTOMER -> {
                                System.out.println("*** ADD CUSTOMER ***");
                                customerDataList.addCustomer();
                                localChangeCount++;
                            }

                            case InventoryMenu.DELETE_CUSTOMER -> {
                                System.out.println("*** DELETE CUSTOMER ***");
                                customerDataList.removeCustomer();
                                localChangeCount++;
                            }

                            case InventoryMenu.UPDATE_CUSTOMER -> {
                                System.out.println("*** UPDATE CUSTOMER ***");
                                customerDataList.updateName();
                                localChangeCount++;
                            }

                            case InventoryMenu.SAVE_TO_FILE -> {

                                saveCustFile(customerDataList);

                                globalChangeCount = 0;
                                localChangeCount = 0;
                            }

                            case InventoryMenu.DISPLAY_LIST -> {
                                customerDataList.displayList();
                            }
                            case InventoryMenu.RETURN_TO_MAIN -> {

                                if (localChangeCount > 0) {

                                    System.out.print("Unsaved changes to customer file. Exit without saving? Y/N: ");

                                    boolean yesNoLoop = true;

                                    while (yesNoLoop) {

                                        Scanner input = new Scanner(System.in);
                                        String response = input.nextLine();

                                        if (response.equalsIgnoreCase("n")) {
                                            System.out.println();
                                            yesNoLoop = false;
                                        } else if (response.equalsIgnoreCase("y")) {
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
                    System.out.printf("The following %d TVs are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
                        System.out.printf("\t" + tv + "%n");
                    }
                    System.out.println();
                }
                case InventoryMenu.END -> {

                    if (customerQueue.isEmpty()) {

                        loop = false;

                        if (globalChangeCount > 0) {

                            System.out.print("Unsaved changes to customer file. Exit without saving? Y/N: ");

                            boolean yesNoLoop = true;
                            while (yesNoLoop) {

                                Scanner input = new Scanner(System.in);
                                String response = input.nextLine();

                                if (response.equalsIgnoreCase("n")) {
                                    System.out.println();
                                    yesNoLoop = false;
                                    loop = true;
                                } else if (response.equalsIgnoreCase("y")) {
                                    System.out.printf("Changes not saved.%n%n");
                                    yesNoLoop = false;
                                } else {
                                    System.out.print("Invalid input. Exit without saving? Y/N: ");
                                }

                            }

                        }

                    } else {
                        System.out.printf("There are still %d customer(s) who have not checked out." +
                                "%nPlease make sure all customers are processed before ending the program.%n%n", customerQueue.size());
                    }
                }
            }

        }

        System.out.println("Saving inventory file...");
        saveTVFile(tvStack, "stack.txt");
        System.out.println("Thank you for using ACME inventory control.");

    }

    // method to display main menu, validate selection, and return selection as integer
    public static int displayMainMenu() throws NumberFormatException {

        //@formatter:off
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
        //@formatter:on

        Scanner input = new Scanner(System.in);
        int result = 0;
        boolean invalidInput = true;

        System.out.printf("%nPlease enter the menu choice: ");

        while (invalidInput) {
            // try/catch block that makes sure input can be parsed as an integer
            try {
                result = Integer.parseInt(input.nextLine());
                // after confirming that input can be parsed as integer, checks to see if it falls between 1st and
                // last menu choices
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

    // method to display customer menu, validate selection, and return selection as integer
    public static int displayCustomerMenu() throws NumberFormatException {

        // @formatter:off
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
        // @formatter:on

        Scanner input = new Scanner(System.in);
        int result = 0;
        boolean invalidInput = true;

        System.out.printf("%nPlease enter the menu choice: ");

        while (invalidInput) {
            // try/catch block that makes sure input can be parsed as an integer
            try {
                result = Integer.parseInt(input.nextLine());
                // after confirming that input can be parsed as integer, checks to see if it falls between 1st and
                // last menu choices
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

    // method to open TV inventory file
    public static Stack<TV> openTVFile(String filePath) {
        Path path = Paths.get(filePath);
        Stack<TV> stack = new Stack<>();
        // copies TV IDs from .txt file onto stack
        try (Scanner input_file = new Scanner(path)) {
            // takes each line of input file and stores it as a separate TV
            while (input_file.hasNext()) {
                TV nextTV = new TV(input_file.nextLine());
                stack.push(nextTV);
            }
        } catch (IOException error) {
            System.out.printf("Error: File '%s' not found in local directory!%n", filePath);
        }
        return stack;
    }

    // method to save TV inventory file
    public static void saveTVFile(Stack<TV> tvStack, String filePath) {

        // creates stringbuilder object as basis for output file
        StringBuilder output = new StringBuilder();

        // adds ID numbers of remaining TVs in stack to file, plus newline character
        for (TV tv : tvStack) {
            output.append(tv.getId_number());
            output.append("\n");
        }
        // checks if last character is a newline and removes if necessary
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

    // method to open customer file
    public static CustomerData openCustFile(String filePath) {
        Path path = Paths.get(filePath);
        // initiates new CustomerData instance
        CustomerData customerDataList = new CustomerData();
        // pull list object from CustomerData instance
        LinkedList<Customer> list = customerDataList.getList();

        // reads through input file, interpreting odd lines as customer names and even lines as account numbers
        try (Scanner input_file = new Scanner(path)) {
            // creates customer objects using name and account numbers and adds to list
            while (input_file.hasNext()) {
                Customer customer = new Customer(input_file.nextLine(), input_file.nextLine());
                list.add(customer);
            }
        } catch (IOException error) {
            System.out.printf("Error: File '%s' not found in local directory!%n", filePath);
        }
        // adds newly created list back to CustomerData instance
        customerDataList.setList(list);
        return customerDataList;
    }

    // method to save customer file
    public static void saveCustFile(CustomerData customerDataList) {

        // creates stringbuilder object as basis for output file
        StringBuilder output = new StringBuilder();

        // adds customer names and account numbers to file, plus newline character
        for (Customer customer : customerDataList) {
            output.append(customer.getName());
            output.append("\n");
            output.append(customer.getAccount_number());
            output.append("\n");
        }
        // checks if last character is a newline and removes if necessary
        if (output.length() > 0 && output.charAt(output.length() - 1) == '\n') {
            output.deleteCharAt(output.length() - 1);
        }

        // prints save data mesasge
        System.out.println("*** SAVE CUSTOMER DATA ***");
        System.out.print("Enter file name (w/ .txt): ");

        // initiates Scanner and sentinel variable for validation loop
        Scanner input = new Scanner(System.in);
        boolean loop = true;

        // validation loop
        while(loop) {

            String filePath = input.nextLine();
            // if file path does not end in ".txt", does not exit loop
            if(filePath.lastIndexOf(".txt")!=filePath.length()-4) {
                System.out.print("Filename must end in .txt! Try again: ");
            } else {
                // if filename DOES end in ".txt", attempts to save file
                try (PrintWriter outputFile = new PrintWriter(filePath)) {
                    outputFile.print(output);
                    // if file write is successful, exit loop
                    loop = false;
                    System.out.printf("File saved.%n%n");
                } catch (IOException e) {
                    // if file write throws an error, does not exit loop
                    System.out.print("Failed to write file! Try again: ");
                }
            }
        }
    }

    // static method for displaying lab title and disclaimer at beginning of output
    public static void showHeader(int labNum, String labName, String labTitle) {

        System.out.printf("Lab %d: %s%n", labNum, labName);
        System.out.println("Copyright ©2024 - Howard Community College. All rights reserved; Unauthorized duplication prohibited");
        System.out.printf("CMSY 265 %s%n%n", labTitle);

    }

}