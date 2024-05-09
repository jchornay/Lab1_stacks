/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main implements InventoryMenu {
    public static void main(String[] args) {

        showHeader(7, "Sets and Maps", "TV Inventory Control Program");

        boolean loop = true;

        // initiates stack of TV objects by calling static method to open file
        Stack<TV> tvStack = openTVFile("stack.txt");

        // looks through IDs in TV stack to determine highest ID number as starting point for new IDs
        int highest_ID = 0;
        for (TV tv : tvStack) {
            // initializes integer of ID to be compared
            int compare_ID;
            // isolates substring in ID number following the "-" symbol, assumed to be an integer
            String currentID = tv.getId_number().substring(tv.getId_number().lastIndexOf("-") + 1);
            try {
                // attempts to parse substring as integer
                compare_ID = Integer.parseInt(currentID);
                // if successfully parsed AND number is higher than current highest, update highest ID number
                if (compare_ID > highest_ID) {
                    highest_ID = compare_ID;
                }
            } catch (NumberFormatException e) {
                // if unable to parse substring as integer, defaults highest ID number to number of TVs
                highest_ID = tvStack.size() - 1;
            }
        }

        // TODO: prompt user for filepath in case it varies from custfile.txt
        // initiates CustomerData object by calling static method to open file
        CustomerData customerDataList = openCustFile("custfile.txt");
        // sorts list in ascending order
        customerDataList.setList(insertSortHelper(customerDataList));

        // initiates queue of customer objects
        Queue<Customer> customerQueue = new LinkedList<>();
        // initiates binary tree
        BinaryTree tree = null;


        boolean firstPurchase = true;


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


                    // initiates BinaryTree of TVType objects by calling static method to open file
                    if(firstPurchase){
                        tree = openTVTypeFile();
                        firstPurchase = false;
                    }

                    if (tvStack.size() > 0) {

                        System.out.println();
                        customerDataList.displayList();

                        Scanner input = new Scanner(System.in);
                        System.out.print("Please enter account number or 'NONE': ");
                        String accountNumberInput = input.nextLine();

                        // allows user to create new customer upon entering NONE
                        if (accountNumberInput.equalsIgnoreCase("none")) {
                            System.out.println("*** ADD CUSTOMER ***");
                            accountNumberInput = customerDataList.addCustomer();
                            // sorts list in ascending order following edit
                            customerDataList.setList(insertSortHelper(customerDataList));
                            // requires list to be saved to file before proceeding
                            saveCustFile(customerDataList);
                        } else if (customerDataList.findCustomer(accountNumberInput) == null) {
                            System.out.println("*** NO MATCH FOUND - ADD CUSTOMER ***");
                            customerDataList.addCustomer(accountNumberInput);
                            // sorts list in ascending order following edit
                            customerDataList.setList(insertSortHelper(customerDataList));
                            // requires list to be saved to file before proceeding
                            saveCustFile(customerDataList);
                        }

                        // pulls customer from data list using account number as search term
                        Customer customer = customerDataList.findCustomer(accountNumberInput);
                        TVType result = null;
                        int quantity = 0;

                        if (customer != null) {

                            System.out.printf("Customer is: %s", customer.getName());
                            System.out.println();

                            boolean purchase_loop = true;
                            while (purchase_loop) {

                                // formatted menu
                                System.out.println();
                                System.out.println("TV Options:");
                                System.out.printf("%-8s%-18s%-18s%s%n", "Item", "Brand", "Model", "Cost");
                                System.out.printf("%-8s%-18s%-18s%s%n", "----", "-----", "-----", "----");

                                // traverses Binary Tree of TVTypes in order (cheapest to most expensive) and prints
                                // formatted details
                                tree.helperTraverseInOrder();

                                // validation loop to make sure user chooses existing brand/model combination
                                boolean inner_purchase_loop = true;
                                while (inner_purchase_loop) {
                                    System.out.print("Please enter in the brand: ");
                                    String brand = input.nextLine();
                                    System.out.print("Please enter in the model: ");
                                    String model = input.nextLine();

                                    // uses recursive search to see if exact brand/model exists in binary tree
                                    result = tree.helperRecursiveSearch(brand, model);

                                    // if not found, repeat loop until found
                                    if (result == null) {
                                        System.out.print("TV not found, try again! ");
                                        // if found, exit loop
                                    } else {
                                        customer.setTvType(result);
                                        inner_purchase_loop = false;
                                    }
                                }

                                // prints formatted summary of purchase
                                System.out.printf("%-26s%s%n", "Item", "Cost");
                                System.out.printf("%-26s%s%n", "-------------------", "----");
                                System.out.printf("%-26s%s%n",
                                        String.format("%s - %s", result.getBrand(), result.getModel()),
                                        String.format("$%-4.2f", result.getPrice()));

                                // validation loop to make sure user purchases valid quantity
                                boolean inner_purchase_loop_2 = true;
                                while (inner_purchase_loop_2) {

                                    System.out.print("Please enter the number of TVs purchased: ");

                                    try {
                                        quantity = Integer.parseInt(input.nextLine());

                                        if (quantity > tvStack.size()) {
                                            System.out.print("Error - Not enough TVs left in inventory!");
                                            System.out.printf("%nTVs left in inventory: %d. You purchased: %d. Try " +
                                                    "again.%n", tvStack.size(), quantity);
                                        } else if (quantity > 0) {
                                            inner_purchase_loop_2 = false;
                                            purchase_loop = false;
                                        } else {
                                            throw new NumberFormatException();
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.printf("Error - Input must be positive integer! Try again.%n");
                                    }
                                }
                            }

                            System.out.printf("%nCustomer %s purchased the following TVs:%n", customer.getName());

                            // prints formatted summary of purchase
                            for (int i = 0; i < quantity; i++) {
                                System.out.printf("\t" + tvStack.peek() + "%n");
                                customer.setNumber_purchased(customer.getNumber_purchased() + 1);
                                TV tv = tvStack.pop();
                                tv.setTvType(result);
                                customer.getId_purchased().add(tv);
                            }

                            boolean deliveryLoop = true;
                            while(deliveryLoop) {
                                System.out.print("Does the customer want the TVs delivered? y/n: ");
                                String response = input.nextLine();
                                if (response.equalsIgnoreCase("y")) {
                                    System.out.print("Please enter customer address: ");
                                    appendDelInfoFile(customer.getName(), input.nextLine(),
                                            customer.getAccount_number(),
                                            customer.getNumber_purchased());
                                    deliveryLoop = false;
                                } else if(response.equalsIgnoreCase("n")){
                                    deliveryLoop = false;
                                } else {
                                    System.out.println("Please type 'y' or 'n': ");
                                }
                            }

                            // adds customer to checkout queue if not already present (this check allows for more than
                            // one transaction per customer)
                            if (!customerQueue.contains(customer)) {
                                customerQueue.add(customer);
                            }
                            System.out.printf("\tThere are %d TVs left in inventory.%n%n", tvStack.size());
                        }
                    }
                    else {
                        System.out.printf("%nThere are no TVs left in inventory!%n%n");
                    }
                }

                // CUSTOMER_CHECKOUT case: allows customers to be checked out one at a time
                case InventoryMenu.CUSTOMER_CHECKOUT -> {
                    if (customerQueue.isEmpty()) {
                        System.out.printf("There are no customers left to check out!%n%n");
                    } else {
                        System.out.printf(customerQueue.poll().toString());
                        System.out.printf("%n%nThere are %d customers left to check out.%n%n", customerQueue.size());
                    }
                }

                // CUSTOMER_UPDATE case: allows customer edit menu to be accessed
                case InventoryMenu.CUSTOMER_UPDATE -> {

                    boolean innerLoop = true;
                    int localChangeCount = 0;

                    while (innerLoop) {

                        switch (displayCustomerMenu()) {

                            case InventoryMenu.ADD_CUSTOMER -> {
                                System.out.println("*** ADD CUSTOMER ***");
                                customerDataList.addCustomer();
                                // sorts list in ascending order following edit
                                customerDataList.setList(insertSortHelper(customerDataList));
                                localChangeCount++;
                            }

                            case InventoryMenu.DELETE_CUSTOMER -> {
                                System.out.println("*** DELETE CUSTOMER ***");
                                customerDataList.removeCustomer();
                                // sorts list in ascending order following edit
                                customerDataList.setList(insertSortHelper(customerDataList));
                                localChangeCount++;
                            }

                            case InventoryMenu.UPDATE_CUSTOMER -> {
                                System.out.println("*** UPDATE CUSTOMER ***");
                                customerDataList.updateName();
                                // sorts list in ascending order following edit
                                customerDataList.setList(insertSortHelper(customerDataList));
                                localChangeCount++;
                            }

                            case InventoryMenu.SAVE_TO_FILE -> {

                                saveCustFile(customerDataList);
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

                case DISPLAY_DELIVERY -> {

                    MaxHeap delInfo = openDelInfoFile();

                    int i = 0;
                    System.out.println();
                    System.out.println("Delivery Report");
                    System.out.println("---------------");
                    while(delInfo.getCurrentSize()>0){
                        System.out.printf("Delivery Stop #%d:%n", ++i);
                        DelInfo display = delInfo.removeRoot();
                        System.out.println(display);
                    }

                    System.out.println();

                }

                // DISPLAY_INVENTORY case: displays formatted version of inventory
                case InventoryMenu.DISPLAY_INVENTORY -> {
                    // uses the inherent toString() method to show TV's left in stack
                    System.out.printf("%nThe following %d TVs are left in inventory:%n", tvStack.size());
                    for (TV tv : tvStack) {
                        System.out.printf("\t" + tv + "%n");
                    }
                    System.out.println();
                }

                // END case: checks that all customers have been checked out, then saves inventory file and ends program
                case InventoryMenu.END -> {

                    if (customerQueue.isEmpty()) {

                        loop = false;

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
                        "%n%d - Display Delivery List" +
                        "%n%d - Display Inventory" +
                        "%n%d - End Program",
                InventoryMenu.STOCK_SHELVES,
                InventoryMenu.FILL_ORDER,
                InventoryMenu.RESTOCK_RETURN,
                InventoryMenu.RESTOCK_INVENTORY,
                InventoryMenu.CUSTOMER_UPDATE,
                InventoryMenu.CUSTOMER_PURCHASE,
                InventoryMenu.CUSTOMER_CHECKOUT,
                InventoryMenu.DISPLAY_DELIVERY,
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

    // method to open TVType file
    public static BinaryTree openTVTypeFile() {

        BinaryTree tree = new BinaryTree();

        boolean invalidFile = true;
        while (invalidFile) {

            Scanner input = new Scanner(System.in);
            System.out.print("Please enter in the file path for TV type data (.txt): ");

            Path path = Paths.get(input.nextLine());

            try (Scanner input_file = new Scanner(path)) {
                // takes each line of input file and stores it as a separate TV
                while (input_file.hasNext()) {
                    TVType nextTVType = new TVType(input_file.nextLine(), input_file.nextLine(),
                            Double.valueOf(input_file.nextLine()));
                    tree.add(new Node(nextTVType));
                }
                invalidFile=false;
            } catch (IOException error) {
                System.out.printf("Error: File not found in local directory! Try again.%n");
            }
        }
        return tree;
    }

    // method to open DelInfo file
    public static MaxHeap openDelInfoFile() {

        MaxHeap heap = new MaxHeap(InventoryMenu.MAXIMUM_DELIVERIES);

        boolean invalidFile = true;
        while (invalidFile) {

            Scanner input = new Scanner(System.in);
            System.out.print("Please enter in the delivery info file path (.txt): ");

            Path path = Paths.get(input.nextLine());

            try (Scanner input_file = new Scanner(path)) {
                // takes each line of input file and stores it as a separate TV
                while (input_file.hasNext()) {
                    DelInfo delInfo = new DelInfo(input_file.nextLine(), input_file.nextLine(), input_file.nextLine()
                            , Integer.valueOf(input_file.nextLine()));
                    heap.insertNode(delInfo);
                }
                invalidFile=false;
            } catch (IOException error) {
                System.out.printf("Error: File not found in local directory! Try again.%n");
            }
        }

        heap.constructMaxHeap();
        return heap;
    }

    // method to append data to DelInfo file
    public static void appendDelInfoFile(String newName, String newAddress, String newAccount, int newNumPurchased) {

        // creates stringbuilder object as basis for output file
        StringBuilder output = new StringBuilder();
        String filePath = null;

        boolean invalidFile = true;
        while (invalidFile) {

            Scanner input = new Scanner(System.in);
            System.out.print("Please enter in the delivery info file path (.txt): ");

            filePath = input.nextLine();
            Path path = Paths.get(filePath);

            try (Scanner input_file = new Scanner(path)) {
                // takes each line of input file and appends it to file
                while (input_file.hasNext()) {
                    output.append(input_file.nextLine());
                    output.append("\n");
                }
                invalidFile=false;
            } catch (IOException error) {
                System.out.printf("Error: File not found in local directory! Try again.%n");
            }
        }

        output.append(newName);
        output.append("\n");
        output.append(newAddress);
        output.append("\n");
        output.append(newAccount);
        output.append("\n");
        output.append(newNumPurchased);

        try (PrintWriter outputFile = new PrintWriter(filePath)) {
            outputFile.print(output);
            System.out.printf("File saved.%n%n");
        } catch (IOException e) {
            // redundant catch block because same filepath is used in previous try/catch block for reading file, no
            // additional validation needed
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
        while (loop) {

            String filePath = input.nextLine();
            // if file path does not end in ".txt", does not exit loop
            if (filePath.lastIndexOf(".txt") != filePath.length() - 4) {
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

    /*
     * NOTE: the following code implements the INSERTION SORT algorithm as described by Tom Scott in his YouTube
     * video 'Why My Teenage Code Was Terrible: Sorting Algorithms And Big O Notation'
     *
     * URL: https://www.youtube.com/watch?v=RGuJga2Gl_k
     *
     * implementation is my own work, adapted from a previous recursion assignment
     */

    // helper method used by recursive sort method
    public static LinkedList<Customer> insertSortHelper(CustomerData customerData) {
        // converts list of customers into array
        Customer[] tempArray = customerData.toCustomerArray();
        // insertion sort algorithm, starting at index 0
        for (int startIndex = 0; startIndex < tempArray.length; startIndex += 1) {
            tempArray = insertSortRecursive(tempArray, startIndex);
        }
        // converts sorted array back to List and then returns as LinkedList
        List<Customer> sortedCustomerList = Arrays.asList(tempArray);
        return new LinkedList<>(sortedCustomerList);
    }

    // recursive insertion sort method
    public static Customer[] insertSortRecursive(Customer[] customers, int startIndex) {

        // for first run through list, makes no changes (otherwise the code would get an index out of bounds error
        // for trying to compare to an index of -1
        if (startIndex == 0) {
            return customers;
        }

        // starting at index 1, compares each account number String lexicographically to the account number of the
        // one before it
        else if (customers[startIndex].getAccount_number().compareTo(customers[startIndex - 1].getAccount_number()) < 0) {

            // if the current account is 'smaller' than the one before it (i.e. has a .compareTo() value<0), then
            // switch the position of the two accounts in the array
            Customer a = customers[startIndex];
            Customer b = customers[startIndex - 1];
            customers[startIndex - 1] = a;
            customers[startIndex] = b;

            // then decrement the startIndex, comparing the newly switched account to its new index-1 neighbor
            return insertSortRecursive(customers, startIndex - 1);
        }

        // base case, i.e. the recursive function has iterated through the entire list without finding an account out
        // of order
        else {
            return customers;
        }
    }

    // static method for displaying lab title and disclaimer at beginning of output
    public static void showHeader(int labNum, String labName, String labTitle) {

        System.out.printf("Lab %d: %s%n", labNum, labName);
        System.out.println("Copyright ©2024 - Howard Community College. All rights reserved; Unauthorized duplication prohibited");
        System.out.printf("CMSY 265 %s%n%n", labTitle);
    }
}