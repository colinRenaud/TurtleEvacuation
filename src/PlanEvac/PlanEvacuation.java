package PlanEvac;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import turtlekit.kernel.Patch;
import turtlekit.kernel.TurtleKit.Option;
import Utiles.MethodeUtileEnv;

public class PlanEvacuation extends MethodeUtileEnv{
	/**
	 * @param buffer : buffer qui contient les coordonnées du plan
	 * @param windowDimension : Dimension de la fenetre pour le viewer TK
	 * @param nomFichier : nom de l'image du plan à importer
	 * @param couleurLimite : couleur permettant de délimiter les images plus blanches ou plus noires
	 * @param couleurSalle : couleur de la salle du batiment
	 * @param couleurMur : couleur des murs du batiment
	 */
	private BufferedImage buffer;
	private final static String windowDimension = Integer.toString(500) + ","  + Integer.toString(500);
	private final String nomfichier = "plan.png";
	private final int couleurLimite = 142 ;
	private final int couleurSalle = 255; // mur blanc
	private final int couleurMur = 1; // salle noire


	public void activate()  {	
		/*
		 *  construction de la grille à partie de la classe mère
		 */
		super.activate();
		try {
			buffer = super.importerImage(nomfichier);  // importation de l'image
		} catch (IOException e) {
			e.printStackTrace();
			buffer = null;
		}		
		/* adaptation de l'image du plan
		 * les patch seront soit des murs soit la salle
		 * on oriente une couleur vers le blanc ou le noir selon le niveau de gris(couleurLimite)
		 */
		super.adapterImage(buffer,couleurSalle,couleurMur,couleurLimite);
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = buffer.getHeight()-1; j > 0; j--) {
				Patch p = getPatch(i, j); 
				p.setColor(new Color(buffer.getRGB(i, j)));
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