package Utiles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import PlanEvac.Feu;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;


public abstract class AgentTurtle extends Turtle  {

	/**
	 * count the number of displacement
	 */
	protected int nbStep;

	/**
	 * determined the actual role of the agent
	 */
	public String role;

	/**
	 * is true if the agent as been alerted of the fire
	 */
	public boolean beAlert;	

	/**
	 * position x in flights
	 */
	protected int xdep;	

	/**
	 * position y in flights
	 */
	protected int ydep;		

	/**
	 * target of an agent
	 */
	public AgentTurtle target;

	/**
	 * position x of the gathering point
	 */
	public int pointX;

	/**
	 * position y of the gathering point
	 */
	public int pointY;

	/**
	 * counter of action to determined when they hace to change
	 */
	protected int cptAction;


	/**
	 * radius of visibility of agent
	 */
	protected int visibility = 15;	

	/**
	 * radius to follow an agent
	 */
	protected final int followDistance = 7;

	/**
	 * radius to heard an agent
	 */
	protected final int listenDistance = 20;

	/**
	 * is true if the turtle is following another turtle
	 */
	public boolean following;


	protected abstract String move();

	protected abstract String evacuate();

	protected abstract void goOut();

	protected abstract void information();

	protected abstract String waitHere();

	protected abstract String comeHere();

	public void setTarget( AgentTurtle t ){
		this.target = t;
	}

	public void setPointXY ( int x , int y ){
		this.pointX = x;
		this.pointY = y;
	}

	public void setFollowing(){
		following = true;
	}

	public void setInAlert(){//*****INUTILE????*****************
		this.beAlert = true;
	}

	public void loseTarget(){
		target = null;
	}

	/**
	 * 
	 * @return x variation
	 */
	public double varX(){
		return pointX - xcor();
	}

	/**
	 * 
	 * @return y variation
	 */
	public int varY(){
		return pointY - ycor();
	}



	/**
	 * kill the agent if he is on fire
	 */
	protected void burn() {
		if(pheromoneCollision("feu") && ! isOut()) {
			information();
			setPatchColor( Color.gray );
			killAgent(this);
		}
	}

	/**
	 * @return true if the turtle has the same index as the Pheromone (same index => same place on the grid)
	 *
	 */	
	protected boolean pheromoneCollision(String pheroName) {
		return getEnvironment().getPheromone(pheroName).get(xcor(),ycor()) >  2;
	}
	public boolean wallBetweenTurtles( Turtle t ){//pzetite modif de généralisation pour turtle
		return wallBetweenTurtlesAux( t.getX(), t.getY() );
	}


	public ArrayList<Feu> fireArround(){//nouvelle méthode
		List<Turtle> t = getOtherTurtlesWithRole(30, true, "feu" );
		ArrayList<Feu> newListe = new ArrayList<Feu>();
		for ( int i = 0; i < t.size(); i++ ){
			if( !wallBetweenTurtles( t.get(i) ) )
				newListe.add( (Feu) t.get(i) );
		}
		return newListe;
	}


	/**
	 * 
	 * @param x xcor of the other turtle
	 * @param y ycor of the other turtle
	 * @return true if there is a wall between the turtle and the point
	 */
	public boolean wallBetweenTurtlesAux( double x, double y ){
		if ( (int)x == xcor() && (int)y == ycor() )
			return false;
		else if ( getPatchAt( (int)x, (int)y ).getColor().getRed() == 255 )
			return true;
		else{
			double d = distance(x,y);
			double x1 = x + (getX() - x)/d;
			double y1 = y + (getY() - y)/d;
			return wallBetweenTurtlesAux( x1 , y1  );//******************************HELP****************************
		}
	}

	/**
	 * 
	 * @param t the turtle
	 * @return true if there is a wall between the turtles
	 */
	public boolean wallBetweenTurtles( AgentTurtle t ){
		return wallBetweenTurtlesAux( t.getX(), t.getY() );
	}

	/**
	 * 
	 * @param visibility
	 * @param role
	 * @return the nearest turtle which is not behind a wall
	 */
	public AgentTurtle getNearestTurtleNoWall( int visibility, String role ){
		List < AgentTurtle > liste = getOtherTurtlesWithRole( visibility, true, role, AgentTurtle.class );
		for ( int i = 0; i < liste.size(); i++ ){
			if ( !wallBetweenTurtles( liste.get(i) ) ){
				return liste.get(i);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param visibility
	 * @return a list of workers which are not behind a wall
	 */
	public ArrayList<AgentTurtle> getWorkersNoWall( int visibility ){
		List<AgentTurtle> liste = getOtherTurtlesWithRole( visibility, true, "work", AgentTurtle.class );
		ArrayList<AgentTurtle> listeNoWrap = new ArrayList<AgentTurtle>();
		for ( int i = 0; i < liste.size(); i++ ){
			if( !wallBetweenTurtles( liste.get(i)  ) )
				listeNoWrap.add( liste.get(i) );
		}
		return listeNoWrap;
	}

	/**
	 * motion they do when they are working
	 * @return
	 */
	public String work(){
		if ( beAlert )
			return "evacuate";
		List<Feu> t = fireArround();// donner le role de feu au Feu
		if ( t.size() > 0 )
			return "evacuate";
		else{
			wigglec();
			if( distance(xdep,ydep) > 5 ){
				setHeading( getHeading() + 180);
				fdc(1);
			}
		}
		return "work";
	}

	/**
	 * 
	 * @return true is the next patch is a turtle
	 */
	protected boolean AgentCollision() {	
		return ! getNextPatch().isEmpty();
	}

	/**
	 * 
	 * @return true if the next patch is a wall
	 */
	protected boolean WallCollision(){	
		return getNextPatch().getColor().getRed() == 255;
	}

	/**
	 * 
	 * @param n
	 */
	protected void fdc ( double n ){//*************on va peut etre g�n�raliser et mettre fdc(1) pour tous***********
		if(getNextPatch() != null) {
			if(AgentCollision()){
				setHeading(getHeading()+100);
				randomHeading(40);
				fdc(1);
			}
			else if (! WallCollision()){
				fd(n);
				nbStep++;
			}			
			else{
				setHeading(getHeading()+100);
				randomHeading(40);
			}
		}	
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return true if there is no turtles at (x,y)
	 */
	protected boolean nextPatchEmpty( double x, double y){
		return getPatchAt( (int)x,(int)y ).isEmpty();
	}

	/**
	 * do one step for his target
	 */
	protected void followTarget(){//attention: collision avec les autres agents tr�s forte! va falloir y remedier
		double d = distance( target );
		double varx = target.xcor() - xcor();
		double vary = target.ycor() - ycor();
		if ( nextPatchEmpty( getX() + varx/d, getY() + vary/d )){
			setXY( getX() + varx/d, getY() + vary/d );
			nbStep++;
		}
	}



	protected void wigglec (){
		this.randomHeading(120);
		if(getNextPatch() != null) {
			if(AgentCollision()) 
				fd(0);
			else if (! WallCollision()){
				fd(1);
				nbStep++;
			}			
			else
				fd(0);
		}
	}

	protected boolean isOut(){
		return getPatchColor() == Color.green;
	}

}