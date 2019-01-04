package ie.gmit.sw;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.Timer;


/*
 * This is a God class and is doing way too much. The instance variables cover everything from isometric to 
 * Cartesian drawing and the class has methods for loading images and converting from one coordinate space to
 * another.
 * 
 */
public class GameView extends JPanel implements ActionListener {
	
	
	private static GameView instance = null;

	private static final long serialVersionUID = 777L;
	private static final int DEFAULT_IMAGE_INDEX = 0;
	
	//Encapsulate the things that vary...
	public static final int DEFAULT_VIEW_SIZE = 1280;
	private static final int TILE_WIDTH = 128;
	private static final int TILE_HEIGHT = 64;
	protected Sprite player;
	
	
	private ImageLoader LoadImages = new ImageLoader();
	private GetIsometric GetIso = new GetIsometric();
	//private EventManager eventmanager = new EventManager(player);

	//Do we really need two models like this?
	private int[][] matrix;
	private int[][] things;
	
	private BufferedImage[] tiles; //Note that all images, including sprites, have dimensions of 128 x 64. This make painting much simpler.
	private BufferedImage[] objects; //Taller sprites can be created, by using two tiles (head torso, lower body and legs) and improve animations
	private Color[] cartesian = {Color.GREEN, Color.GRAY, Color.DARK_GRAY, Color.ORANGE, Color.CYAN, Color.YELLOW, Color.PINK, Color.BLACK}; //This is a 2D representation
	
	private Timer timer; //Controls the repaint interval.
	private boolean isIsometric = true; //Toggle between 2D and Isometric (Z key)
	
	public GameView(int[][] matrix, int[][] things, Sprite player2) throws Exception {
		init();
		this.matrix = matrix;
		this.things = things;
		this.player = player2;
		
		setBackground(Color.WHITE);
		setDoubleBuffered(true); //Each image is buffered twice to avoid tearing / stutter
		timer = new Timer(100, this); //calls the actionPerformed() method every 100ms
		timer.start(); //Start the timer
	}
	
	public GameView() {
		// TODO Auto-generated constructor stub
	}

	private void init() throws Exception {
		//SpriteFactory builder = new SpriteFactory();
		tiles = LoadImages.loadImages("./resources/images/ground", tiles);
		objects = LoadImages.loadImages("./resources/images/objects", objects);
		player = SpriteFactory.playerInstance();
	}
	
	public void toggleView() {
		isIsometric = !isIsometric;
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) { //This is called each time the timer reaches zero
		this.repaint();
	}

	
	
	public void paintComponent(Graphics g) { //This method needs to execute quickly...
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int imageIndex = -1, x1 = 0, y1 = 0;
		Point point;
		
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				imageIndex = matrix[row][col];
				
				if (imageIndex >= 0 && imageIndex < tiles.length) {
					//Paint the ground tiles
					if (isIsometric) {
						x1 = GetIso.getIsoX(col, row);
						y1 = GetIso.getIsoY(col, row);
						
						g2.drawImage(tiles[DEFAULT_IMAGE_INDEX], x1, y1, null);
						if (imageIndex > DEFAULT_IMAGE_INDEX) {
							g2.drawImage(tiles[imageIndex], x1, y1, null);
						}
					} else {
						x1 = col * TILE_WIDTH;
						y1 = row * TILE_HEIGHT;
	        			if (imageIndex < cartesian.length) {
	        				g2.setColor(cartesian[imageIndex]);
	        			}else {
	        				g2.setColor(Color.WHITE);
	        			}
						
	        			g2.fillRect(x1, y1, TILE_WIDTH, TILE_WIDTH);
					}
					//Paint the object or things on the ground
					
					
					imageIndex = things[row][col];
					g2.drawImage(objects[imageIndex], x1, y1, null);
				}
			}
		}
		
		//Paint the player on  the ground
		point = GetIso.getIso(player.getPosition().getX(), player.getPosition().getY());
		g2.drawImage(player.getImage(), point.getX(), point.getY(), null);
	}

	public static GameView getInstance(){
		if (instance == null) {
			instance = new GameView();

		}
		return instance;
	}
}