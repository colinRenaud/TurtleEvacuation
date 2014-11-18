package PlanEvac;

import java.awt.Color;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.TurtleKit.Option;
import turtlekit.pheromone.Pheromone;
import turtlekit.viewer.PheromoneViewer;

public class Feu extends Turtle{

	protected Pheromone s;

	protected void activate(){
		super.activate();
		setNextAction("act");
		setColor(Color.red);
		s = getEnvironment().getPheromone("bobi", 20,50);
	}

	@SuppressWarnings("deprecation")
	protected String act(){
		s.incValue(xcor(), ycor() , 10000);
		int i = (int) (Math.random()*10 - 5 + this.s.getEvaporationCoefficient());
		if (i<7)
			i=7;
		if(i>50)
			i=50;
		s.setEvaporationPercentage(i);
		return "act";
	}



	public static void main(String[] args) {
		executeThisTurtle(10
				,Option.viewers.toString(),PheromoneViewer.class.getName()
				,Option.envHeight.toString(),"400"
				,Option.envWidth.toString(),"400"
				,Option.startSimu.toString()
				);
	}
}