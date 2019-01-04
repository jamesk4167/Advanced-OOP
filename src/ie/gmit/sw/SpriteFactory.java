package ie.gmit.sw;

import java.awt.image.BufferedImage;

public class SpriteFactory {
	
	public static Sprite playerInstance() throws Exception {
		Sprite playerSprite = new Sprite();
		
		
		playerSprite.setName("Player 1");
		playerSprite.setPosition(new Point(0,0));
		playerSprite.drawSprite(ImageLoader.loadImages("./resources/images/sprites/default", null));
		
		return playerSprite;
	}
	
	
	
	

}
