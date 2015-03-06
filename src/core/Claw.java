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
			if(getClaw())
				open();
			
			else
				close();
		}
	}
	
	/**
	 * Opens the claw
	 */
	public void open()
	{
		noid.set(DoubleSolenoid.Value.kForward);
	}
	
	/**
	 * Closes the claw
	 */
	public void close()
	{
		noid.set(DoubleSolenoid.Value.kReverse); 
	}
	
	/**
	 * Gets the status of claw,
	 * returns true if closed,
	 * returns false if open
	 * @return
	 */
	public boolean getClaw() 
	{
		return noid.get() == DoubleSolenoid.Value.kReverse;
	}
}