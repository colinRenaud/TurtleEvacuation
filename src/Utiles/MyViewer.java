
package Utiles;


import java.awt.Color;
import java.awt.Graphics;

import PlanEvac.PlanEvacuation;
/*import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;*/

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;
import turtlekit.viewer.PheromoneViewer;



public class MyViewer extends PheromoneViewer{

	/**
	 * make the viewer see the wall , and the out
	 * 
	 */

	public void paintPatch(final Graphics g, final Patch p, final int x, final int y, final int index) {
		super.paintPatch(g, p, x, y, index);
		if(p.getColor().getRed()== 255) {
			g.setColor(Color.WHITE);
			g.fillRect(x, y, cellSize, cellSize);
		}
		if(p.getColor() == Color.GREEN) {
			g.setColor(Color.GREEN);
			g.fillRect(x, y, cellSize, cellSize);
		}	
	}
	/**
	 * make the builder see the agent with a different size
	 */
	public void paintTurtle(final Graphics g, final Turtle t, final int i, final int j) {
		g.setColor(t.getColor());
		g.fillRect(i , j ,PlanEvacuation.getEnvWidth()/60,PlanEvacuation.getEnvheight()/60 );
		 // g.drawImage(img,i,j, 4, 4, (ImageObserver) this);
		
	}
	
}
