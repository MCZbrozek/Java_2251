/*
Name: Michael Zbrozek
Date: 3/2/2024
Purpose: Read in hr.txt, store Persons in PersonSet(), print to console
Sources:
ChatGPT - See prompts in comments
GeeksforGeeks - See links in comments
file_io_example	

Files: 
Main.Java
Person.java
PersonList.Java
PersonSet.java

*/

import java.util.ArrayList;

public class PersonSet implements PersonList {

    // Write a class named, PersonSet, that implements the interface PersonList.
    // Using an ArrayList
    private ArrayList<Person> personList;

    public PersonSet() {
        this.personList = new ArrayList<Person>();
    }

    /*
     * Overrides the ArrayList add method, checks to see if the personList.contains
     * the Person object, before adding it to the list.
     */
    @Override
    public void add(Person person) {
        if (!personList.contains(person)) {
            personList.add(person);
            System.out.println(" A newPerson was added to the PersonSet.");
        } else {
            System.out.println(person + " is already in the PersonSet");
        }

    }

    /*
     * Overrides the ArrayList get method, checks to see if the personList.contains
     * the Person object, before adding it to the list.
     */

    @Override
    public Person get(int index) {
        if (index >= 0 && index < personList.size()) {
            return personList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Custom error - Index out of range in ArrayList \"PersonList\"");
        }
    }

    // Method to iterate and print the person list using the toString method defined
    // for person objects
    public void printPersonSet() {
        String format = "| %-15s | %-6.2f | %-6.2f |%n";
        // https://www.geeksforgeeks.org/stringbuilder-class-in-java-with-examples/
        StringBuilder result = new StringBuilder();

        // Header for table output
        result.append("+-----------------+--------+--------+\n");
        result.append("| Name            | Height | Weight |\n");
        result.append("+-----------------+--------+--------+\n");

        // Footer

        for (Person person : personList) {
            result.append(String.format(format, person.getName(), person.getHeight(), person.getWeight()));
            result.append("+-----------------+--------+--------+\n");
        }

        System.out.println(result.toString());
    }

}
