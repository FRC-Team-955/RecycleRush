package core;

import lib.Config;
import lib.Controller;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Claw 
{
	private DoubleSolenoid clawNoid = new DoubleSolenoid(Config.Claw.chnSolOne, Config.Claw.chnSolTwo);
	private DoubleSolenoid alignClawNoid = new DoubleSolenoid(Config.Claw.chnAlignClawSolOne, Config.Claw.chnAlignClawSolTwo);
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
		if(contr.getButton(Config.ContrElevator.btAlignClawToggle))
		{
			if(getAlignClaw())
				closeAlignClaw();
			
			else
				openAlignClaw();
		}
		
		if(contr.getButton(Config.ContrElevator.btClawToggle))
		{
			if(getTopClaw())
				openTopClaw();
			
			else
				closeTopClaw();
		}
	}
	
	/**
	 * Opens the claw
	 */
	public void openTopClaw()
	{
		clawNoid.set(DoubleSolenoid.Value.kForward);
	}
	
	/**
	 * Closes the claw
	 */
	public void closeTopClaw()
	{
		clawNoid.set(DoubleSolenoid.Value.kReverse); 
	}
	
	/**
	 * Gets the status of claw,
	 * returns true if closed,
	 * returns false if open
	 * @return
	 */
	public boolean getTopClaw() 
	{
		return clawNoid.get() == DoubleSolenoid.Value.kReverse;
	}
	
	public void openAlignClaw()
	{
		alignClawNoid.set(DoubleSolenoid.Value.kForward); 
	}
	
	/**
	 * Closes the claw
	 */
	public void closeAlignClaw()
	{
		alignClawNoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	/**
	 * Gets the status of claw,
	 * returns true if closed,
	 * returns false if open
	 * @return
	 */
	public boolean getAlignClaw() 
	{
		return !(alignClawNoid.get() == DoubleSolenoid.Value.kReverse);
	}
}