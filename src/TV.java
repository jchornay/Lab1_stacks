/**
 * Lab 4: An inventory control program that implements stacks, queues, iterable lists, and recursion in order to allow
 * the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date March 21st, 2024
 * @version 1.4
 */

public class TV {

    // TV id number
    private String id_number;

    // getter and setter
    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    // default constructor
    public TV() {
        this.id_number = null;
    }

    // full constructor
    public TV(String id_number) {
        this.id_number = id_number;
    }

    // toString
    @Override
    public String toString(){
        return "TV id number: " + this.id_number;
    }
}
