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
    
    private RobotPart[] parts = new RobotPart[3]; // make three parts
    private final static int RADAR = 0;
    private final static int GUN = 1;
    private final static int TANK = 2;
    
    public void run()
    {
        parts[RADAR] = new Radar();
        parts[GUN] = new Gun();
        parts[TANK] = new Tank();

        // initialize each part
        for ( int i = 0; i < parts.length; i++ )
        {
            // behold, the magic of polymorphism
            parts[i].init();
        }

        // iterate through each part, moving them as we go
        for ( int i = 0; true; i = ( i + 1 ) % parts.length )
        {
            // polymorphism galore!
            parts[i].move();
            if ( i == 0 ) {
                execute();
            }
        }
    }
 
    public void onScannedRobot( ScannedRobotEvent e )
    {
        Radar radar = (Radar)parts[RADAR];
        if ( radar.shouldTrack( e ) ) {
            enemy.update( e, this );
        }
    }
    
    public void onRobotDeath( RobotDeathEvent e )
    {
        Radar radar = (Radar)parts[RADAR];
        if ( radar.wasTracking( e ) ) {
            enemy.reset();
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
    
    public class Gun implements RobotPart
    {
        public void init()
        {
            setAdjustGunForRobotTurn(true);
        }
        public void move()
        {
         
        }
    }
    public class Tank implements RobotPart
    {
        public void init()
        {
            setColors(null, Color.RED, Color.GREEN, null, new Color(150, 0, 150));
        }
        public void move()
        {
        }
    }
    
}
