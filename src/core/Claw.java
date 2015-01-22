package core;

import util.Config;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import util.Controller;


public class Claw {
	DoubleSolenoid noid = new DoubleSolenoid(Config.Claw.chnSolOne, Config.Claw.chnSolTwo);
	Controller contr;
	
	/**
	 * Constructor
	 * @param newContr controller
	 */
	public Claw(Controller newContr)
	{
		contr = newContr;
	}
	
	/**
	 * If the open and closes claw by setting Solenoid values
	 */
	public void run()
	{
		if (contr.getButton(Config.Claw.btOpen))
			noid.set(DoubleSolenoid.Value.kForward);
		
		else if(contr.getButton(Config.Claw.btClose))
			noid.set(DoubleSolenoid.Value.kReverse); 
	}
}
