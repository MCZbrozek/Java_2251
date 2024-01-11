
Run with:
In your project's src folder, compile with this:
> javac -d ..\classes -cp ..\..\GameMinimal\classes *.java

Then In your project's classes folder, execute with this:
> java -cp ..\..\GameMinimal\classes; MainTopDown
or this to center on player
> java -cp ..\..\GameMinimal\classes; MainTopDown -center


Move with the arrow keys or WASD
Aim with the mouse
Left click mouse to attack
Number keys: 1, 2, 3 are special attacks
"p" to pause



TODO LEFT OFF HERE
I was thinking of making a 2d house of the dying sun sim next, but now I'm not sure.
Also, GameMinimal already has ballistic movement. Why are these two separate projects?

	1. enemy movement with high friction ballistic motion

	2. contrails to see movement


-Scenario object: has timings and sets of enemies to spawn at each timing. Creates initial base placement and enemies. Initially make it duplicate the current behavior of spawning enemies. Make it easy to load in other scenarios, like the defend location mission.
Has a stack of behaviors. Behavior might be to spawn enemies or push more behaviors on the stack. Make it text based so you can eventually read these out of a file.
  -condition, action
	condition can be elapsed seconds, number of enemies remaining, or specific unit dead
	action: a set of "spawn number of particular enemy" commands or
			push condition, action pair on the stack
No need to make it so general yet, just make it work.

-defend a location mission
  - create new base object
  - enemies are given a default target of the base
  - enemies send out a circular pulse every half to 1 second (randomly). If the pulse hits the player, they change default target to the player. if it does not hit player, they go back to targeting base.
  - if all enemies are cleared, player wins, go to new mission
  - if base dies, player loses, exit game

-attack a location mission
  - special new unit "base" references board and can take damage.
  - the base spawns units

-allies
-ricochet attack off walls and the damage increases with each ricochet
-duration abilities like slow enemy movement or attack cooldown, damage up, defense up, chance to dodge, etc.
-chaining together abilities in sequence
-Gain exp from kills. Options for upgrades appear when exp bar fills and player can click choice of options. Options are just icons that appear on side of screen. Player clicks to choose. Upgrade health, speed, ability damage, etc.
-Enemies drop gold next. Or more abilities. Or more enemy behaviors. Or some obstacles with camera centered on player and player can run around in the world more.
-Abilities that punch or shoot incoming bullets out of the air

1 
2 create an exit object. When player touches it it tells board to advance to next level. Board has a level integer which increments and is used as seed to generate next level. For now all levels are the same.
	This object is a sprite with a custom draw method and special collision handling. It needs a board reference.
3 when player dies, save the player's name and level achieved to a high scores file. Voila! You have a game. Can high score code be reused? It's also in bullethell.
3A Display high scores when player dies. Give player option to reset.
4 Create a separate object mainly for organization that has enemy types, enemy attacks, and how enemies scale with level. Keep it simple. No new enemies or attacks yet and linear scaling or whatever.
5 Create an object that contains the player's scaling schedule and also a formula that determines experienced gained based on level of enemy and player's own current level. This object will keep track of player's level.
6 Get the player levle up object, player, and dead enemies all talking to each other. Draw an experience bar on the screen for the player.
7 Duplicate the player's health and mana bars next to the experience bar.
8 Auto-level up (boost health, mana, damage) of player when player levels up. Add choice of stat allocation later.
9 Change attacks to be either fixed damage amount or based on player damage.

Goal of the player: level up as high as possible as fast as possible in a clone of Diablo's hardcore mode. There's a scoreboard to get on. There's a time element as well as a survival element.

Feature list:
	Enemies drop gold, insta-health, insta-mana, and equipment.
	Shops sell random equipment, equipment upgrades, heal the player. Occassionally instead of new room, player finds a shop.
	Procedurally generate over-head rooms.
	Player can flee to next room without defeating all baddies, but sacrifices experience in the process, but maybe gains experience faster by getting to tougher enemies.
	Each room has semi-random enemies, an exit, some walls, and loot lying around.
	Progress through rooms determines enemy level. Enemy health scales faster than enemy damage scales faster than player health and damage.
	Player levels up and must determine which abilities and stats to upgrade or unlock. These are the primary choices in the game.
	Player has health, mana, and experience bars.

Abilities (including what needs implemented for them to work):
	Directional shield
		temporary status affect with a condition that affects damage received. also knowledge of hit direction
	Stun, invincibility, stasis (combination of stun and invincible)
		temporary status affects affecting movement, damage, and attacking, but not cooldowns
	Penetrating shot
		boolean on shots needed
	AoE that lingers like storm
		shots need ability to do damage per second versus one-time damage. Perhaps all you need is whether damage, which should be a double, gets multiplied by elapsed seconds or not. Health should be a double too.
	Dash
		Attacks can't be the only ability. This ability makes the player rapidly move to a given location and perhaps gives the player the ability to shove other units out of their way. This action should go on a stack of some sort since it executes instead of everything else and nothing else can happen until this is done.
	Teleport
		Similar to dash, but easier since it is instantaneous.
	Dispel missiles - all missiles are dropped out of the air
		Not too hard, just a large radius shot whose bitmask affects enemy bullets. Custom code in the collision handler needs written. Shot will need access to the Board.
	Ricochet shot
		A boolean in shot will suffice.
	Spawn temporary wall
		Worry about this after walls.
	Scare - enemies briefly flee
		Probably a status affect that adds 180 to the goal angle. Boolean and a timer needed.
	Gravity bomb - suck all enemies toward location
		This will be an object unto itself.
	Temporary all cooldown reduction
		Status affect influencing timing.
	Life steal
		Various variables in shot influencing whether or not and how much to steal
	Mana steal
		Various variables in shot influencing whether or not and how much to steal
	Regenerate life
		passive ability. This is fairly easy once we have an ability that is extended by Attack.
	Regenerate mana
		passive ability. This is fairly easy once we have an ability that is extended by Attack.
	Temporarily convert all damage into life
		Status effect interposed between received damage and player.
	Temporarily convert all damage into mana
		Status effect interposed between received damage and player.
	Meditate - stun self to regenerate life/mana
		Status effect.
	Lucky shot - single shot that, if it lands a killing blow converts the creature to a health potion equal to that creature's max life.
		Special Shot-extending object needed that only damages single target and checks if target died.
		All shots should have a boolean for whether or not they can hit multiple targets. Perhaps an integer for how many they can hit.
	Slow - slow movement speed and cooldown rate of target
		Status effect that influences elapsed time.
	Dispel - remove all spell effects from target
		Easy: remove all status effects. May need ability to click directly on enemy or player.
	Convert all in-the-air missiles into health potions
		See Dispel missiles above.
	Raining gold - convert all in-the-air missiles into money
		See Dispel missiles above.
	Counting coup - touching enemies recovers health or mana
		Probably a status effect, but we'll also have to check bit masks and see how the shove-out code works.
	Convert - mana becomes health
		A small amount of custom code in the ability.
	Bullet time - everything that is not the player moves in slow motion
		You probably want to implement this at the board level. In fact, I think it's already in GameMinimal.
	Blood lust - attacking drains own life but deals extra damage
		status effect, I think?
	180 - target's angle is flipped
		Special shot object is probably the way to go here.
	Passive aggressive - damage dealt to any enemies present over time
		Abilities can do their own thing when they update.

Abilities will need internal level up code and an integer specifying their level.

Some hits make the target temporarily invincible after being hit.
Most or all attacks have a short stun duration.
Interruptible attacks that take time before they take affect?
Need to be able to pass a position or target to the shots.
Damage to rear of enemy provides bonus damage.

A game of position and actions. Knowing when to flee, when to attack. This suggests the following:
Abilities like temporary invincibility or temporary conversion of hits into energy. Weapons with optimal ranges. Missile countermeasures that can counter all missiles currently in the air so that you might wait for more missiles to fire before using them, enemies with timed vulnerabilities such as a shield that turns on and off, ricochet shots, teleporting behind an enemy, making an enemy turn their back and flee, creating temporary barriers to clog a path, shaped damage such as lines and circles. Abilities to slow, speed up, stun, or otherwise alter movement. Creating clusters of enemies.

Use this game to compare performance of drawing black over the whole background versus blacking out small pieces of the screen?

A game of crowd management, timing, and flanking.
