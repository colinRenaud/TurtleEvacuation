package Utiles;

import java.awt.Color;

import PlanEvac.Feu;

import turtlekit.kernel.Turtle;
import turtlekit.pheromone.Pheromone;

public abstract class AgentTurtle extends Turtle  {
	/**
	 * number of steps throughout the simulation
	 */
	protected int nbPas;
	/**
	 * detection radius of neighboring agents
	 */
	protected int visibilite;	
	/**
	 * agent probability to be panic
	 */
	protected double probaPaniquer;	
	/**
	 * agent bravery between 0 and 100
	 */
	protected int courage;	
	/**
	 * current agent role
	 */
	protected String role;
	/**
	 * the agent is alerted or not
	 */
	protected boolean alerte;	
	/**
	 * initial x coordinate
	 */
	protected int xdep;	
	/**
	 * initial y coordinate
	 */
	protected int ydep;		
	/**
	 * traces left by the agent
	 */
	protected Pheromone traine;

	/**
	 * @param x
	 * @param y
	 * @param d
	 */
	public void resterPasLoin( double x, double y, double d){
		wigglec();
		if( distance(x,y) > d ){
			setHeading( getHeading() + 180);
			fdc(1);
		}
	}

	/**
	 * @param
	 * @param
	 * @return
	 */	
	public double distance (double x, double y){		
		return Math.sqrt(Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2) );
	}

	/**
	 * @param xm : other turtle x coordinate
	 * @param ym : other turtle y coordinate
	 * @param angle : current turtle'visibility radius
	 * @return true if the agent is behing the turtle to follow  , false else
	 */
	protected boolean isBehind( double angle, double xm, double ym){	
		//on defini les coordonn������es du cercle derriere
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

	/**
	 * @return : true if the next patch is a turtle 
	 */
	protected boolean AgentCollision() {	
		return ! getNextPatch().isEmpty();
	}

	/**
	 * @return true if the next patch is a wall (white)
	 */
	protected boolean WallCollision(){	
		return getNextPatch().getColor().getRed() == 255;
	}

	/**
	 * @param n : number of steps to make 
	 * the agents move if there isn't collision on the next patch
	 */
	protected void fdc ( double n ){		
		if(getNextPatch() != null) {
			if(AgentCollision())
				fd(0);
			else if (! WallCollision()){
				fd(n);
				nbPas++;
			}			
			else{
				setHeading(getHeading()+100);
				randomHeading(40);
			}
		}	
	}

	/**
	 * 
	 */
	protected void wigglec (){
		this.randomHeading(120);
		if(getNextPatch() != null) {
			if(AgentCollision()) 
				fd(0);
			else if (! WallCollision()){
				fd(1);
				nbPas++;
			}			
			else
				fd(0);
		}
	}
	/**
	 * 
	 * @return
	 */
	/*protected boolean fireCollision() {
		
	}*/
	
	/**
	 * @return true if the agent is out of the building , false else
	 */
	protected boolean isOut(){
		return getPatchColor() == Color.green;
	}



}
