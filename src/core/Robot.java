package core;

import lib.Config;
import lib.Controller;
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
	private Controller contrDrive = new Controller(Config.ContrDrive.chn, Config.ContrDrive.maxButtons);
	private Drive drive = new Drive(contrDrive);
	private Claw claw = new Claw (contrDrive);
	private Elevator elevator = new Elevator(contrDrive);
	private Dashboard dashboard = new Dashboard(drive, elevator, claw);
	private boolean teleopRan = false;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
    	contrDrive.flipLeftX(true);
    	contrDrive.flipRightX(true);
    	contrDrive.flipRightY(true);
    }

    /**
     * This function is called once before autonomous
     */
    public void autonomousInit()
    {
    	dashboard.openLogFile();
    	drive.init(Config.Drive.idRobotCentric, dashboard.getBotAngleOffset());
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {

    }
    
    /**
     * This function is called once before teleop
     */
    public void teleopInit()
    {
    	teleopRan = true;
    	dashboard.openLogFile();
    	drive.init(Config.Drive.idFieldCentric, dashboard.getBotAngleOffset());
    	elevator.unBrake();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	contrDrive.update();
//      drive.run();
    	elevator.testPID();
        claw.run();
        dashboard.update();
    }

    public void disabledInit()
    {
    	elevator.resetPID();
    	
    	if(teleopRan)
    	{
    		dashboard.closeLogFile();
    		teleopRan = false;
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    
    }
}