import static turtlekit.kernel.TurtleKit.Option.startSimu;

import java.awt.Color;


import java.util.List;

import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.TurtleKit.Option;
import turtlekit.pheromone.Pheromone;


public class agent extends Turtle {
	
	protected int nbPas;
	protected int visibilite;		//rayon de detection des agents alentours
	protected double probaPaniquer;	//nombre de pas avant la panique
	protected int courage;			//entre 0 et 100
	protected String role;
	protected boolean alerte;		//alerté de l'incendie ou non
	protected int xdep;				//position de depart
	protected int ydep;				//position de depart
	protected Pheromone traine;
	
	protected Color couleurPerdu = Color.pink;
	protected Color couleurMeneur = Color.red;
	protected Color couleurSuiveur = Color.cyan;
	protected Color couleurPanique = Color.white;
	protected Color couleurSuivre = Color.orange;
	
	protected void activate(){
		super.activate();
		/*while( getY() >= getEnvironment().getBuf().getH() || getX() >= getEnvironment().getWidth() ){
			randomLocation();
		}*/
		this.nbPas = 0;
		this.courage = (int)(Math.random()*100);
		this.alerte = false;
		this.xdep = this.xcor();
		this.ydep = this.ycor();
		this.role = "travailler";
		setNextAction("travailler");
		traine = getEnvironment().getPheromone("te",1,7);
	}
	
	protected boolean getAlerte(){
		return this.alerte;
	}
	
	protected String travailler(){
		List<Turtle> t = getOtherTurtles(20, true);
		if ( this.alerte == true ){
			return "evacuer";
		}
		/*if ( ((agent) t.get(0)).getAlerte() == true ){
			return "evacuer";
		}	*/	
		resterPasLoin( this.xdep, this.ydep, 5);
		return "travailler";
	}
	
	
	protected String evacuer(){
		if ( this.courage > 90 ){
			return "etreMeneur";
		}
		else if ( this.courage < 30){
			return "etrePerdu";
		}
		else{
			return "etreSuiveur";
		}
	}
	
	
	protected String perdu(){
		List<Turtle> liste = getOtherTurtles( this.visibilite, false);
		if ( nbPas > probaPaniquer ){
			return "etrePanique";
		}
		else if( liste.size() > 2){
			return "etreSuiveur";}
		else{
			wiggle();
			nbPas++;
			return "perdu";
		}
	}
	
	protected String meneur(){
		List<Turtle> liste = getOtherTurtlesWithRole( this.visibilite, true, "suiveur");
		//plus de gens le suive, moins il avance vite
		double v = liste.size()*0.1;
		
		if ( nbPas < 5 ){//nbr de pas avant de changer de direction
			fd(1-v);
			nbPas++;
			return "meneur";
		}
		else{
			double val = Math.random()*60 - 30;//variation de leur direction de deplacement
			nbPas = 0;
			setHeading( getHeading() + val );
		}		
		return "meneur";
	}
	
	protected String suiveur(){
		List<Turtle> liste = getOtherTurtlesWithRole( this.visibilite, false, "meneur");
		
		if ( liste.isEmpty() == false ){
			wiggle();
			return "partirSuivre";
		}
		else if(nbPas > 500){
			nbPas = 0;
			return "etrePerdu";
		}
		else{
			nbPas++;
			wiggle();
			return "suiveur";
		}	
	}
	
	protected String suivre(){
		Turtle t = getNearestTurtleWithRole( this.visibilite , "meneur");
		if ( t == null ){
			wiggle();
			return "etreSuiveur";
		}
		else if ( etreDerriere(t.getHeading(), t.getX(), t.getY() ) ){
			setHeading( t.getHeading() );
			fd(1);
			//follow(t);
			return "suivre";	
		}
		else{	
			return "etreSuiveur";//a revoir
		}
	}
	
	protected String panique(){
		double p = Math.random()*100;
		if ( p < 10 ){
			wiggle();
		}
		if ( getOtherTurtlesWithRole(this.visibilite, false,"meneur").size() > 0){
			return "etreSuiveur";
		}
		return "panique";
	}
	
	protected String etrePerdu(){
		this.visibilite = 15;
		setColor( couleurPerdu );
		giveUpRole( this.role );
		this.role = "perdu";
		playRole( this.role);
		
		this.probaPaniquer = Math.random()*500 + 1000;
		return "perdu";
	}
	
	protected String etreSuiveur(){
		this.visibilite = 15;
		setColor( couleurSuiveur );
		giveUpRole( this.role );
		this.role = "suiveur";
		playRole( this.role);
		return "suiveur";
	}
	
	protected String partirSuivre(){
		this.visibilite = 15;
		setColor( couleurSuivre );
		return "suivre";
	}
	
	protected String etreMeneur(){
		this.visibilite = 15;
		setColor( couleurMeneur );
		giveUpRole(this.role);
		this.role = "meneur";
		playRole(this.role);
		return "meneur";
	}
	
	protected String etrePanique(){
		this.visibilite = 10;
		setColor( couleurPanique );
		giveUpRole( this.role );
		this.role = "panique";
		playRole( this.role );
		return "panique";
	}
	
	public void resterPasLoin( double x, double y, double d){
		wiggle();
		if( distance(x,y) > d ){
			setHeading( getHeading() + 180);
			fd(1);
		}
	}
	
	public double distance ( double x, double y){
		return Math.sqrt( Math.pow(this.getX() - x, 2) + Math.pow(this.getY() - y, 2) );
	}
	
	protected boolean etreDerriere( double h, double xm, double ym){//return true si elle est derriere la tortue a suivre
		double angle = h;
		double y = Math.cos(Math.toRadians(angle))*this.visibilite;
		double x = Math.sin(Math.toRadians(angle))*this.visibilite;
		
		//on defini les coordonnées du cercle derriere
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
	
	protected void follow( Turtle t ){
		int x = t.xcor();
		int y = t.ycor();
		
		double d = this.distance(x,y);
		
		double x1 = (this.getX() - x)/(d/2);
		double y1 = (this.getY() - y)/(d/2);
		
		setXY(x1,y1);
	}

	

	public static void main(String[] args) {
		
		executeThisTurtle(10
				, Option.envDimension.toString()
				,Option.cuda.toString()
				,startSimu.toString()
				);
				
	}
}