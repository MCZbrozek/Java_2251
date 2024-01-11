
===========

This is a minimal code base for building a variety of future games off of.

In src
javac -d ../classes *.java

===========

https://www.geeksforgeeks.org/run-java-class-file-different-directory/

To use these files in a different directory
cd into the src folder then compile the classes to where you want them like this
> javac -d ../../MyProject/classes *.java

SPECIFICALLY:

In your project's src folder, compile with this:
> javac -d ..\classes -cp ..\..\GameMinimal\classes *.java

Then In your project's classes folder, execute with this:
> java -cp ..\..\GameMinimal\classes; ExampleMain
assuming ExampleMain is the Main class.


===========

Build Jars like so:
https://stackoverflow.com/questions/1082580/how-to-build-jars-from-intellij-properly

Here's how to build a jar with IntelliJ 10 http://blogs.jetbrains.com/idea/2010/08/quickly-create-jar-artifact/

File -> Project Structure -> Project Settings -> Artifacts -> Click green plus sign -> Jar -> From modules with dependencies...

The above sets the "skeleton" to where the jar will be saved to. To actually build and save it do the following:

Extract to the target Jar

OK

Build | Build Artifact

===========
