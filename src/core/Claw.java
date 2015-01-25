package core;

import util.Config;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import util.Controller;


public class Claw {
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
		if (contr.getButton(Config.Claw.btOpen))
			openClaw();
		
		else if(contr.getButton(Config.Claw.btClose))
			closeClaw();
	}
	
	public void openClaw()
	{
		noid.set(DoubleSolenoid.Value.kForward);
	}
	
	public void closeClaw()
	{
		noid.set(DoubleSolenoid.Value.kReverse); 
	}
}
