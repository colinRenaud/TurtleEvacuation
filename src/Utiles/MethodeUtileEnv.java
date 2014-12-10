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
	
	
	public static BufferedImage importerImage(String fichier) throws IOException {	
		try{
			return ImageIO.read(MethodeUtileEnv.class.getResource("/Images/"+fichier));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void adapterImage(BufferedImage buffer , int c1 , int c2, int limit) {
		
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				Color pixelColor = new Color(buffer.getRGB(i,j)); 
				int r=pixelColor.getRed();
				int g=pixelColor.getGreen();
				int b=pixelColor.getBlue();			
				if(r < limit | g < limit | b < limit){ 
					r = c1; g = c1; b = c1;
				}
				else { 
					r = c2; g = c2; b = c2;
				} 
				buffer.setRGB(i, j, new Color(r,g,b).getRGB());
			}
		}
	}
	
	

	
	

}