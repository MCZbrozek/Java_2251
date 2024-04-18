public class DoublyLinkedSortedList implements DoublyLinkedSortedListInterface {
    private HurricaneRowData value = null;
    private DoublyLinkedSortedList next = null;
    private DoublyLinkedSortedList previous = null;

    /* Constructor */
    public DoublyLinkedSortedList() {
    }

    /* Constructor */
    public DoublyLinkedSortedList(HurricaneRowData value) {
        this.value = value;
    }

    public HurricaneRowData getValue() {
        return value;
    }

    public void setNext(DoublyLinkedSortedList next) {
        this.next = next;
    }

    public void setPrevious(DoublyLinkedSortedList previous) {
        this.previous = previous;
    }

    public DoublyLinkedSortedList getNext() {
        return next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public DoublyLinkedSortedList getPrevious() {
        return previous;
    }

    public DoublyLinkedSortedList getFirst() {
        if (previous == null) {
            return this;
        } else {
            return previous.getFirst();
        }
    }

    public DoublyLinkedSortedList getLast() {
        if (next == null) {
            return this;
        } else {
            return next.getLast();
        }
    }

    /*
     * Pre: The list has at least one element.
     * Post: Removes this linked list element from the
     * list and returns the previous link
     * if it's not null, otherwise return
     * the next link.
     */
    public DoublyLinkedSortedList remove() {
        if (previous != null) {
            previous.setNext(next);
            return previous;
        }
        if (next != null) {
            next.setPrevious(previous);
            return next;
        }
    }

    // Remove the DoublyLinkedSortedList element that has toRemove as its value
    public DoublyLinkedSortedList remove(HurricaneRowData toRemove) {
        DoublyLinkedSortedList current = getFirst();
        if (toRemove.equals(current)) {
            current.remove();
            return current;
        }
        while (current.hasNext()) {
            current = current.getNext();
            if (toRemove.equals(current)) {
                current.remove();
                return current;
            }
        }
        return null;
    }

    /*
     * Post: Adds a linked list element with value
     * equal to the given value to this
     * linked list in order descending
     * sorted by ACE!
     */
    public void add(HurricaneRowData new_value) {
        if (value == null) {
            value = new_value;
            return;
        }
        DoublyLinkedSortedList temp = new DoublyLinkedSortedList(new_value);
        if (value.getAce() > new_value.getAce()) {
            if (next == null) {
                setNext(temp);
            } else if (new_value.getAce() >= next.getValue().getAce()) {
                DoublyLinkedSortedList nextRef = next;
                setNext(temp);
                temp.setNext(nextRef);
            } else {
                next.add(new_value);
            }
        } else if (previous == null) {
            setPrevious(temp);
        } else {
            previous.add(new_value);
        }
    }
}