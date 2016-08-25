package lib;

import edu.wpi.first.wpilibj.Talon;

/**
 * Wraps set of two motors that make up one side of the robot
 * @author Trevor
 *
 */
public class TwoCimGroup {

	public CIM c1;
	public Talon c2;
	boolean m1IsReverse;
	boolean m2IsReverse;
	
	/**
	 * Creates grouping given 2 motors
	 * @param m1Chn
	 * @param m2Chn
	 * @param m1IsFlipped
	 * @param m2IsFlipped
	 */
	public TwoCimGroup(int m1Chn, int m2Chn, boolean m1IsFlipped){
		c1 = new CIM(m1Chn, m1IsFlipped);
		c2 = new Talon(m2Chn);
	}
	
	/**
	 * Sets all motors to given speed with ramping
	 * @param velocity between -1 to 1
	 */
	public void set(double velocity) {
		c1.set(velocity);
		c2.set(velocity);
		
	}
}