package testing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable{
	
	int x, y, xDirection, yDirection	;
	private Image doubleBufferImage;
	private Graphics doubleBufferGraphics;
	Image face;
	Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 30);
	
	//Function handling the game running
	public void run(){
		try{
			
			while(true){
				//Constantly call the move function while monitoring it
				move();
				
				//How much time (in milliseconds) is the thread put to sleep
				//This is very important as the computer will try to run at the fastest speed possible otherwise
				//To make it go faster, decrease the value below and vice versa
				Thread.sleep(5);
				
				
			}
		}
		catch(Exception e){
			System.out.println("Error");
		}
	}
	
	//Function handling the ball movement
	public void move(){
		//Move the ball
		x += xDirection;
		y += yDirection;
		
		//Handle basic, non-elastic collision
		if(x <= 0){
			//Reverse the direction once the limit is hit, giving a bouncing effect
			setXDirection(1);
		}
		//1024 - 50 = 974 pixels due to ball size
		if(x >= 974){
			//Reverse the direction once the limit is hit, giving a bouncing effect
			setXDirection(-1);
		}
		//0 + 25 = 25 pixels due to window menu
		if(y <= 25 ){
			//Reverse the direction once the limit is hit, giving a bouncing effect
			setYDirection(1);
		}
		//768 - 50 = 718 pixels due to ball size
		if(y >= 718){
			//Reverse the direction once the limit is hit, giving a bouncing effect
			setYDirection(-1);
		}
	}
	
	//Function implementing the x-axis direction of the ball
	public void setXDirection(int xDir){
		xDirection = xDir;
	}
	
	//Function implementing the y-axis direction of the ball
	public void setYDirection(int yDir){
		yDirection = yDir;
	}
	
	//Function handling keys pressed
	public class ActionListener extends KeyAdapter{
		//Method for when a key is pressed
		public void keyPressed(KeyEvent e){
			//Stores value of key pressed by user in keyCode variable
			int keyCode = e.getKeyCode();
			
			if(keyCode == e.VK_LEFT){
				setXDirection(-1);
			}
			if(keyCode == e.VK_RIGHT){
				setXDirection(1);
			}
			if(keyCode == e.VK_UP){
				//Y axis is on top left of screen hence -1 as up
				setYDirection(-1);
			}
			if(keyCode == e.VK_DOWN){
				setYDirection(1);
			}
		}
		
		//Method for when a key is released
		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();
			
			if(keyCode == e.VK_LEFT){
				setXDirection(-1);
			}
			if(keyCode == e.VK_RIGHT){
				setXDirection(1);
			}
			if(keyCode == e.VK_UP){
				//Y axis is on top left of screen hence -1 as up
				setYDirection(-1);
			}
			if(keyCode == e.VK_DOWN){
				setYDirection(1);
			}
			
		}
	}
	
	//Function handling the game window
	public Game(){
		//Load tile
		ImageIcon tile = new ImageIcon("C:/Users/Sylvain/Documents/Java/testing/src/testing/coin.png"); 
		face = tile.getImage();
		
		//Game properties
		//Takes care of keys pressed
		addKeyListener(new ActionListener());
		//Title of the window
		setTitle("The best game ever");
		//Size of window (1024x768)
		setSize(1024,768);
		//Allow or not users to resize the window created
		setResizable(false);
		//Allow or not the window to be visible
		setVisible(true);
		//Changes background color
		setBackground(Color.CYAN);
		//Stops the program running when the window is closed (memory issues otherwise)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Default position of shape
		x = 500;
		y = 300;
	}
	
	//Function which just makes the ball move instead of act like a paint brush painting the screen
	//Essentially prevents the game from leaving the previous position drawn on the screen
	public void paint (Graphics g){
		doubleBufferImage = createImage(getWidth(), getHeight());
		doubleBufferGraphics = doubleBufferImage.getGraphics();
		paintComponent(doubleBufferGraphics);
		g.drawImage(doubleBufferImage, 0, 0, this);
	}
	
	//Function drawing the shape on the window
	public void paintComponent(Graphics g){
		//Write text
		g.setFont(font);
		g.setColor(Color.MAGENTA);
		g.drawString("Hello tile, I am Magenta!", 100, 100);
		
		//Draw a tile
		g.drawImage(face, x, y, this);
		
		//Clears the window of any previous objects
		repaint();
	}
	
	//Main function run
	public static void main(String[] args){
		//Make a new game
		Game FirstGame =  new Game();
		
		//Threads
		Thread t1 = new Thread(FirstGame);
		t1.start();
		
	}
	
}
