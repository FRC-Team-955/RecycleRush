
package core;

import util.Config;
import auto.Auto;
import util.Controller;
import util.navX.NavX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort;

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
	private SerialPort serial = new SerialPort(Config.Drive.navXBaudRate, SerialPort.Port.kMXP); 
	private NavX navX = new NavX(serial, (byte) 50, 0);
	private Drive drive = new Drive(contrDrive, 0, navX);
	private Claw claw = new Claw (contrDrive);
	private Elevator elevator = new Elevator(contrDrive);
	private Dashboard dash = new Dashboard(drive, elevator, claw, navX);
	private Auto auto = new Auto(drive, claw, elevator, dash);
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
    	contrDrive = new Controller(Config.ContrDrive.chn, Config.ContrDrive.maxButtons, Config.ContrDrive.linearity);
    	dash.update();
    }
    
    public void autonomousInit()
    {
    	auto.getAutoMode();
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	auto.run();
    	dash.update();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {     
    	drive.run();
    	dash.update();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
    
    }   
}