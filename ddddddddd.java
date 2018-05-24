package bsn;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
 
import robocode.*;
import robocode.util.*;
/**
 * A modular bot adhering to the RoboPart Interface.
 * 
 * http://robowiki.net/wiki/SuperMercutio 
 * 
 * @author TODO Your Name
 * @version TODO Date
 * 
 * @author Period - TODO Your Period
 * @author Assignment - PartsBot
 * 
 * @author Sources - TODO list collaborators
 */
public class LoserBot extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
    
    private RobotPart[] parts = new RobotPart[1]; // make three parts
    private final static int RADAR = 0;
    
    final static double FIRE_POWER=2;
    final static double FIRE_SPEED=20-FIRE_POWER*3;
    final static double BULLET_DAMAGE=10;
    /*
     * change these statistics to see different graphics.
     */
    final static boolean PAINT_MOVEMENT=true;
    final static boolean PAINT_GUN=false;
 
    static double enemyEnergy;
    
    
    public void run()
    {
        parts[RADAR] = new Radar();
        

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
        int radarDirection;
        public void init()
        {
            setAdjustRadarForGunTurn(true);
            radarDirection = 0;
        }
        public void move()
        {
            if ( enemy.none() )
            {
                // look around
                setTurnRadarRight( 360 );
            }
            else
            {
                // keep him inside a cone
                double turn = getHeading() - getRadarHeading()
                    + enemy.getBearing();
                turn += 30 * radarDirection;
                setTurnRadarRight( turn );
                radarDirection *= -1;
                enemy.reset();
            }
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
    
    
    /*
     * This is where we will paint our waves;
     */
    
    /*
     * This class is the data we will need to use our movement waves.
     */
    public static class MovementWave{
        Point2D.Double origin;
        double startTime;
        double speed;
        double angle;
        double latVel;
    }
    /*
     * This class is the data we will need to use for our targeting waves.
     */
    public class GunWave{
        double speed;
        Point2D.Double origin;
        int velSeg;
        double absBearing;
        double startTime;
    }
    

}
