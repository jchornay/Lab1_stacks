/**
 * Lab 3: An inventory control program that implements stacks, queues, and iterable lists in order to allow the user to check inventory and make changes at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @date March 14th, 2024
 * @version 1.2
 */

public interface InventoryMenu {

    // main menu variables
    int STOCK_SHELVES = 1;
    int FILL_ORDER = 2;
    int RESTOCK_RETURN = 3;
    int RESTOCK_INVENTORY = 4;
    int CUSTOMER_PURCHASE = 5;
    int CUSTOMER_CHECKOUT = 6;
    int CUSTOMER_UPDATE = 7;
    int DISPLAY_INVENTORY = 8;
    int END = 9;
    int STOCKING_QUANTITY = 5;

    // customer submenu options
    int ADD_CUSTOMER = 1;
    int DELETE_CUSTOMER = 2;
    int UPDATE_CUSTOMER = 3;
    int SAVE_TO_FILE = 4;
    int DISPLAY_LIST = 5;
    int RETURN_TO_MAIN = 6;

}