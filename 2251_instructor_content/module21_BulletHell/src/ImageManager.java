import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

public class ImageManager
{	//Store all the images that have ever been loaded.
	private Hashtable<String, BufferedImage> images = new Hashtable<String, BufferedImage>();

	public ImageManager()
	{	
		this.loadAlienMissiles();
		this.loadExplosions();
		//load the sprite sheet images/Spritesheet/sheet.xml
		//Use it to load all images
		//https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
		try
		{
			//Load image sheet
			BufferedImage image_sheet = loadImage("images/sheet.png");
			//Get xml file describing how to cut out images from the image sheet.
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("images/sheet.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(input);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("SubTexture");
			System.out.println("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" 
						+ nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					System.out.println(eElement.getAttribute("name"));
					System.out.println(eElement.getAttribute("x"));
					System.out.println(eElement.getAttribute("y"));
					System.out.println(eElement.getAttribute("width"));
					System.out.println(eElement.getAttribute("height"));					
					//Get an empty and transparent buffered image of the size of the image to cut out
					BufferedImage result = new BufferedImage(
							Integer.parseInt(eElement.getAttribute("width")),
							Integer.parseInt(eElement.getAttribute("height")),
							Transparency.TRANSLUCENT);
					int x1 = Integer.parseInt(eElement.getAttribute("x"));
					int y1 = Integer.parseInt(eElement.getAttribute("y"));
					int x2 = x1 + Integer.parseInt(eElement.getAttribute("width"));
					int y2 = y1 + Integer.parseInt(eElement.getAttribute("height"));

					result = copySrcIntoDstAt(
							image_sheet,
							result,
							x1, y1, x2, y2);

					//playerShip images are upside down compared
					//to enemy ships. Fix this inconsistency here.
					if(eElement.getAttribute("name").startsWith("playerShip"))
					{
						result = getTransformedImage(result, Math.PI);
					}
					
					//Save the image in the hashtable
					this.images.put(eElement.getAttribute("name"), result);
				}
			}
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (SAXException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void loadExplosions()
	{
		//Load images in images/explosions/
		//There are 3 sheets: explosion_01_strip13.png, also 02 and 03
		int[] heights = {190,195,193};
		int[] widths = {2548,2665,2496};
		String[] image_list = {"01", "02", "03"};
		int image_count = 13;
		int width;
		for(int i=0; i<3; i++)
		{
			//Load image sheet
			System.out.println("TESTING IN ImageManager images/explosions/explosion_"+image_list[i]+"_strip13.png");
			BufferedImage image_sheet = loadImage("images/explosion_"+image_list[i]+"_strip13.png");
			//The sheet's dimensions 2548 x 190
			//The sheet has 13 explosions
			//196 is width of each individual explosion
			for (int temp = 0; temp < image_count; temp++) 
			{	//Get an empty and transparent buffered image of 
				//the size of the image to cut out
				width = widths[i]/image_count;
				BufferedImage result = new BufferedImage(
						width, //width
						heights[i], //height
						Transparency.TRANSLUCENT);
				int x1 = temp*width;
				int x2 = x1 + width;
				result = copySrcIntoDstAt(
						image_sheet,
						result,
						x1, 0, x2, heights[i]);
				//Save the image in the hashtable
				this.images.put(image_list[i]+"firey_explosion"+Integer.toString(temp), result);
			}
		}
	}

	//These missiles require a rotation to be consistent with 
	//other objects in the game.
	private void loadAlienMissiles()
	{	//http://opengameart.org/content/animated-alien-bomb-rocket
		BufferedImage result;
		for(int i=1; i<=9; i++)
		{	//Load image
			result = loadImage("images/aliendropping000"+i+".png");
			//Rotate the image
			result = getTransformedImage(result, -Math.PI/2);
			//Save the image in the hashtable
			this.images.put("alien_missile"+Integer.toString(i-1), result);
			//testing
			System.out.println("images/Alien-Bomb-by-MillionthVector/aliendropping000"+i+".png");
			System.out.println("alien_missile"+Integer.toString(i-1));
		}
	}

	/* Retrieve a copy of the image from the hashtable.
	 * If the image is not yet in the hashtable, create it. */
	public BufferedImage getImage(String image_id, double angle)
	{
		//Base case
		if(this.images.containsKey(image_id))
		{	//The image is already in the array! Return a copy of it.
			if(angle == 0)
			{
				return this.deepCopy(this.images.get(image_id));
			}
			else
			{
				return this.getTransformedImage(this.deepCopy(this.images.get(image_id)), angle);
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
	
	// ===================== BELOW THIS LINE ARE ONLY PRIVATE METHODS =====================
	
	//Get a rotated buffered image
	public BufferedImage getTransformedImage(
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
		/*System.out.println("Testing in ImageMaker.getTransformedImage");
		System.out.println(cos);
		System.out.println(sin);
		System.out.println(w);
		System.out.println(h);
		System.out.println(neww);
		System.out.println(newh);*/
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
	private BufferedImage toCompatibleImage(BufferedImage image)
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
					image.getWidth(), image.getHeight(), image.getTransparency());
		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		// return the new optimized image
		return new_image; 
	}

	//Load a buffered image
	private BufferedImage loadImage(String image_file)
	{
		BufferedImage img = null;
		try
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(image_file);
			img = ImageIO.read(input);
		}
		catch(IOException e)
		{
			System.out.println("IOException: Error loading image: '"+image_file+"'.");
			e.printStackTrace();
			System.exit(0);
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("IllegalArgumentException: Error loading image: '"+image_file+"'.");
			e.printStackTrace();
			System.exit(0);			
		}
		/* TODO TESTING Speed things up, I hope. This claim
		 * should be tested. */
		return toCompatibleImage(img);
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
	
	/* TODO There is a faster way to do the following, but this worked for me and I had trouble getting
	 * the faster way working. It would be easier if I could google questions.
	 * http://stackoverflow.com/questions/2825837/java-how-to-do-fast-copy-of-a-bufferedimages-pixels-unit-test-included
	 */
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
		for (int x = x1; x < x2; x++)
		{
            for (int y = y1; y < y2; y++)
            {
            	//TODO TESTING
            	/*System.out.println("Testing in ImageManager.");
            	System.out.println(dx);
            	System.out.println(dy);
            	System.out.println(x);
            	System.out.println(y);
            	System.out.println(src.getWidth());
            	System.out.println(src.getHeight());
            	System.out.println(dst.getWidth());
            	System.out.println(dst.getHeight());*/
            	if(!isTransparent(x, y, src))
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

	/* Detect if requested pixel is transparent */
	private boolean isTransparent(int x, int y, BufferedImage img)
	{
		//http://stackoverflow.com/questions/8978228/java-bufferedimage-how-to-know-if-a-pixel-is-transparent
		//The first byte is the alpha value.
		//int transparency = ((img.getRGB(x,y) & 0xff000000) >> 24);
		//return transparency;
		//Or
  		int pixel = img.getRGB(x,y);
  		return (pixel>>24) == 0x00;
	}
}