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
	private CANTalon mtElevatorOne = new CANTalon(Config.Elevator.idMtElevatorOne);
	private CANTalon mtElevatorTwo = new CANTalon(Config.Elevator.idMtElevatorTwo);
	
	private DoubleSolenoid noid = new DoubleSolenoid(Config.Elevator.chnNoidOne, Config.Elevator.chnNoidTwo);
	private LimitSwitch limitTop = new LimitSwitch(Config.Elevator.chnLimitSwitchTop, false);
	private LimitSwitch limitBot = new LimitSwitch(Config.Elevator.chnLimitSwitchBottom, false);
	private Encoder enc = new Encoder(Config.ElevatorEnc.chnEncOne, Config.ElevatorEnc.chnEncTwo);
	
	private Timer timerBrake = new Timer();
	private Timer testTimer = new Timer();
	private Controller contr;
	
	private boolean wasMoving = false;
	private int prevEnc = 0;
	//private PID pid = new PID(Config.Elevator.kP, Config.Elevator.kI, Config.Elevator.kP);
	
	/**
	 * Constructor
	 * @param newContr controller
	 */
	public Elevator(Controller newContr)
	{
		contr = newContr;
		enc.setDistancePerPulse(Config.ElevatorEnc.distancePerPulse);
		enc.reset();
	}

	/**
	 * Runs the elevator motor depending on joystick inputs
	 */
	public void run()
	{		
//		System.out.println("Encoder " + enc.getDistance());
//		System.out.println("Encoder Count: " + enc.get());
		
		System.out.println("Limit Top" + limitTop.get());
		System.out.println("Limit Bot" + limitBot.get());
		
		if(contr.getRawButton(Config.Elevator.btUp) && !limitTop.get())
		{
			// Auto disengages brakes if the brakes are engaged
			if(getBrake())
			{
				timerBrake.start();
				unBrake();
			}
			
			// Moves the elevator motor after the piston disengages 
			else if(timerBrake.get() > Config.Elevator.minTimerVal)
			{
				setSpeed(Config.Elevator.elevatorUpSpeed);
				System.out.println("1 SPEED");
			}
		}
		
		else if(contr.getRawButton(Config.Elevator.btDown) && !limitBot.get())
		{
			// Auto disengages brakes if the brakes are engaged
			if(getBrake())
			{
				unBrake();
				timerBrake.start();
			}
			
			// Moves the elevator motor after the piston disengages
			else if(timerBrake.get() > Config.Elevator.minTimerVal)
			{
				setSpeed(Config.Elevator.elevatorDownSpeed);
				System.out.println("-1 SPEED");
			}
		}
		
		else
		{		
			setSpeed(0);
			
			// Auto brakes after the motor stops
			if(!getBrake())
			{
				brake();
				timerBrake.reset();
			}
		
			System.out.println("0 SPEED");
		}
	}
	
	public void runXBox()
	{		
		System.out.println("Encoder " + enc.getDistance());
		System.out.println("Encoder Count: " + enc.get());
		
		System.out.println("Limit Top" + limitTop.get());
		System.out.println("Limit Bot" + limitBot.get());
//		
		if(contr.getRawButton(Config.ContrXBox.btElevatorUp) && !limitTop.get())
		{
			if(getBrake())
			{
				timerBrake.start();
				unBrake();
			}
			
			else if(timerBrake.get() > Config.Elevator.minTimerVal)
			{
				setSpeed(Config.Elevator.elevatorUpSpeed);
				System.out.println("1 SPEED");
			}
		}
		
		else if(contr.getRawButton(Config.ContrXBox.btElevatorDown) && !limitBot.get())
		{
			if(getBrake())
			{
				unBrake();
				timerBrake.start();
			}
			
			else if(timerBrake.get() > Config.Elevator.minTimerVal)
			{
				setSpeed(Config.Elevator.elevatorDownSpeed);
				System.out.println("-1 SPEED");
			}
		}
		
		else
		{		
			setSpeed(0);
			
			if(!getBrake())
			{
				brake();
				timerBrake.reset();
			}
		
			System.out.println("0 SPEED");
		}
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
		noid.set(DoubleSolenoid.Value.kReverse);
	}
	
	/**
	 * Disengages brake
	 */
	public void unBrake()
	{
		noid.set(DoubleSolenoid.Value.kForward);
	}
	
	/**
	 * Checks if the brake is engaged or not
	 * @return the brake position 
	 */
	public boolean getBrake()
	{
		return noid.get() == DoubleSolenoid.Value.kReverse;
	}
}
