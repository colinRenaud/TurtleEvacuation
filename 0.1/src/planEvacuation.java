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
import java.io.IOException;
import turtlekit.kernel.TurtleKit.Option;



public class planEvacuation extends turtleImage{
	
	private static String windowDimension = Integer.toString(500) + ","  + Integer.toString(500);
	private final  String path = "/home/spiraltek/Documents/IUT/Projet Tuteuré/TurtleKit/src/turtelkitincendie/plan.jpeg";
	private static final int couleurLimite = 142 ;
	private static final int couleurSalle = 220; // mur blanc
	private static final int couleurMur = 0; // salle noire
		
	
	public void activate()  {
		// construction de la grille à partie de la classe mère
			try {
				super.importerImage(path);  // importation de l'image
			} catch (IOException e) {e.printStackTrace();}		
			/* adaptation de l'image du plan
			 * les patch seront soit des murs soit la salle
			 * on oriente une couleur vers le blanc ou le noir selon le niveau de gris(couleurLimite)
			 */
			super.adapterImage(buffer,couleurSalle,couleurMur,couleurLimite); 
            super.activate();  //activation
            //this.windowDimension = Integer.toString( buffer.getHeight() ) + "," + Integer.toString( buffer.getWidth());
	}
	
	
	public static void main(String[] args) {	
		executeThisEnvironment(	
				//Option.turtles.toString(),agent.class.getName()+",50"
				,Option.envDimension.toString(), windowDimension,
				Option.startSimu.toString()
				);
	}
}