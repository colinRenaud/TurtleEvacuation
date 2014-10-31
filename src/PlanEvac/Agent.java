/**
 *@author : DUFFAU  Johnathan , RABAUD Eliot , COLIN Renaud
 *Classe permettant de gerer le comportement des agents pour la simulation incendie
 */

package PlanEvac;

import java.awt.Color;
import java.util.List;
import Utiles.AgentTurtle;
import turtlekit.kernel.Turtle;

public class Agent extends AgentTurtle {

	/**
	 * @param couleurPerdu : couleur d'un agent perdu 
	 * @param couleurMeneur : couleur d'un agent meneur
	 * @param couleurSuiveur : couleur d'un agent qui et en recherche de meneur
	 * @param couleurPanique : couleur d'un agent paniqué 
	 * @param couleurSuivre : couleur d'un agent qui suit un meneur
	 */

	protected final Color couleurPerdu = Color.pink;
	protected final Color couleurMeneur = Color.red;
	protected final Color couleurSuiveur = Color.cyan;
	protected final Color couleurPanique = Color.green;
	protected final Color couleurSuivre = Color.orange;

	protected void activate(){
		/**
		 * active un agent 
		 */
		super.activate();
		nbPas = 0;
		courage = (int)(Math.random()*100);
		alerte = true;
		xdep = xcor();
		ydep = ycor();
		role = "travailler";
		setNextAction("travailler");
		//traine = getEnvironment().getPheromone("te",1,7);
		while(getPatch().getColor().getRed() != 1) 
			randomLocation();
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
		resterPasLoin( xdep, ydep, 5);
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
		 * @return l'état possible d'un agent perdu : etrePanique ou  etreSuiveur ou  Perdu
		 * 
		 */
		List<Turtle> liste = getOtherTurtles( visibilite, false);
		if ( nbPas > probaPaniquer ){
			return "etrePanique";
		}
		else if( liste.size() > 2){ 
			return "etreSuiveur";}
		else{    
			wigglec();
			nbPas++;
			return "perdu";
		}
	}


	protected String meneur() {	
		/**
		 * @return  d'état possible pour un agent meneur : meneur
		 */
		List<Turtle> liste = getOtherTurtlesWithRole( visibilite, true, "suiveur");
		//plus de gens le suive, moins il avance vite
		double v = liste.size()*0.1;

		if ( nbPas < 5 ){//nbr de pas avant de changer de direction
			fdc(1-v);
			nbPas++;
		}
		else{
			nbPas = 0;
			randomHeading(60);
		}
		return "meneur";
	}

	protected String suiveur(){
		/**
		 * @return létat possible pour un agenten recherche de meneur à suivre : 
		 * 
		 */
		List<Turtle> liste = getOtherTurtlesWithRole( visibilite, false, "meneur");

		if ( liste.isEmpty() == false ){
			wigglec();
			return "partirSuivre";
		}
		else if(nbPas > 500){
			nbPas = 0;
			return "etrePerdu";
		}
		else{
			nbPas++;
			wigglec();
			return "suiveur";
		}	
	}

	protected String suivre(){
		/**
		 * @return l'etat possible pour un élement qui suit un meneur
		 */
		Turtle t = getNearestTurtleWithRole( visibilite , "meneur");
		if ( t == null ){
			wigglec();
			return "etreSuiveur";
		}
		else if ( etreDerriere(t.getHeading(), t.getX(), t.getY() ) ){
			setHeading( t.getHeading() );
			fdc(1);
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
			wigglec();
		}
		if ( getOtherTurtlesWithRole(visibilite, false,"meneur").size() > 0){
			return "etreSuiveur";
		}
		return "panique";
	}

	protected String etrePerdu(){
		/**
		 * @return l'état possible pour un agent qui est perdu
		 */
		visibilite = 15;
		setColor( couleurPerdu );
		giveUpRole( role );
		role = "perdu";
		playRole( role);

		probaPaniquer = Math.random()*500 + 1000;
		return "perdu";
	}

	protected String etreSuiveur(){
		visibilite = 15;
		setColor( couleurSuiveur );
		giveUpRole( role );
		role = "suiveur";
		playRole( role);
		return "suiveur";
	}

	protected String partirSuivre(){
		visibilite = 15;
		setColor( couleurSuivre );
		return "suivre";
	}

	protected String etreMeneur(){
		visibilite = 15;
		setColor( couleurMeneur );
		giveUpRole(role);
		role = "meneur";
		playRole(role);
		return "meneur";
	}

	protected String etrePanique(){
		visibilite = 10;
		setColor( couleurPanique );
		giveUpRole( role );
		role = "panique";
		playRole( role );
		return "panique";
	}

}