package lib;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch 
{
	private DigitalInput limit;
	private boolean flipped;
	
	/**
	 * Constructor
	 * @param chn the channel for the limit switch
	 */
	public LimitSwitch(int chn, boolean isFlipped)
	{
		limit = new DigitalInput(chn);
		flipped = isFlipped;
	}
	
	/**
	 * Gets the value of the Limit Switch
	 * @return Limit Switch value
	 */
	public boolean get()
	{
		return flipped ? !limit.get(): limit.get();
	}
}
