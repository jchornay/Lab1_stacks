/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */


public class TVType implements Comparable<TVType> {

    // empty constructor
    public TVType() {
        this.brand = "Unknown";
        this.model = "Unknown";
        this.price = 0.0;
    }

    // partial constructor
    public TVType(String brand, String model){
        this.brand = brand;
        this.model = model;
        this.price = 0.0;
    }

    // full parameter constructor
    public TVType(String brand, String model, Double price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }

    // compareTo method returns an integer > 0 if TVType being compared has a higher price, < 0 if it has a lower
    // price, and = 0 if it has the same price as the object it is being compared to
    @Override
    public int compareTo(TVType other) {
        return this.getPrice().compareTo(other.getPrice());
    }

    // instance variables
    private String brand;
    private String model;
    private Double price;

    // getters and setters
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    // toString method to build formatted list using StringBuilder
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append(String.format("%-18s", this.brand));
        output.append(String.format("%-18s", this.model));
        output.append("$");
        output.append(String.format("%7.2f", this.price));
        return output.toString();
    }
}