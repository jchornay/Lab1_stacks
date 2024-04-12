/**
 * Lab 5: An inventory control program that implements stacks, queues, iterable lists, recursion, and trees in order to
 * allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date April 11th, 2024
 * @version 1.5
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {

    // instance variables
    private String name;
    private String account_number;
    private int number_purchased;
    private ArrayList<TV> id_purchased;

    private TVType tvType;

    // NOTE: cost_purchased variable removed for being redundant, all instances replaced with
    // number_purchased*cost_per_TV
    public double cost_per_TV = 199.95 * 1.06;


    // default constructor, with empty ArrayList
    public Customer() {
        this.name = "Ima Placeholder";
        this.account_number = "ABC123-00";
        this.number_purchased = 0;
        this.id_purchased = new ArrayList<>();
        this.tvType = null;
    }

    // partial constructor, with empty ArrayList
    public Customer(String name, String account_number) {
        this.name = name;
        this.account_number = account_number;
        this.number_purchased = 0;
        this.id_purchased = new ArrayList<>();
        this.tvType = null;
    }

    // full constructor
    public Customer(String name, String account_number, int number_purchased, ArrayList<TV> id_purchased, TVType tvType) {
        this.name = name;
        this.account_number = account_number;
        this.number_purchased = number_purchased;
        this.id_purchased = id_purchased;
        this.tvType = tvType;
    }

    @Override
    // uses stringbuilder to build customer receipt and return as string
    public String toString() {

        //@formatter:off
        StringBuilder receipt = new StringBuilder();
        receipt.append(String.format("" +
                        "*** CHECKOUT RECEIPT ***" + "" +
                        "%n\tCustomer: %s" + "" +
                        "%n\tAccount Number: %s",
                this.getName(),
                this.getAccount_number()));

        // checks if any purchases have been made
        // if no purchases made, displays appropriate message
        if (this.getId_purchased().isEmpty()) {
            receipt.append(String.format("%nNo TVs purchased!"));
        // if purchases were made, iterates through ArrayList and prints each TV ID
        } else {
            double purchaseTotal = 0;
            receipt.append(String.format("%n*** PURCHASE ***"));
            for (TV tv : this.getId_purchased()) {
                receipt.append(String.format("%n\tTV ID Purchased is: %s", tv.getId_number()));
                purchaseTotal+=tv.getTvType().getPrice();
            }
            receipt.append(String.format("%n\t%d TV(s) for $%.2f", this.getNumber_purchased(), purchaseTotal));
        }
        //returns stringbuilder object as string
        return receipt.toString();
    }
    //@formatter:on

    // getters and setters
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

    public ArrayList<TV> getId_purchased() {
        return id_purchased;
    }

    public void setId_purchased(ArrayList<TV> id_purchased) {
        this.id_purchased = id_purchased;
    }

    public TVType getTvType() {
        return tvType;
    }

    public void setTvType(TVType tvType) {
        this.tvType = tvType;
    }

}
