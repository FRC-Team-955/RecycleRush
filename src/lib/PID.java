package lib;

import edu.wpi.first.wpilibj.Timer;

/**
 * PID controller
 * 
 * @author Merfoo
 */
public class PID
{
	// Constants P, I, D
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    
    // For limiting the error sum, integral of error
    private boolean limitErr = false;
    private double maxErr = 0;
    private double minErr = 0;
    private double errSum = 0;
    
    // Prev error/time for derivative of error
    private double prevErr = 0;
    private double errD = 0;
    private double prevTime = 0;
    private double deltaT = 0;
    private double deltaSysT = 0;
    private double prevSysTime = 0;
    
    // Output of pid
    private double output = 0;
    
    // Statuses of pid
    private boolean isRunning = false;
    private boolean needReset = false;
    
    // Timer used for derivative of error
    private Timer timer = new Timer();
    
    /**
     * Creates a pid controller with kP, kI, kD constants based on args
     * 
     * @param newP
     * @param newI
     * @param newD 
     */
    public PID(double newP, double newI, double newD)
    {
        kP = newP;
        kI = newI;
        kD = newD;
    }
    
    public void setErrLimits(double min, double max)
    {
    	minErr = min;
    	maxErr = max;
    }
    
    public void setErrLimitMode(boolean mode)
    {
    	limitErr = mode;
    }
    
    public boolean getErrLimitMode()
    {
    	return limitErr;
    }
    
    /**
     * Updates the pid loop output value
     * 
     * @param curr
     * @param want 
     */
    public void update(double curr, double want)
    {
        deltaT = timer.get() - prevTime;     // time diff since last update call
        prevTime = timer.get();                     // update prevTime value
        double errP = want - curr;                  // err = diff in pos aka proportional
        errSum += errP * deltaT;                    // integral of the err aka total err
        
        // Only limit the error if limit err mode is true
        if(limitErr)
        {	
	        if(errSum >= maxErr)
	        	errSum = maxErr;
	        
	        else if(errSum <= minErr)
	        	errSum = minErr;
        }
        
        errD = (errP - prevErr) / deltaT;   	    // derivative of err aka change in err
        prevErr = errP;
        output = (errP * kP) + (errSum * kI) + (errD * kD);
    
        deltaSysT = (System.currentTimeMillis() - prevSysTime) / 1000;
        prevSysTime = (deltaSysT * 1000) + prevSysTime;
    }
    
    /**
     * Returns the output from the pid loop
     * 
     * @return 
     */
    public double getOutput()
    {
        return output;
    }
    
    /**
     * Sets the kP, kI, kD constants
     * 
     * @param newP
     * @param newI
     * @param newD 
     */
    public void setConsts(double newP, double newI, double newD)
    {
        kP = newP;
        kI = newI;
        kD = newD;
    }
    
    /**
     * Resets the pid loop by setting output, totalErr, prevErr to 0, and resetting timer
     */
    public void reset()
    {
    	needReset = false;
        output = 0;
        errSum = 0;
        prevErr = 0;
        prevTime = 0;
        timer.reset();
    }
    
    /**
     * Starts local Timer
     */
    public void start()
    {
    	timer.start();
    	isRunning = true;
    }
    
    /**
     * Stops local Timer
     */
    public void stop()
    {
    	timer.stop();
    	isRunning = false;
    }
    
    /**
     * @return Returns true if the pid is running
     */
    public boolean isRunning()
    {
    	return isRunning;
    }

    
    /**
     * 
     * @return The total accumulation of error, also called the 'I' term
     */
    public double getErrSum()
    {
    	return errSum;
    }
    /**
     * 
     * @return The 'D' term - the rate of change of error
     */
    public double getErrD()
    {
    	return errD;
    }

    /**
     * 
     * @return The difference in time between the previous sample and current sample in seconds
     */
    
    public double getDeltaT()
    {
    	return deltaT;
    }
    
    public void setNeedReset(boolean newNeedReset)
    {
    	needReset = newNeedReset;
    }
    
    public boolean getNeedReset()
    {
    	return needReset;
    }
    
    /**
     * 
     * @return The difference in time based on the system clock (not the WPI Timer class) in milliseconds
     */
    public double getDeltaSysT()
    {
    	return deltaSysT;
    }
}