package utilites;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import android.util.Log;

public class Survivors{

	/**
	 * 
	 */
	
	Survivor empty = new Survivor(0, 0, 0, 0, "empty",0,0);
	private Survivor [] survivors = new Survivor [5];
	
	public Survivors()
	{
		survivors[0] = empty;
		survivors[1] = empty;
		survivors[2] = empty;
		survivors[3] = empty;
		survivors[4] = empty;
		
	}
	
	public Survivors(Survivor one, Survivor two, Survivor three)
	{
		survivors[0] = one;
		survivors[1] = two;
		survivors[2] = three;
		survivors[3] = empty;
		survivors[4] = empty;
	}
	
	public Survivors(Survivor one, Survivor two, Survivor three, Survivor four, Survivor five)
	{
		survivors[0] = one;
		survivors[1] = two;
		survivors[2] = three;
		survivors[3] = four;
		survivors[4] = five;
	}
	
	public void removeSurvivors(String name)
	{
		for(int i =0; i<survivors.length; i++)
		{
			if(name.equals(survivors[i].getName()))
			{
				survivors[i] = empty;
				break;
			}
		}
	}
	
	public Survivor [] getSurvivors()
	{
		return this.survivors;
	}
	
	public void setSurvivor(Survivor survivor, int index)
	{
		survivors[index] = survivor;
	}
	
	public void setSurvivors( Survivor [] newSurvivors )
	{
		this.survivors = newSurvivors;
	}
}
