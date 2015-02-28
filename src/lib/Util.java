package lib;

public class Util 
{	
	/**
	 * Limits the sudden increase or decrease of the motor
	 * @param currSpeed The speed the motor is currently at
	 * @param wantSpeed The speed wanted
	 * @param rampRate the max difference between these speeds
	 * @return Adjusted speed value
	 */
	public static double ramp(double currSpeed, double wantSpeed, double rampRate)
	{
		if(Math.abs(currSpeed - wantSpeed) > rampRate) 
        {
            if(wantSpeed > currSpeed)
            	return (currSpeed + rampRate);
            
        	return (currSpeed - rampRate);
        }
		
		return wantSpeed;
	}
	
	/**
	 * Converts angles greater than 180 to their opposite coterminal angle
	 * @param ang the angle to be converted
	 * @return adjusted angle
	 */
	public static double absoluteAngToRelative(double ang)
	{
		if(ang > 180)
			return -(360 - ang);
		
		return ang;
	}
	
	/**
	 * Rounds the double to the nearest hundreths place
	 * @param num
	 * @return
	 */
	public static double round(double num)
	{
		return Math.floor((num * 100) + 0.5) / 100;
	}
}