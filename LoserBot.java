package robo;

import java.util.Random;
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
    private Radar newRadar = new Radar();
    private int firemode = getOthers();
    private double enemy_energy = 100;
    private int movedir = 1;
    private int turndir = 1;
    private Random rand = new Random();
    public void run()
    {
        //don't do anything until u find the enemy robot
        newRadar.move();
        int turndir = rand.nextInt(1);
    }
 
    public void onScannedRobot( ScannedRobotEvent e )
    {
        Radar radar = (Radar) newRadar;
        if ( radar.shouldTrack( e ) ) {
            enemy.update( e, this );
        }
        firemode = getOthers();
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
        } runaway(e);
    }
    //when the robot is in duel mode
    public void duelmode(ScannedRobotEvent e) {
        //run predictive_shooter()
        if(e.getlife <= 20) {
            //shoot things
            engage(e);
        } runaway(e);
    }
    
    public boolean didenemyshoot(ScannedRobotEvent e){
        if(e.getEnergy() != enemy_energy){
            return true;
        } return false;
    }
    
    //act like a chicken to dodge bullets
    public void runaway(ScannedRobotEvent e){
        if(didenemyshoot(e)){
            //moves away from the enemy
            setTurnRight(5 * turndir);
            setAhead(e.getVelocity * movedir);
            movedir *= -1;
        }
    }
    
    //attack the enemy robot
    public void engage(ScannedRobotEvent e){
        //move towards the bot
        /*
        .....code
        */
        predictiveshooter();
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
}
