	package core;

import lib.Config;
import lib.Controller;
import lib.Config.FileSaver;
import lib.navX.NavX;
import edu.wpi.first.wpilibj.Compressor;
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
	private Controller contrDrive = new Controller(Config.ContrDrive.chn, Config.ContrDrive.maxButtons, Config.ContrDrive.linearity);
	private SerialPort serial = new SerialPort(Config.Drive.navXBaudRate, SerialPort.Port.kMXP); 
	private NavX navX = new NavX(serial, (byte) 50);
	private Drive drive = new Drive(contrDrive, navX);
	private Claw claw = new Claw (contrDrive);
//	private Elevator elevator = new Elevator(contrDrive);
	private ElevatorEnc elevatorEnc = new ElevatorEnc(contrDrive);
//	private Dashboard dash = new Dashboard(drive, elevatorEnc, claw, navX);
	private Compressor comp = new Compressor();
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
//    	drive.init(Config.Drive.idRobotCentric, dash.getAngleOffset());
    	contrDrive.flipLeftX(true);
    	contrDrive.flipRightX(true);
    	contrDrive.flipRightY(true);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {

    }
    
    public void teleopInit()
    {
//    	drive.init(Config.Drive.idFieldCentric, dash.getAngleOffset());
//    	elevatorEnc.unBrake();
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	contrDrive.update();
//    	if(contrDrive.getButton(Config.ContrXBox.btElevatorUp))
//    		System.out.println("true");
//      elevator.runXBox();
//      drive.run();
    	elevatorEnc.testPID();
        claw.runXBox();
//      dash.update();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    
    }

    public void disabledInit()
    {
    	elevatorEnc.resetPID();
    }
}