//THIS FILE WAS COPIED OVER FROM GAMEMINIMAL ON 4/1/2023

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ImageManager
{
	//Store all the images that have ever been loaded.
	private Hashtable<String, BufferedImage> images = new Hashtable<String, BufferedImage>();

	public ImageManager(){}

	/** Get a cropped copy of the image using the custom dimensions. */
	public BufferedImage getImage(
			String image_id, double angle,
			int custom_width, int custom_height)
	{
		if(angle == 0)
		{
			return this.loadImage(image_id, custom_width, custom_height);
		}
		else
		{
			return this.getTransformedImage(
					this.loadImage(image_id, custom_width, custom_height),
					image_id,
					angle,
					true);
		}
	}

	/** Retrieve a copy of the image from the
	hashtable. If the image is not yet in the
	hashtable, create it and add it to the hashtable. */
	public BufferedImage getImage(String image_id, double angle)
	{	//Base case
		if(this.images.containsKey(image_id))
		{	//The image is already in the array! Return a copy of it.
			if(angle == 0)
			{
				return this.deepCopy(this.images.get(image_id));
			}
			else
			{
				return this.getTransformedImage(
						this.deepCopy(this.images.get(image_id)),
						image_id,
						angle,
						true);
			}
		}
		else //Recursive case
		{	//Load the image
			BufferedImage bi = this.loadImage(image_id);
			//Put the image in the hashtable
			this.images.put(image_id, bi);
			//Return a copy of the image.
			return this.getImage(image_id, angle);
		}
	}

	/** Use this to add create overlayed images and add them to the hashtable, like
	 the image of the ship with engine  image over top of it. */
	public void addOverlayedImage(
			String ship_image_file,
			String engine_image_file)
	{	//If the image is already loaded, do nothing.
		if(this.images.containsKey(ship_image_file+engine_image_file))
		{
			return;
		}
		else
		{	//Get the base image
			BufferedImage base_image = this.getImage(
					ship_image_file,
					0); //angle,
			BufferedImage engine_image = this.getImage(
					engine_image_file,
					Math.PI/2); //angle
			int adjustment = -26;
			//The engines are not all the same size. Adjust as needed.
			if(engine_image_file == "engine2.png"){adjustment = -23;}
			else if(engine_image_file == "engine4.png"){adjustment = -14;}
			//For now set engines in a standard position
			int x = (int)(adjustment+base_image.getWidth()/2-engine_image.getWidth()/2);
			int y = (int)(           base_image.getHeight()/2-engine_image.getHeight()/2);
			//Copy the attachment image over the base image
			base_image = this.copySrcIntoDstAt(
					engine_image,
					this.getImage(ship_image_file,0),
					x, y);
			base_image = toCompatibleImage(base_image); //Minor speed gain
			//Add image to the hashtable.
			this.images.put(ship_image_file+engine_image_file, base_image);
		}
	}

	/**Get a rotated buffered image*/
	public BufferedImage getTransformedImage(
			BufferedImage image,
			String image_id,
			double angle,
			boolean autosave)//if false, image will not be autosaved
	{
		//Get the image from the hashtable if it's in there.
		String two_decimals = Utils.getPercentAngle(angle);
		if(this.images.containsKey(image_id+two_decimals))
		{
			return this.images.get(image_id+two_decimals);
		}
		//Otherwise create the rotated image, put it in
		//the hashtable and then return it.
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
		result = toCompatibleImage(result); //Minor speed gain
		if(autosave)
		{
			this.images.put(image_id+two_decimals, result);
		}
		return result;
	}

	/* ========== ALL FUNCTIONS AFTER THIS POINT ARE PRIVATE ========== */

	/** See if this method speeds up image loading.
	http://stackoverflow.com/questions/196890/java2d-performance-issues */
	public BufferedImage toCompatibleImage(BufferedImage image)
	{
		return this.toCompatibleImage(image,image.getWidth(), image.getHeight());
	}

	/** See if this method speeds up image loading.
	 http://stackoverflow.com/questions/196890/java2d-performance-issues */
	private BufferedImage toCompatibleImage(BufferedImage image,
			int custom_width, int custom_height)
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
		BufferedImage new_image = gfx_config.createCompatibleImage(
					custom_width, custom_height, image.getTransparency());
		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		// return the new optimized image
		return new_image; 
	}

	/** Load a buffered image.
	  * This does not save the image in the hashtable. */
	private BufferedImage loadImage(String image_file)
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
		return toCompatibleImage(img);
	}

	/** Load a buffered image and crop it to the specified dimensions.
	 * This does not save the image in the hashtable. */
	private BufferedImage loadImage(String image_file,
			int custom_width, int custom_height)
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
		return toCompatibleImage(img, custom_width, custom_height);
	}
	
	/** According to this link
	 * http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	 * This is the way to clone a buffered image. TODO Now I need to test to see if it is 
	 * faster than loading a new buffered image from file. */
	private BufferedImage deepCopy(BufferedImage bi) 
	{
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		//return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null).getSubimage(0, 0, bi.getWidth(), bi.getHeight());
	}

	/** http://stackoverflow.com/questions/2825837/java-how-to-do-fast-copy-of-a-bufferedimages-pixels-unit-test-included
	 * This is used for cutting out images from the spritesheet. */
	private BufferedImage copySrcIntoDstAt(
			BufferedImage src,
			BufferedImage dst,
			int x1, int y1,
			int x2, int y2)
	{
		if(x2 < x1 || y2 < y1)
		{
			System.out.println("WARNING in ImageManager.copySrcIntoDstAt. Negative dx or dy. Continuing without drawing attachment.");
			return dst;
		}
		boolean transparent_pixel = false;
		for (int x = x1; x < x2; x++)
		{
			for (int y = y1; y < y2; y++)
			{
				try{
					transparent_pixel = isTransparent(x, y, src);
				}catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
					System.out.println("\n\nCaught checking transparent pixel in src.");
					System.out.println("x: "+Integer.toString(x));
					System.out.println("y: "+Integer.toString(y));
					System.out.println("src width: "+Integer.toString(src.getWidth()));
					System.out.println("src height: "+Integer.toString(src.getHeight()));
					System.out.println("dst width: "+Integer.toString(dst.getWidth()));
					System.out.println("dst height: "+Integer.toString(dst.getHeight()));
					System.exit(0);
				}
				if(!transparent_pixel)
				{
					try{
						dst.setRGB( x-x1, y-y1, src.getRGB(x,y) );
					}catch(ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
						System.out.println("\n\nx: "+Integer.toString(x));
						System.out.println("y: "+Integer.toString(y));
						System.out.println("src width: "+Integer.toString(src.getWidth()));
						System.out.println("src height: "+Integer.toString(src.getHeight()));
						System.out.println("dst width: "+Integer.toString(dst.getWidth()));
						System.out.println("dst height: "+Integer.toString(dst.getHeight()));
						System.exit(0);
					}
				}
			}
		}
		return dst;
	}

	/** http://stackoverflow.com/questions/2825837/java-how-to-do-fast-copy-of-a-bufferedimages-pixels-unit-test-included
	 * Copy src into dst at location x1, y1.
	 * This is used for overlaying images. */
	private BufferedImage copySrcIntoDstAt(
			BufferedImage src,
			BufferedImage dst,
			int x1, int y1)
	{
		boolean transparent_pixel = false;
		for (int x=0; x<src.getWidth(); x++)
		{
			for (int y=0; y<src.getHeight(); y++)
			{
				transparent_pixel = isTransparent(x, y, src);
				if(!transparent_pixel)
				{
					try{
						dst.setRGB( x+x1, y+y1, src.getRGB(x,y) );
					}catch(ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
						System.out.println("\n\nx: "+Integer.toString(x));
						System.out.println("y: "+Integer.toString(y));
						System.out.println("src width: "+Integer.toString(src.getWidth()));
						System.out.println("src height: "+Integer.toString(src.getHeight()));
						System.out.println("dst width: "+Integer.toString(dst.getWidth()));
						System.out.println("dst height: "+Integer.toString(dst.getHeight()));
						System.exit(0);
					}
				}
			}
		}
		return dst;
	}

	/** Detect if requested pixel is transparent */
	private boolean isTransparent(int x, int y, BufferedImage img)
	{
		//http://stackoverflow.com/questions/8978228/java-bufferedimage-how-to-know-if-a-pixel-is-transparent
		//The first byte is the alpha value.
		return (img.getRGB(x,y)>>24) == 0x00;
	}

}