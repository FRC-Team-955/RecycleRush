package core;

import lib.Config;
import lib.Controller;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

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
	private Elevator elevator = new Elevator(contrDrive);
	private Dashboard dashboard = new Dashboard(drive, elevator, contrDrive);
	private Timer timer = new Timer();
		
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
    	timer.start();
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	//contrDrive.update();
    	//double[] rTheta = contrDrive.getRTheta();
    	//drive.move(rTheta[0], rTheta[1]);
    	elevator.runPID();
        dashboard.update();
        if(timer.get() < 10) {
        	//drive.move(0.5, Math.PI/2);        	
        } else {
        	//drive.move(0, 0);
        }
    }

    public void disabledInit()
    {
    	elevator.resetPID();
    }
}