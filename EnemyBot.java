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
     * smthing
     */
    public EnemyBot()
    {
        reset();
    }

    /**
     * smthing
     * @return the name
     */
    public double getBearing()
    {
        // TODO Your code here
        return bearing; // Fix this!!
    }

    /**
     * smthing
     * @return the name
     */
    public double getDistance()
    {
        // TODO Your code here
        return distance; // Fix this!!
    }

    /**
     * smthing
     * @return the name
     */
    public double getEnergy()
    {
        // TODO Your code here
        return energy; // Fix this!!
    }

    /**
     * smthing
     * @return the name
     */
    public double getHeading()
    {
        // TODO Your code here
        return heading; // Fix this!!
    }

    /**
     * smthing
     * @return the name
     */
    public double getVelocity()
    {
        // TODO Your code here
        return velocity; // Fix this!!
    }

    /**
     * smthing
     * @return the name
     */
    public String getName()
    {
        // TODO Your code here
        return name; // Fix this!!
    }

    /**
     * smthing
     * @param srEvt     somerobot
     */
    public void update( ScannedRobotEvent srEvt )
    {
        bearing = srEvt.getBearing();
        distance = srEvt.getDistance();
        energy = srEvt.getEnergy();
        heading = srEvt.getHeading();
        velocity = srEvt.getVelocity();
        name = srEvt.getName();
    }
    
    /**
     * smthing
     */
    public void reset()
    {
        bearing = 0;
        distance = 0;
        energy = 0;
        heading = 0;
        velocity = 0;
        name = "";
    }

    /**
     * smthing
     * @return bool
     */
    public boolean none()
    {
        return name.length() == 0;
    }
}