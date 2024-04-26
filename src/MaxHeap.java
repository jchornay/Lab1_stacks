/**
 * Lab 6: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, and heaps in
 * order to allow the user to access and modify TV inventory, customer accounts, transactions, and deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.6
 * date April 25th, 2024
 */

public class MaxHeap {

    // instance variables
    private DelInfo[] heap;
    private int maxSize;
    private int currentSize;

    // constructor for 1-indexed heap
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

    private int parent(int currentIndex){return currentIndex/2;}
    private int leftChild(int currentIndex){return 2*currentIndex;}
    private int rightChild(int currentIndex){return (2*currentIndex)+1;}
    // returns true if current index is located in 'second half' of heap, essentially ensuring that it is a leaf node
    // based off the inherent organizational structure
    private boolean isLeaf(int currentIndex) {
        return currentIndex >= (currentSize / 2) && currentIndex <= currentSize;
    }

    private void swap(int indexA, int indexB){
        DelInfo temp = heap[indexA];
        heap[indexA] = heap[indexB];
        heap[indexB] = temp;
    }

    public void insertNode(DelInfo delInfo)  {
        if (currentSize >= maxSize) {
            return;
        }
        currentSize+=1;
        heap[currentSize] = delInfo;
        int currentIndex = currentSize;

        while (heap[currentIndex].getNumberPurchased() > heap[parent(currentIndex)].getNumberPurchased())
        {
            swap(currentIndex, parent(currentIndex));
            currentIndex = parent(currentIndex);
        }
    }

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
        for (int index = (currentSize / 2); index >= 1; index-=1) {
            maxHeapify(index);
        }
    }


    // create removeRoot() method for removing maximum element from the heap
    public DelInfo removeRoot()  {
        DelInfo rootElement = heap[1];
        heap[1] = heap[currentSize];
        currentSize-=1;
        maxHeapify(1);
        return rootElement;
    }
}


