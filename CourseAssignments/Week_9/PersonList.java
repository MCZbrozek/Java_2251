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
public interface PersonList {
    // The interface should have two abstract methods:

    // A. add – This method takes a Person as input and returns void.
    void add(Person person);

    // B. get – This method takes an int as input and returns a Person at the
    // corresponding index of the input int.
    Person get(int index);
}
