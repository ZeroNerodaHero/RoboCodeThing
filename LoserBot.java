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
    

    
    // ... declare the RobotPart interface and classes that implement it here
    // They will be _inner_ classes.
    /**
     * 
     *  ewfewwf
     *  efwfwefw
     *
     *  @author  wzhang831
     *  @version May 10, 2018
     *  @author  Period: TODO
     *  @author  Assignment: Robo05PartsBot
     *
     *  @author  Sources: TODO
     */
    public interface RobotPart
    {
        /**
         * nothing here
         */
        public void init();

        /**
         * nothing here
         */
        public void move();
    }

    /**
     *  follow it with additional details about its purpose, what abstraction
     *  it represents, and how to use it.
     *
     *  @author  wzhang831
     *  @version May 10, 2018
     *  @author  Period: TODO
     *  @author  Assignment: Robo05PartsBot
     *
     *  @author  Sources: TODO
     */
    public class Radar implements RobotPart
    {
        /**
         * nothing hre
         */
        public void init()
        {
            setAdjustRadarForGunTurn(true);
        }

        /**
         * nothing hre
         */
        public void move()
        {
            setTurnRadarRight(90);
        }

        
        
        /**
         * 
         * here
         * @param e     no
         * @return  boolean value
         */
        public boolean shouldTrack( ScannedRobotEvent e )
        {
            // track if we have no enemy, the one we found is significantly
            // closer, or we scanned the one we've been tracking.
            return ( enemy.none() || e.getDistance() < enemy.getDistance() - 70
                || e.getName().equals( enemy.getName() ) );
        }

        /**
         * 
         * here
         * @param e     no
         * @return  boolean value
         */
        public boolean wasTracking( RobotDeathEvent e )
        {
            return e.getName().equals( enemy.getName() );
        }
    }

    /**
     * 
     *  al details about its purpose, what abstraction
     *  it represents, and how to use it.
     *
     *  @author  wzhang831
     *  @version May 10, 2018
     *  @author  Period: TODO
     *  @author  Assignment: Robo05PartsBot
     *
     *  @author  Sources: TODO
     */
    public class Gun implements RobotPart
    {
        /**
         * fwef
         */
        public void init()
        {
            setAdjustGunForRobotTurn(true);
        }
        
        
        
        
        
        /**
         * nooooooooo
         */
        public void move()
        {
         // don't shoot if I've the is no enemy
            if ( enemy.none() ) {
                return;
            }
            double firePower = Math.min( 500 / enemy.getDistance(), 3 );
            double bulletSpeed = 20 - firePower * 3;
            long time = (long)( enemy.getDistance() / bulletSpeed );
            double futureX = enemy.getFutureX( time );
            double futureY = enemy.getFutureY( time );
            double absDeg = absoluteBearing( getX(), getY(), futureX, futureY );
            setTurnGunRight( normalizeBearing( absDeg - getGunHeading() ) );
            if ( getGunHeat() == 0 && Math.abs( getGunTurnRemaining() ) < 10 )
            {
                setFire( firePower );
            }
        }
    }
    /**
     *  w it with additional details about its purpose, what abstraction
     *  it represents, and how to use it.
     *
     *  @author  wzhang831
     *  @version May 10, 2018
     *  @author  Period: TODO
     *  @author  Assignment: Robo05PartsBot
     *
     *  @author  Sources: TODO
     */
    public class Tank implements RobotPart
    {
        /**
         * fwef
         */
        public void init()
        {
            setColors(null, Color.RED, Color.GREEN, null, new Color(150, 0, 150));
        }

        /**
         * fwef
         */
        public void move()
        {
            setAhead(10);
            setAhead(-10);
        }
    }
    
}
