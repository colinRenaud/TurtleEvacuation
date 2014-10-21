package Utiles;

import java.awt.Color;
import turtlekit.kernel.Turtle;
import turtlekit.pheromone.Pheromone;


public abstract class AgentTurtle extends Turtle  {

	protected int nbPas;
	protected int visibilite;		//rayon de detection des agents alentours
	protected double probaPaniquer;	//nombre de pas avant la panique
	protected int courage;			//entre 0 et 100
	protected String role;
	protected boolean alerte;		//alert�� de l'incendie ou non
	protected int xdep;				//position de depart
	protected int ydep;				//position de depart
	protected Pheromone traine;
	
	
	public void resterPasLoin( double x, double y, double d){
		wigglec();
		if( distance(x,y) > d ){
			setHeading( getHeading() + 180);
			fdc(1);
		}
	}
	
	public double distance ( double x, double y){
		return Math.sqrt( Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2) );
	}
	
	protected boolean etreDerriere( double h, double xm, double ym){//return true si elle est derriere la tortue a suivre
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
	
	protected void follow( Turtle t ){// marche pas
		int x = t.xcor();
		int y = t.ycor();
		
		double d = this.distance(x,y);
		
		double x1 = (this.getX() - x)/(d/2);
		double y1 = (this.getY() - y)/(d/2);
		
		setXY(x1,y1);
	}
	
	protected boolean collision(){// bug
		if ( getPatchAt( this.dx(), this.dy() ).getColor() == Color.black ){
			return false;
		}
		else{
			return true;
		}
	}
	
	protected void fdc ( double n ){
		if ( collision() == false){
			fd(1);
		}
	}
	
	protected void wigglec (){
		this.randomHeading();
		if ( collision() == false){
			fd(1);
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
