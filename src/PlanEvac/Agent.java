/**
 *@author : DUFFAU  Johnathan , RABAUD Eliot , COLIN Renaud
 *Classe permettant de gerer le comportement des agents pour la simulation incendie
 */

package PlanEvac;

import java.awt.Color;
import java.util.List;
import Utiles.NewAgentTurtle;
import turtlekit.kernel.Turtle;

public class Agent extends NewAgentTurtle {
	
	/**
	 * color of a left agent
	 */
	private final Color couleurPerdu = Color.pink;
	/**
	 * color of a leader agent
	 */
	private final Color couleurMeneur = Color.red;
	/**
	 * color of a agent who is searching for a leader
	 */
	private final Color couleurSuiveur = Color.cyan;
	/**
	 * color of a panic agent
	 */
	private final Color couleurPanique = Color.green;
	/**
	 * color of a gent who follow a leader
	 */
	private final Color couleurSuivre = Color.orange;
	
	private int pasTmp;
	
	private final String fireName = "bobi";

	/**
	 * activate an agent 
	 */
	protected void activate(){
		
		super.activate();
		nbPas = 0;
		pasTmp= 0;
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

	/**
	 * @return true if the agent is alerted , false if not
	 */
	protected boolean getAlerte(){		
		return alerte;
	}

	/**
	 * @return the action to make if there's an alerte when the agent works
	 */
	protected String travailler(){		
		List<Turtle> t = getOtherTurtles(20, true);
		if (alerte)
			return "evacuer";
		/*if ( ((agent) t.get(0)).getAlerte() == true ){
			return "evacuer";
		}	*/	
		resterPasLoin( xdep, ydep, 5);
		return "travailler";
	}

	/**
	 * @return the state of a  agent who is working , according to his bravery
	 */
	protected String evacuer(){		
		if (courage > 90 )
			return "etreMeneur";
		else if (courage < 30)
			return "etrePerdu";
		return "etreSuiveur";
	}

	/**
	 * @return the state of a agent who's is left  : etrePanique  or etreSuiveur or  Perdu
	 */
	protected String perdu(){
		if ( isOut() )
			return "sorti";
		if(pheromoneCollision(fireName)) 
			return "burn";
		List<Turtle> liste = getOtherTurtles( visibilite, false);
		if ( pasTmp > probaPaniquer )
			return "etrePanique";
		else if( liste.size() > 2)
			return "etreSuiveur";  
		wigglec();
		pasTmp++;
		return "perdu";
	}


	/**
	 * @return the possible state for a leader 
	 */
	protected String meneur() {	
		if ( isOut() )
			return "sorti";
		if(pheromoneCollision(fireName)) 
			return "burn";
		List<Turtle> liste = getOtherTurtlesWithRole( visibilite, true, "suiveur");
		//plus de gens le suive, moins il avance vite
		double v = liste.size()*0.1;

		if (pasTmp < 5){//nbr de pas avant de changer de direction
			fdc(1-v);
			pasTmp++;
		}
		else{
			pasTmp = 0;
			randomHeading(60);
		}
		return "meneur";
	}

	/**
	 * @return the possible state for a agent who is searching for d'un agent perdu 
	 */
	protected String suiveur(){
		if ( isOut() )
			return "sorti";
		if(pheromoneCollision(fireName)) 
			return "burn";
		List<Turtle> liste = getOtherTurtlesWithRole( visibilite, false, "meneur");
		if (liste.isEmpty() == false){
			wigglec();
			return "partirSuivre";
		}
		else if(pasTmp > 500){
			pasTmp = 0;
			return "etrePerdu";
		}
		else{
			pasTmp++;
			wigglec();
			return "suiveur";
		}		
	}

	/**
	 * @return l'etat possible pour un ��lement qui suit un meneur
	 */
	protected String suivre(){
		if ( isOut() )
			return "sorti";
		if(pheromoneCollision(fireName)) 
			return "burn";
		Turtle t = getNearestTurtleWithRole( visibilite , "meneur");
		if ( t == null ){
			wigglec();
			return "etreSuiveur";
		}
		else if (isBehind(t.getHeading(), t.getX(), t.getY() ) ){
			setHeading( t.getHeading() );
			fdc(1);
			//follow(t); en attente de validation
			return "suivre";	
		}
		else{	
			return "etreSuiveur";//a revoir
		}
	}

	/**
	 * @return le changement d'��tat possible pour un agent paniqu��
	 */
	protected String panique(){
		if ( isOut() )
			return "sorti";
		if(pheromoneCollision(fireName)) 
			return "burn";
		double p = Math.random()*100;
		if (p < 10)
			wigglec();
		if (getOtherTurtlesWithRole(visibilite, false,"meneur").size() > 0)
			return "etreSuiveur";
		return "panique";
	}

	/**
	 * @return l'��tat possible pour un agent qui est perdu
	 */
	protected String etrePerdu(){	
		visibilite = 15;
		setColor( couleurPerdu );
		giveUpRole( role );
		role = "perdu";
		playRole( role);
		probaPaniquer = Math.random()*500 + 1000;
		return "perdu";
	}

	/**
	 * 
	 * @return
	 */
	protected String etreSuiveur(){
		visibilite = 15;
		setColor( couleurSuiveur );
		giveUpRole( role );
		role = "suiveur";
		playRole( role);
		return "suiveur";
	}

	/**
	 * 
	 * @return
	 */
	protected String partirSuivre(){
		visibilite = 15;
		setColor( couleurSuivre );
		return "suivre";
	}

	/**
	 * @return
	 */
	protected String etreMeneur(){
		visibilite = 15;
		setColor( couleurMeneur );
		giveUpRole(role);
		role = "meneur";
		playRole(role);
		return "meneur";
	}

	/**
	 * @return
	 */
	protected String etrePanique(){
		visibilite = 10;
		setColor( couleurPanique );
		giveUpRole( role );
		role = "panique";
		playRole( role );
		return "panique";
	}
	
	/**
	 * a agent who succes to escape the building
	 */
	protected void sorti(){
		System.out.println("OUF , JE SUIS VIVANT");
		killAgent(this);
	}
	
	/**
	 * kill the agent if he is on fire
	 */
	protected void burn() {
		if(pheromoneCollision(fireName) && ! isOut()) {
			System.out.println("MERDE JE BRULE  !!!!!!!!!!!!");
			killAgent(this);
		}
	}

}