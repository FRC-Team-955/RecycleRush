package core;

import edu.wpi.first.wpilibj.Talon;
import util.Config;
import util.MyJoystick;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Gyro;
import java.lang.Math;

public class Drive {
	Talon rightTalonOne;
	Talon rightTalonTwo;
	
	Talon leftTalonOne;
	Talon leftTalonTwo;
	
	Talon frontTalon;
	
	Talon backTalon;
	
	MyJoystick joy;
	
	Gyro gyro;
	
	public Drive (MyJoystick newJoy, int port) {

        joy = newJoy;
        gyro = new Gyro(port);
        gyro.reset();
    }
	
	public void fieldCentric() {
		double rightJoyXPos = joy.getRightStickX();
		
		//Moving with left joy
		if(rightJoyXPos < Config.Drive.tolerance && rightJoyXPos > -Config.Drive.tolerance) {
			double joyX = joy.getLeftStickX();
			double joyY = joy.getLeftStickY();
			
			double joyMag = Math.sqrt((joyX * joyX) + (joyY * joyY));
			double joyAng = (450 - Math.toDegrees(Math.atan2(1, 0))) % 360;
			double angDiff = getGyro() - joyAng;
			double centerSpeed = joyMag * Math.sin(Math.toRadians(angDiff));
			double sideSpeed = joyMag * Math.cos(Math.toRadians(angDiff));
			
			setSpeed(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
		}
		
		//Turning with right joy
		else{
			rightTalonOne.set(rightJoyXPos);
			rightTalonTwo.set(rightJoyXPos);
			
			leftTalonOne.set(-rightJoyXPos);
			leftTalonTwo.set(-rightJoyXPos);
			
			frontTalon.set(rightJoyXPos);
			backTalon.set(-rightJoyXPos);
		}
		
		
	} 

	public void setSpeed(double leftSpeed, double rightSpeed, double frontSpeed, double backSpeed) {
		rightTalonOne.set(rightSpeed);
		rightTalonTwo.set(rightSpeed);
		
		leftTalonOne.set(leftSpeed);
		leftTalonTwo.set(leftSpeed);
		
		frontTalon.set(frontSpeed);		
		backTalon.set(backSpeed);	
	}
	
	public double getGyro() {
		double finalAngle = (90 + (gyro.getAngle() - 360));
		finalAngle = finalAngle % 360; 
		return finalAngle;
	}
}
