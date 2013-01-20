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
	
	private Survivor [] survivors = new Survivor [5];
	
	public Survivors()
	{
		
	}
	
	public Survivors(Survivor one, Survivor two, Survivor three, Survivor four, Survivor five)
	{
		survivors[0] = one;
		survivors[1] = two;
		survivors[2] = three;
		survivors[3] = four;
		survivors[4] = five;
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
