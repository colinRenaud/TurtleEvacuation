package PlanEvac;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import turtlekit.kernel.TurtleKit.Option;
import Utiles.MethodeUtileEnv;

public class PlanEvacuation extends MethodeUtileEnv{
	/**
	 * @param buffer : buffer which  store the picture coordinates
	 * @param windowDimension : windows dimension for  TK viewer
	 * @param nomFichier : plan's picture name 
	 * @param couleurLimite : couleur permettant de délimiter les images plus blanches ou plus noires
	 * @param couleurSalle : room' color
	 * @param couleurMur : wall' color
	 */
	private BufferedImage buffer;
	private final static String windowDimension = Integer.toString(500) + ","  + Integer.toString(500);
	private final String nomfichier = "plan.png";
	private final int couleurLimite = 142 ;
	private final int couleurSalle = 255; // white wall 
	private final int couleurMur = 1; // black room


	public void activate()  {	
		/*
		 *  buffer read and TK'grid construction
		 */
		super.activate();
		try {
			buffer = super.importerImage(nomfichier);  // picture's import 
		} catch (IOException e) {
			e.printStackTrace();
			buffer = null;
		}		
		/* plan'picture adaptation
		 * patch are wall or room
		 * the buffer'elements color will be white or dark
		 */	
		super.adapterImage(buffer,couleurSalle,couleurMur,couleurLimite);
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = buffer.getHeight()-1; j > 0; j--) {
				getPatch(i, j).setColor(new Color(buffer.getRGB(i, j))); // current patch'color modification according to current buffer element
			}			
		}
	}

	public static void main(String[] args) {	
		executeThisEnvironment(	
				Option.turtles.toString(),Agent.class.getName()+",30",
				Option.envDimension.toString(), windowDimension,
				Option.startSimu.toString()
				//,Option.noWrap.toString()
				);
	}
}