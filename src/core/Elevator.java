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
		double visionHeight = (socket.getDistance());
		//System.out.println(socket.getDistance());
		
		if(visionHeight == 1000)
		{
			brake();
		} else {
			unBrake();
		}
		
		// Set the want position based on button that was pressed
		//if(contr.getButton(Config.ContrElevator.btLvlOne)) {
			setHeight(visionHeight);
		//}
		if(visionHeight < Config.Sockets.maxHeight) {
			pidElevator.update(visionHeight, Config.Sockets.wantHeight);			
		}else {
			pidElevator.update(Config.Sockets.wantHeight, Config.Sockets.wantHeight);
		}
		
		double speed = pidElevator.getOutput();
		System.out.println("vision: " + socket.getDistance() + " | Speed: " + speed);
		speed = Util.limit(speed, Config.Elevator.minElevatorSpeed, Config.Elevator.maxElevatorSpeed);
		setSpeed(speed);
			
		//update();
		
	}
	
	/**
	 * Update the elevator position for pid mode
	 */
	public void update()
	{
		double speed = 0;
		
		if(changeElevatorHeight)
		{
			// Unbrake if braked, start timer for disengaging
			if(getBrake())
			{
				SmartDashboard.putBoolean("Brake Disengaged", true);
				unBrake();
				tmBrake.reset();
				tmBrake.start();
			}
			
			// Reset/Start the pid once the brake has completely disengaged, or if
			// a new height was chosen when the pid is already running, or if
			// the brake was not braked to begin with
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime || pidElevator.isRunning() || (!getBrake() && !pidElevator.isRunning()))//tmBrake.get() == 0)
			{
				tmBrake.stop();
				tmBrake.reset();
				
				if(pidElevator.getNeedReset())
					pidElevator.reset();
				
				pidElevator.start();
				changeElevatorHeight = false;
			}
		}
		
		// Update the pid if the pid is running
		if(pidElevator.isRunning())
		{			
			// TODO: Consider adding deadband value for getRate()
			// If the bottom limit switch gets hit and the elevator is moving down
			// or if the top limit switch gets hit and the elevator is moving up
			// set the want position to current height as that means we hit the 
			// mechanical limits and we should stop trying to go any further
			// TODO get boundaries working with getRate() 
			if(/*(getLimitSwitchBot() && wantPos < getHeight()) ||*/ getHeight() > Config.Elevator.maxElevatorHeightLimit)/*getLimitSwitchTop() && wantPos > getHeight())*/
			{
				//setHeight(getHeight());
				wantPos = Config.Elevator.maxElevatorHeight;
				System.out.println("In limit");
				//wantPos = getHeight();
				//pidElevator.reset();
			}
			
			// Update the pid with curr/want position
			pidElevator.update(getHeight(), wantPos);
			speed = pidElevator.getOutput();
			
			// TODO: These are for debugging purposes
			if(pidElevator.getErrSum() > maxErrI)
				maxErrI = pidElevator.getErrSum();
			
			if(Math.abs(pidElevator.getErrD()) > Math.abs(maxErrD))
				maxErrD = pidElevator.getErrD();
			
			// If the height diff and rate is small we've reached our destination, thus
			// activate the brake and turn off the pid AFTER the brake has made physical contact
			if((Math.abs(wantPos - getHeight()) < Config.Elevator.maxHeightDiff && Math.abs(getRate()) < Config.Elevator.minEncRate))
			{
				// If not braked brake the elevator, but keep pid running
				if(!getBrake())
				{
					brake();
					// Run brake timer, stop encoder rate timer, keep pid running when breaking, may be dangerous
					tmBrake.reset();
					tmBrake.start();
					tmEncRate.stop();
					tmEncRate.reset();
				}
			}
						
			// Once the brake has made complete contact, then stop the pid and set the speed to 0
			if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
			{
				tmBrake.stop();
				tmBrake.reset();
				pidElevator.stop();
				speed = 0;
				upMode = false;
				downMode = false;
			}
		}
		
		speed = Util.limit(speed, Config.Elevator.minElevatorSpeed, Config.Elevator.maxElevatorSpeed);
		setSpeed(speed);
	}
	
	/**
	 * Sets the wanted height for the elevator, inches
	 * @param newHeight
	 */
	public void setHeight(double newHeight)
	{
		// If the new height is already where we are and we're braked don't move the elevator again
		if(Math.abs(newHeight - Config.Sockets.wantHeight) < Config.Elevator.maxHeightDiff && getBrake())
			return;
		
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
	 * Sets the want elevator height based on the tote level wanted, and based on
	 * drop off mode and height mode
	 * Levels 1 to 7 inclusive only
	 * @param level
	 */
	public void setToteLevel(int level)
	{
		// Limit the tote level to be within boundaries
		level = (int)Util.limit(level, Config.Elevator.minToteLevel, Config.Elevator.maxToteLevel);
		double newHeight = (level - 1) * Config.Elevator.toteHeight;
		
		// If in drop off mode increase height to give it clearance for totes
		if(dropOffMode)
			newHeight += (level - 1) > 0 ? Config.Elevator.clearanceHeight : 0;
			
		if(heightType == Config.Elevator.heightTypeStep && (level - 1) == 0)
			newHeight += Config.Elevator.clearanceHeight;
		
		// Subtract height for the fact that totes stack in eachother, thus losing height
		newHeight -= (level - 1) * Config.Elevator.toteLossHeight;
		
		// Add base height based on height type since there are different heights for on the field
		if(heightType == Config.Elevator.heightTypeGround)
			newHeight += Config.Elevator.baseHeightGround;
		
		else if(heightType == Config.Elevator.heightTypeScoring)
			newHeight += Config.Elevator.baseHeightScoring;
		
		else if(heightType == Config.Elevator.heightTypeStep)
			newHeight += Config.Elevator.baseHeightStep;
		
		
		
		setHeight(Util.limit(newHeight, 0, Config.Elevator.maxElevatorHeight));
	}
	
	/**
	 * Sets the elevator for drop off totes or picking up totes
	 * true = drop off mode
	 * false = pick up mode
	 * @param wantDropOff
	 */
	public void setDropOffMode(boolean wantDropOff)
	{
		dropOffMode = wantDropOff;
	}
	
	/**
	 * Returns the height of the elevator, positive the higher the elevator is, inches
	 * @return
	 */
	public double getHeight()
	{
		// Flip the direction because negative is actually up on the elevator
		return -enc.getDistance();
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