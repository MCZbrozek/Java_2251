Diep.io-inspired game written in Java

In your project's src folder, compile with this:
> javac -d ..\classes -cp ..\..\GameMinimal\classes *.java

Then In your project's classes folder, execute with this:
> java -cp ..\..\GameMinimal\classes; MainTopDown ffa

Select a particular game mode from among the following options:
	ffa - free for all
	team - team game
	bomb - bomberman
	survive - survival mode
	pool - billiards mode
	soccer
	test - testing/tutorial mode.

You can add the additional command line input "camera" to make an invincible invisible player to act as a camera for spectating as in.
> java -cp ..\..\GameMinimal\classes; MainTopDown ffa camera

You can add the additional command line input "xp" to give the player a ton of starting xp for testing purposes or just for fun. Think of it as a cheat code. NOTE that this only works in ffa mode.
> java -cp ..\..\GameMinimal\classes; MainTopDown ffa xp

=== CONTROLS:

Move player with WASD keys or arrow keys.
Point with the mouse. Shoot with left click.
"e" to toggle autofire.
"r" to toggle upgrades menu.
"t" to toggle leaderboard.
"p" to pause.
"escape" to quit.
"space bar" to switch perspective.


=== PROGRAMMING STUDENT SUGGESTED EXERCISE:

Make a copy of one of the example NPC.java files provided and modify it to create your own fighter.

Edit BoardTopDown.java to change the lineup of NPC personalities in the arena.
