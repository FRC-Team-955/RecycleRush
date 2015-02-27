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
//	private double baseHeight = 0;
//	private int level = 0;
//	private boolean mode = false;
//	
//	private int [] levels = 
//	{
//		Config.ContrElevator.btLvlOne,
//		Config.ContrElevator.btLvlTwo,
//		Config.ContrElevator.btLvlThree,
//		Config.ContrElevator.btLvlFour,
//		Config.ContrElevator.btLvlFive,
//		Config.ContrElevator.btLvlSix
//	};
	
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
				tmBrake.start();
				unBrake();
			}
			
			// Moves the elevator motor after the brake disengages 
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
			{
				setSpeed(Config.Elevator.elevatorUpSpeed);
				System.out.println("GOING UP");
			}
		}
		
		else if(contr.getRawButton(Config.ContrElevator.btElevatorDown) && !limitBot.get())
		{
			// Auto disengages brakes if the brakes are engaged
			if(getBrake())
			{
				unBrake();
				tmBrake.start();
			}
			
			// Moves the elevator motor after the brake disengages
			else if(tmBrake.get() > Config.Elevator.brakeDisengageTime)
			{
				setSpeed(Config.Elevator.elevatorDownSpeed);
				System.out.println("GOING DOWN");
			}
		}
		
		else
		{		
			setSpeed(0);
			
			// Auto brakes after the motor stops
			if(!getBrake())
			{
				brake();
				tmBrake.reset();
			}
		
			System.out.println("0 SPEED");
		}
	}
	
	/**
	 * Tests the PID for the elevator
	 */
	public void testPID()
	{	
		double speed = 0;
		
		if(contr.getButton(Config.ContrElevator.btEnableElevatorPID))
			pidMode = true;
		
		else if(contr.getButton(Config.ContrElevator.btDisableElevatorPID))
			pidMode = false;
		
		if(pidMode)
		{
			if(contr.getButton(Config.ContrElevator.btElevatorUp))
			{
				pidUp.reset();
				pidUp.startTimer();
				
				// Negative is up
				wantPos = -5;
			}
			
			else if(contr.getButton(Config.ContrElevator.btElevatorUp2))
			{
				pidUp.reset();
				pidUp.startTimer();
				
				// Negative is up
				wantPos = -15;
			}
			
			pidUp.update(enc.getDistance(), wantPos);
			speed = pidUp.getOutput();
		}
		
		System.out.println("Speed " + speed + " : " + "Encoder " + enc.getDistance());
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
		pidUp.stopTimer();
		pidUp.reset();
		pidDown.stopTimer();
		pidDown.reset();
	}
}