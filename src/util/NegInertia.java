package util;

public class NegInertia {
	double negInertiaScalar = Config.Drive.negInteriaAdjust;
	double negInertiaAccumulator;
	double prevTurnSpeed;
	
	/**
	 * Gets an adjusted turn speed to account for the robot's inertia
	 * @param turnSpeed the speed you want to turn at
	 * @return the adjusted turn speed
	 */
	public double getTurn(double turnSpeed)
	{
		double newTurnSpeed = turnSpeed;
		double negInertia = newTurnSpeed - prevTurnSpeed;
		prevTurnSpeed = turnSpeed;
		double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;
        newTurnSpeed = negInertiaAccumulator;
        
        if (negInertiaAccumulator > 1) 
            negInertiaAccumulator -= 1;
         else if (negInertiaAccumulator < -1) 
            negInertiaAccumulator += 1;
         else 
            negInertiaAccumulator = 0;
        
        return newTurnSpeed;
		
	}
	
	public void reset()
	{
		negInertiaAccumulator = 0;
		prevTurnSpeed = 0;
	}
}
