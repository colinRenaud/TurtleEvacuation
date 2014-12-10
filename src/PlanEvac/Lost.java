package PlanEvac;

import java.awt.Color;
import java.util.List;

import Utiles.AgentTurtle;

public class Lost extends AgentTurtle{

	boolean following;


	protected void activate(){//****************modifier les param de visibilit�
		super.activate();
		this.role = "lost";
		playRole( "lost" );
		nbStep = 0;
		cptAction = 0;
		beAlert = false;
		xdep = xcor();
		ydep = ycor();
		setNextAction( "work" );
		while(getPatch().getColor().getRed() != 1) 
			randomLocation();
	}

	protected boolean targetLost(){//return true if the agent has lost his target
		return distance( target ) > visibility;
	}


	protected String evacuate() {
		giveUpRole( "work" );
		playRole( role );
		setColor( Color.green );
		return "shout";
	}

	protected String move(){
		if ( isOut() )
			return "goOut";
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( cptAction < 5 ){
			wigglec();
			cptAction++;
			return "move";
		}
		else{
			wigglec();
			cptAction = 0;
			return "shout";
		}
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
			return "move";
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
			return "returnLost";

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

	/**
	 * the agent become panic
	 * @return the next action
	 */
	protected String returnLost(){
		target = null;
		following = false;
		giveUpRole("follower");
		playRole( "follow" );
		return "shout";
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

	/**
	 * the agent shout because he is lost, alerting the others agents
	 * @return the next action
	 */
	protected String shout(){//**************************modif a faire dans le reste
		List<AgentTurtle > list = getWorkersNoWall( listenDistance );
		for (int i = 0; i < list.size(); i++ ){
			list.get(i).setInAlert();
		}
		return "move";
	}

	//protected String rest();//se reposer

	//protected String flee();//s'nefuire

	//protected String runEverywhere();

	protected void goOut() {
		information();
		killAgent( this );
	}

	protected String waitHere() {
		return "waitHere";
	}


	@Override
	protected void information() {
		// TODO Auto-generated method stub

	}


}
