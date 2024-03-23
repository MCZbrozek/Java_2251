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
public interface SortedTreeSetInterface // Binary Tree
{
	public Person getPerson();

	public boolean hasLeft();

	public void setLeft(SortedTreeSet left);

	public SortedTreeSet getLeft();

	public boolean hasRight();

	public void setRight(SortedTreeSet right);

	public SortedTreeSet getRight();

	public void add(Person p);

}