

=== RUNNING INSTRUCTIONS

In your project's src folder, compile with this:
> javac -d ..\classes -cp ..\..\GameMinimal\classes *.java

Then In your project's classes folder, execute with this:
> java -cp ..\..\GameMinimal\classes; ExampleMain mapTest

=== DEVELOPMENT NOTES

This was initially copied from GameMinimalExample on 2023/11/4

The goal is to create a system for reading maze/race courses from file and then having moving ships with sensors navigate through the maze.

Start by deleting GameMinimalExample down to size with rect and circle obstacles and basic movement controls.

Specify in a text file, locations on map of obstacle rectangles and circles and make it so those can be read in from file.

Make basic L shaped race course with one circle obstacle in it. I went overboard. See mapL.

Add in a finish line point. Detect when this is reached and record and display the winner on the screen, but keep the game running.
It's harder to program, but you DON'T NEED checkpoints! Let the programmers create breadcrumb trails for themselves!

Create an automated racer that takes the finish line as input and navigates to it.
-Test on mapTest

TODO LEFT OFF HERE

In sense method there are problems because distance to sprite DOES NOT correspond to distance to point of impact for circular collisions OR for rectangular. Instead the distanceTo is always to center.
	In GameMinimal, Sprite.java currently has two distanceTo methods, one to a point, and one to a Sprite. Both calculate distance from the Sprite's center.
What is needed is some sort of sensor report from Board's getLineCollisions. The point of impact IS being calculated, but then it's being ignored in favor of the distance to the center of the sprite.
This is a problem in Racer and in Sensor and it should be changed in GameMinimal once and for all.

left off in Ship
Make the automated racer sense obstacles and move around them.
Test on mapModerate then on mapHard.
	-Limit player abilities to things like accelerate and brake and tie the turn rate to the speed.




A ballistic motion hardmode?
You do have access to all of SpritePhysics's public methods

Touch sensors
	public ArrayList<SpritePhysics> getCollisionsWith(SpritePhysics s)
Proximity sensors
	public ArrayList<SpritePhysics> getAllWithinDistance(double x, double y, double distance)
