package util;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
	private DigitalInput limit;
	private boolean flipped;
	
	/**
	 * Constructor
	 * @param chn the channel for the limit switch
	 */
	public LimitSwitch(int chn)
	{
		limit = new DigitalInput(chn);
		flipped= false;
	}
	
	/**
	 * Gets the value of the Limit Switch
	 * @return Limit Switch value
	 */
	public boolean get()
	{
		if(flipped)
			return !limit.get();
		else
			return limit.get();	
	}
}
