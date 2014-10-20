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

/*
 * Classe permettant de transformer une image en un environnement turtleKit
 * Dans un environnement turtleKit , l'attribut patchGrid est une matrice qui contient tous les patchs
 * 
 * @author Eliot Rabaud , Jonathan Duffau , Renaud Colin
 * @version 1.0 
 */

package Utiles;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import turtlekit.kernel.Patch;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.TurtleKit.Option;

public abstract class turtleImage extends TKEnvironment{

	protected BufferedImage buffer; //buffer qui contient l'image

	protected void importerImage(String path) throws IOException {
		/*
		 * importe une image de type jpg, bmp, jpeg, wbmp, png, gif au sein du buffer
		 *  @param : chemin de l'image à importer
		 */
		try{
			buffer = ImageIO.read(new File(path));	
		} catch (IOException e) {e.printStackTrace();}
	}


	protected void adapterImage(BufferedImage buffer , int c1 , int c2, int limit) {
		/**
		 * @param Buffer  : contient les pixels à modifier
		 * @param limit : borne intermediaire pour modifier une couleur
		 * @param c1 : couleur 1 
		 * @param c2 = couleur 2
		 * permet d'adapter l'image à une situation précise 
		 * en appliquant une transformation sur les pixels de l'image de facon à avoir 2 couleurs seulement
		 * cette transformation permet de simplifier l'image afin de pouvoir l'exploiter sous turtleKit
		 */

		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				// creation d'une couleur à partie des composantes RGB du pixel
				Color pixelColor = new Color(buffer.getRGB(i,j));  

				// recuperer les composantes RGB  de cette couleur
				int r=pixelColor.getRed();
				int g=pixelColor.getGreen();
				int b=pixelColor.getBlue();				
				//test si la couleur est proche de C1 si oui le pixel se met noir
				// on considère qu'a partir de la limite  //si toutes 
				//les composantes sont égales alors c'est gris 
				if(r < limit | g < limit | b < limit){
					r = c1; g = c1; b = c1;	// donc on transforme selon la première couleur
				}
				//sinon on transforme selon la deuxieme
				else {
					r = c2; g = c2; b = c2;								            	
				}
				// on crée un code unique qui continue les trois composantes RGB dans une seule variable
				int rgb= new Color(r,g,b).getRGB(); 
				buffer.setRGB(i, j, rgb); // modification du buffer
			}
		}
	}
	
	public int getWidht() {
		return buffer.getWidth();
	}
	
	public int getHeight() {
		return buffer.getHeight();
	}

	protected void activate() {
		/**
		 * lors de l'activation on va creer des patchs, 
		 * qui auront comme couleurs les composantes RGB des pixels de l'image stockée dans buffer
		 */
		super.activate();		
		for (int i = 0; i < buffer.getWidth(); i++) {
			for (int j = 0; j < buffer.getHeight(); j++) {
				// pour chaque case du buffer on crée un patch(i,j) dans patchGrid
				Patch p = getPatch(i, j); 
				p.setColor(new Color(buffer.getRGB(i, j)));  // ce patch prend les couleurs du pixel stocké dans le buffer
			}
		}
	}


}
