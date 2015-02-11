package auto;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class SimpleNavigationSystem {

	
	int previousTime;
	double totalAccelerationX;
	double totalAccelerationY;
	
	double positionX;
	double positionY;
	
	//TODO Switch to NavX accelerometer
	BuiltInAccelerometer accel = new BuiltInAccelerometer();
	
	
	public SimpleNavigationSystem(){
		
		positionX = 0;
		positionY= 0;
		
		totalAccelerationX = 0;
		totalAccelerationY = 0;
		
		previousTime = (int) System.currentTimeMillis();
		
		
		
	}
	
	public double getXPosition(){
		
		int currentTime = ((int) System.currentTimeMillis()) - previousTime;
		previousTime = (int) (System.currentTimeMillis());
		
		
		double postion = ((.5 * accel.getX()) * ((currentTime * currentTime)/1000)) + totalAccelerationX + positionX;
		
		totalAccelerationX += accel.getX();		
		positionX = postion;
		
		return positionX;
		
	}
	
	
	public double getYPosition(){
		int currentTime = ((int) System.currentTimeMillis()) - previousTime;
		previousTime = (int) (System.currentTimeMillis());
		
		
		double postion = ((.5 * accel.getY()) * (currentTime * currentTime)) + totalAccelerationY + positionY;
		
		totalAccelerationY += accel.getY();		
		positionY = postion;
		
		return positionY;
		
	}
	
}

