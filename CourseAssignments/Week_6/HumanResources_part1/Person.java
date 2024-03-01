
/*Write a class named Person.*/

public class Person {
    /* Write three attributes for storing name, height, and weight information. */

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

}
