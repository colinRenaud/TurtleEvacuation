package PlanEvac;


import java.awt.Color;
import java.util.List;

import turtlekit.kernel.Turtle;
import Utiles.AgentTurtle;

public class Panic extends AgentTurtle{

	boolean following;


	//faire une arraylist de gathering pour les poto panics!!!!!!!!!!!!!!!

	protected void activate(){//****************modifier les param de visibilit�
		super.activate();
		this.role = "panic";
		playRole( "panic" );
		nbStep = 0;
		cptAction = 0;
		beAlert = false;
		xdep = xcor();
		ydep = ycor();
		setNextAction( "work" );
		while(getPatch().getColor().getRed() != 1) 
			randomLocation();
	}

	/**
	 * @return true if the agent has lost his target
	 */
	protected boolean targetLost(){
		if ( target == null )
			return true;
		return distance( target ) > visibility;
	}

	/**
	 * the agent become panic
	 * @return the next action
	 */
	protected String returnPanic(){
		target = null;
		following = false;
		giveUpRole("follower");
		playRole( "panic" );
		return "shout";
	}

	/**
	 * the agent as been alerted of fire, he takes his role and shout
	 */
	protected String evacuate() {
		giveUpRole( "work" );
		playRole( role );
		setColor( Color.yellow );
		return "shout";
	}

	/**
	 * he moves
	 */
	protected String move(){
		if ( isOut() )
			return "goOut";
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( cptAction < 5 ){
			cptAction++;
			return "move";
		}
		else{
			wigglec();
			cptAction = 0;
			return "shout";
		}
	}

	protected String tetanize(){
		return "tetanize";
	}

	/**
	 * 
	 * @return
	 */
	protected String gather(){//***********************revoir cette partie
		if ( distance( target ) > 2 ){
			setHeadingTowards( target );
			fdc(1);
			return "gather";
		}
		else
			return "stayNear";
	}

	/**
	 * stay in close from his target
	 * @return
	 */
	protected String stayNear(){//donner une chance d'avoir un groupe plus grand************************
		if ( distance( target ) > 3 ){
			setHeadingTowards( target );
			fdc(1);
		}
		else if ( cptAction == 5 ){
			wigglec();
			cptAction = 0;
		}
		else
			cptAction++;
		return "shout";
	}

	/**
	 * the agent follow his target
	 * @return his next action
	 */
	protected String follow(){
		if ( isOut() )
			return "goOut";
		if( pheromoneCollision( "feu" ) )
			return "burn";
		double d = distance( target );
		if ( targetLost() )
			return "returnPanic";

		if ( d > followDistance ){
			callLeader();
			return "come";
		}

		if ( d < 3 )
			return "follow";

		else{
			followTarget();
			return "follow";
		}
	}

	protected String come(){
		if ( isOut() )
			return "goOut";
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( cptAction > 2 ){
			cptAction = 0;
			return "follow";
		}
		else{
			followTarget();
			cptAction++;
			return "come";
		}
	}

	/**
	 * ask the Leader to wait for him
	 */
	protected void callLeader(){
		target.setTarget( this );
		target.setNextAction( "waitPlease" );
	}

	protected String comeHere(){//rejoint le meneur qui vient de l'appeller
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( isOut() )
			return "goOut";
		if ( pointX != xcor() || pointY != ycor() ){//il est trop loin, il avance
			double d = distance( pointX, pointY );
			setXY( getX() + varX()/d, getY() + varY()/d );//attention ils passent at travers les murs*****************************
			nbStep++;
			return "comeHere";
		}
		else
			return "shout";
	}


	protected void goOut() {
		information();
		killAgent( this );
	}

	protected String shout(){//**************************ajout de la methode nearestTurtle no wall
		List<AgentTurtle > list = getWorkersNoWall( listenDistance );
		for (int i = 0; i < list.size(); i++ ){
			list.get(i).setInAlert();
		}
		AgentTurtle l = getNearestTurtleNoWall( visibility, "panic" );
		if ( l != null ){
			target = l;
			return "gather";
		}
		return "move";
	}

	/**
	 * s'enfuir du feu
	 * @return
	 */
	//protected String flee();


	@Override
	protected void information() {
		// TODO Auto-generated method stub

	}


	@Override
	protected String waitHere() {
		return "waitHere";
	}


}
