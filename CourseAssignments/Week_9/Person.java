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

public class Person implements Comparable<Person> {
    /*
     * Per ChatGPT suggestion, I implemented Comparable in order to get access to
     * the compareTo method which starts on line 120
     */

    // Instance variables
    private String name;
    private double height;
    private double weight;

    // Contstructor declaration
    public Person(String name, double height, double weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
    }
    /*
     * Write a toString method that returns the Person data in a database-ready
     * String format.
     */

    // Getters and setters - still ain't gonna lie, I used VS Code's
    // "generate getters and setters"
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /*
     * Source ChatGPT
     * Prompt - Show me method for a toString() method that outputs data in a table
     * format
     */
    @Override
    public String toString() {
        String format = "| %-15s | %-6.2f | %-6.2f |%n";
        // https://www.geeksforgeeks.org/stringbuilder-class-in-java-with-examples/
        StringBuilder result = new StringBuilder();

        // Header for table output
        result.append("+-----------------+--------+--------+\n");
        result.append("| Name            | Height | Weight |\n");
        result.append("+-----------------+--------+--------+\n");
        result.append(String.format(format, name, height, weight));

        // Footer
        result.append("+-----------------+--------+--------+\n");

        return result.toString();

    }

    /*
     * Source ChatGPT -
     * Prompt -
     * "Why would i need to Override the .equals method before using the ArrayList.contains() method in Java? "
     * "If you want to check for the presence of an object based
     * on its content or
     * attributes rather than its memory location, you need to override the equals()
     * method in the class of the objects you are storing in the ArrayList. "
     */

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            System.out.println("Object is null");
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof Person)) {
            System.out.println("Object is not instance of person");
            return false;
        }

        Person person = (Person) o;

        return name.equals(person.name) && height == person.height && weight == person.weight;
    }

    @Override
    public int compareTo(Person otherPerson) {
        // Implements comparison logic from Comparable
        // Return a negative value if this object is less than the other,
        // a positive value if greater, and 0 if they are equal.

        // Compare based on name
        return this.name.compareTo(otherPerson.name);

    }
}
