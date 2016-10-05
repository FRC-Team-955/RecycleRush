package core;

import lib.Config;
import lib.Controller;
import lib.PID;
import lib.Util;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator 
{
	// Sockets
	public SocketCore socket = new SocketCore();
	
	// CAN Talons for controlling the elevator
	private CANTalon mtElevatorOne = new CANTalon(Config.Elevator.idMtElevatorOne);
	private CANTalon mtElevatorTwo = new CANTalon(Config.Elevator.idMtElevatorTwo);
	
	// Solenoid for brake
	private DoubleSolenoid noidBrake = new DoubleSolenoid(Config.Elevator.chnNoidOne, Config.Elevator.chnNoidTwo);
	
	// Encoder on the elevator for height
	private Encoder enc = new Encoder(Config.Elevator.chnEncA, Config.Elevator.chnEncB);
	
	// PID for elevator to gracefully reach wanted height
	private PID pidElevator = new PID(Config.Elevator.kUpP, Config.Elevator.kUpI, Config.Elevator.kUpD);

	// Timers for elevator brake and elevator encoder
	private Timer tmBrake = new Timer();
	private Timer tmEncRate = new Timer();
	
	// Controller for controlling the elevators
	private Controller contr;
	
	// PID variables
	private boolean upMode = false;
	private boolean downMode = false;
	private boolean changeElevatorHeight = false;
	private double wantPos = 0;
	private double prevHeight = 0;
	
	// Variables to help tune the PID by holding data about the PID
	private double maxErrI = 0;
	private double maxErrD = 0;
	
	// Different modes for the elevator
	private boolean dropOffMode = false;
	private int heightType = Config.Elevator.heightTypeGround;
	
	private int [] levels = 
	{
		Config.ContrElevator.btLvlOne,
		Config.ContrElevator.btLvlTwo,
		Config.ContrElevator.btLvlThree,
		Config.ContrElevator.btLvlFour,
		Config.ContrElevator.btLvlFive,
	};
	
	/**
	 * Constructor
	 * @param newContr controller
	 */
	public Elevator(Controller newContr)
	{
		contr = newContr;
		enc.setDistancePerPulse(Config.Elevator.distancePerPulse);
		enc.reset();
		setHeight(Config.Sockets.wantHeight);
		
		// Start socket thread
		new Thread(socket).start();		
		
		// Enable min/max limits for pid, set min/max limits for pid 
		pidElevator.setErrLimitMode(true);
		pidElevator.setErrLimits(Config.Elevator.minErrorSum, Config.Elevator.maxErrorSum);
	}
	
	/**
	 * Runs the PID for the elevator
	 */
	public void runPID()
	{		
		//double visionHeight = (500);
		double visionHeight = (socket.getDistance());
		double distChange = Config.Sockets.wantHeight - visionHeight;
		if(visionHeight != prevHeight) {
			setHeight(getHeight() + distChange);
			System.out.println("updating");
		}
		
		if(visionHeight < Config.Sockets.maxHeight) {
			pidElevator.update(getHeight(), getWantHeight());
		} else {
			pidElevator.update(Config.Sockets.wantHeight, Config.Sockets.wantHeight);
		}
		
		double speed = pidElevator.getOutput();
		System.out.println("want: " + getWantHeight());
		System.out.println("diff: " + distChange);
		System.out.println("height: " + getHeight());
		speed = Util.limit(speed, Config.Elevator.minElevatorSpeed, Config.Elevator.maxElevatorSpeed);
		setSpeed(speed); 
		
		prevHeight = visionHeight;
	}
	
	/**
	 * Sets the wanted height for the elevator, inches
	 * @param newHeight
	 */
	public void setHeight(double newHeight)
	{
		if(newHeight < 0) {
			newHeight = 0;
		} else if (newHeight > Config.Elevator.maxHeight) {
			newHeight = Config.Elevator.maxHeight;
		}
		
		// If the new height is > current height, use the pid constants for up
		if(newHeight >= Config.Sockets.wantHeight)
		{
			pidElevator.setConsts(Config.Elevator.kUpP, Config.Elevator.kUpI, Config.Elevator.kUpD);
			pidElevator.setNeedReset(!upMode);  // Only need to reset when changing directions
			upMode = true;
			downMode = false;
		}
		
		// If the new height is < current height, use the pid constants for down
		if(newHeight < Config.Sockets.wantHeight)
		{
			pidElevator.setConsts(Config.Elevator.kDownP, Config.Elevator.kDownI, Config.Elevator.kDownD);
			pidElevator.setNeedReset(!downMode);  // Only need to reset when changing directions
			downMode = true;
			upMode = false;
		}
		
		// Update want pos and set change elevator boolean status to true
		wantPos = newHeight;
		changeElevatorHeight = true;
	}
	
	/**
	 * Returns the height of the elevator, positive the higher the elevator is, inches
	 * @return
	 */
	public double getHeight()
	{
		// Flip the direction because negative is actually up on the elevator
		double height = enc.getDistance();
		System.out.println("raw height: " + height);
		if(height > Config.Elevator.maxElevatorHeight) {
			return Config.Elevator.maxElevatorHeight;
		} else if(height <= Config.Elevator.minElevatorHeight) {
			return Config.Elevator.minElevatorHeight;
		}
		return height;
	}
	
	/**
	 * Returns the wanted the height of the elevator that the user wants, 
	 * positive is higher for the elevator, inches
	 * @return
	 */
	public double getWantHeight()
	{
		return wantPos;
	}
	
	/**
	 * Returns true if elevator is trying to get to a destination currently,
	 * returns false if elevator is not doing anything
	 * @return
	 */
	public boolean isRunning()
	{
		return pidElevator.isRunning();
	}
	
	/**
	 * Sets the height type which will change the elevator height offset
	 * 3 height types available currently
	 * scoring,
	 * step,
	 * ground
	 * @param newHeightType
	 */
	public void setHeightType(int newHeightType)
	{
		switch(newHeightType)
		{
			case Config.Elevator.heightTypeScoring:
			{
				heightType = newHeightType;
				break;
			}
				
			case Config.Elevator.heightTypeStep:
			{
				heightType = newHeightType;
				break;
			}
			
			default:
			{
				heightType = Config.Elevator.heightTypeGround;
				break;
			}
		}
	}
	
	/**
	 * Sets both elevator motors to the wanted speed, voltage, positive to move elevator up
	 * @param newSpeed the speed wanted
	 */
	public void setSpeed(double newSpeed)
	{		
		// Only ramp the elevator when the direction of the 
		// acceleration matches the direction of the velocity, 
		// direction, because that means we want the elevator
		// to go faster thus we need to ramp it
		//if(Math.signum(getRate()) == Math.signum(newSpeed - getSpeed()))
			//newSpeed = Util.ramp(getSpeed(), newSpeed, Config.Elevator.maxRampRate);
		
		// Flip the speed direction because negative is actually up on the elevator
		//System.out.println("Speed" + newSpeed);
		newSpeed = -newSpeed;
		mtElevatorOne.set(newSpeed);
		mtElevatorTwo.set(newSpeed);
	}
	
	/**
	 * Gets the speed of the elevator, voltage, positive for up
	 * @return
	 */
	public double getSpeed()
	{
		// Flip the direction because negative is actually up on the elevator
		return -mtElevatorOne.get();
	}
	
	/**
	 * Returns the elevator rate, inches/sec positive for up
	 * @return
	 */
	public double getRate()
	{
		// Flip the direction because negative is actually up on the elevator
		return -enc.getRate();
	}
	
	/**
	 * Returns the raw encoder ticks unscaled by distance per pulse, positive for up
	 * @return
	 */
	public double getEncTicks()
	{
		// Flip the direction because negative is actually up on the elevator
		return -enc.get();
	}
	
	/**
	 * Engages brake
	 */
	public void brake()
	{
		noidBrake.set(DoubleSolenoid.Value.kReverse);
	}
	
	/**
	 * Disengages brake
	 */
	public void unBrake()
	{
		noidBrake.set(DoubleSolenoid.Value.kForward);
	}
	
	/**
	 * Checks if the brake is engaged or not, true if engaged
	 * @return the brake position 
	 */
	public boolean getBrake()
	{
		return noidBrake.get() == DoubleSolenoid.Value.kReverse;
	}

	/**
	 * Resets the Elevator PID
	 */
	public void resetPID()
	{
		pidElevator.stop();
		pidElevator.reset();
	}
	
	public boolean getDropoffMode()
	{
		return dropOffMode;
	}
}