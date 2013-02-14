package utilites;


/**
 * this class is the achitype that survivors fit
 * an array of this is maintained in the main thread that will hold a list of the players current survivors
 * 
 * needs to track if the survivor is
 * - within reinforced section
 * - skills
 * - 
 * @param mobility holds the int value for the skill value the survivor has for movement, higher skill moves more spaces per turn
 * @param scavange holds the int value for the skill value the survivor has for finding food and resources, higher skill increases chances of finding more food and resources at locations
 * @param building holds the int value for the skill value the survivor has for building structures, higher skill effects the number of turns it takes to build a structure
 * @param metab holds the value of the survivors metabolism the higher this is the more food that they consume per turn.
 * 
 * @param safe holds a boolean value for thats is used to determine if the survivor is within a safe location.
 * 
 * @author shaun
 *
 */
public class Survivor {

	private int mobility = 0;
	private int scavange = 0;
	private int building = 0;
	private int metab = 0;
	private String name= "";
	private int xPoint;
	private int yPoint;
	
	
	private boolean safe = true;
	
	public Survivor(){
		
	}
	
	public Survivor(int mob, int scav, int build, int metab, String name, int x, int y)
	{
		this.building = build;
		this.metab = metab;
		this.mobility = mob;
		this.scavange = scav;
		this.name = name;
		this.xPoint = x;
		this.yPoint = y;
	}
	
	public int getMob()
	{
		return this.mobility;
	}
	
	public void setMob(int mob)
	{
		this.mobility = mob;
	}
	
	public int getScav()
	{
		return this.scavange;
	}
	
	public void setScav(int scav)
	{
		this.scavange = scav;
	}
	
	public int getbuilding()
	{
		return this.building;
	}
	
	public void setBuilding(int build)
	{
		this.building = build;
	}
	
	public int getMet()
	{
		return this.metab;
	}
	
	public void setMet(int met)
	{
		this.metab = met;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getX()
	{
		return this.xPoint;
	}
	
	public void setX(int x)
	{
		this.xPoint = x;
	}
	
	public int getY()
	{
		return this.yPoint;
	}
	
	public void setY(int y)
	{
		this.yPoint = y;
	}
}
