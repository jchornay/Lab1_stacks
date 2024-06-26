/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */

public class TV implements Comparable<TV> {

    // TV id number
    private String id_number;
    // TVType object
    private TVType tvType;

    // getters and setters
    public String getId_number() {
        return id_number;
    }
    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public TVType getTvType() {
        return tvType;
    }
    public void setTvType(TVType tvType) {
        this.tvType = tvType;
    }

    // default constructor
    public TV() {
        this.id_number = null;
        this.tvType = null;
    }

    // partial constructor
    public TV(String id_number) {
        this.id_number = id_number;
        this.tvType = null;
    }

    // full constructor
    public TV(String id_number, TVType tvType){
        this.id_number = id_number;
        this.tvType = tvType;
    }

    // toString
    @Override
    public String toString(){
        if (this.getTvType() == null) {
            return "TV id number: " + this.id_number;
        } else {
            return String.format("TV id number: %s%n\t\tBrand: %s%n\t\tModel: %s%n\t\tPrice: %.2f%n",
                    this.id_number,
                    this.getTvType().getBrand(), this.getTvType().getModel(), this.getTvType().getPrice());
        }
    }

    @Override
    public int compareTo(TV compare) {
        return this.getId_number().compareTo(compare.getId_number());
    }
}