package ie.gmit.sw;

import java.awt.image.BufferedImage;

public interface Spriator {

	public String getName();
	public void setName(String name);

	public Point getPosition();
    public void setPosition(Point Position);

	public void drawSprite(BufferedImage[] img);
	

}