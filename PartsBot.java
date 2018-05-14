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
    public double normalizeBearing( double angle )
    {
        while ( angle > 180 ) {
            angle -= 360;
        }
        while ( angle < -180 ) {
            angle += 360;
        }
        return angle;
    }
    
    /**
     * 
     * weeddddooododfsdfs
     * @param x1    john
     * @param y1    jon
     * @param x2    johnny
     * @param y2    jonny
     * @return smthing dopble
     */
    public double absoluteBearing( double x1, double y1, double x2, double y2 )
    {
        double xo = x2 - x1;
        double yo = y2 - y1;
        double hyp = Point2D.distance( x1, y1, x2, y2 );
        double arcSin = Math.toDegrees( Math.asin( xo / hyp ) );
        double bearing = 0;

        if ( xo > 0 && yo > 0 )
        { // both pos: lower-Left
            bearing = arcSin;
        }
        else if ( xo < 0 && yo > 0 )
        { // x neg, y pos: lower-right
            bearing = 360 + arcSin; 
        }
        else if ( xo > 0 && yo < 0 )
        { // x pos, y neg: upper-left
            bearing = 180 - arcSin;
        }
        else if ( xo < 0 && yo < 0 )
        { // both neg: upper-right
            bearing = 180 - arcSin;
        }

        return bearing;
    }
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
        Radar radar = (Radar)parts[RADAR];
        if ( radar.shouldTrack( e ) ) {
            enemy.update( e, this );
        }
    }
    /**
     * @param e     no
     */
    public void onRobotDeath( RobotDeathEvent e )
    {
        Radar radar = (Radar)parts[RADAR];
        if ( radar.wasTracking( e ) ) {
            enemy.reset();
        }
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