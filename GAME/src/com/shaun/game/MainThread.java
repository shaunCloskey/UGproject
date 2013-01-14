package com.shaun.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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

	//survivors is a hash map that contains the key is the survivors name, the value is the object of class Survivor
	private HashMap<String, Survivor> survivors = new HashMap<String, Survivor>(5);
	private Iterator it = survivors.entrySet().iterator();
	
	private probHandler prob;
	private int food = 50;
	private int dogRed = 1;
	private int banditRed = 2;
	private int fireRed = 4;
	private int desertRed = 5;
	
	private String [] survivorNames ={"bob", "john", "kate", "morgan", "paul", "mary", "liam" , "mark" , "peter" , "greg" , "andrew" , "ed" , "pong" , "jimmy" , "trent" , "sarah" , "cazz" , "mickeal" , "jerry" , "elly"}; 
	private List <String> knownSurvivors = new ArrayList<String>();
	
	/**
	 * method to remove a survivor from the list of survivors
	 */
	public void removeSurvivor(Survivor s)
	{
		String name = s.getName();
		if( survivors.containsKey(name) )
		{
			survivors.remove(name);
		}
	}
	
	/**
	 * method to add a survivor to the list of survivors
	 */
	public void addSurvivor(Survivor s)
	{
		survivors.put(s.getName(), s);
	}
	
	/**
	 * method to decide what the player has found on the square they have scavenged on
	 * need to consider the place on the grid, survivor skill level to determine the food amount
	 */
	public Vector scavangeEvent(Survivor s, Point coordinate)
	{
				
		Vector<Integer> v = new Vector<Integer>();
		// take the point and create the maximum food for the square using the distance of the point from Home point 
		Random generator = new Random();
		int maxFood = generator.nextInt(3);
		
		// take the skill of the survivor and work out how much food is lost
		int foodSca = s.getScav();
		int addedFood = generator.nextInt(foodSca);
		
		int foodVal = maxFood + addedFood;
		v.add(foodVal);
		
		//do above for resources
		int maxResources = generator.nextInt(3);
		
		int resSca = s.getScav();
		int addedRes = generator.nextInt(resSca);
		
		int resVal = maxResources + addedRes;
		v.add(resVal);
		
		if(knownSurvivors.size() != survivorNames.length)
		{
			presentNewSurvivor();
		}
		
		return v;
	}
	
	
	/**
	 * called when scavanging a location, it will work out if the player has run into a new survivor
	 * and handles the finding of new survivor
	 */
	private void presentNewSurvivor() {
		Survivor poSurvivor = getNewSurvivor();
		
		
	}

	/**
	 * gets a new Survivor for the player
	 */
	private Survivor getNewSurvivor()
	{
		Survivor newSurvivor;
		while(true)
		{
			//get random new survivor 
			Random generator = new Random();
			int surInt  = generator.nextInt(20);
			String newName = survivorNames[surInt];
			
			if(!knownSurvivors.contains(newName))
			{
				int mob = generator.nextInt(5) + 1;
				int scav = generator.nextInt(5) + 1;
				int build = generator.nextInt(5) + 1;
				int metab = generator.nextInt(5) + 1;
				newSurvivor = new Survivor(mob, scav, build, metab, newName);
				break;
			}
		}
		
		return newSurvivor;
		
		
	}

	/**
	 * method that removes a replaces a survivors from a list
	 * @param sOld it is the survivor to be removed form the list
	 * @param sNew the survivor to be added to the list
	 */
	public void replaceSurvivor(Survivor sOld, Survivor sNew)
	{
		removeSurvivor(sOld);
		addSurvivor(sNew);
		
		knownSurvivors.add(sNew.getName());
	}
	
	/**
	 * method to ignore a survivor from a list
	 */
	public void ignore(Survivor s)
	{
		knownSurvivors.add(s.getName());
		
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
