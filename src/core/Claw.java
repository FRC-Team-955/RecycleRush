package core;

import lib.Config;
import lib.Controller;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Claw 
{
	private DoubleSolenoid noid = new DoubleSolenoid(Config.Claw.chnSolOne, Config.Claw.chnSolTwo);
	private Controller contr;
	
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
		if(contr.getButton(Config.ContrElevator.btClawToggle))
		{
			if(getClawStatus())
				closeClaw();
			
			else
				openClaw();
		}
	}
	
	/**
	 * Opens the claw
	 */
	public void openClaw()
	{
		noid.set(DoubleSolenoid.Value.kForward);
	}
	
	/**
	 * Closes the claw
	 */
	public void closeClaw()
	{
		noid.set(DoubleSolenoid.Value.kReverse); 
	}
	
	/**
	 * Gets the status of claw, open or closed
	 * @return If the solenoid is open, it returns true
	 */
	public boolean getClawStatus() 
	{
		return noid.get() == DoubleSolenoid.Value.kForward;
	}
}