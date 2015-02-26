package lib;

public class Util 
{	
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
}