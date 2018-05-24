package robo;

import robocode.*;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * A modular bot adhering to the RoboPart Interface.
 * 
 * @author TODO Your Name
 * @version TODO Date
 * 
 * @author Period - TODO Your Period
 * @author Assignment - PartsBot
 * 
 * @author Sources - TODO list collaborators
 */
public class PartsBot extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
    private newRadar = new Radar();
    public void run()
    {
        newRadar.move();
    }
 
    public void onScannedRobot( ScannedRobotEvent e )
    {
        Radar radar = (Radar) newRadar;
        if ( radar.shouldTrack( e ) ) {
            enemy.update( e, this );
        }
    }
    
    public void onScannedRobot( ScannedRobotEvent e )
    {
        Radar radar = (Radar) newRadar;
        if ( radar.shouldTrack( e ) ) {
            enemy.update( e, this );
        }
        //if the number of bots != 1 -> melee mode
        if(firemode != 1) {
            meleemode(e);
        } else {
            duelmode(e);
        }
    }
    //when the robot is in melee mode
    public void meleemode(ScannedRobotEvent e) {
        //run predictive_shooter()
        firemode = getOthers();
        if(getOthers == 1) {
            duelmode(e)
        }
    }
    //when the robot is in duel mode
    public void duelmode(ScannedRobotEvent e) {
        //run predictive_shooter()
        if(e.getlife <= 20) {
            //shoot things
            predictiveshooter();
        }
    }

    public interface RobotPart
    {
        public void init();
        public void move();
    }
    
    public class Radar implements RobotPart
    {
        public void init()
        {
            setAdjustRadarForGunTurn(true);
        }
        public void move()
        {
            setTurnRadarRight(90);
        }
        public boolean shouldTrack( ScannedRobotEvent e )
        {
            // track if we have no enemy, the one we found is significantly
            // closer, or we scanned the one we've been tracking.
            return ( enemy.none() || e.getDistance() < enemy.getDistance() - 70
                || e.getName().equals( enemy.getName() ) );
        }
        public boolean wasTracking( RobotDeathEvent e )
        {
            return e.getName().equals( enemy.getName() );
        }
    }
}
