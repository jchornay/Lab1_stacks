/**
 * Lab 5: An inventory control program that implements stacks, queues, iterable lists, recursion, and trees in order to
 * allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.5
 * date April 11th, 2024
 */

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class CustomerData implements Iterable<Customer>, Serializable {
    // method to add new customer to list, returns account number as string
    public String addCustomer() {

        // initializes sentinel loop and other variables
        boolean loop = true;
        String name = null;
        String account = null;

        while (loop) {

            // takes two strings as input for account name and number
            Scanner input = new Scanner(System.in);
            System.out.print("New account name: ");
            name = input.nextLine();
            System.out.print("New account number: ");
            account = input.nextLine();

            // checks to see if customer already exists
            Customer customer = findCustomer(account);

            // if customer found (i.e. object returned is NOT null) displays account information and prompts user to
            // try again
            if (customer != null) {
                System.out.printf("Account %s already exists!%n", account);
                System.out.printf("*** ACCOUNT DETAILS ***" + "%n\tName: %s" + "%n\tNumber: %s%n%n", customer.getName(), customer.getAccount_number());
            } else {
                // exits loop once new account number is chosen
                loop = false;
            }

        }

        // creates a new customer object with name and account number
        Customer customer = new Customer(name, account);

        // confirms selection
        if (confirmSelection(customer)) {
            this.list.add(customer);
            System.out.printf("Account created.%n%n");
        } else {
            System.out.printf("Action cancelled.%n%n");
        }

        return account;
    }

    // method to add new customer to list IF ACCOUNT NUMBER IS PROVIDED BUT NAME DOES NOT EXIST
    public void addCustomer(String account) {

        // takes string for account name
        Scanner input = new Scanner(System.in);
        System.out.print("New account name: ");
        String name = input.nextLine();

        // creates a new customer object with name and account number
        Customer customer = new Customer(name, account);

        // confirms selection
        if (confirmSelection(customer)) {
            this.list.add(customer);
            System.out.printf("Account created.%n%n");
        } else {
            System.out.printf("Action cancelled.%n%n");
        }
    }


    // method to remove existing customer from list
    public void removeCustomer() {

        // initializes sentinel loop and customer variable
        boolean loop = true;
        Customer customer = null;

        while (loop) {

            // takes string as input for account number
            Scanner input = new Scanner(System.in);
            System.out.print("Account number to be deleted: ");

            // checks to see if customer already exists
            customer = findCustomer(input.nextLine());

            // if customer is not found, shows list of customers and does NOT exit loop, resulting in prompt for
            // account number again
            if (customer == null) {
                System.out.printf("Account does not exist!%n%n");
                this.displayList();
            // if customer is found, exits loop
            } else {
                loop = false;
            }
        }

        // confirms removal of customer
        if (confirmSelection(customer)) {
            this.list.remove(customer);
            System.out.printf("Account deleted.%n%n");
        } else {
            System.out.printf("Action cancelled.%n%n");
        }
    }

    // method to update existing customer's name
    public void updateName() {

        // initializes sentinel loop and customer variable
        boolean loop = true;
        Customer customer = null;
        Scanner input = new Scanner(System.in);

        while (loop) {

            // takes string as input for account number
            System.out.print("Account number to be updated: ");

            // checks to see if customer already exists
            customer = findCustomer(input.nextLine());

            // if customer is not found, shows list of customers and does NOT exit loop, resulting in prompt for
            // account number again
            if (customer == null) {
                System.out.printf("Account does not exist!%n%n");
                this.displayList();
            // if customer is found, exits loop
            } else {
                loop = false;
            }
        }

        // takes string as input for new name
        System.out.printf("%nExisting account name: %s" + "%nNew account name: ", customer.getName());
        String newName = input.nextLine();

        // confirms name replacement
        if (confirmSelection(customer)) {
            System.out.printf("Account %s name changed from %s to %s!%n%n", customer.getAccount_number(), customer.getName(), newName);
            customer.setName(newName);
        } else {
            System.out.printf("Action cancelled.%n%n");
        }

    }

    // method to find if account exists in list (returns null if unable to find account)
    public Customer findCustomer(String account) {

        // initializes customer object as null
        Customer result = null;
        // if customer with matching account number is found, updates object that customer
        for (Customer customer : this.list) {
            if (customer.getAccount_number().equals(account)) {
                result = customer;
            }
        }
        // returns either null or customer object
        return result;
    }

    // method to display list of customers
    public void displayList() {

        // index variable used to number list
        int index = 0;
        System.out.println("*** CUSTOMER LIST ***");
        // prints out formatted customer list
        for (Customer customer : this.list) {
            index++;
            System.out.printf("%s", "Customer");
            System.out.printf("%3d: ", index);
            System.out.printf("%-20s", customer.getName());
            System.out.printf("Account No.: %s%n", customer.getAccount_number());
        }
        System.out.println();
    }

    public Customer[] toCustomerArray(){
        // creates empty object and customer arrays
        Object[] objectArray = this.getList().toArray();
         Customer[] customerArray = new Customer[this.getList().size()];
         // itereates through object array and casts each Object to type Customer
         for(int i = 0; i<this.getList().size(); i++){
            customerArray[i] = (Customer) objectArray[i];
         }
         return customerArray;
    }

    // private method used internally to confirm account actions
    private boolean confirmSelection(Customer customer) {
        Scanner input = new Scanner(System.in);
        System.out.printf("*** ACCOUNT DETAILS ***" + "%n\tName: %s" + "%n\tNumber: %s", customer.getName(), customer.getAccount_number());
        System.out.printf("%nType 'CONFIRM' to confirm action on this account: ");
        // returns TRUE if user types 'confirm', FALSE if anything else is typed
        return input.nextLine().equalsIgnoreCase("confirm");
    }

    // class instance variable and getter/setter
    public LinkedList<Customer> getList() {
        return list;
    }
    public void setList(LinkedList<Customer> list) {
        this.list = list;
    }
    private LinkedList<Customer> list;
    // default constructor
    public CustomerData() {
        this.list = new LinkedList<Customer>();
    }
    // iterator
    @Override
    public Iterator<Customer> iterator() {
        return this.list.iterator();
    }

}