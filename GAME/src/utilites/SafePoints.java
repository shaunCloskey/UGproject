package utilites;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

public class SafePoints implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Point> safePoints = new ArrayList<Point>();
	
	public void addPoint(Point point)
	{
		this.safePoints.add(point);
	}
	
	public void removePoint(Point point)
	{
		this.safePoints.remove(point);
	}
	
	public ArrayList<Point> getPoints()
	{
		return this.safePoints;
	}
	
	
	public static byte [] serializeObject(Object o)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();
			
			byte[] buf = bos.toByteArray();
			
			return buf;
		} catch (IOException ioe){
			Log.e("serializeObject", "error", ioe);
			return null;
		}
	}
	
	 public static Object deserializeObject(byte[] b) 
	 { 
		 try { 
			 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b)); 
		     Object object = in.readObject(); 
		     in.close(); 
		     return object; 
		 } catch(ClassNotFoundException cnfe) { 
			 Log.e("deserializeObject", "class not found error", cnfe); 
		 
		     return null; 
		 } catch(IOException ioe) { 
		      Log.e("deserializeObject", "io error", ioe); 
		 
		      return null; 
		 } 
	 }
}
