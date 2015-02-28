package core;

import lib.Config;
import lib.Controller;
import lib.LimitSwitch;
import lib.PID;
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
	private PID pidUp = new PID(Config.Elevator.kUpP, Config.Elevator.kUpI, Config.Elevator.kUpD);
	private PID pidDown = new PID(Config.Elevator.kDownP, Config.Elevator.kDownI, Config.Elevator.kDownD);
	
	private Timer tmBrake = new Timer();
	private Controller contr;
	
	// PID
	private boolean pidMode = true;
	private double wantPos = 0;
	
	private boolean elevatorUpPushed = false;
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
		Config.ContrElevator.btLvlFive,
		Config.ContrElevator.btLvlSix
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
	}

	/**
	 * Runs the elevator motor depending on joystick inputs
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
				setSpeed(Config.Elevator.elevatorUpSpeed);
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
				brake();
			}
		
			//System.out.println("0 SPEED");
		}
	}
	
	/**
	 * Tests the PID for the elevator
	 */
	public void testPID()
	{	
		// Default elevator speed to 0
		double speed = 0;
		
		// Set to pid mode if button pressed
		if(contr.getButton(Config.ContrElevator.btEnableElevatorPID))
			pidMode = true;
		
		// Set to non pid mode if button pressed
		else if(contr.getButton(Config.ContrElevator.btDisableElevatorPID))
			pidMode = false;
		
		// Run pid if in pid mode
		if(pidMode)
		{	
			// TODO: The minus 3 is temp because we dont want to test high yet since no limit switch implementation
			// Set the want position based on button that was pressed
			for(int i = 0; i < levels.length - 3; i++)
			{
				if(contr.getButton(levels[i]))
				{
					wantPos = i * Config.Elevator.toteHeight * -1;
					elevatorUpPushed = true;
				}
			}
			
			if(elevatorUpPushed)
			{
				// Unbrake if braked, start timer for disengaging
				if(getBrake())
				{
					unBrake();
					tmBrake.reset();
					tmBrake.start();
				}
				
				// Start the pid once the brake has completely disengaged
				else if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
				{
					tmBrake.stop();
					pidUp.reset();
					pidUp.start();
					elevatorUpPushed = false;
				}
			}
			
			// Update the pid if the pid is running
			if(pidUp.isRunning())
			{
				// Update the pid with curr/want position
				pidUp.update(enc.getDistance(), wantPos);
				speed = pidUp.getOutput();
				
				// If the rate is small and the elevator height diff is small as well we've reached our destination, thus
				// activate the brake and turn off the pid AFTER the brake has made physical contact
				if(Math.abs(wantPos - enc.getDistance()) < Config.Elevator.maxHeightDiff && Math.abs(enc.getRate()) < Config.Elevator.maxBrakeRate)
				{
					// If not braked brake the elevator, but keep pid running
					if(!getBrake())
					{
						brake();
						// Run timer, keep pid running when breaking, may be dangerous
						tmBrake.reset();
						tmBrake.start();
					}
				}
				
				// Once the brake has made complete contact, then stop the pid and set the speed to 0
				if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
				{
					tmBrake.stop();
					pidUp.stop();
					speed = 0;
				}
			}
			
			// If pid is not running set elevator speed to 0 (should probably brake too)
			else
				speed = 0;
		}
		
		System.out.println("Speed " + speed + " : " + "Encoder " + enc.getDistance() +" Encode Rate " + enc.getRate());
		setSpeed(speed);
	}
	
	/**
	 * Sets both elevator motors to the wanted speed
	 * @param speed the speed wanted
	 */
	public void setSpeed(double speed)
	{
		mtElevatorOne.set(speed);
		mtElevatorTwo.set(speed);
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
		pidUp.stop();
		pidUp.reset();
		pidDown.stop();
		pidDown.reset();
	}
	
}