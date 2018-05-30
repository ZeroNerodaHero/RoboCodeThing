package bsn;

import robocode.*;



import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Random;
import robocode.control.*;


import robocode.util.*;



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
public class PBot extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();

    private RobotPart[] parts = new RobotPart[3]; // make three parts

    private final static int RADAR = 0;

    private final static int GUN = 1;

    private final static int TANK = 2;
    
    static double enemyEnergy;
    
    static double energyChange;

    //private BattlefieldSpecification  map = new BattlefieldSpecification();
    /**
     * something
     */

    public void run()
    {
        
        parts[RADAR] = new Radar();
        parts[GUN] = new Gun();
        parts[TANK] = new Tank();
        
        enemy.reset();
        enemyEnergy = 100;
        
        // initialize each part
        for ( int i = 0; i < parts.length; i++ )
        {
            // behold, the magic of polymorphism
            parts[i].init();
        }

        // iterate through each part, moving them as we go
        for ( int i = 0; true; i = ( i + 1 ) % parts.length )
        {
            
            
            
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
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        energyChange=(enemyEnergy-(e.getEnergy()));
        enemyEnergy = e.getEnergy();
        Radar radar = (Radar)parts[RADAR];
        if ( true ) {//radar.shouldTrack( e ) ) {
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
            radarDirection = 1;
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            

        }


        /**
         * something
         */
        public void move()
        {
            
            if ( enemy.none() )
            {
                // look around
                setTurnRadarRight( Double.POSITIVE_INFINITY );
                
            }
            else
            {
                // keep him inside a cone
                double turn = normalizeBearing(enemy.getBearing() - getRadarHeading()
                   + getHeading());
                
                turn += 10* radarDirection;
                setTurnRadarRight( turn );
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
        /**
         * something
         */
        public void init()
        {
            setAdjustGunForRobotTurn(true);
            
            //setTurnGunRightRadians(- getHeading());
        }


        /**
         * something
         */
        public void move()
        {
         // don't shoot if I've got no enemy
            if ( enemy.none() ) {
                
                return;
            }
            //System.out.println( "gun move:" + enemy.getX() + " " + enemy.getY() );    
            double absoluteBearing = getHeading() + enemy.getBearing();
            // calculate firepower based on distance
            double firePower = Math.min( 500 / enemy.getDistance(), 3 );
            // calculate speed of bullet
            double bulletSpeed = 20 - firePower * 3;
            // distance = rate * time, solved for time
            long time = (long)( enemy.getDistance() / bulletSpeed );
            if (enemy.getDistance() >= 150) 
                time = time/2;
            // calculate gun turn to predicted x,y location
            double futureX = enemy.getFutureX( time );
            double futureY = enemy.getFutureY( time );
            double absDeg = absoluteBearing(getX(), getY(), futureX,futureY);
            setTurnGunRight( normalizeBearing( absDeg - getGunHeading()) );
            //System.out.println("enemy.getX\t" + enemy.getX() + "\tenemy.getY\t" + enemy.getY());
            // non-predictive firing can be done like this:
            // double absDeg = absoluteBearing(getX(), getY(), enemy.getX(),enemy.getY());
            /**
            if (absDeg > 247) {
                setFire(1);
            }
            */
            // turn the gun to the predicted x,y location
            //setTurnGunRight( normalizeBearing( absDeg - getGunHeading()) );
            
            
            //System.out.println( "THE TURN:" + normalizeBearing( absDeg - getGunHeading()) );
            // if the gun is cool and we're pointed in the right direction, shoot!
            
            if ( getGunHeat() == 0 && Math.abs( getGunTurnRemaining() ) < 10 )
            {
                setFire( firePower );
            }
            
            
            
            
        }
        
        
        
        double absoluteBearing(double x1, double y1, double x2, double y2) {
            double xo = x2-x1;
            double yo = y2-y1;
            double hyp = Point2D.distance(x1, y1, x2, y2);
            double arcSin = Math.toDegrees(Math.asin(xo / hyp));
            double bearing = 0;
            
            if (xo > 0 && yo > 0) { // both pos: lower-Left t r
                bearing = arcSin;
            } else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right t l
                bearing = 360 + arcSin; // arcsin is negative here, actually 360 - ang 
            } else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left b r
                bearing = 180 - arcSin;
            } else if (xo < 0 && yo < 0) { // both neg: upper-right b l
                bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
            }
           //System.out.println( "ABSbearing: x: " + xo + " y: "+ yo + " hyp: " + hyp + " b: " + bearing );
            return bearing;
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
        int i;
        double rand;
        boolean turn, commit;
        Random r = new Random();

        /**
         * something
         */
        public void init()
        {
            moveDirection = 1;
            i = 0;
            rand =  r.nextInt( 50 ) + 10;
            turn = false;
            commit = false;
            //setTurnRight(-getHeading()-0);
        }


        /**
         * something
         */
        public void move()
        {
            
            
            
            //double forwarddist = r.nextDouble() * 150;
            
            i++;
            /**
            if ( ((getX() - 50 <= 18 || getX() + 50 >= 782 || getY() - 50 <= 18 || getY() + 50 >= 582) || getVelocity() == 0) && i >25 ) {
                moveDirection *= -1;
                i = 0;
            }
            */
            if ((enemy.getDistance() < 200 && getEnergy() - 5> enemy.getEnergy() && !enemy.none()) || commit ) { //&& getEnergy() > enemy.getEnergy()
                System.out.println( "dist\t" + enemy.getDistance() );
                commit = true;
                
                setTurnRight( normalizeBearing( enemy.getBearing() ) );
                if (Math.abs( getTurnRemaining() ) < 10)
                    setAhead(200);
            }
            else {
                System.out.println( "Rand\t" + i + "\t" + moveDirection );
                if(getVelocity() == 8) {
                    turn = true;
                }
                if(i == rand) {
                    moveDirection *= -1;
                    i = 0;
                    rand =  r.nextInt( 50 ) + 10;
                    turn = false;
                }
                if (getVelocity() == 0 && turn == true) {
                    moveDirection *= -1;
                    i = 0;
                } 
                // spiral toward our enemy
                setTurnRight( normalizeBearing( enemy.getBearing() + 90
                    - ( 30 * moveDirection ) ) );
                setAhead( enemy.getDistance() * moveDirection );
                //setAhead( rand * moveDirection );
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
