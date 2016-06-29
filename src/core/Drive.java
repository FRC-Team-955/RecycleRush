package core;

import lib.Config;
import lib.TwoCimGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;

import java.lang.Math;

/**
 * Core components that make the robot move
 * 
 * @author Trevor
 *
 */
public class Drive {
	
	private Encoder encLeft;
	private Encoder encRight;
	public TwoCimGroup leftCimGroup = new TwoCimGroup(Config.Drive.leftC1Chn, Config.Drive.leftC2Chn, Config.Drive.leftC1IsFlipped);
	public TwoCimGroup rightCimGroup = new TwoCimGroup(Config.Drive.rightC1Chn, Config.Drive.rightC2Chn, Config.Drive.rightC1IsFlipped);
	
	double xPos;
	double yPos;
	double x;
	double y;
	
	public Drive () {
		
	}
	
	/**
	 * Moves the robot in a specified direction at a specified velocity
	 * 
	 * @param r velocity between -1 and 1
	 * @param theta angle of joystick in radians
	 * 
	  */
	public void move(double r, double theta) {
		xPos = r*Math.cos(theta);
		yPos = r*Math.sin(theta);
		
		x = xPos * Math.abs(xPos);
		y = yPos * Math.abs(yPos);
		
		double left = y + x;
		double right = y - x;
        leftCimGroup.set(left);
        rightCimGroup.set(right);
	}
}