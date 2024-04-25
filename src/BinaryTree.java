/**
 * Lab 6: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, and heaps in
 * order to allow the user to access and modify TV inventory, customer accounts, transactions, and deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.6
 * date April 25th, 2024
 */

public class BinaryTree {

    // root node
    private Node root;

    // boolean to denote whether a given element is found in the tree or not
    private static Boolean isFound;

    // private variable used to reset index every time list of TV types is displayed (need to have a variable outside
    // of recursion for it to work properly)
    private Integer index;

    // private variable used to hold results of recursive search method (need to have a variable outside of recursion
    // for it to work properly)
    private TVType searchResult;

    // method to reset boolean flag to false
    public static void resetSearch() {
        isFound = false;
    }

    // empty constructor which sets root to null and index (for list purposes) to zero
    public BinaryTree() {
        this.root = null;
        this.index = 0;
    }

    // method that calls recursive helper function to add node to existing tree,
    // or create new root node if tree is empty
    public void add(Node newNode) {

        root = addRecursive(root, newNode);

    }

    // recursive helper method for adding node to binary tree
    private Node addRecursive(Node currentNode, Node newNode) {

        // creates a new node at the end of the recursive call, i.e. a new leaf on the tree at the point where no node is found
        // (if tree is empty, new node becomes root node)
        if (currentNode == null) {
            return newNode;
        }

        // if tree is not empty, compares new node value (i.e. the price of the TVType) to current node
        // if new node has a value LESS than current node, attempts to add new node as the left child to current node
        if (newNode.getTvType().compareTo(currentNode.getTvType()) < 0) {
            currentNode.setLeftChild(addRecursive(currentNode.getLeftChild(), newNode));
        }

        // if new node has a value GREATER than current node, attempts to add new node as the right child to current
        // node
        if (newNode.getTvType().getPrice().compareTo(currentNode.getTvType().getPrice()) > 0) {
            currentNode.setRightChild(addRecursive(currentNode.getRightChild(), newNode));
        }

        // after child node is added recursively, returns node of newly enlarged tree
        return currentNode;

    }

    // helper method for recursive traversal which resets index every time method is called (when i attempted to
    // increment the index as a local variable inside the recursion, it never worked as intended)
    public void helperTraverseInOrder() {
        this.setIndex(0);
        recursiveTraverseInOrder(this.getRoot());
    }

    // recursive method to traverse tree in order (left to right) and list TVs from cheapest to most expensive
    public void recursiveTraverseInOrder(Node node) {
        if (node != null) {
            recursiveTraverseInOrder(node.getLeftChild());
            this.setIndex(this.getIndex() + 1);
            System.out.printf("%-8d%s%n", this.getIndex(), node.getTvType().toString());
            recursiveTraverseInOrder(node.getRightChild());
        }

    }

    // helper method for recursive search which resets searchResult every time method is called (when i attempted to
    // return the correct TVType object from inside the recursion, it never worked as intended)
    public TVType helperRecursiveSearch(String brand, String model) {
        this.setSearchResult(null);
        recursiveSearch(this.getRoot(), new TVType(brand, model));
        return this.getSearchResult();
    }

    // search method identical in structure to traversal method
    public void recursiveSearch(Node node, TVType searchTerm) {
        if (node != null) {
           if ((node.getTvType().getModel().equalsIgnoreCase(searchTerm.getModel())) && (node.getTvType().getBrand().equalsIgnoreCase(searchTerm.getBrand()))) {
                this.setSearchResult(node.getTvType());
            } else {
                recursiveSearch(node.getLeftChild(), searchTerm);
                recursiveSearch(node.getRightChild(), searchTerm);
            }
        }
    }

    // getters and setters
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public static Boolean getIsFound() {
        return isFound;
    }

    public static void setIsFound(Boolean isFound) {
        BinaryTree.isFound = isFound;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public TVType getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(TVType searchResult) {
        this.searchResult = searchResult;
    }

}