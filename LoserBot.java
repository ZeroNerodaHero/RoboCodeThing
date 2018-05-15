package robo;

import robocode.*;

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
public class LoserBot extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();

    private RobotPart[] parts = new RobotPart[3]; // make three parts

    private final static int RADAR = 0;

    private final static int GUN = 1;

    private final static int TANK = 2;


    /**
     * something
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
            if ( i == 0 )
            {
                execute();
            }
        }
    }


    /**
     * something
     * 
     * @param e
     *            something
     */
    public void onScannedRobot( ScannedRobotEvent e )
    {
        Radar radar = (Radar)parts[RADAR];
        if ( radar.shouldTrack( e ) ) {
            enemy.update( e, this );
        }
            
    }


    /**
     * something
     * 
     * @param e
     *            something
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
     * @return interface something
     */
    public interface RobotPart
    {
        /**
         * something
         * TODO Write your method description here.
         */
        public void init();
        
        /**
         * something
         * TODO Write your method description here.
         */
        public void move();
    }


    /**
     * 
     * TODO Write your method description here.
     * 
     * @return double
     * @param x1
     *            a
     * @param y1
     *            a
     * @param x2
     *            a
     * @param y2
     *            a
     */
    // computes the absolute bearing between two points
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
            bearing = 360 + arcSin; // arcsin is negative here, actually 360 -
                                    // ang
        }
        else if ( xo > 0 && yo < 0 )
        { // x pos, y neg: upper-left
            bearing = 180 - arcSin;
        }
        else if ( xo < 0 && yo < 0 )
        { // both neg: upper-right
            bearing = 180 - arcSin; // arcsin is negative here, actually 180 +
                                    // ang
        }

        return bearing;
    }


    /**
     * 
     * TODO Write your method description here.
     * 
     * @param angle a
     *            
     * @return double 
     */
    // normalizes a bearing to between +180 and -180
    public double normalizeBearing( double angle )
    {
        while ( angle > 180 )
        {
            angle -= 360;
        }

        while ( angle < -180 )
        {
            angle += 360;
        }

        return angle;
    }


    /**
     * 
     * TODO Write a one-sentence summary of your class here. TODO Follow it with
     * additional details about its purpose, what abstraction it represents, and
     * how to use it.
     *
     * @author shuang248
     * @version May 11, 2018
     * @author Period: TODO
     * @author Assignment: Robo05PartsBot
     *
     * @author Sources: TODO
     */
    public class Radar implements RobotPart
    {
        /**
         * something
         */
        int radarDirection;


        /**
         * something
         */
        public void init()
        {
            setAdjustRadarForGunTurn( true );
            radarDirection = 0;

        }


        /**
         * something
         */
        public void move()
        {
            if ( enemy.none() )
            {
                // look around if we have no enemy
                setTurnRadarRight( 360 );
            }
            else
            {
                // oscillate the radar
                double turn = getHeading() - getRadarHeading() + enemy.getBearing();
                turn += 30 * radarDirection;
                setTurnRadarRight( normalizeBearing( turn ) );
                radarDirection *= -1;
            }

        }


        /**
         * something
         * 
         * @return something
         * @param e something
         */
        public boolean shouldTrack( ScannedRobotEvent e )
        {
            // track if we have no enemy, the one we found is significantly
            // closer, or we scanned the one we've been tracking.
            return ( enemy.none() || e.getDistance() < enemy.getDistance() - 70
                || e.getName().equals( enemy.getName() ) );
        }


        /**
         * something
         * 
         * @return soemthing
         * @param e something
         */
        public boolean wasTracking( RobotDeathEvent e )
        {
            return e.getName().equals( enemy.getName() );
        }
    }


    /**
     * 
     * TODO Write a one-sentence summary of your class here. TODO Follow it with
     * additional details about its purpose, what abstraction it represents, and
     * how to use it.
     *
     * @author shuang248
     * @version May 11, 2018
     * @author Period: TODO
     * @author Assignment: Robo05PartsBot
     *
     * @author Sources: TODO
     */
    public class Gun implements RobotPart
    {
        private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
        private byte radarDirection = 1;
        private byte moveDirection = 1;

        /**
         * something
         */
        public void init()
        {
            
            
        }


        /**
         * something
         */
        public void move()
        {
         // divorce radar movement from gun movement
            setAdjustRadarForGunTurn( true );
            // divorce gun movement from tank movement
            setAdjustGunForRobotTurn( true );
            // we have no enemy yet
            enemy.reset();
            // initial scan
            setTurnRadarRight( 360 );

            while ( true )
            {
                // rotate the radar
                setTurnRadarRight( 360 );

                // sit & spin
                setTurnRight( 5 );
                setAhead( 20 );
                execute();
            }
        }


        /**
         * 
         * TODO Write your method description here.
         * 
         * @return double
         * @param x1
         *            a
         * @param y1
         *            a
         * @param x2
         *            a
         * @param y2
         *            a
         */

        double absoluteBearing( double x1, double y1, double x2, double y2 )
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
                bearing = 360 + arcSin; // arcsin is negative here, actually 360
                                        // -
                                        // ang
            }
            else if ( xo > 0 && yo < 0 )
            { // x pos, y neg: upper-left
                bearing = 180 - arcSin;
            }
            else if ( xo < 0 && yo < 0 )
            { // both neg: upper-right
                bearing = 180 - arcSin; // arcsin is negative here, actually 180
                                        // +
                                        // ang
            }

            return bearing;
        }


        /**
         * 
         * TODO Write your method description here.
         * 
         * @param angle a
         *            
         * @return a
         */
        // normalizes a bearing to between +180 and -180
        double normalizeBearing( double angle )
        {
            while ( angle > 180 )
            {
                angle -= 360;
            }

            while ( angle < -180 )
            {
                angle += 360;
            }

            return angle;
        }
    }


    /**
     * something
     */
    public class Tank implements RobotPart
    {
        /**
         * something
         */
        private byte moveDirection;


        /**
         * something
         */
        public void init()
        {
            moveDirection = 1;
        }


        /**
         * something
         */
        public void move()
        {
            // turn slightly toward our enemy
            setTurnRight( normalizeBearing(
                enemy.getBearing() + 90 - ( 15 * moveDirection ) ) );

            // strafe toward him
            if ( getTime() % 20 == 0 )
            {
                moveDirection *= -1;
                setAhead( 150 * moveDirection );
            }
        }


        /**
         * @return double something
         * @param angle something
         */
        double normalizeBearing( double angle )
        {
            while ( angle > 180 ) {
                angle -= 360;
            }
                
            while ( angle < -180 ) {
                angle += 360;
            }
                
            return angle;
        }
    }
}
