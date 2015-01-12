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
		if(rightJoyXPos < 0.1 || rightJoyXPos > -0.1) {
			double joyX = joy.getLeftStickX();
			double joyY = joy.getLeftStickY();
			
			double r = (Math.sqrt(joyX * joyX) + (joyY * joyY));
			double a = Math.atan2(joyY, joyX);
			double b = (getGyro() + (360 - a));
			double finalTriAngle = b - getQuadrental(b);
			double frontBackMotors = (r * Math.sin(finalTriAngle));
			double leftRightMotors = (r * Config.Drive.speedMultiplier * Math.cos(finalTriAngle));
			
			setFrontBackSpeed(frontBackMotors);
			setLeftRightSpeed(leftRightMotors);
		}
		//Turning with right joy
		
		
		if(rightJoyXPos > 0.1 || rightJoyXPos < -0.1) {
			rightTalonOne.set(rightJoyXPos);
			rightTalonTwo.set(rightJoyXPos);
			
			leftTalonOne.set(rightJoyXPos);
			leftTalonTwo.set(rightJoyXPos);
			
			frontTalon.set(rightJoyXPos);
			backTalon.set(rightJoyXPos);
		}
	}
	public void setFrontBackSpeed(double speed) {
		frontTalon.set(speed);
		backTalon.set(speed);
	}
	
	public void setLeftRightSpeed(double speed) {
		rightTalonOne.set(speed);
		rightTalonTwo.set(speed);
		
		leftTalonOne.set(speed);
		leftTalonTwo.set(speed);
	}
	
	public void setRightSpeed(double speed) {
		rightTalonOne.set(speed);
		rightTalonTwo.set(speed);
	}
	
	public void setLeftSpeed(double speed) {
		leftTalonOne.set(speed);
		leftTalonTwo.set(speed);
	}
	
	public void setFrontSpeed(double speed) {
		frontTalon.set(speed);
	}
	
	public void setBackSpeed(double speed) {
		backTalon.set(speed);
	}
	
	public void setLinearSpeed(double speed) {
		rightTalonOne.set(speed);
		rightTalonTwo.set(speed);
		
		leftTalonOne.set(speed);
		leftTalonTwo.set(speed);
		
		frontTalon.set(0);
		backTalon.set(0);
	}
	
	public void stop() {
		rightTalonOne.set(0);
		rightTalonTwo.set(0);
		
		leftTalonOne.set(0);
		leftTalonTwo.set(0);
		
		frontTalon.set(0);		
		backTalon.set(0);	
	}
	
	public void linearTurn(double speed) {
		rightTalonOne.set(speed);
		rightTalonTwo.set(speed);
		
		leftTalonOne.set(-speed);
		leftTalonTwo.set(-speed);
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
