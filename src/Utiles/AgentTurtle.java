package Utiles;

import turtlekit.kernel.Turtle;
import turtlekit.pheromone.Pheromone;

public abstract class AgentTurtle extends Turtle  {
	/**
	 * @param nbPas : number of steps throughout the simulation
	 * @param visibilité : detection'radius of neighboring agents
	 * @param probaPaniquer : agent'probability to be panic
	 * @param courage : agent'bravery between 0 and 100
	 * @param role : current agent'role
	 * @param alerte : the agent is alerted or not
	 * @param xdep : initial x coordinate
	 * @param ydep : initial y coordinate
	 * @param traine : traces left by the agent
	 */

	protected int nbPas;
	protected int visibilite;		
	protected double probaPaniquer;	
	protected int courage;			
	protected String role;
	protected boolean alerte;		
	protected int xdep;				
	protected int ydep;				
	protected Pheromone traine;

	public void resterPasLoin( double x, double y, double d){
		wigglec();
		if( distance(x,y) > d ){
			setHeading( getHeading() + 180);
			fdc(1);
		}
	}

	public double distance ( double x, double y){
		/*
		 * @return the distance between the current agent and the (x,y) position
		 */
		return Math.sqrt( Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2) );
	}

	protected boolean isBehind( double angle, double xm, double ym){
		/**
		 * @return true if the agent is behing the turtle to follow  , false else
		 * @param xm : other turtle x coordinate
		 * @param ym : other turtle y coordinate
		 * @param angle : current turtle'visibility radius
		 */
		
		//on defini les coordonn��es du cercle derriere
		double xo = xm - Math.cos(Math.toRadians(angle))*visibilite;
		double yo = ym - Math.sin(Math.toRadians(angle))*visibilite;
		//la tortue sera derriere la meneuse si elle est dans le rayon du cercle de centre O(xo, yo)
		return distance(xo,yo) < visibilite ;
	}

	/*protected void follow( Turtle t ){
	 // doesn't work for the moment
		int x = t.xcor();
		int y = t.ycor();
		double d = this.distance(x,y);
		double x1 = (this.getX() - x)/(d/2);
		double y1 = (this.getY() - y)/(d/2);
		setXY(x1,y1);
	}*/

	protected boolean AgentCollision() {
		/**
		 * @return : true if the next patch is a turtle 
		 */
		return ! getNextPatch().isEmpty();
	}

	protected boolean WallCollision(){
		/**
		 * @return true if the next patch is a wall (white)
		 */
		return getNextPatch().getColor().getRed() == 255;
	}

	protected void fdc ( double n ){
		/**
		 * @param n : number of steps to make 
		 * the agents move if there isn't collision on the next patch
		 */
		if(getNextPatch() != null) {
			if(AgentCollision())
				fd(0);
			else if (! WallCollision())
				fd(n);
			else{
				setHeading(getHeading()+100);
				randomHeading(40);
			}
		}	
	}

	protected void wigglec (){
		/**
		 * 
		 */
		this.randomHeading(120);
		if(getNextPatch() != null) {
			if(AgentCollision()) 
				fd(0);
			else if (! WallCollision())
				fd(1);
			else
				fd(0);
		}
	}
}
