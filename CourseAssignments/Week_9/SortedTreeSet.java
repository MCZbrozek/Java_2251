/*
Name: Michael Zbrozek
Date: 3/13/2024
Purpose: Read in hr.txt, store Persons in PersonSet(), print to console
Sources:
ChatGPT - See prompts in comments
GeeksforGeeks - See links in comments
file_io_example	
Tree.java - instructor examples

Files: 
Main.Java
Person.java
SortedTreeSet.java
SortedTreeSetInterface.java

*/

public class SortedTreeSet implements SortedTreeSetInterface {

    private Person person; // Person object
    private SortedTreeSet left;
    private SortedTreeSet right;
    private boolean hasValue;

    // I like default constructors, apparently
    public SortedTreeSet() {
        // I am empty inside :(
    }

    // Constructor for SortedTreeSet, sets person to value of person object, and
    // right and left to null
    public SortedTreeSet(Person person) {
        this.person = person;
        this.left = null;
        this.right = null;
        this.hasValue = false;
    }

    // Allows me to retrieve the person object at each location in the tree.
    public Person getPerson() {
        return person;
    }

    // Checks to see if there is a left facing branch from this location in the
    // tree. "Left child"
    public boolean hasLeft() {
        return left != null;
    }

    // Allows me to set the left child of each location in the tree.
    public void setLeft(SortedTreeSet left) {
        this.left = left;
    }

    // Allows me to retrieve the left child of this location in the tree.
    public SortedTreeSet getLeft() {
        return left;
    }

    // Checks to see if there is a right facing branch from this location in the
    // tree. "right child"
    public boolean hasRight() {
        return right != null;
    }

    // Allows me to set the right child of each location in the tree.
    public void setRight(SortedTreeSet right) {
        this.right = right;
    }

    // Allows me to retrieve the right child of this location in the tree.
    public SortedTreeSet getRight() {
        return right;
    }

    @Override
    public void add(Person p) {
        // Add a new person to the tree in sorted alphabetically
        // If the tree is empty set this object to be the root
        if (person == null) {
            person = p;
            this.hasValue = true;
            // My tests just to see what the heck is going on
            System.out.println("The first person is " + p.getName());
            return;
        }
        if (p.compareTo(person) < 0) {
            // Add to the left subtree
            System.out.println(p.getName() + " was added to the left subtree");
            if (hasLeft()) {
                left.add(p);
            } else {
                setLeft(new SortedTreeSet(p));
            }
        } else if (p.compareTo(person) > 0) {
            // Add to the right subtree
            System.out.println(p.getName() + " was added to the right subtree");
            if (hasRight()) {
                right.add(p);
            } else {
                setRight(new SortedTreeSet(p));
            }
        } else if (p.compareTo(person) == 0) {
            System.out.println(p.getName() + " is already part of the tree.");
        }

        // public void add(Person person) {
        // add a new person object to the tree sorted by ....?
        /*
         * public void add(Person p)
         * {
         * if(this.value.compareTo(p) > 0)
         * {
         * add p in the left branch
         * }
         * else if(this.value.compareTo(p) < 0)
         * {
         * add p in the right branch
         * }
         * else
         * {
         * //p and this.value are equal and we already contain the person
         * }
         * }
         */
    }

    // Method below modified from Neal Holtshulte Tree.Java -

    @Override
    // Suggestion from ChatGPT, which initializes a stringBuilder and private helper
    // method.
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        toStringHelper(stringBuilder, this);
        return stringBuilder.toString();
    }

    private void toStringHelper(StringBuilder sb, SortedTreeSet node) {
        if (node == null) {
            return;
        }

        // Traverse the left subtree and add it's value to the stringBuilder
        toStringHelper(sb, node.left);

        // At each node, append the person data
        sb.append(node.person).append("\n");

        // Traverse the left subtree and add it's value to the stringBuilder
        toStringHelper(sb, node.right);
    }

}
