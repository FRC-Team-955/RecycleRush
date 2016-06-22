package lib;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Talon;

/**
 * Basic motor controller for regular talon
 * @author Trevor
 *
 */
public class CIM extends CANTalon {
	boolean isFlipped;
	
	/**
	 * 
	 * @param channel
	 * @param isFlipped
	 */
	public CIM (int channel, boolean isFlipped) {
		super(channel);
		this.isFlipped = isFlipped;
	}
	
	/**
	 * Sets the CIM to a specified speed, using isFlipped boolean
	 * @param speed
	 */
	public void set(double speed){
		if(isFlipped)
			super.set(-speed);
		
		else
			super.set(speed);
			
	}
	
	/**
	 * Returns the value the talon was set to
	 */
	public double get(){	
		if(isFlipped){
			return (-super.get());
		}
		
		return super.get();
	}
	
	public void ramp(double wantSpeed, double rampRate){
		if(Math.abs(wantSpeed - get()) > rampRate){
			
			if(wantSpeed > get())
				set(get() +  rampRate);
			
			else
				set(get() - rampRate);
			
		}
		
		else {
			set(wantSpeed);
		}
	}
}