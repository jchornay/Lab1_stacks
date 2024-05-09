/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */

public class MaxHeap {

    // instance variables
    private DelInfo[] heap;
    private int maxSize;
    private int currentSize;

    // constructor for 1-indexed heap, with empty delivery info item containing maximum integer value
    public MaxHeap(int maxSize){
        this.heap = new DelInfo[maxSize+1];
        this.maxSize = maxSize;
        this.currentSize = 0;

        this.heap[0] = new DelInfo("","","",Integer.MAX_VALUE);
    }

    // getter
    public int getCurrentSize() {
        return currentSize;
    }
    public DelInfo getDelInfo(int index){
        return this.heap[index];
    }

    // node variables such that location of parent and children is defined mathematically
    private int parent(int currentIndex){return currentIndex/2;}
    private int leftChild(int currentIndex){return 2*currentIndex;}
    private int rightChild(int currentIndex){return (2*currentIndex)+1;}
    // returns true if current index is located in 'second half' of heap, essentially ensuring that it is a leaf node
    // based off the inherent organizational structure
    private boolean isLeaf(int currentIndex) {
        return (currentIndex > (currentSize/2) && currentIndex <= currentSize);
    }

    // swaps DelInfo objects at indexA and indexB
    private void swap(int indexA, int indexB){
        DelInfo temp = heap[indexA];
        heap[indexA] = heap[indexB];
        heap[indexB] = temp;
    }

    // inserts new node
    public void insertNode(DelInfo delInfo)  {
        // exits method if heap has already reached max size
        if (currentSize >= maxSize) {
            return;
        }
        // increments heap size
        currentSize+=1;
        // places new delInfo object at new index
        heap[currentSize] = delInfo;
        int currentIndex = currentSize;

        // starting at the new index at the end of the heap, compares the delInfo object at that index with its parent
        while (heap[currentIndex].getNumberPurchased() > heap[parent(currentIndex)].getNumberPurchased())
        {
            // if the newly added object is greater than its parent, swaps their positions
            swap(currentIndex, parent(currentIndex));
            // after the swap, updates the currentIndex variable to new position and runs the check again
            currentIndex = parent(currentIndex);
        }
    }

    //
    private void maxHeapify(int currentIndex) {
        if(!isLeaf(currentIndex)) {

            // checks if value at current index is less than either of its children (since this is a max heap, it
            // should be greater than both children)
            if (heap[currentIndex].getNumberPurchased() < heap[leftChild(currentIndex)].getNumberPurchased() ||
                    heap[currentIndex].getNumberPurchased() < heap[rightChild(currentIndex)].getNumberPurchased())
            {
                // if left child is greater than right child, left child swaps with parent to become new parent node
                // and process repeats with new left child node (the one that was just the parent)
                if (heap[leftChild(currentIndex)].getNumberPurchased() > heap[rightChild(currentIndex)].getNumberPurchased())
                {
                    swap(currentIndex, leftChild(currentIndex));
                    maxHeapify(leftChild(currentIndex));
                }
                // same as above but for right child
                else
                {
                    swap(currentIndex, rightChild(currentIndex));
                    maxHeapify(rightChild(currentIndex));
                }
            }
        }
    }

    // starts heapifying the array in the middle, since the organizational structure guarantees everything after the
    // middle to be a leaf
    public void constructMaxHeap(){
        for (int index = (currentSize / 2); index >= 0; index-=1) {
            maxHeapify(index);
        }
    }

    // create removeRoot() method for removing maximum element from the heap
    public DelInfo removeRoot()  {
        DelInfo rootElement = heap[1];
        heap[1] = heap[currentSize];
        currentSize -= 1;
        maxHeapify(1);
        return rootElement;
    }
}