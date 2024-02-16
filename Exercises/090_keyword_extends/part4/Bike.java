
/*
Name: Michael Zbrozek
Date: 2/16/2024
Purpose: Extends exercise
Sources:
Brightspace video
	

Files: 
Main.Java
Bike.java
Car.java
Jetski.java

*/

import java.security.SecureRandom;;

public class Bike extends Vehicle {
    private String pedestrian = "Mike";
    private double distanceTraveled = 0;
    private int timesYelled = 0;
    private String[] yellAtDrivers = { "Car's are dumb", "Yield, Jerk!", "Bikes Rule", "Get off your phone!!" };

    public void yellAtDrivers() {
        SecureRandom rand = new SecureRandom();
        int rand_int = rand.nextInt(4);
        System.out.println(pedestrian + " Says - " + yellAtDrivers[rand_int]);
        timesYelled++;
    }

    public void ride() {
        distanceTraveled = distanceTraveled + 4;
    }

    @Override
    public String toString() {
        return "\nSick Bike!"
                + " The rider is "
                + pedestrian
                + ", he has ridden "
                + distanceTraveled
                + " miles and has yelled at "
                + timesYelled
                + " drivers!";
    }
}
