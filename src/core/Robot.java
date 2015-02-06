
package core;

import util.Config;
import auto.Auto;
import util.Controller;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot 
{
	private Controller contrDrive;
	private Dashboard dash;
	private Drive drive = new Drive(contrDrive, dash.getAngelOffset());
	private Claw claw = new Claw (contrDrive);
	private Elevator elev = new Elevator(contrDrive);
	private Auto auto = new Auto(drive, claw, elev);
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
    	auto.getAutoMode();
    	contrDrive = new Controller(Config.ContrDrive.chn, Config.ContrDrive.maxButtons, Config.ContrDrive.linearity);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	auto.run();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {     
    	drive.run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
    
    }   
}