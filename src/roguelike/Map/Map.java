package roguelike.Map;

import java.awt.Color;
import java.util.*;
import roguelike.utility.*;
import roguelike.levelBuilding.*;

public class Map{
	public Tile[][] map;
	public int width;
	public int height;
	public Point start, current;
	public List <Point> frontier = new ArrayList <Point> ();
	public List <Point> toRemove = new ArrayList <Point> ();
	public List <Point> deadEnds = new ArrayList <Point> ();
	public List <Point> potentialDoors = new ArrayList <Point> ();
	public List <Point> connections = new ArrayList <Point> ();
	public List <String> test = new ArrayList <String> ();
	public List <Room> rooms = new ArrayList <Room> ();
	public int maxRoomSize;
	public int minRoomSize;
	public int numRoomTries;
	public boolean[][] connected;

	public Map(int width, int height){
		this.width = width;
		this.height = height;
		map = new Tile[width][height];
		this.minRoomSize = 3;
		this.maxRoomSize = 8;
		this.numRoomTries = 100;
		this.connected = new boolean[width][height];}

	public int getWidth(){
		return this.width;}
	
	public int getHeight(){
		return this.height;}
	
	public char glyph(int x, int y){
		return map[x][y].glyph();}
	
	public Color color(int x, int y){
		return map[x][y].color();}
	
	public Map buildLevel(){
		for (int x = 0; x < this.width; x++){
			for(int y = 0; y < this.height; y++){
				map[x][y] = Tile.WALL;
			}
		}
		startMaze();
		
		return this;}

	public void startMaze(){
		for(int i = 1; i < width - 2; i++){
			for(int j = 1; j < height - 2; j++){
				if(isSolid(i, j)){
					generateMaze(i, j);
				}
			}
		}
	}
	
	public boolean isSolid(int x, int y){
		if((map[x][y] == Tile.WALL) 
			&& (map[x + 1][y] == Tile.WALL)
			&& (map[x - 1][y] == Tile.WALL)
			&& (map[x][y - 1] == Tile.WALL)
			&& (map[x][y + 1] == Tile.WALL)
			&& (map[x+1][y+1] == Tile.WALL)
			&& (map[x+1][y-1] == Tile.WALL)
			&& (map[x-1][y+1] == Tile.WALL)
			&& (map[x-1][y-1] == Tile.WALL)){
				return true;
			}
		else{
			return false;
		}
	}
	
	public void generateMaze(int x, int y){
		Point start = new Point(x, y, null);
		map[start.x][start.y] = Tile.FLOOR;
		buildFrontier(start);
		updateFrontier();
		while(!frontier.isEmpty()){
			Point current = frontier.remove(RandomGen.rand(0, frontier.size() - 1));
			carvePath(current);
			buildFrontier(current);
			updateFrontier();
		}
	}
	
	public void buildFrontier(Point p){
		if((p.x + 2 <= width - 1) && (p.y - 1 >= 0) && (p.y + 1 <= height - 1)){ // East
			if(map[p.x + 1][p.y] == Tile.WALL){
				if(map[p.x + 2][p.y] != Tile.FLOOR){
					if(map[p.x + 1][p.y - 1] != Tile.FLOOR){
						if(map[p.x + 1][p.y + 1] != Tile.FLOOR){
							if(map[p.x + 2][p.y + 1] != Tile.FLOOR){
								if(map[p.x + 2][p.y - 1] != Tile.FLOOR){
									frontier.add(new Point(p.x + 1, p.y, p));
								}
							}
						}
					}
				}
			}
		}
		if((p.x - 2 >= 0) && (p.y - 1 >= 0) && (p.y + 1 <= height - 1)){ // West
			if(map[p.x - 1][p.y] == Tile.WALL){
				if(map[p.x - 2][p.y] != Tile.FLOOR){
					if(map[p.x - 1][p.y - 1] != Tile.FLOOR){
						if(map[p.x - 1][p.y + 1] != Tile.FLOOR){
							if(map[p.x - 2][p.y + 1] != Tile.FLOOR){
								if(map[p.x - 2][p.y - 1] != Tile.FLOOR){
									frontier.add(new Point(p.x - 1, p.y, p));
								}
							}
						}
					}
				}
			}
		}
		if((p.x - 1 >= 0) && (p.x + 1 <= width - 1) && (p.y - 2 >= 0)){ // North
			if(map[p.x][p.y - 1] == Tile.WALL){
				if(map[p.x][p.y - 2] != Tile.FLOOR){
					if(map[p.x - 1][p.y - 1] != Tile.FLOOR){
						if(map[p.x + 1][p.y - 1] != Tile.FLOOR){
							if(map[p.x + 1][p.y - 2] != Tile.FLOOR){
								if(map[p.x - 1][p.y - 2] != Tile.FLOOR){
									frontier.add(new Point(p.x, p.y - 1, p));
								}
							}
						}
					}
				}
			}
		}
		if((p.x - 1 >= 0) && (p.x + 1 <= width - 1) && (p.y + 2 <= height - 1)){ // South
			if(map[p.x][p.y + 1] == Tile.WALL){
				if(map[p.x][p.y + 2] != Tile.FLOOR){
					if(map[p.x - 1][p.y + 1] != Tile.FLOOR){
						if(map[p.x + 1][p.y + 1] != Tile.FLOOR){
							if(map[p.x + 1][p.y + 2] != Tile.FLOOR){
								if(map[p.x - 1][p.y + 2] != Tile.FLOOR){
									frontier.add(new Point(p.x, p.y + 1, p));
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void updateFrontier(){
		for(int i = 0; i < frontier.size() - 1; i++){
			int floorCount = 0;
			if(frontier.get(i).x > frontier.get(i).parent.x){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
			}
			if(frontier.get(i).x < frontier.get(i).parent.x){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
			}
			if(frontier.get(i).y < frontier.get(i).parent.y){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
			}
			if(frontier.get(i).y > frontier.get(i).parent.y){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
			}
				if(floorCount > 1){
					toRemove.add(frontier.get(i));
				}
		}
		for(Point p : toRemove){
			frontier.remove(p);
		}
	}
	
	public void carvePath(Point s){
		map[s.x][s.y] = Tile.FLOOR;
	}
	public void placeRooms(){
		int h = RandomGen.rand(minRoomSize, maxRoomSize);
		if(h % 2 == 0){
			h = h + 1;
		}
		int w = RandomGen.rand(h, maxRoomSize);
		if(w % 2 == 0){
			w = w + 1;
		}
		int x = RandomGen.rand(1, (this.width - w - 2));
		int y = RandomGen.rand(1, (this.height - h - 2));
		if(x % 2 == 0){
			x += 1;
		}
		if(y % 2 == 0){
			y += 1;
		}
		
		Room newRoom = new Room(x, y, w, h);
		
		boolean failed = false;
		for(Room otherRoom : rooms){
			if(newRoom.intersects(otherRoom)){
				failed = true;
				break;
			}
		}
		if(!failed){
			createRoom(newRoom);
			rooms.add(newRoom);
		}
	}
	public void createRoom(Room newRoom){
		for(int i = 0; i < newRoom.width ; i++){
			for(int j = 0; j < newRoom.height; j++){
				map[newRoom.getX1() + i][newRoom.getY1() + j] = Tile.FLOOR;
			}
		}
	}
	
	public void findDeadEnds(){
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(map[x][y] == Tile.FLOOR){
				int wallCount = 0;
					if(map[x][y-1] == Tile.WALL){
						wallCount++;
					}
					if(map[x-1][y] == Tile.WALL){
						wallCount++;
					}
					if(map[x+1][y] == Tile.WALL){
						wallCount++;
					}
					if(map[x][y+1] == Tile.WALL){
						wallCount++;
					}
					if(wallCount >= 3){
						Point newP = new Point(x, y);
						deadEnds.add(newP);
					}
				}
			}
		}
	}
	public void removeDeadEnds(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				int wallCount = 0;
				if(map[i][j] == Tile.FLOOR){
					if((map[i - 1][j] == Tile.WALL) || (map[i - 1][j] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i + 1][j] == Tile.WALL) || (map[i + 1][j] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i][j - 1] == Tile.WALL) || (map[i][j - 1] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i][j + 1] == Tile.WALL) || (map[i][j + 1] == Tile.PERM_WALL)){
						wallCount++;
					}
					if(wallCount >= 3){
						Point newP = new Point(i, j);
						deadEnds.add(newP);
					}
				}
			}
		}
		for(Point p : deadEnds){
			map[p.x][p.y] = Tile.WALL;
		}
		deadEnds.clear();
	}
	
	public Tile tile(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.BOUNDS;
		else
			return map[x][y];}
}

