package framework.com;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import android.graphics.Point;

import utilites.Survivor;
import utilites.probHandler;



/*
 * events
 * dogs 1-4 of 12 lose 1 food
 * bandit 8-10 of 12 lose 2 food
 * fire 6-8 of 12 lose 4 food
 * survivor leaves 4 or 10 of 12 lose 5 food and one survivor
 * 
 */

public class MainThread {

	private HashMap<String, Survivor> survivors = new HashMap<String, Survivor>(5);
	private Iterator it = survivors.entrySet().iterator();
	private probHandler prob;
	private int food = 50;
	private int dogRed = 1;
	private int banditRed = 2;
	private int fireRed = 4;
	private int desertRed = 5;
	
	
	/**
	 * method to decide what the player has found on the square they have scavenged on
	 * need to consider the place on the grid, survivor skill level to determine the food amount
	 */
	public int scavangeEvent(Survivor s, Point coordinate)
	{
		// take the point and create the maximum food for the square using the distance of the point from Home point 
		Random generator = new Random();
		int maxFood = generator.nextInt(3);
		
		// take the skill of the survivor and work out how much food is lost
		int scavange = s.getScav();
		int addedFood = generator.nextInt(scavange);
		
		return maxFood + addedFood;
		
		//do above for resources
		
	}
	
	
	
	/**
	 * runs though all the probabilities for the game and determines if an event has occured
	 * 
	 */
	public void betweenTurnEvents(int turn )
	{
		//run through all survivors and determine if events have occurred
		if(turn > 2)
		{
			//iterate throught the whole hashmap survivor and run events
			while(it.hasNext())
			{
				HashMap.Entry pairs = (HashMap.Entry)it.next();
				Survivor s = (Survivor) pairs.getValue();
				
				
				boolean dog = prob.eventDog(s);
				boolean bandit = prob.eventBandit(s);
				boolean fire = prob.eventFire(s);
				boolean desert = prob.eventDesert(s);
				
				//use the values returned from the methods to find the values for the food lost and display the list of events to the player etc
				if(dog)
				{
					food -= dogRed;
				}
				
				if(bandit)
				{
					food -= banditRed;
				}
				
				if(fire)
				{
					food -= fireRed;
				}
				
				if(desert)
				{
					food -= desertRed;
					//remove the said survivor from the list
					survivors.remove(pairs.getKey());
				}
			}
		}
		
		
	}
}
