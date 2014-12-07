
/**
 * @author : DuffauJohnathan , RabaudEliot , ColinRenaud
 */

package PlanEvac;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.TurtleKit;
import turtlekit.kernel.TurtleKit.Option;
import Utiles.Fenetre;
import Utiles.MethodeUtileEnv;
import Utiles.MyViewer;

public class PlanEvacuation extends TKEnvironment{

	/**
	 * buffer which store the picture coordinates
	 */
	private static BufferedImage buffer;
	/**
	 * couleur permettant de d��limiter les images plus blanches ou plus noires
	 */
	private final int couleurLimite = 142 ;
	/**
	 * room color
	 */
	private final int couleurSalle = 255; // white wall
	/**
	 * Wall color
	 */
	private final int couleurMur = 1; // black room
	/**
	 * size of the outside
	 */
	private final static int arroundSize = 50;
	
	/**
	 * read the buffer and build the TK grid
	 */
	public void activate() {
		
		try {
			buffer = MethodeUtileEnv.importerImage(getMadkitProperty("plan")); // picture's import
		} catch (IOException e) {e.printStackTrace();buffer = null;}
		System.out.println(getMadkitProperty("plan"));
		MethodeUtileEnv.adapterImage(buffer,couleurSalle,couleurMur,couleurLimite);	
		setMadkitProperty(TurtleKit.Option.envWidth, ""+(buffer.getWidth()+arroundSize));
		setMadkitProperty(TurtleKit.Option.envHeight, ""+(buffer.getHeight()+arroundSize));
		
		super.activate();
		// current patch'color modification according to current buffer element 
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = buffer.getHeight()-1; j > 0; j--) 		
				getPatch(i,j).setColor(new Color(buffer.getRGB(i, j))); 
		}	
		// the agent who are not between the walls are outside so they are green
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = buffer.getHeight()-1; j > 0; j--) {
				if( !betweenWalls( buffer,  i,  j) )
					getPatch(i,j).setColor( Color.green );
			}
		}	
		// top bordure
		for (int i = buffer.getWidth(); i<buffer.getWidth()+arroundSize; i++) {  
			for (int j = 0; j < getHeight(); j++) 
				getPatch(i,j).setColor(Color.green);
		}
		// left bordure
		for (int i = 0; i < buffer.getWidth()+arroundSize; i++) {
			for (int j = buffer.getHeight(); j < buffer.getHeight()+arroundSize; j++) 
				getPatch(i,j).setColor(Color.green);
		}		
		
	}
	/**
	 * @return true if there is a wall on the top of the agent , false else
	 * @param i 
	 * @param j
	 * @param h 
	 * 
	 */
	public boolean wallTop(int i, int j , int h) {
		if(j>h)
			return false;
		else if(getPatch(i,j).getColor().getRed() == 255)
			return true;
		return wallTop(i,j+1,h);
	}
	
	/**
	 * @param i
	 * @param j
	 * @param w
	 * @return
	 */
	public boolean wallRight(int i, int j , int w) {
		if(i>w)
			return false;
		else if(getPatch(i,j).getColor().getRed() == 255)
			return true;
		return wallTop(i+1,j,w);
	}
	
	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean wallBot(int i, int j) {
		if(j<0)
			return false;
		else if(getPatch(i,j).getColor().getRed() == 255)
			return true;
		return wallBot(i,j-1);
	}
	/**
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean wallLeft(int i, int j) {
		if(i<0)
			return false;
		else if( getPatch(i,j).getColor().getRed() == 255)
			return true;
		return wallBot(i-1,j);
	}
	/**
	 * 
	 * @param buffer
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean betweenWalls(BufferedImage buffer, int i, int j){
		return (wallTop(i,j,buffer.getWidth()) && wallBot(i,j) ) || wallLeft(i,j) && wallRight(i,j,buffer.getHeight() );
	}
	
	/**
	 * @return the Width of the Tk environnement
	 */
	public static int getEnvWidth(){
		return buffer.getWidth()+arroundSize;
	}
	
	/** 
	 * @return the height of the TK environnement
	 */
	public static int getEnvheight(){
		return buffer.getHeight()+arroundSize;
	}
	

}