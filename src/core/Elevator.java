package core;

import lib.Config;
import lib.Controller;
import lib.LimitSwitch;
import lib.PID;
import lib.Util;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class Elevator 
{
	// CAN Talons for controlling the elevator
	private CANTalon mtElevatorOne = new CANTalon(Config.Elevator.idMtElevatorOne);
	private CANTalon mtElevatorTwo = new CANTalon(Config.Elevator.idMtElevatorTwo);
	
	// Solenoid for brake
	private DoubleSolenoid noidBrake = new DoubleSolenoid(Config.Elevator.chnNoidOne, Config.Elevator.chnNoidTwo);
	
	// Limit switches on top/bottom to prevent out of bounds
	private LimitSwitch limitTop = new LimitSwitch(Config.Elevator.chnLimitSwitchTop, false);
	private LimitSwitch limitBot = new LimitSwitch(Config.Elevator.chnLimitSwitchBottom, false);
	
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
	private boolean pidMode = true;
	private boolean changeElevatorHeight = false;
	private double wantPos = 0;
	
	// Variables to help tune the PID by holding data about the PID
	private double maxErrI = 0;
	private double maxErrD = 0;
	
	// Different modes for the elevator
	private boolean dropOffMode = true;
	private int heightType = Config.Elevator.heightTypeGround;
	
	private int [] levels = 
	{
		Config.ContrElevator.btLvlOne,
		Config.ContrElevator.btLvlTwo,
		Config.ContrElevator.btLvlThree,
		Config.ContrElevator.btLvlFour,
		Config.ContrElevator.btLvlFive
		//Config.ContrElevator.btLvlSix
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
		
		// Enable min/max limits for pid, set min/max limits for pid 
		pidElevator.setErrLimitMode(true);
		pidElevator.setErrLimits(Config.Elevator.minErrorSum, Config.Elevator.maxErrorSum);
	}

	/**
	 * Runs the elevator motor depending on button inputs
	 */
	public void run()
	{		
		System.out.println("Encoder " + enc.getDistance());
		System.out.println("Limit Top" + limitTop.get());
		System.out.println("Limit Bot" + limitBot.get());
		
		if(contr.getRawButton(Config.ContrElevator.btElevatorUp) && !limitTop.get())
		{
			// Auto disengages brakes if the brakes are engaged
			if(getBrake())
			{
				tmBrake.reset();
				tmBrake.start();
				unBrake();
			}
			
			// Moves the elevator motor after the brake disengages 
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
			{
				setSpeed(.25);
				//System.out.println("GOING UP");
			}
		}
		
		else if(contr.getRawButton(Config.ContrElevator.btElevatorDown) && !limitBot.get())
		{
			// Auto disengages brakes if the brakes are engaged
			if(getBrake())
			{
				tmBrake.reset();
				tmBrake.start();
				unBrake();
			}
			
			// Moves the elevator motor after the brake disengages
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
			{
				setSpeed(Config.Elevator.elevatorDownSpeed);
				//System.out.println("GOING DOWN");
			}
		}
		
		else
		{		
			setSpeed(0);
			
			// Auto brakes after the motor stops
			if(!getBrake())
			{
				tmBrake.stop();
//				brake();
			}
		
			//System.out.println("0 SPEED");
		}
	}
	
	/**
	 * Tests the PID for the elevator
	 */
	public void runPID()
	{	
		if(contr.getButton(Config.ContrElevator.btDropOff))
			setDropOffMode(!dropOffMode);
			
		else if(contr.getButton(Config.ContrElevator.btToggleBrake))
		{
			if(getBrake())
				unBrake();
			
			else
			{
				pidElevator.stop();
				pidElevator.reset();
				brake();
			}
		}
		
		// Set the want position based on button that was pressed
		for(int i = 0; i < levels.length; i++)
			if(contr.getButton(levels[i]))
				setToteLevel(i + 1);
		
		update();
	}
	
	/**
	 * Sets the wanted height for the elevator, inches
	 * @param newHeight
	 */
	public void setHeight(double newHeight)
	{
		// If the new height is already where we are dont move the elevator
		if(Math.abs(newHeight - getHeight()) < Config.Elevator.maxHeightDiff)
			return;
		
		// If the new height is > current height, use the pid constants for up
		if(newHeight > getHeight())
			pidElevator.setConsts(Config.Elevator.kUpP, Config.Elevator.kUpI, Config.Elevator.kUpD);
			
		// If the new height is < current height, use the pid constants for down
		if(newHeight < getHeight())
			pidElevator.setConsts(Config.Elevator.kDownP, Config.Elevator.kDownI, Config.Elevator.kDownD);
			
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
		if(level < Config.Elevator.minToteLevel)
			level = Config.Elevator.minToteLevel;
		
		else if(level > Config.Elevator.maxToteLevel)
			level = Config.Elevator.maxToteLevel;
		
		double newHeight = (level - 1) * Config.Elevator.toteHeight;
		
		// If in drop off mode increase height to give it clearance for totes
		if(dropOffMode)
			newHeight += (level - 1) > 0 ? Config.Elevator.toteClearanceHeight : 0;
		
		// Subtract height for the fact that totes stack in eachother, thus losing height
		newHeight -= (level - 1) * Config.Elevator.toteLossHeight;
		
		// Add base height based on height mode since there are different heights for on the field
		if(heightType == Config.Elevator.heightTypeGround)
			newHeight += Config.Elevator.baseHeightGround;
		
		else if(heightType == Config.Elevator.heightTypeScoring)
			newHeight += Config.Elevator.baseHeightScoring;
		
		else if(heightType == Config.Elevator.heightTypeStep)
			newHeight += Config.Elevator.baseHeightStep;
		
		setHeight(newHeight);
	}
	
	/**
	 * Sets the elevator for drop off totes or picking up totes
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
				unBrake();
				tmBrake.reset();
				tmBrake.start();
			}
			
			// Reset/Start the pid once the brake has completely disengaged, or if
			// a new height was chosen when the pid is already running, or if
			// the brake was not braked to begin with
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime || pidElevator.isRunning() || tmBrake.get() == 0)
			{
				tmBrake.stop();
				tmBrake.reset();
				pidElevator.reset();
				pidElevator.start();
				
				changeElevatorHeight = false;
			}
		}
		
		// Update the pid if the pid is running
		if(pidElevator.isRunning())
		{
//			if(limitBot.get())
//			{
//				wantPos = getHeight();
//				
//				// TODO: Noodle proof this, a noodle could potentially
//				// hit the limit switch thus reset it while in reality the
//				// actual height was not even close to the bottom
//				enc.reset();
//			}
			
			// TODO: Consider adding deadband value for getRate()
			// If the bottom limit switch gets hit and the elevator is moving down
			// or if the top limit switch gets hit and the elevator is moving up
			// set the want position to current height as that means we hit the 
			// mechanical limits and we should stop trying to go any further
			if((limitBot.get() && getRate() < 0) || (limitTop.get() && getRate() > 0))
			{
				wantPos = getHeight();
				pidElevator.reset();
			}
			
			// Update the pid with curr/want position
			pidElevator.update(getHeight(), wantPos);
			speed = pidElevator.getOutput();
			
			// TODO: These are for debugging purposes
			if(pidElevator.getErrSum() > maxErrI)
				maxErrI = pidElevator.getErrSum();
			
			if(Math.abs(pidElevator.getErrD()) > Math.abs(maxErrD))
				maxErrD = pidElevator.getErrD();
			
			// TODO: Get getting current from the pdp working so that finding out whether a motor is stalling
			// would be easier and more effective that way
			// If the encoder rate is continuously less than than a certain value for a certain time, engage the break
			// we don't want to stall out the motors too long even if we haven't reached are wanted height
//			if(Math.abs(getRate()) < Config.Elevator.minEncRate)
//				tmEncRate.start();
//			
//			else
//			{
//				tmEncRate.stop();
//				tmEncRate.reset();
//			}
			
			// If the height diff and rate is small we've reached our destination, thus
			// activate the brake and turn off the pid AFTER the brake has made physical contact
			if((Math.abs(wantPos - getHeight()) < Config.Elevator.maxHeightDiff && Math.abs(getRate()) < Config.Elevator.minEncRate) || tmEncRate.get() >= Config.Elevator.maxEncStallTime)
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
			}
		}
		
		System.out.println
		(
				"Speed " + Util.round(speed) + 
				" : Encoder " + Util.round(getHeight()) + 
				" : Encoder Clicks " + -enc.get() +
				" : Encode Rate " + getRate() + 
				" : Want Height " + wantPos +
    			" : Max Error " + maxErrI +
    			" : Max Error diff " + maxErrD
		);
		
		setSpeed(speed);
	}
	
	/**
	 * Sets both elevator motors to the wanted speed, voltage, positive to move elevator up
	 * @param speed the speed wanted
	 */
	public void setSpeed(double speed)
	{
		// Flip the speed direction because negative is actually up on the elevator
		
		// Only ramp the elevator when the direction of the 
		// acceleration matches the direction of the velocity, 
		// direction not negativity
		if(speed - getSpeed() > 0)
			speed = -Util.ramp(getSpeed(), speed, Config.Elevator.maxRampRate);
		
		else
			speed = -speed;
		
		mtElevatorOne.set(speed);
		mtElevatorTwo.set(speed);
	}
	
	/**
	 * Gets the speed of the elevator, voltage, postive for up
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
	 * Resets the PID
	 */
	public void resetPID()
	{
		pidElevator.stop();
		pidElevator.reset();
	}
}