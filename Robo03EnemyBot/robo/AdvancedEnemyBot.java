package robo;

import robocode.*;

/**
 * Record the advanced state of an enemy bot.
 * 
 * @author TODO Your Name
 * @version TODO Date
 * 
 * @author Period - TODO Your Period
 * @author Assignment - AdvancedEnemyBot
 * 
 * @author Sources - TODO list collaborators
 */
public class AdvancedEnemyBot extends EnemyBot
{
    private double x;
    private double y;
/**
 * sdfsf
 */
    public AdvancedEnemyBot()
    {
        reset();
    }
/**
 *@return x
 */
    public double getX()
    {
        // TODO Your code here
        return x; // Fix this!!
    }
/**
 * 
 * TODO Write your method description here.
 * @return y
 */
    public double getY()
    {
        // TODO Your code here
        return y; // Fix this!!
    }
/**
 * 
 * TODO Write your method description here.
 * @param e ScannedRobotEvent
 * @param robot Robot
 */
    public void update( ScannedRobotEvent e, Robot robot )
    {
        super.update( e );
        double absBearingDeg = (robot.getHeading() 
                        + e.getBearing());
        if (absBearingDeg < 0) 
        {
            absBearingDeg += 360;
        }
     // yes, you use the _sine_ to get the X value because 0 deg is North
        x = robot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * 
                        e.getDistance();
     // yes, you use the _cosine_ to get the Y value because 0 deg is North
        y = robot.getY() + Math.cos(Math.toRadians(absBearingDeg)) *
                        e.getDistance();
        
    }
    /**
     * TODO Write your method description here.
     * @param when getFutureX
     * @return futureX
     */
    public double getFutureX( long when )
    {
        // TODO Your code here
        return x + Math.sin(Math.toRadians(getHeading())) *
                        getVelocity() * when; // Fix this!!
    }
/**
 * 
 * TODO Write your method description here.
 * @param when getFutureY
 * @return futureY
 */
    public double getFutureY( long when )
    {
        // TODO Your code here
        return y + Math.cos(Math.toRadians(getHeading())) * 
                        getVelocity() * when; // Fix this!
    }
/**
 * sdsf
 */
    public void reset()
    {
        // TODO Your code here
        super.reset();
        x = 0.0;
        y = 0.0;
    }

}