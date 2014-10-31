package Utiles;

import turtlekit.kernel.Turtle;
import turtlekit.pheromone.Pheromone;

public abstract class AgentTurtle extends Turtle  {
	/**
	 * @param nbPas : nombre de pas effectués par l'agent tout au long de la simu
	 * @param visibilité : rayon de detection des agents alentours
	 * @param probaPaniquer : nombre de pas avant la panique
	 * @param courage : courage de l'agent entre 0 et 100
	 * @param role : role de l'agent lors de la simulation
	 * @param alerte : alerté de l'incendie ou non 
	 * @param xdep : position de départ en x
	 * @param ydep : position de départ en y
	 * @param traine : traces laissées par un agent
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

	protected boolean etreDerriere( double angle, double xm, double ym){
		/**
		 * @return true si l'agent est derriere la tortue a suivre , false sinon
		 */
		//on defini les coordonn��es du cercle derriere
		double xo = xm - Math.cos(Math.toRadians(angle))*visibilite;
		double yo = ym - Math.sin(Math.toRadians(angle))*visibilite;
		//la tortue sera derriere la meneuse si elle est dans le rayon du cercle de centre O(xo, yo)
		return distance(xo,yo) < visibilite ;
	}

	/*protected void follow( Turtle t ){// marche pas
		int x = t.xcor();
		int y = t.ycor();
		double d = this.distance(x,y);
		double x1 = (this.getX() - x)/(d/2);
		double y1 = (this.getY() - y)/(d/2);
		setXY(x1,y1);
	}*/

	protected boolean collisionAgent() {
		/**
		 * @return : true if the next patch is a turtle 
		 */
		return ! getNextPatch().isEmpty();
	}

	protected boolean collisionMur(){
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
			if(collisionAgent())
				fd(0);
			else if (! collisionMur())
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
			if(collisionAgent()) 
				fd(0);
			else if (! collisionMur())
				fd(1);
			else
				fd(0);
		}
	}
}
