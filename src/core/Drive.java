package core;

import edu.wpi.first.wpilibj.Talon;

public class Drive {
	Talon rightTalonOne;
	Talon rightTalonTwo;
	
	Talon leftTalonOne;
	Talon leftTalonTwo;
	
	Talon frontTalon;
	
	Talon backTalon;
	
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
}
