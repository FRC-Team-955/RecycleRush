package lib;

import edu.wpi.first.wpilibj.Timer;

/**
 * PID controller
 * 
 * @author Merfoo
 */
public class PID
{
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;
    private double maxErr = 0;
    private double minErr = 0;
    private double errSum = 0;
    private double prevErr = 0;
    private double output = 0;
    private double prevTime = 0;
    private boolean isRunning = false;
    private boolean limitErr= false;
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
    	double deltaT = timer.get() - prevTime;		// time diff since last update call
        prevTime = timer.get();						// update prevTime value
        double errP = want - curr;      			// err = diff in pos aka proportional
        errSum += errP;               			// integral of the err aka total err
        
        // Only limit the error if the mode 
        if(limitErr)
        {	
	        if(errSum >= maxErr)
	        	errSum = maxErr;
	        
	        else if(errSum <= minErr)
	        	errSum = minErr;
        }
        
        double errD = (errP - prevErr) / deltaT;   	// derivative of err aka change in err
        output = (errP * kP) + ((errSum * kI) * deltaT) + (errD * kD);
        prevErr = errP;
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
     * Returns true if the pid is running
     */
    public boolean isRunning()
    {
    	return isRunning;
    }

    public double getErrSum()
    {
    	return errSum;
    	  	
    }
}