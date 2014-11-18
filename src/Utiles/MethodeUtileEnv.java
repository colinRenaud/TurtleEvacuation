/**
 * Classe contenant les methodes utiles à toute classe d'environnement
 * @author : DUFFAU Johnathan , RABAUD Eliot , COLIN Renaud
 */


package Utiles;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class MethodeUtileEnv {
	
	/**
	 * @param fichier : plan's picture name
	 * @return a BufferedImage which store the picture coordinates
	 */
	public static BufferedImage importerImage(String fichier) throws IOException {	
		try{
			return ImageIO.read(MethodeUtileEnv.class.getResource("/Images/"+fichier));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * @param Buffer : buffer which store the picture coordinates
	 * @param limit : borne intermediaire pour modifier une couleur
	 * @param c1 : color 1
	 * @param c2 = color 2
	 * plan'picture adaptation
	 * patch are wall or room
	 * the buffer'elements color will be white or dark
	 */	
	public static void adapterImage(BufferedImage buffer , int c1 , int c2, int limit) {
		
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				Color pixelColor = new Color(buffer.getRGB(i,j)); //color creation from pixel'RGB components
				int r=pixelColor.getRed();
				int g=pixelColor.getGreen();
				int b=pixelColor.getBlue();			
				if(r < limit | g < limit | b < limit){ //si la couleur est proche de C1 le pixel se met noir
					r = c1; g = c1; b = c1;
				}
				else { // sinon on met au blanc
					r = c2; g = c2; b = c2;
				} 
				buffer.setRGB(i, j, new Color(r,g,b).getRGB());// on cr��e un code unique qui continue les trois composantes RGB dans une seule variable
			}
		}
	}

	
	

}