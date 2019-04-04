package roguelike.utility;

import roguelike.levelBuilding.Map_Object;

public class Point extends Map_Object{
	public int x;
	public int y;
	public Point parent;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	  public Point(int x, int y, Point p) {
	    this.x = x;
	    this.y = y;
	    parent = p;
	   }
}
