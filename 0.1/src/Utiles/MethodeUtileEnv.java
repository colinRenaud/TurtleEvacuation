/*******************************************************************************
 * TurtleKit 3 - Agent Based and Artificial Life Simulation Platform
 * Copyright (C) 2011-2014 Fabien Michel
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import turtlekit.kernel.Patch;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.TurtleKit.Option;

public abstract class MethodeUtileEnv extends TKEnvironment{

	protected BufferedImage importerImage(String s) throws IOException {
		/**
		 * @param path : contient le chemin d'accés a limage
		 * @return 
		 */
		//URL path = getClass().getResource("/projetA2/src/plan.png");
		try{
			//return ImageIO.read( path );
			return ImageIO.read( new File("/media/usb0/workspace/planEvacuation/src/plan.png"));
		} catch (IOException e) {e.printStackTrace();
		return null;}
		
	}
	
	protected void adapterImage(BufferedImage buffer , int c1 , int c2, int limit) {
		/**
		 * @param Buffer  : contient les pixels ?? modifier
		 * @param limit : borne intermediaire pour modifier une couleur
		 * @param c1 : couleur 1 
		 * @param c2 = couleur 2
		 * permet d'adapter l'image ?? une situation pr??cise 
		 * en appliquant une transformation sur les pixels de l'image de facon ?? avoir 2 couleurs seulement
		 * cette transformation permet de simplifier l'image afin de pouvoir l'exploiter sous turtleKit
		 */
		
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				// creation d'une couleur ?? partie des composantes RGB du pixel
				Color pixelColor = new Color(buffer.getRGB(i,j));  
				
				// recuperer les composantes RGB  de cette couleur
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
				int rgb= new Color(r,g,b).getRGB(); // on cr??e un code unique qui continue les trois composantes RGB dans une seule variable
				buffer.setRGB(i, j, rgb); // modification du buffer
			}
		}
	}

		
	protected void activate() {
		/**
		 * lors de l'activation on va creer des patchs, 
		 * qui auront comme couleurs les composantes RGB des pixels de l'image stock??e dans buffer
		 */
		super.activate();		
		}
	


}
