package PlanEvac;

import java.awt.Color;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.TurtleKit.Option;
import turtlekit.pheromone.Pheromone;
import turtlekit.viewer.PheromoneViewer;
import Utiles.NewAgentTurtle;

public class Feu extends Turtle{

	/**
	 * 
	 */
	protected Pheromone s;
	private static int evaporation = 0;

	/**
	 * 
	 */
	protected void activate(){
		super.activate();
		setNextAction("act");
		setColor(Color.red);
		s = getEnvironment().getPheromone("bobi", 20,50); // evaporation / diffusion : 20/50
		while(getPatch().getColor().getRed() != 1) 
			randomLocation();
	}

	@SuppressWarnings("deprecation")
	/**
	 * @return
	 */
	protected String act(){
		s.incValue(xcor(), ycor() , 10000);
		int i = (int) (Math.random()*10 - 5 + this.s.getEvaporationCoefficient());
		if (i<7)
			i = 7 + evaporation;
		if(i>50)
			i = 50 + evaporation;
		s.setEvaporationPercentage(i);
		//wiggle();
		return "act";	
	}

	
}