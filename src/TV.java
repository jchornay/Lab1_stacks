/**
 * Lab 3: An inventory control program that implements stacks, queues, and iterable lists in order to allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date March 15th, 2024
 * @version 1.2
 */

public class TV {

    private String id_number;

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

    @Override
    public String toString(){
        return "The TV id number is: " + this.id_number;
    }
}
