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
     * calls reset
     */
    public AdvancedEnemyBot()
    {
        reset();
    }

    /**
     * 
     * john was here
     * @return x    things
     */
    public double getX()
    {
        // TODO Your code here
        return x;
    }

    /**
     * 
     * nooo john
     * @return y    thiongs
     */
    public double getY()
    {
        // TODO Your code here
        return y;
    }

    /**
     * 
     * so much thingggssgsg
     * @param e     wow
     * @param robot     [ppzl no
     */
    public void update( ScannedRobotEvent e, Robot robot )
    {
        super.update( e );
        double absBearingDeg = (robot.getHeading() + e.getBearing());
        if (absBearingDeg < 0) {
            absBearingDeg += 360;
        }
        x = robot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance();
        y = robot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance();
    }

    /**
     * 
     * wrewrwer
     * @param when  john is here
     * @return some x
     */
    public double getFutureX( long when )
    {
        // TODO Your code here
        return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
    }

    /**
     * 
     * wrewrwer
     * @param when  john is here
     * @return some x
     */
    public double getFutureY( long when )
    {
        // TODO Your code here
        return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
    }

    /**
     * twfwfwfwef
     */
    public void reset()
    {
        super.reset();
        x = 0;
        y = 0;
    }

}