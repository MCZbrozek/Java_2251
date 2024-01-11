public class Spawner
{
	private static boolean TESTING_ENEMIES_ON = true;
	
	private double energy_reset = 1;
	private double energy_countdown = 0;

	private double life_reset = 5;
	private double life_countdown = 0;

	private double random_reset = 5;
	private double random_countdown = 0;

	private double asteroid_countdown = 0;

	private double[] enemy_resets =   {1.5, 2.5, 3.5, 4.5};
	private double[] enemy_countdowns={1.5, 2, 2.5, 3};

	private double alien_missile_reset = 5;
	private double alien_missile_countdown = 0;

	private double elapsed_time = 0;

	private Board board_reference;
	
	public Spawner(Board b)
	{
		board_reference=b;
	}

	public void update(double elapsed_seconds)
	{
		this.elapsed_time += elapsed_seconds;
		//energy
		energy_countdown -= elapsed_seconds;
		if(energy_countdown < 0)
		{
			board_reference.addTangible(
					Pickup.getPickup(Pickup.BOLT, 
									Main.ScreenWidth,
									Utils.random_generator.nextInt(Main.ScreenHeight-20),
									this.board_reference)
					);
			energy_countdown = energy_reset + Utils.random_generator.nextDouble();
		}
		//life
		life_countdown -= elapsed_seconds;
		if(life_countdown < 0)
		{
			board_reference.addTangible(
					Pickup.getPickup(Pickup.LIFE, 
									Main.ScreenWidth,
									Utils.random_generator.nextInt(Main.ScreenHeight-20),
									this.board_reference)
					);
			life_countdown = life_reset + Utils.random_generator.nextDouble();
		}
		//random power up
		random_countdown -= elapsed_seconds;
		if(random_countdown < 0)
		{
			board_reference.addTangible(
					Pickup.getRandomPickup(Main.ScreenWidth,
									Utils.random_generator.nextInt(Main.ScreenHeight-20),
									this.board_reference)
					);
			//Linearly increasing to a maximum
			random_countdown = Math.min((100+elapsed_time)*random_reset/500,
					random_reset + Utils.random_generator.nextDouble());
		}
		//asteroid
		asteroid_countdown -= elapsed_seconds;
		if(asteroid_countdown < 0)
		{
			board_reference.addTangible(
							new Asteroid(Main.ScreenWidth,
									Utils.random_generator.nextInt(Main.ScreenHeight-20),
									3, //size
									37, //collision radius
									"meteorBrown_big"+Integer.toString(Utils.random_generator.nextInt(4)+1)+".png",
									board_reference)
							);
			//Varying reset by a sin wave.
			asteroid_countdown =((200.0-elapsed_time)*2.0/200.0)*Math.sin(Math.PI*elapsed_time/10.0)+((200.0-elapsed_time)*2.0/200.0)+0.2;
		}
		//Alien missile
		alien_missile_countdown -= elapsed_seconds;
		if(alien_missile_countdown < 0)
		{
			board_reference.addTangible(
							new MissileSeeking(Main.ScreenWidth,
									Utils.random_generator.nextInt(Main.ScreenHeight-20),
									0, //angle
									board_reference,
									board_reference.getPlayer())
							);
			//Varying reset by a sin wave.
			alien_missile_countdown = alien_missile_reset;
		}
		//enemy
		if(TESTING_ENEMIES_ON)
		{
			int shield_level = 0; double temp;
			for(int i=0; i<enemy_resets.length; i++)
			{
				enemy_countdowns[i] -= elapsed_seconds;
				if(enemy_countdowns[i] < 0)
				{
					//Randomly, with increasing chance, give enemies shields.
					shield_level = 0;
					temp = Utils.random_generator.nextDouble();
					if(temp < elapsed_time/100.0) //After 100 seconds, all enemies have shields
					{	//Every 30 seconds, the enemy shield level increases.
						shield_level = Math.min(3, (int)(elapsed_time/30.0));
					}
					//Create the enemy.
					if(i==0)
					{	//vertically stationary weak enemy
						board_reference.addTangible(
									new Enemy(Main.ScreenWidth,
											Utils.random_generator.nextInt(Main.ScreenHeight-20),
											this.board_reference,
											"enemyRed1.png",
											false, //move vertically
											1, //health
											shield_level)
									);
					}else if(i==1)
					{	//vertically moving weak enemy
						board_reference.addTangible(
								new Enemy(Main.ScreenWidth,
										Utils.random_generator.nextInt(Main.ScreenHeight-20),
										this.board_reference,
										"enemyGreen3.png",
										true, //move vertically
										1, //health
										shield_level)
								);
					}else if(i==2)
					{	//vertically stationary strong enemy
						board_reference.addTangible(
								new Enemy(Main.ScreenWidth,
										Utils.random_generator.nextInt(Main.ScreenHeight-20),
										this.board_reference,
										"enemyBlue4.png",
										false, //move vertically
										3, //health
										shield_level)
								);
					}else
					{	//vertically moving strong enemy
						board_reference.addTangible(
								new Enemy(Main.ScreenWidth,
										Utils.random_generator.nextInt(Main.ScreenHeight-20),
										this.board_reference,
										"enemyBlack5.png",
										true, //move vertically
										3, //health
										shield_level)
								);
					}
					//Linearly decreasing reset to a minimum
					enemy_countdowns[i] = Math.max(1000*enemy_resets[i]/(100+elapsed_time),
							enemy_resets[i] + Utils.random_generator.nextDouble());
				}
			}//for(int i=0; i<enemy_resets.length; i++)
		}
	}//public void update(double elapsed_seconds)
}