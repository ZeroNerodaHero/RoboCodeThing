package bsn;

import robocode.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Random;
import robocode.control.*;
import robocode.util.*;

public class PBot extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();
    private RobotPart[] parts = new RobotPart[3]; // make three parts
    private final static int RADAR = 0;
    private final static int GUN = 1;
    private final static int TANK = 2;
   
    static double enemyEnergy;
    static double energyChange;

    /**
    once the game starts, the run function is invoked and makes:
    - a radar:
        Scans 360 and locks onto the closet enemy
    - a gun:
        Predicitve Shooter
    - a tank(body)
        Moves in a spiral
        Rams the enemy if close enough
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


    /**
     * when we lock on to something, we will:
        get the bot's degrees from our bot
        the energy and the change
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

    public void onRobotDeath( RobotDeathEvent e )
    {
        Radar radar = (Radar)parts[RADAR];
        if ( radar.wasTracking( e ) ) {
            enemy.reset();
        }  
    }

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
    
    
    //----------------------------------------------------------------
    public class Radar implements RobotPart
    {
        int radarDirection;
        public void init()
        {
            setAdjustRadarForGunTurn( true );
            radarDirection = 1;
            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
        
        //if there is no enemy scan 360
        //else follow it
        public void move()
        {
            if ( enemy.none() )
            {
                setTurnRadarRight( Double.POSITIVE_INFINITY );
            }
            else
            {
                // keep him inside a cone
                double turn = normalizeBearing(enemy.getBearing() - getRadarHeading()
                   + getHeading());
                
                turn += 10 * radarDirection;
                setTurnRadarRight( turn );
                radarDirection *= -1;
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

    //----------------------------------------------------------------
    public class Gun implements RobotPart
    {
        public void init()
        {
            setAdjustGunForRobotTurn(true);
        }
        
        public void move()
        {
         // don't shoot if there is no enemy
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
            
            if (enemy.getDistance() >= 150) time /= 2;
                
            // calculate gun turn to predicted x,y location
            double futureX = enemy.getFutureX( time );
            double futureY = enemy.getFutureY( time );
            double absDeg = absoluteBearing(getX(), getY(), futureX,futureY);
            setTurnGunRight( normalizeBearing( absDeg - getGunHeading()) );
            
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

    //----------------------------------------------------------------
    public class Tank implements RobotPart
    {
        private int moveDirection;
        int i;
        double rand;
        boolean turn, commit;
        Random r = new Random();

        public void init()
        {
            moveDirection = 1;
            i = 0;
            rand =  r.nextInt( 50 ) + 10;
            turn = false;
            commit = false;
        }

        
        public void move()
        {
            i++;
            /** rams the enemy if:
                it is close and is there
                has less energy than our bot
                commit makes the robot always ram after ramming once
            */
            if ((enemy.getDistance() < 200 && getEnergy() - 5> enemy.getEnergy() && !enemy.none()) || commit ) {
                System.out.println( "dist\t" + enemy.getDistance() );
                commit = true;
                
                setTurnRight( normalizeBearing( enemy.getBearing() ) );
                if (Math.abs( getTurnRemaining() ) < 10)
                    setAhead(200);
            }
            /**
            It will move back at forth if it hits a wall or after a random amount of time(i)
            Spiral movement
            */
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
            }
        }

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
