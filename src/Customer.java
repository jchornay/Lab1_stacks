/**
 * Lab 2: An inventory control program that implements stacks to allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date February 29th, 2024
 * @version 1.0
 */

import java.util.ArrayList;

public class Customer {

    public Customer() {
    }

    public Customer(String name, String account_number, int number_purchased, ArrayList<TV> id_purchased){
        this.name = name;
        this.account_number = account_number;
        this.number_purchased = number_purchased;
        this.id_purchased = id_purchased;

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
        cost_purchased = this.number_purchased*199.95*1.06;
        return cost_purchased;
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
