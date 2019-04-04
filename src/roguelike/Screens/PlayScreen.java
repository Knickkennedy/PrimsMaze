package roguelike.Screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import roguelike.Map.*;

public class PlayScreen implements Screen {
	private int screenWidth;
	private int screenHeight;
	private int mapHeight;
	public Map newMap;
	
	public PlayScreen(){
		screenWidth = 80;
		screenHeight = 28;
		mapHeight = screenHeight - 4;
		createLevel();
		}
	
	private void createLevel(){
		newMap = new Map(screenWidth, mapHeight).buildLevel();
	}
	@Override
	public void displayOutput(AsciiPanel terminal) {
		displayTiles(terminal);
	}

	private void displayTiles(AsciiPanel terminal) {
		for (int x = 0; x < screenWidth; x++){
	        for (int y = 0; y < mapHeight; y++){
	            terminal.write(newMap.glyph(x, y), x, y, newMap.color(x, y));
	            }
	        }
	}
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()){
		case KeyEvent.VK_ESCAPE: return new LoseScreen();
		case KeyEvent.VK_ENTER: return new WinScreen();
		case KeyEvent.VK_SPACE: newMap.removeDeadEnds();
		/*case KeyEvent.VK_LEFT:
		case KeyEvent.VK_NUMPAD4: player.move(-1, 0); break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_NUMPAD6: player.move( 1, 0); break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_NUMPAD8: player.move( 0,-1); break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_NUMPAD2: player.move( 0, 1); break;
		case KeyEvent.VK_NUMPAD7: player.move(-1,-1); break;
		case KeyEvent.VK_NUMPAD9: player.move( 1,-1); break;
		case KeyEvent.VK_NUMPAD1: player.move(-1, 1); break;
		case KeyEvent.VK_NUMPAD3: player.move( 1, 1); break;*/
		}
		
		return this;
	}
}
