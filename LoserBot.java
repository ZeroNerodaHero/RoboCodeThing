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
    /**
     * 
     * eqeqeqweq
     * @param angle     john
     * @return double
     */
    // normalizes a bearing to between +180 and -180
    
    /**
     * John is not here
     */
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

    /**
     * @param e    no
     */
    public void onScannedRobot( ScannedRobotEvent e )
    {
        
    }
    /**
     * @param e     no
     */
    public void onRobotDeath( RobotDeathEvent e )
    {
        
    }

    
    // ... put normalizeBearing and absoluteBearing methods here

    
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
        }

        /**
         * nothing hre
         */
        public void move()
        {
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
        }
        
        
        
        
        
        /**
         * nooooooooo
         */
        public void move()
        {
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
        }
    }
    
}
