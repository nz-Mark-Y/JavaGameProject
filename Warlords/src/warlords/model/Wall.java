package warlords.model;
import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import warlords.tests.IWall;

public class Wall implements IWall {
	private int x;
	private int y;
	public static int height = 30;
	public static int length = 30; 
	private boolean destroyed;
	private int belongsTo;
	private Rectangle wallView;
	private AudioClip wallExplosion = new AudioClip(new File("sounds/explosion.wav").toURI().toString());

	public Wall(int owner) {
		x = 0;
		y = 0;
		belongsTo = owner;
		destroyed = false;
	}

	public Wall(int x, int y, int owner) {
		this.x = x;
		this.y = y;
		belongsTo = owner;
		destroyed = false;
	}

	@Override
	public void setXPos(int x) {
		this.x = x;
	}

	@Override
	public void setYpos(int y) {
		this.y = y;
	}

	public int getXPos() {
		return x;
	}

	public int getYPos() {
		return y;
	}

	public int getOwner() {
		return belongsTo;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	public void destroy() {
		destroyed = true;

		//Play sound
		wallExplosion.play();

		//Remove from the player's view
		wallView.setVisible(false);
	}

	public void setWallView(Rectangle input) {
		wallView = input;
	}

	public Rectangle getWallView() {
		return wallView;
	}
}
