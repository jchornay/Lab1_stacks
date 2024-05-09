/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */

public class DelInfo {

    // instance variables
    private String name;
    private String address;
    private String accountNumber;
    private int numberPurchased;

    // default constructor
    public DelInfo(){
        this.name = "Ima Placeholder";
        this.accountNumber = "ABC123-00";
        this.address = "123 AnyStreet";
        this.numberPurchased = 0;
    }

    // full constructor
    public DelInfo(String name, String address, String accountNumber, int numberPurchased){
        this.name = name;
        this.address = address;
        this.accountNumber = accountNumber;
        this.numberPurchased = numberPurchased;
    }

    // getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getNumberPurchased() {
        return numberPurchased;
    }
    public void setNumberPurchased(int numberPurchased) {
        this.numberPurchased = numberPurchased;
    }

    @Override
    public String toString(){
        return String.format("Name: %s\t\tAccount: %s%nAddress: %s%nTVs Purchased: %d%n", this.getName(),
                this.getAccountNumber(), this.getAddress(), this.getNumberPurchased());
    }

}
