package roguelike.levelBuilding;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
    FLOOR('.', AsciiPanel.brightGreen),
    ROOM_FLOOR('.', AsciiPanel.brightGreen),
    WALL('#', AsciiPanel.brightBlack),
    PERM_WALL('#', AsciiPanel.brightBlack),
    BOUNDS('X', AsciiPanel.black),
	STAIRS_DOWN('>', AsciiPanel.white),
    STAIRS_UP('<', AsciiPanel.white),
	DOOR('+', AsciiPanel.white);

    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    Tile(char glyph, Color color){
        this.glyph = glyph;
        this.color = color;
    }
    
    public boolean isDiggable() {
        return this == Tile.WALL;
    }
    
    public boolean isGround() {
        return this != Tile.WALL && this != Tile.BOUNDS;
    }
    
    public boolean isBounds(){
    	return this == BOUNDS;
    }
}
