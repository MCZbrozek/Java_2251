import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class ImageManager
{
	//Store all the images that have ever been loaded.
	private Hashtable<String, BufferedImage> images = new Hashtable<String, BufferedImage>();

	public ImageManager()
	{
		
	}
	
	/* Retrieve a copy of the image from the hashtable.
	 * If the image is not yet in the hashtable, create it. */
	public BufferedImage getImage(String image_id, double angle,
			int custom_width, int custom_height, boolean use_custom)
	{
		//Special case for getting cropped images.
		if(use_custom)
		{
			return this.loadImage(image_id,
					custom_width, custom_height, use_custom);
		}
		//Base case
		else if(this.images.containsKey(image_id))
		{	//The image is already in the array! Return a copy of it.
			if(angle == 0)
			{
				return this.deepCopy(this.images.get(image_id));
			}
			else
			{
				return this.createTransformedImage(this.deepCopy(this.images.get(image_id)), angle);
			}
		}
		else //Recursive case
		{	//Load the image
			BufferedImage bi = this.loadImage(image_id,
							0, 0, false);
			//Put the image in the hashtable
			this.images.put(image_id, bi);
			//Return a copy of the image.
			return this.getImage(image_id, angle,
					custom_width, custom_height, use_custom);
		}
	}
	
	// ===================== BELOW THIS LINE ARE ONLY PRIVATE METHODS =====================
	
	//Load a rotated buffered image
	private BufferedImage createTransformedImage(
			BufferedImage image, 
			double angle)
	{
		double sin = Math.abs(Math.sin(angle));
		double cos = Math.abs(Math.cos(angle));
		//Get the dimensions of the image to be rotated.
		int w = image.getWidth();
		int h = image.getHeight();
		//Calculate the width and height the image will be after its rotation
		int neww = (int) Math.floor(w * cos + h * sin);
		int newh = (int) Math.floor(h * cos + w * sin);
		//Get an empty and transparent buffered image of the new size
		BufferedImage result = new BufferedImage(neww, newh, Transparency.TRANSLUCENT);
		Graphics2D g2d = result.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//Translate and rotate the buffered image
		g2d.translate((neww - w) / 2, (newh - h) / 2);
		g2d.rotate(angle, w / 2, h / 2);
		//Draw the old image on this newly rotated image and return it.
		g2d.drawRenderedImage(image, null);
		g2d.dispose();
		return result;
	}

	//See if this method speeds up image loading.
	//http://stackoverflow.com/questions/196890/java2d-performance-issues
	private BufferedImage toCompatibleImage(BufferedImage image, 
			int custom_width, int custom_height, boolean use_custom)
	{
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
				getLocalGraphicsEnvironment().getDefaultScreenDevice().
				getDefaultConfiguration();
		/*
		 * if image is already compatible and optimized for current system 
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
		{
			return image;
		}
		// image is not optimized, so create a new image that is
		BufferedImage new_image;
		if(use_custom)
		{
			//System.out.println("Using custom width in Sprite.toCompatibleImage: "+
			//		Integer.toString(custom_width)+", "+Integer.toString(custom_height));
			new_image = gfx_config.createCompatibleImage(
					custom_width, custom_height, image.getTransparency());
		}
		else
		{
			new_image = gfx_config.createCompatibleImage(
					image.getWidth(), image.getHeight(), image.getTransparency());
		}
		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		// return the new optimized image
		return new_image; 
	}

	//Load a buffered image
	private BufferedImage loadImage(String image_file,
			int custom_width, int custom_height, 
			boolean use_custom)
	{
		BufferedImage img = null;
		try 
		{
			img = ImageIO.read(new File(image_file));
		}
		catch (IOException e) 
		{
			System.out.println("Error loading image: '"+image_file+"'.");
			e.printStackTrace();
			System.exit(0);
		}
		/* TODO TESTING Speed things up, I hope. This claim
		 * should be tested. */
		return toCompatibleImage(img, custom_width, custom_height, 
				use_custom);
	}
	
	/* According to this link
	 * http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	 * This is the way to clone a buffered image. TODO Now I need to test to see if it is 
	 * faster than loading a new buffered image from file.
	 */
	private BufferedImage deepCopy(BufferedImage bi) 
	{
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		//return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null).getSubimage(0, 0, bi.getWidth(), bi.getHeight());
	}

	/* Currently unused
	 * Detect if requested pixel is transparent
	 */
	/*private int isTransparent(int x, int y, BufferedImage img)
	{
		//http://stackoverflow.com/questions/8978228/java-bufferedimage-how-to-know-if-a-pixel-is-transparent
		//The first byte is the alpha value.
		int transparency = ((img.getRGB(x,y) & 0xff000000) >> 24);
		//Or
		/*
  		int pixel = img.getRGB(x,y);
  		if( (pixel>>24) == 0x00 ) {
  			return true;
  		}
		 */
	//	return transparency;
	//}	
}