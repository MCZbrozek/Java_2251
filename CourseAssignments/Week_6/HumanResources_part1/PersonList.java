
/*Write an interface named PersonList. 
 */
public interface PersonList {
    // The interface should have two abstract methods:

    // A. add – This method takes a Person as input and returns void.
    void add(Person person);

    // B. get – This method takes an int as input and returns a Person at the
    // corresponding index of the input int.
    Person get(int index);
}
