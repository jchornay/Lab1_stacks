/**
 * Lab 3: An inventory control program that implements stacks, queues, and lists in order to allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date March 14th, 2024
 * @version 1.2
 */

import java.util.ArrayList;

public class Customer {

    // default constructor
    public Customer() {
        this.name = "Sir Placeholder";
        this.account_number = "ABC123";
        this.number_purchased = 0;
        this.id_purchased = null;
        this.cost_purchased = 0;
    }

    // constructor
    public Customer(String name, String account_number, int number_purchased, ArrayList<TV> id_purchased) {
        this.name = name;
        this.account_number = account_number;
        this.number_purchased = number_purchased;
        this.id_purchased = id_purchased;
        this.cost_purchased = number_purchased * 199.95 * 1.06;

    }

    @Override
    public String toString() {

        StringBuilder receipt = new StringBuilder();
        receipt.append(String.format("Checkout Receipt:" + "%nCustomer: %s" + "%nAccount Number: %s" + "%nPurchased %d TVs for $%.2f", this.getName(), this.getAccount_number(), this.getNumber_purchased(), this.getCost_purchased()));

        for (TV tv : this.getId_purchased()) {
            receipt.append(String.format("%nTV ID Purchased is: %s", tv.getId_number()));
        }

        return receipt.toString();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public int getNumber_purchased() {
        return number_purchased;
    }

    public void setNumber_purchased(int number_purchased) {
        this.number_purchased = number_purchased;
    }

    public double getCost_purchased() {
        return this.cost_purchased;
    }

    public void setCost_purchased(double cost_purchased) {
        this.cost_purchased = cost_purchased;
    }

    public ArrayList<TV> getId_purchased() {
        return id_purchased;
    }

    public void setId_purchased(ArrayList<TV> id_purchased) {
        this.id_purchased = id_purchased;
    }

    private String name;

    private String account_number;

    private int number_purchased;

    private double cost_purchased;

    private ArrayList<TV> id_purchased;

}
