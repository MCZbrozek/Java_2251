import java.util.ArrayList;

public class PersonSet implements PersonList {

    // Write a class named, PersonSet, that implements the interface PersonList.
    // Using an ArrayList
    private ArrayList<Person> personList;

    public PersonSet() {
        this.personList = new ArrayList<>();
    }

    /*
     * Overrides the ArrayList add method, checks to see if the personList.contains
     * the Person object, before adding it to the list.
     */
    @Override
    public void add(Person person) {
        if (!personList.contains(person)) {
            personList.add(person);
            System.out.println(person + " Has been added to the PersonSet.");
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

    // In addition to implementing add and get methods, PersonSet must make sure
    // that no duplicate Persons are added. If you want to use the ArrayList’s
    // built-in contains method to make this easier, you will need to add an equals
    // method to Person. See below for more details.

}
