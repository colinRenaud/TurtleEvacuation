/**
 * Classe contenant les methodes utiles à toute classe d'environnement 
 * @author : DUFFAU  Johnathan , RABAUD Eliot , COLIN Renaud
 */
package Utiles;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import turtlekit.kernel.TKEnvironment;

public abstract class MethodeUtileEnv extends TKEnvironment{

	protected BufferedImage importerImage(String  fichier) throws IOException {
		/**
		 * @param fichier : plan's picture name 
		 * @return a BufferedImage which  store the picture coordinates
		 */
		try{
			return ImageIO.read(getClass().getResource("/Images/"+fichier)); 
		} catch (IOException e) {
			e.printStackTrace();
		    return null;
		}		
	}

	protected void adapterImage(BufferedImage buffer , int c1 , int c2, int limit) {
		/**
		 * @param Buffer  : buffer which store the picture coordinates
		 * @param limit : borne intermediaire pour modifier une couleur
		 * @param c1 : color 1 
		 * @param c2 = color 2
		 * permet d'adapter l'image a une situation precise 
		 * en appliquant une transformation sur les pixels de l'image de facon a  avoir 2 couleurs seulement
		 * cette transformation permet de simplifier l'image afin de pouvoir l'exploiter sous turtleKit
		 */

		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				// color creation from pixel'RGB components
				Color pixelColor = new Color(buffer.getRGB(i,j));  

				int r=pixelColor.getRed();
				int g=pixelColor.getGreen();
				int b=pixelColor.getBlue();				
				//test si la couleur est proche de C1 si oui le pixel se met noir
				// on consid??re qu'a partir de la limite  //si toutes 
				//les composantes sont ??gales alors c'est gris 
				if(r < limit | g < limit | b < limit){
					r = c1; g = c1; b = c1;	// donc on transforme selon la premi??re couleur
				}
				//sinon on transforme selon la deuxieme
				else {
					r = c2; g = c2; b = c2;								            	
				}
				int rgb= new Color(r,g,b).getRGB(); // on crée un code unique qui continue les trois composantes RGB dans une seule variable
				buffer.setRGB(i, j, rgb); // modification du buffer
			}
		}
	}


	protected void activate() {
		/**
		 * activation de l'environnement
		 */
		super.activate();		
	}

}
