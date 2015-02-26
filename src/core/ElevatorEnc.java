package core;

import lib.Config;
import lib.Controller;
import lib.LimitSwitch;
import lib.PID;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;

public class ElevatorEnc 
{
	private CANTalon mtElevatorOne = new CANTalon(Config.Elevator.idMtElevatorOne);
	private CANTalon mtElevatorTwo = new CANTalon(Config.Elevator.idMtElevatorTwo);
	private LimitSwitch limitTop = new LimitSwitch(Config.ElevatorEnc.chnLimitSwitchTop, false);
	private LimitSwitch limitBottom = new LimitSwitch(Config.ElevatorEnc.chnLimitSwitchBottom, true);
	private Encoder enc = new Encoder(Config.ElevatorEnc.chnEncOne, Config.ElevatorEnc.chnEncTwo);
	private PID pidUp = new PID(Config.ElevatorEnc.kUpP, Config.ElevatorEnc.kUpI, Config.ElevatorEnc.kUpD);
	private PID pidDown = new PID(Config.ElevatorEnc.kDownP, Config.ElevatorEnc.kDownI, Config.ElevatorEnc.kDownD);
	private DoubleSolenoid noid = new DoubleSolenoid(Config.Elevator.chnNoidOne, Config.Elevator.chnNoidTwo);
	private Controller contr;
	private double wantPos = 0;
	private double baseHeight = 0;
	private int level = 0;
	private double speed = 0;
	private boolean pidMode = true;
	private boolean mode = false;
	
	//Button number array
	private int [] levels = 
	{
		Config.ElevatorEnc.btLvlOne,
		Config.ElevatorEnc.btLvlTwo,
		Config.ElevatorEnc.btLvlThree,
		Config.ElevatorEnc.btLvlFour,
		Config.ElevatorEnc.btLvlFive,
		Config.ElevatorEnc.btLvlSix
	};

	/**
	 * Constructor
	 * @param newContr controller
	 */
	public ElevatorEnc(Controller newContr)
	{
		contr = newContr;
		enc.reset();
//		mtElevatorOne.changeControlMode(CANTalon.ControlMode.Position);
//		mtElevatorOne.changeControlMode(CANTalon.ControlMode.Position);
		enc.setDistancePerPulse(Config.ElevatorEnc.distancePerPulse);
		baseHeight = 0;
	}
	
	/**
	 * Runs the elevatorMove function while adjusting parameters
	 */
	public void run()
	{
		if(contr.getButton(1))
		{
			pidUp.startTimer();
			wantPos = 36.3;		// want pos in inches
		}
		
		pidUp.update(enc.getDistance(), wantPos);
		
		setSpeed(pidUp.getOutput());	
	}
	
	public void testPID()
	{	
		if(contr.getButton(Config.ContrXBox.btSwitchPID))
			pidMode = true;
		
		else if(contr.getButton(Config.ContrXBox.btSwitchZero))
			pidMode = false;
		
		if(pidMode)
		{
			if(contr.getButton(Config.ContrXBox.btElevatorUp))
			{
				pidUp.reset();
				pidUp.startTimer();
				
				// Negative is up
				wantPos = -5;
			}
			
			else if(contr.getButton(Config.ContrXBox.btElevatorUp2))
			{
				pidUp.reset();
				pidUp.startTimer();
				
				// Negative is up
				wantPos = -15;
			}
			
			pidUp.update(enc.getDistance(), wantPos);
			speed = pidUp.getOutput();
		}
		
		else
			speed = 0;
		
		System.out.println("Speed " + speed + " : " + "Encoder " + enc.getDistance());
		setSpeed(-speed);
	}
	
	public void setSpeed(double speed)
	{
		// Talons wired so - is up
		mtElevatorOne.set(-speed);
		mtElevatorTwo.set(-speed);
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
	
	public void resetPID()
	{
		pidUp.reset();
		pidUp.stopTimer();
		pidDown.reset();
		pidDown.stopTimer();
	}
}