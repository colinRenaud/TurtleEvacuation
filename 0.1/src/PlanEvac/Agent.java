/*
 * @autoh : duffau johnathan , rabaud eliot , colin renaud
 */

package PlanEvac;

import static turtlekit.kernel.TurtleKit.Option.startSimu;
import java.awt.Color;
import java.util.List;

import Utiles.AgentTurtle;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.TurtleKit.Option;
import turtlekit.pheromone.Pheromone;


public class Agent extends AgentTurtle {
	

	protected Color couleurPerdu = Color.pink;
	protected Color couleurMeneur = Color.red;
	protected Color couleurSuiveur = Color.cyan;
	protected Color couleurPanique = Color.white;
	protected Color couleurSuivre = Color.orange;
	
	protected void activate(){
		super.activate();
		this.nbPas = 0;
		this.courage = (int)(Math.random()*100);
		this.alerte = true;
		this.xdep = this.xcor();
		this.ydep = this.ycor();
		this.role = "travailler";
		setNextAction("travailler");
		traine = getEnvironment().getPheromone("te",1,7);
	}
	
	protected boolean getAlerte(){
		/*
		 * @return : si l'agent est alerté ou non
		 */
		return alerte;
	}
	
	protected String travailler(){
		/*
		 * @return l'action à réaliser si il y a une alerte ou non lorsque l'agent travaille
		 */
		List<Turtle> t = getOtherTurtles(20, true);
		if ( alerte == true ){
			return "evacuer";
		}
		/*if ( ((agent) t.get(0)).getAlerte() == true ){
			return "evacuer";
		}	*/	
		resterPasLoin( this.xdep, this.ydep, 5);
		return "travailler";
	}
	
	
	protected String evacuer(){
		/*
		 * @return le comportement de l'agent lors de l'alerte selon son courage
		 * 
		 */
		if (courage > 90 ){
			return "etreMeneur";
		}
		else if (courage < 30){
			return "etrePerdu";
		}
		else{
			return "etreSuiveur";
		}
	}
	
	
	protected String perdu(){
		/**
		 * @return le changement d'état possible pour un agent perdu
		 */
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
	
	
	protected String meneur() {	
		/**
		 * @return le changement d'état possible pour un agent meneur
		 */
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
		/**
		 * @return le changement d'état possible pour un agent suiveur
		 * 
		 */
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
			//follow(t); en attente de validation
			return "suivre";	
		}
		else{	
			return "etreSuiveur";//a revoir
		}
	}
	
	protected String panique(){
		/**
		 * @return le changement d'état possible pour un agent paniqué
		 */
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
	

	public static void main(String[] args) {
		
		executeThisTurtle(10
				, Option.envDimension.toString()
				,Option.cuda.toString()
				,startSimu.toString()
				);
				
	}
}