
package PlanEvac;


import java.awt.Color;
import java.awt.Graphics;

import turtlekit.kernel.Patch;
import turtlekit.viewer.PheromoneViewer;
import turtlekit.viewer.TKDefaultViewer;



public class MyViewer extends PheromoneViewer{


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

	/*public void paintPatch(Graphics arg0, Patch arg1, int arg2, int arg3,int arg4) {
	super.paintPatch(arg0, arg1, arg2, arg3, arg4);
	if(arg1.getColor().getRed() == 255){
		arg0.setColor(Color.white);
		arg0.fillRect(arg2, arg3, cellSize, cellSize);
	}
	if(arg1.getColor() == Color.GREEN) {
		arg0.setColor(Color.GREEN);
		arg0.fillRect(arg2, arg3, cellSize, cellSize);
	}
}*/
}
