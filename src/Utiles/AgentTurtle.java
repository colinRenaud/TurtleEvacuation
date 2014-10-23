package Utiles;

import java.awt.Color;
import turtlekit.kernel.Turtle;
import turtlekit.pheromone.Pheromone;


public abstract class AgentTurtle extends Turtle  {
	/**
	 * @param nbPas : nombre de pas effectués par l'agent tout au long de la simu
	 * @param visibiité : rayon de detection des agents alentours
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
		 * @return la distance entre les coordonnées courantes de l'agent et (x,y)
		 */
		return Math.sqrt( Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2) );
	}

	protected boolean etreDerriere( double h, double xm, double ym){
		/**
		 * @return true si l'agent est derriere la tortue a suivre , false sinon
		 */

		double angle = h;
		double y = Math.cos(Math.toRadians(angle))*this.visibilite;
		double x = Math.sin(Math.toRadians(angle))*this.visibilite;

		//on defini les coordonn��es du cercle derriere
		double xo = xm - y;
		double yo = ym - x;
		//la tortue sera derriere la meneuse si elle est dans le rayon du cercle de centre O(xo, yo)
		if ( this.distance(xo,yo) < this.visibilite ){
			return true;
		}
		else{
			return false;
		}
	}

	/*protected void follow( Turtle t ){// marche pas
		int x = t.xcor();
		int y = t.ycor();

		double d = this.distance(x,y);

		double x1 = (this.getX() - x)/(d/2);
		double y1 = (this.getY() - y)/(d/2);

		setXY(x1,y1);
	}*/

	protected boolean collisionMur(){
		/**
		 * @return si un agent va entrer en collision avec un mur
		 */

		if ( getNextPatch().getColor() == Color.black ){
			return false;
		}
		return true;
	}

	protected void fdc ( double n ){
		/**
		 * avancement si il n'y pas de collision
		 */
		if ( collisionMur() == false){
			fd(1);
		}
	}

	protected void wigglec (){
		this.randomHeading();
		if ( collisionMur() == false){
			fd(1);
		}
	}
}
