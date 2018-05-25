package bsn;

import robocode.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Random;
import robocode.util.*;

public class PBot extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();

    private RobotPart[] parts = new RobotPart[3]; // make three parts

    private final static int RADAR = 0;

    private final static int GUN = 1;

    private final static int TANK = 2;
    
    static double enemyEnergy;

    public void run()
    {
        parts[RADAR] = new Radar();
        parts[GUN] = new Gun();
        parts[TANK] = new Tank();
        
        enemy.reset();
        
        enemyEnergy = 100;
        for ( int i = 0; i < parts.length; i++ )
        {
            parts[i].init();
        }
        for ( int i = 0; true; i = ( i + 1 ) % parts.length )
        {
            parts[i].move();
            
            if ( i == 0 )
            {
                execute();
            }
        }
    }
    
    public void onScannedRobot( ScannedRobotEvent e )
    {
        double absBearing=e.getBearingRadians()+getHeadingRadians();
        
        
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
            setAdjustRadarForGunTurn( true );
            radarDirection = 1;
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            

        }

        public void move()
        {
            
            if ( enemy.none() )
            {
                setTurnRadarRight( Double.POSITIVE_INFINITY );
                
            }
            else
            {
                double turn = normalizeBearing(enemy.getBearing() - getRadarHeading()
                   + getHeading());
                
                turn += 10* radarDirection;
                setTurnRadarRight( turn );
                radarDirection *= -1;
            }
        }

        public boolean shouldTrack( ScannedRobotEvent e )
        {
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
            if ( enemy.none() ) {
                return;
            }
                
            double absoluteBearing = getHeading() + enemy.getBearing();
            // calculate firepower based on distance
            double firePower = Math.min( 500 / enemy.getDistance(), 3 );
            // calculate speed of bullet
            double bulletSpeed = 20 - firePower * 3;
            // distance = rate * time, solved for time
            long time = (long)( enemy.getDistance() / bulletSpeed );

            // calculate gun turn to predicted x,y location
            double futureX = enemy.getFutureX( time );
            double futureY = enemy.getFutureY( time );
            
            setTurnGunRight( normalizeBearing( absoluteBearing - getGunHeading()) );
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


        /**
         * something
         */
        public void init()
        {
            moveDirection = 1;
            
            //setTurnRight(-getHeading()+90);
        }


        /**
         * something
         */
        public void move()
        {
            
            Random r = new Random();
            // anti haveatitbot
            //double forwarddist = 150;
            double forwarddist = r.nextDouble() * 150;
            // turn slightly toward our enemy
            setTurnRight( normalizeBearing(
                enemy.getBearing() + 90 - ( 15 * moveDirection ) ) );

            // strafe toward him
            if ( getTime() % 20 == 0 )
            {
                moveDirection *= -1;
                setAhead( forwarddist * moveDirection );
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
