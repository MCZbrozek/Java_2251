
=== FOR STUDENTS

Copy over all files for students EXCEPT PlayerNeal.java.
Students should read PlayerExample.java thoroughly and also SensorReport.java
They are welcome to read any other files they want, but basically they need to make a copy of PlayerExample.java, modify it, and then in BoardTopDown.java insert their new file here:
		//Create the player
		//player1 = new PlayerNeal(this);
		player1 = new PlayerExample(this);
Of course they could also just modify PlayerExample in place without creating a new file.

=== RUNNING INSTRUCTIONS

Run with:
In your project's src folder, compile with this:
> javac -d ..\classes -cp ..\..\GameMinimal\classes *.java

Then In your project's classes folder, execute with this:
> java -cp ..\..\GameMinimal\classes; MainTopDown
or pass in a random seed, or the hardmode flag with obstacles to be avoided, or a time limit in seconds, for instance:
> java -cp ..\..\GameMinimal\classes; MainTopDown -seed 2427 -hardmode -timelimit 60


"p" to pause


=== DEVELOPMENT NOTES

This was initially copied from GameTopDown on 2023/11/1

The goal is to create a system of sensors and basic commands like move forward, turnLeft, turnRight and then set students lose to write code to solve problems like collecting pickups, or collecting pickups while avoiding bad icons, or maze navigation.
The idea is that students would choose what sensors to put on their vehicle and how to use those sensors (for instance, in one challenge, you only get one sensor and you have to realize you can spin it like radar or sweep it back and forth).
Sensors provide distance to target info.

A secondary goal is to get good sensors working for other game applications.

Useful code is available in:

board in gameMinimal has a variety of methods for getting collisions, including 
Line sensors
	public ArrayList<SpritePhysics> getLineCollisions(
			double origin_x, double origin_y,
			double angle, double length,
			double beam_width)
Touch sensors
	public ArrayList<SpritePhysics> getCollisionsWith(SpritePhysics s)
Proximity sensors
	public ArrayList<SpritePhysics> getAllWithinDistance(double x, double y, double distance)

Ship.java in Diep around line 505 has code to draw a laser and get everything hit by the laser

SpriteTopDown in Diep uses g2d.fillPolygon(x_points,y_points,spikes); to draw filled shapes for different ships.

First step is to cut down game content to a player object and some pickups.

Next, put line sensors on player.

Next, automate player's search for pellets.

Display number of pellets remaining and elapsed time on screen.

Create class named PlayerNeal that extends Player.java then hide in Player.java as much as you can.

Make it simpler for students to add in code. Make the methods more user friendly.
Look in PlayerNeal. What is better left hidden? Use degrees instead of radians?

Also a PlayerExample that extends Player.java and demonstrates basic functionality

Make a strategy that remembers pellets or seeks near pellets first. The sensors would need to return the sprites themselves.
DONE See Strategy05 in PlayerNeal. It collects 50 dots in under a second.

Provide a turnToward method that doesn't use elapsed seconds and demo the distanceTo method in PlayerExample.

Modify sense to return: x, y, distanceTo, and type of the object sensed as a new object! A simple little object with getters and a toString.

Set random seed for consistency.

Track player points and have bad pellets to be avoided alongside good pellets.

Make pellets larger.

Add turnAwayFrom method.

Have a time limit.

Sensors is good to go!
