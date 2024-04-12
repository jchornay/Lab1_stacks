/**
 * Lab 5: An inventory control program that implements stacks, queues, iterable lists, recursion, and trees in order to
 * allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.5
 * date April 11th, 2024
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
    int DISPLAY_INVENTORY = 8;
    int END = 9;

    // quantity used in STOCK_SHELVES action
    int STOCKING_QUANTITY = 5;

    // customer submenu variables
    int ADD_CUSTOMER = 1;
    int DELETE_CUSTOMER = 2;
    int UPDATE_CUSTOMER = 3;
    int SAVE_TO_FILE = 4;
    int DISPLAY_LIST = 5;
    int RETURN_TO_MAIN = 6;

}