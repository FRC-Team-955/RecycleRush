package core;

import util.Config;
import edu.wpi.first.wpilibj.Encoder;
import util.Controller; 
import util.LimitSwitch;
import edu.wpi.first.wpilibj.CANTalon;


public class Elevator 
{
	CANTalon mtElevator = new CANTalon(Config.Elevator.chnMtElevator);
	LimitSwitch limitBottom = new LimitSwitch(Config.Elevator.chnLimitSwitchBottom);
	LimitSwitch limitTop = new LimitSwitch(Config.Elevator.chnLimitSwitchTop);
	Encoder enc = new Encoder(Config.Elevator.chnEncOne, Config.Elevator.chnEncTwo);
	Controller contr;
	boolean mode = false;
	int baseValue = 0;
	//Button number array
	int [] levels = {Config.Elevator.btLvlOne,Config.Elevator.btLvlTwo,Config.Elevator.btLvlThree,Config.Elevator.btLvlFour,Config.Elevator.btLvlFive,Config.Elevator.btLvlSix};
	
	/**
	 * Constructor
	 * @param newContr controller
	 */
	public Elevator(Controller newContr)
	{
		contr = newContr;
		enc.reset();
		mtElevator.changeControlMode(CANTalon.ControlMode.Position);
	}
	
	/**
	 * Runs the elevatorMove function while adjusting parameters
	 */
	public void run()
	{
		// If the switch mode button is pressed set the lowest position on the elevator to six inches
		baseValue = 0;
		if(contr.getButton(Config.Elevator.btModeSwitch))
			mode = !mode;
		if(mode)
			baseValue = Config.Elevator.pulsesPerInch * 6;
			
		// Checks all elevator buttons
		for(int i = 0; i < levels.length; i++)
		{
			if(contr.getButton(levels[i]))
				elevatorMove(levels[i]);
		}	
	}
	
	/**
	 * Moves the elevator to the specific indexing location
	 * @param level
	 */
	public void elevatorMove(int level)
	{
		switch(level)
		{
			case 1: mtElevator.setPosition(baseValue);
			case 2: mtElevator.setPosition(baseValue + Config.Elevator.pulsesPerTote * level);
			case 3: mtElevator.setPosition(baseValue + Config.Elevator.pulsesPerTote * level);
			case 4: mtElevator.setPosition(baseValue + Config.Elevator.pulsesPerTote * level);
			case 5: mtElevator.setPosition(baseValue + Config.Elevator.pulsesPerTote * level);
			case 6: mtElevator.setPosition(baseValue + Config.Elevator.pulsesPerTote * level);
		}
	}
}
