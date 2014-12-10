package PlanEvac;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import turtlekit.kernel.Turtle;
import Utiles.AgentTurtle;

public class Leader extends AgentTurtle{
	
	/**
	 * group of turtle witch are following the leader
	 */
	protected ArrayList<AgentTurtle> group;//peut etre un futur probleme quand un du groupe sortira avant le meneur
	
	/**
	 * count the number of lost which he had helped
	 */
	protected int lostFollowers;
	
	/**
	 * count the number of panics which he had helped
	 */
	protected int panicFollowers;
	
	
	protected void activate(){//****************modifier les param de visibilit�
		super.activate();
		role = "leader";
		playRole( "work" );
		nbStep = 0;
		cptAction = 0;
		beAlert = true;//***********************************
		xdep = xcor();
		ydep = ycor();
		group = new ArrayList<AgentTurtle>();
		setNextAction( "work" );
		while(getPatch().getColor().getRed() != 1) 
			randomLocation();
	}
	//********************************************** nbStep doit augmenter meme pour les setXY!!!!********************************
	/**
	 * the agent has been alerted of fire, he takes his role and think
	 */
	protected String evacuate() {
		giveUpRole( "work" );
		playRole( role );
		setColor( Color.blue );
		return "think";
	}
	
	/**
	 * if there is panics or lost around him, he help them
	 * else he move
	 * @return his next action
	 */
	protected String think(){
		if( pheromoneCollision( "feu" ) )
			return "burn";
		toAlert();
		watchGroup();
		AgentTurtle p = getNearestTurtleNoWall( visibility, "panic");
		if ( p != null && !p.following ){
			target = p;
			target.setTarget( this );
			//target.setTarget( chooseTargetToFollow() );
			return "helpPanic";
		}
		AgentTurtle l = getNearestTurtleNoWall( visibility, "lost" );
		if ( l != null && !l.following ){
			target = l;
			target.setTarget( this );
			//target.setTarget( chooseTargetToFollow() );
			return "helpLost";
		}
		orderTo( "follow" );
		return "move";
	}
	
	protected AgentTurtle chooseTargetToFollow(){//******************nouveaut�*********************!!!!!!!!!!����������������
		if ( group.size() == 0 )
			return this;
		else
			return group.get( group.size() - 1 );
	}
	
	/**
	 * he register his current position
	 * he order his group to wait here for him
	 * he order the panic to wait him
	 * @return the next action: help
	 */
	protected String helpPanic(){
		this.pointX = xcor();
		this.pointY = ycor();
		target.setNextAction( "waitHere");
		orderTo( "waitHere" );
		return "help";
	}
	
	/**
	 * order the lost to follow him
	 * @return the next action: wait here
	 */
	protected String helpLost(){
		makeLostFollow();
		return "waitHere";
	}
	
	/**
	 * alert the workers
	 */
	protected void toAlert(){
		ArrayList < AgentTurtle > liste = getWorkersNoWall( visibility );
		if ( liste.size() > 0 ){
			for ( int i = 0; i < liste.size(); i++ ){
				if ( liste.get(i).beAlert == false  ){
					liste.get(i).setNextAction( "evacuate" );
				}
			}
		}
	}
	
	/**
	 * until he's too far from the panic, he move
	 * if he is close enough he order the panic to follow him
	 * @return the next action: movoToPoint
	 */
	protected String help(){
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( isOut() )
			return "goOut";
		setColor( Color.cyan );// a virer********************************************************
		target.setColor( Color.pink );// sa aussi**********************************************
		if ( distance( target ) > 2 ){
			setHeadingTowards( target );
			fd(1);
			return "help";
		}
		else{
			makePanicFollow();		
			target = null;
			return "moveToPoint";
		}
	}
	
	
	/**
	 * while his target is too far he don't move
	 * else he thinks
	 */
	protected String waitHere(){
		if ( distance( target ) > 3 )
			return "waitHere";
		else{
			target = null;
			return "think"; 
		}
	}
	
	/**
	 * the agent move to a point
	 * @return the next action
	 */
	protected String moveToPoint(){
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( isOut() )
			return "goOut";
		double d = distance( pointX, pointY );
		if ( d > ( followDistance / 2 )  ){
			setXY( getX() + varX()/d, getY() + varY()/d );
			nbStep++;
			return "moveToPoint";
		}
		else{
			return "think";
		}
	}
	
	/**
	 * the agent move
	 */
	protected String move(){//faire que les suiveur soient en file indienne******************************MODIF*********
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( isOut() )
			return "goOut";
		if ( cptAction < 5 ){
			fdc( 1 );
			cptAction++;
			return "move";
		}
		else{
			cptAction = 0;
			randomHeading(60);
			return "think";
		}
	}
	
	
	/**
	 * the agent move to the point where a leader told him to go
	 */
	protected String comeHere(){//pour eviter les p�ssages a travers murs on utilisera la methode wallBetw<een avant
		if( pheromoneCollision( "feu" ) )
			return "burn";
		if ( isOut() )
			return "goOut";
		if ( pointX != xcor() || pointY != ycor() ){
			double d = distance( pointX, pointY );
			setXY( getX() + varX()/d, getY() + varY()/d );
			nbStep++;
			return "comeHere";
		}
		else
			return "think";
	}
	
	/**
	 * the leader wait for a follower which was too far
	 * @return
	 */
	protected String waitPlease(){
		if ( distance( target ) > followDistance - 2 )
			return "waitPlease";
		else{
			target = null;
			return "think";
		}
	}
	
	/**
	 * before to leave the map, the leader tell his group where is the exit
	 * and order them to come here
	 * then he "shout" to warn the others to come here
	 * finally we kill this agent because he is out
	 */
	protected void goOut() {
		if ( group.size() > 0 ){
			for ( int i = 0; i < group.size(); i++ ){
				group.get( i ).setPointXY( xcor(), ycor() );
				group.get( i ).setNextAction( "comeHere" );//revoir si le boolean isWaiting est vraiment utile
			}
		}
		List<AgentTurtle> liste = getOtherTurtles( listenDistance, false, AgentTurtle.class );
		for ( int i = 0; i < liste.size(); i++ ){
			 liste.get(i).setPointXY( xcor(), ycor() );
			 liste.get(i).setNextAction( "comeHere" ); 
		}
		information();
		killAgent( this );
	}
	
	/**
	 * the leader order something to his group
	 * @param o the order
	 */
	protected void orderTo( String o ){
		for ( int i = 0; i < group.size(); i++ ){
			group.get( i ).setNextAction( o );
		}
	}
	
	/**
	 * delete the agent of the group which he had lost
	 */
	protected void watchGroup(){
		for ( int i = 0; i < group.size(); i++ ){
			if ( distance( group.get(i)) > visibility ){
				group.get(i).loseTarget();
				group.remove( group.get(i));
			}
		}
	}
	
	/**
	 * kill the agent if he is on fire
	 */
	protected void burn() {
		if(pheromoneCollision("feu") && ! isOut()) {
			information();
			setPatchColor( Color.gray );
			orderTo( "flee" );//***********************a definir
			killAgent(this);
		}
	}
	
	/**
	 * 
	 */
	protected void makePanicFollow(){
		target.giveUpRole( "panic" );
		target.playRole( "follower ");
		target.setNextAction( "follow" );
		target.setFollowing();
		group.add( target );
		target.setTarget( this );
	}
	
	/**
	 * 
	 */
	protected void makeLostFollow(){
		target.giveUpRole( "lost" );
		target.playRole( "follower ");
		target.setNextAction( "follow" );
		target.setFollowing();
		group.add( target );
		target.setTarget( this );
	}
	
	protected String explore(){
		return "";
	}
	
	protected void information(){//cette m�thode donnera les details enregistr�s par l'agent
		
	}
	
	

}
