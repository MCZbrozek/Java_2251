public class DoublyLinkedSortedList implements DoublyLinkedSortedListInterface {

    private HurricaneRowData value;
    private DoublyLinkedSortedList next;
    private DoublyLinkedSortedList previous;

    // default constructor
    public DoublyLinkedSortedList() {
    }

    // Constructor
    public DoublyLinkedSortedList(HurricaneRowData value) {
        this.value = value;
        this.next = null;
        this.previous = null;
    }

    // Get the value of the current DoublyLinkedSortedList
    public HurricaneRowData getValue() {
        return value;
    }

    // Return true if next is not null - ask the next item if it's next has a value.
    public boolean hasNext() {
        return next != null;
    }

    // Set next to be the given DoublyLinkedSortedList
    public void setNext(DoublyLinkedSortedList next) {
        this.next = next;
    }

    // Return a reference to the next DoublyLinkedSortedList item
    public DoublyLinkedSortedList getNext() {
        return next;
    }

    // Return true if previous is not null
    public boolean hasPrevious() {
        return previous != null;
    }

    // Set previous to be the given DoublyLinkedSortedList
    public void setPrevious(DoublyLinkedSortedList previous) {
        this.previous = previous;
    }

    // Return a reference to the previous DoublyLinkedSortedList
    public DoublyLinkedSortedList getPrevious() {
        return previous;
    }

    /*
     * The next 2 methods below were modified from lecture code, but could be
     * written as ---
     * 
     * public DoublyLinkedSortedList getFirst() {
     * DoublyLinkedSortedList current = this;
     * while (current.hasPrevious()) {
     * current = current.getPrevious();
     * }
     * return current;
     * }
     */

    public DoublyLinkedSortedList getFirst() {
        // if hasPrevious method returns null, then we are the first element and return
        // this value.
        if (!hasPrevious()) {
            return this;
        }
        // if hasPrevious returns true, then run the getFirst method, passed to each
        // item until the first if statement is null.
        else {
            return previous.getFirst();
        }
    }

    public DoublyLinkedSortedList getLast() {
        // if hasNext method returns null, then we are the first element and return
        // this value.
        if (!hasNext()) {
            return this;
        }
        // if hasNext returns true, then run the getFirst method, passed to each
        // item until the first if statement is null.
        else {
            return previous.getLast();
        }
    }

    /*
     * SOURCE: Modified from ChatGPT
     * Prompt: Help me to write a compare method that returns -1, 0, or 1 without
     * using the built-in Double.compare() method
     */
    private int compareAce(HurricaneRowData newData, HurricaneRowData existingData) {
        int newAce = newData.getAceVal();
        int existingAce = existingData.getAceVal();

        if (newAce < existingAce) {
            return -1; // newAce is less than existing
        } else if (newAce > existingAce) {
            return 1; // newAce is greater than existing
        } else {
            return 0; // newAce is equal to existing
        }
    }

    // Remove the DoublyLinkedSortedList element that has toRemove as its value
    /*
     * SOURCE: Modified from ChatGPT
     * PROMPT: Help me to write a method to remove an item from a Doubly Linked
     * List. Show me how this might be implemented in main.java
     * 
     * Explanations added as I understand them.
     */

    public DoublyLinkedSortedList remove(HurricaneRowData toRemove) {
        // create a variable called 'current', set it's value equal to the first item in
        // the list.
        DoublyLinkedSortedList current = getFirst();
        // Traverse the list while current is not a null value
        while (current != null) {
            // if the variable toRemove, matches the data in the list
            if (current.getValue().equals(toRemove)) {
                // Then pass along the reference to previous
                DoublyLinkedSortedList prev = current.getPrevious();
                // And pass along the reference to next
                DoublyLinkedSortedList next = current.getNext();
                // Checks to see if this is not the first node and then sets the new reference
                if (prev != null) {
                    prev.setNext(next);
                }
                // Checks to see if this is not the last node and then sets the new reference
                if (next != null) {
                    next.setPrevious(prev);
                }

                return current;
            }
            current = current.getNext();
        }
        return null; // Element is not found
    }

    // Test method in order to place the data in the DoublyLinkedSortedList
    public void easyInsert(HurricaneRowData newValue) {
        // if there is no starting value, then newValue is the start
        if (value == null) {
            value = newValue;
            return;
        }

        DoublyLinkedSortedList newNode = new DoublyLinkedSortedList(newValue);
        DoublyLinkedSortedList current = getFirst();
        newNode.setNext(current.getNext());
        newNode.setPrevious(current);
    }

    // Insert a new DoublyLinkedSortedList element that has the given newValue in
    // order in the list.
    public void insert(HurricaneRowData newValue) {

        // First create a new node, if one does not exist
        if (value == null) {
            value = newValue;
            return;
        }
        // Create the new Node to add to the DoublyLinkedSortedList
        DoublyLinkedSortedList newNode = new DoublyLinkedSortedList(newValue);
        // Create a temp variable to start at the first value in the linkedList
        DoublyLinkedSortedList current = getFirst();

        // Is this the first node in the list? or is this the smallest value in the
        // list?
        if (!current.hasNext() || compareAce(newValue, current.getValue()) < 0) {
            newNode.setNext(current);
            current.setPrevious(newNode);
            return;
        }

        // The conditions of this while loop check to see that we aren't at the end of
        // the list and the value of the compareTo method (written below) is > 0
        // indicating that the value is Greater than the previous value. While this is
        // true, traverse the list.
        while (current.hasNext() && compareAce(newValue, current.getNext().getValue()) > 0) {
            current = current.getNext();
        }

        // Once the value of compare to == 0 or -1 we break from the while loop and
        // place the item in the list by running the setNext and setPrevious methods to
        // make room for the new data item.
        newNode.setNext(current.getNext());
        newNode.setPrevious(current);
        current.setNext(newNode);

        if (newNode.hasNext()) {
            newNode.getNext().setPrevious(newNode);
        }
    }

    public DoublyLinkedSortedList getMax() {
        if (getFirst() == null) {
            System.out.println("The list is empty.");
            return null;
        }
        // Create a temp variable to start at the first value in the linkedList
        DoublyLinkedSortedList current = getFirst();
        // Create a maxNode variable to hold the current max
        DoublyLinkedSortedList maxNode = current;
        while (current.hasNext()) {
            current = current.getNext();
            if (compareAce(current.getValue(), maxNode.getValue()) > 0) {
                maxNode = current;
            }
        }
        return maxNode;
    }

    // toString method to print the list
    @Override
    public String toString() {
        String format = "| %-6d | %-6d | %-6d | %-6d |%-6d |%n";
        StringBuilder result = new StringBuilder();
        DoublyLinkedSortedList current = getFirst();
        result.append("The year with highest Ace value is: ");
        result.append(current.getMax().getValue().getYear() + "\n");
        result.append("The ACE value was: " + current.getMax().getValue().getAceVal() + "\n");
        result.append("| ACEval | Year   | num1   | num2   | num3  |\n");
        while (current != null) {
            HurricaneRowData temp = current.getValue();
            result.append(String.format(format, temp.getAceVal(), temp.getYear(),
                    temp.getNum1(), temp.getNum2(), temp.getNum3()));
            result.append("\n");
            current = current.getNext();
        }

        return result.toString();
    }

}
