/**
 * Lab 6: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, and heaps in
 * order to allow the user to access and modify TV inventory, customer accounts, transactions, and deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.6
 * date April 25th, 2024
 */

public class Node {

    // node instance variables; tvType object and left & right child nodes
    private TVType tvType;
    private Node leftChild;
    private Node rightChild;

    // constructor that creates new Node with tvType object and no child nodes
    public Node(TVType tvType) {

        this.tvType = tvType;
        this.leftChild = null;
        this.rightChild = null;

    }

    // getters and setters
    public TVType getTvType() {
        return tvType;
    }

    public void setTvType(TVType tvType) {
        this.tvType = tvType;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

}
