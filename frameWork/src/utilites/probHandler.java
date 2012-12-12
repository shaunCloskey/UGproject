package utilites;

import java.util.*;

public class probHandler {

	/*
	 * events
	 * dogs 1-4 of 12 lose 1 food
	 * bandit 8-10 of 12 lose 2 food
	 * fire 6-8 of 12 lose 4 food
	 * survivor leaves 4 or 10 of 12 lose 5 food and one survivor
	 * 
	 */
	
	/**
	 * check if the dog event has occured
	 * 
	 * @return a boolean that tell if the event has occured
	 * 
	 */
	public boolean eventDog(Survivor s)
	{
		Random generator = new Random();
		int roll = generator.nextInt(12) + 1;
		if(roll>=1 && roll <=4)
		{
			return true;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * check if the bandit event has occured
	 * 
	 * @return a boolean that tell if the event has occured
	 * 
	 */
	public boolean eventBandit(Survivor s)
	{
		Random generator = new Random();
		int roll = generator.nextInt(12) + 1;
		if(roll>=8 && roll <=10)
		{
			return true;
		}
		else
		{
			return true;
		}
	}
	
	
	/**
	 * check if the fire event has occured
	 * 
	 * @return a boolean that tell if the event has occured
	 * 
	 */
	public boolean eventFire(Survivor s)
	{
		Random generator = new Random();
		int roll = generator.nextInt(12) + 1;
		if(roll>=6 && roll <=8)
		{
			return true;
		}
		else
		{
			return true;
		}
	}
	
	
	/**
	 * check if the desert event has occured
	 * 
	 * @return a boolean that tell if the event has occured
	 * 
	 */
	public boolean eventDesert(Survivor s)
	{
		Random generator = new Random();
		int roll = generator.nextInt(12) + 1;
		if(roll==4 || roll ==10)
		{
			return true;
		}
		else
		{
			return true;
		}
	}
	
}
