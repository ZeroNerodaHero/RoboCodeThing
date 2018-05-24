package robo;

import robocode.*;

/**
 * Record the state of an enemy bot.
 * 
 * @author TODO Your Name
 * @version TODO Date
 * 
 * @author Period - TODO Your Period
 * @author Assignment - EnemyBot
 * 
 * @author Sources - TODO list collaborators
 */
public class EnemyBot
{
    private double bearing;
    private double distance;
    private double energy;
    private double heading;
    private double velocity;
    private String name;
    /**
     * EnemyBot
     */
    public EnemyBot()
    {
        reset();
    }
    /**
     * EnemyBot getBearing
     */
    /**
     * 
     * TODO Write your method description here.
     * @return bearing
     */
    public double getBearing()
    {
        // TODO Your code here
        return bearing; // Fix this!!
    }
    /**
     * EnemyBot getDistance
     * @return distance
     */
    public double getDistance()
    {
        // TODO Your code here
        return distance; // Fix this!!
    }
    /**
     * EnemyBot getEnergy
     * @return energy
     */
    public double getEnergy()
    {
        // TODO Your code here
        return energy; // Fix this!!
    }
    /**
     * EnemyBot getHeading
     * @return heading;
     */
    public double getHeading()
    {
        // TODO Your code here
        return heading; // Fix this!!
    }
    /**
     * EnemyBot getVelocity
     * @return velocity
     */
    public double getVelocity()
    {
        // TODO Your code here
        return velocity; // Fix this!!
    }
    /**
     * EnemyBot getName
     * @return name
     */
    public String getName()
    {
        // TODO Your code here
        return name; // Fix this!!
    }
    /**
     * EnemyBot update
     * @param srEvt ScannedRobotEvent
     */
    public void update( ScannedRobotEvent srEvt )
    {
        // TODO Your code here
        if (srEvt.getName().equals(name))
        {
            update(srEvt);
        }
    }
    /**
     * EnemyBot reset
     */
    public void reset()
    {
        // TODO Your code here
        
        bearing = 0.0;
        distance = 0.0;
        energy = 0.0;
        heading = 0.0;
        velocity = 0.0;
        name = "";
        
    }
    /**
     * EnemyBot none
     * @return none
     */
    public boolean none()
    {
        return name.length() == 0;
    }
}