
In your project's src folder, compile with this:
> javac -d ..\classes -cp ..\..\GameMinimal\classes *.java

Then In your project's classes folder, execute with this:
> java -cp ..\..\GameMinimal\classes; ExampleMain
assuming ExampleMain is the Main class.

======================================

Most recently I was trying to make a "space clearing" / "maze running" type game using this code, but the downside with that is that this code is set up for a per-frame update, not turn-based actions, so I ended up moving all that turn-based code into a completely different folder \src_space_clearing\

NEXT STEPS: So this should be cleaned up to probably get rid of the ExampleGridMoveRobot since it has a clunky thread sleep in an attempt to make it appear turn-based, but it wasn't really working and the other version works better anyway.
