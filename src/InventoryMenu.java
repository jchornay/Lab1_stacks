/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */

public interface InventoryMenu {

    // main menu variables
    int STOCK_SHELVES = 1;
    int FILL_ORDER = 2;
    int RESTOCK_RETURN = 3;
    int RESTOCK_INVENTORY = 4;
    int CUSTOMER_UPDATE = 5;
    int CUSTOMER_PURCHASE = 6;
    int CUSTOMER_CHECKOUT = 7;
    int DISPLAY_DELIVERY = 8;
    int DISPLAY_INVENTORY = 9;
    int END = 10;

    // quantity used in STOCK_SHELVES action
    int STOCKING_QUANTITY = 5;

    // customer submenu variables
    int ADD_CUSTOMER = 1;
    int DELETE_CUSTOMER = 2;
    int UPDATE_CUSTOMER = 3;
    int SAVE_TO_FILE = 4;
    int DISPLAY_LIST = 5;
    int RETURN_TO_MAIN = 6;

    // delivery variables
    int MAXIMUM_DELIVERIES = 25;

}