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
	private Drive drive = new Drive();
	private Claw claw = new Claw (contrDrive);
	private Elevator elevator = new Elevator(contrDrive);
	private Dashboard dashboard = new Dashboard(drive, elevator, claw, contrDrive);
		
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
    	contrDrive.flipLeftX(false);
    	contrDrive.flipLeftY(true);
    	contrDrive.flipRightX(false);
    	contrDrive.flipRightY(true);
    
    }
    
    /**
     * This function is called once before teleop
     */
    public void teleopInit()
    {
    	elevator.brake();
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	contrDrive.update();
    	double[] rTheta = contrDrive.getRTheta();
    	drive.move(rTheta[0], rTheta[1]);
    	elevator.runPID();
    	claw.run();
        dashboard.update();
    }

    public void disabledInit()
    {
    	elevator.resetPID();
    }
}