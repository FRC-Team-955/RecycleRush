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
		if(rightJoyXPos < 0.1 && rightJoyXPos > -0.1) {
			double joyX = joy.getLeftStickX();
			double joyY = joy.getLeftStickY();
			
			double r = Math.sqrt((joyX * joyX) + (joyY * joyY));
			double a = Math.toDegrees(Math.atan2(joyY, joyX));
			double b = (getGyro() + (360 - a));
			double beta = b % 360;	
			double finalTriAngle = beta - getQuadrental(beta);
			double centerSpeed = (r * Math.sin(finalTriAngle));
			double sideSpeed = (r * Config.Drive.speedMultiplier * Math.cos(finalTriAngle));
			
			setSpeed(sideSpeed, sideSpeed, centerSpeed, centerSpeed);
		}
		//Turning with right joy
		
		// TODO: PUT NEGATIVES
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
		for(int i = 0; finalAngle > 360; i++) {
			finalAngle -= 360;
		}
		return finalAngle;
	}
	
	public int getQuadrental(double angle) {
		if(angle <= 90 && angle > 0) {
			return 0;
		}
		if(angle <= 180 && angle > 90) {
			return 90;
		}
		if(angle <= 270 && angle > 180) {
			return 180;
		}
		if(angle <= 360 && angle > 270) {
			return 270;
		}
		//Stuff got messed up and doesn't work and we need to change stuff
		System.out.println("Stuff got messed up and doesn't work and we need to change stuff");
		return 0;
	}
	
}
