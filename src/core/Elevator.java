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
	// CAN Talons
	private CANTalon mtElevatorOne = new CANTalon(Config.Elevator.idMtElevatorOne);
	private CANTalon mtElevatorTwo = new CANTalon(Config.Elevator.idMtElevatorTwo);
	
	// Solenoid for brake
	private DoubleSolenoid noidBrake = new DoubleSolenoid(Config.Elevator.chnNoidOne, Config.Elevator.chnNoidTwo);
	
	// Limit switches
	private LimitSwitch limitTop = new LimitSwitch(Config.Elevator.chnLimitSwitchTop, false);
	private LimitSwitch limitBot = new LimitSwitch(Config.Elevator.chnLimitSwitchBottom, false);
	
	// Encoder
	private Encoder enc = new Encoder(Config.Elevator.chnEncA, Config.Elevator.chnEncB);
	
	// PIDs
	private PID pidElevator = new PID(Config.Elevator.kUpP, Config.Elevator.kUpI, Config.Elevator.kUpD);
	private PID pidDown = new PID(Config.Elevator.kDownP, Config.Elevator.kDownI, Config.Elevator.kDownD);
	
	// Timers for elevator
	private Timer tmBrake = new Timer();
	private Timer tmEncRate = new Timer();
	
	// Controller for controlling the elevators
	private Controller contr;
	
	// PID
	private boolean pidMode = true;
	private double wantPos = 0;
	private double maxErrD = 0;
	
	private boolean changeElevatorHeight = false;
	private boolean dropOffAdjust = false;
//	private double baseHeight = 0;
//	private int level = 0;
//	private boolean mode = false;
//	
	private int [] levels = 
	{
		Config.ContrElevator.btLvlOne,
		Config.ContrElevator.btLvlTwo,
		Config.ContrElevator.btLvlThree,
		Config.ContrElevator.btLvlFour,
		Config.ContrElevator.btLvlFive
		//Config.ContrElevator.btLvlSix
	};
	
	double maxError = 0;
	
	/**
	 * Constructor
	 * @param newContr controller
	 */
	public Elevator(Controller newContr)
	{
		contr = newContr;
		enc.setDistancePerPulse(Config.Elevator.distancePerPulse);
		enc.reset();
		
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
	public void testPID()
	{	
		// Set to pid mode if button pressed
		if(contr.getButton(Config.ContrElevator.btEnableElevatorPID))
			pidMode = true;
		
		// Set to non pid mode if button pressed
		else if(contr.getButton(Config.ContrElevator.btDisableElevatorPID))
			pidMode = false;
		
		else if(contr.getButton(Config.ContrElevator.btToggleBrake))
		{
			if(getBrake())
				unBrake();
			
			else
				brake();
		}
		
		if(contr.getButton(Config.ContrElevator.btDropOff))
			dropOffAdjust = true;
		
		// Run pid if in pid mode
		if(pidMode)
		{	
			// Set the want position based on button that was pressed
			for(int i = 0; i < levels.length; i++)
			{
				if(contr.getButton(levels[i]))
				{
					setHeight(i * Config.Elevator.toteHeight);
//					double wantHeight = i * Config.Elevator.toteHeight;
//					// One inch is lost per 
//					double heightAdjust =  Config.Elevator.toteClearanceHeight - (i-1);
//					
//					setHeight((wantHeight) + (i > 0 ? heightAdjust : 0));
				}
			}
			
			update();
		}
		
		else
			setSpeed(0);
	}
	
	/**
	 * Sets the wanted height for the elevator, inches
	 * @param newHeight
	 */
	public void setHeight(double newHeight)
	{
		// TODO: Remove the only higher limitation once down pid has been tuned
		// Only allow to get higher since PID for going down has not been tuned yet
		// Set constants for upPID if you are moving up
		// TODO Add buffer for when the height does not exactly match the wanted height 
		if(newHeight > getHeight())
		{
			pidElevator.setConsts(Config.Elevator.kUpP, Config.Elevator.kUpI, Config.Elevator.kUpD);
			wantPos = newHeight + (newHeight > 0 ? Config.Elevator.toteClearanceHeight : 0);
		}
		
		// Set constants for downPID if you are moving down
		if(newHeight < getHeight())
		{
			pidElevator.setConsts(Config.Elevator.kDownP, Config.Elevator.kDownI, Config.Elevator.kDownD);
			wantPos = newHeight  - (dropOffAdjust ? Config.Elevator.dropOffHeightAdjust : 0);
			dropOffAdjust = false;
;
		}
		
		changeElevatorHeight = true;
	}
	
	/**
	 * Returns the height of the elevator, positive the higher the elevator is, inches
	 * @return
	 */
	public double getHeight()
	{
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
			// a new height was chosen when the pid is already running
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime || pidElevator.isRunning())
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
//			
			if(limitTop.get())
			{
				wantPos = getHeight();
				pidElevator.reset();
			}
			
			// Update the pid with curr/want position, with ramping
			pidElevator.update(getHeight(), wantPos);
			speed = Util.ramp(getSpeed(), pidElevator.getOutput(), Config.Elevator.maxRampRate);
			
			if(pidElevator.getErrSum() > maxError)
				maxError = pidElevator.getErrSum();
			
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
    			" : Max Error " + maxError +
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
		mtElevatorOne.set(-speed);
		mtElevatorTwo.set(-speed);
	}
	
	/**
	 * Gets the speed of the elevator, voltage, postive for up
	 * @return
	 */
	public double getSpeed()
	{
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
		pidDown.stop();
		pidDown.reset();
	}
}