/**
 * Lab 5: An inventory control program that implements stacks, queues, iterable lists, recursion, and trees in order to
 * allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.5
 * date April 11th, 2024
 */

public class TV {

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
        return "TV id number: " + this.id_number;
    }
}
