package core;

import auto.AutoPID;
//import lib.CameraFeed;
import lib.CameraFeed;
import lib.Config;
import lib.Controller;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	private AutoPID auto = new AutoPID(drive, elevator, claw);
	private Dashboard dashboard = new Dashboard(drive, elevator, claw, contrDrive);
	private boolean teleopRan = false;
	private Timer tmTest = new Timer();
	private int testStep = 0;
	private boolean elevatorUpRan = false;
	private boolean elevatorDownRan = false;
//	private CameraFeed cam = new CameraFeed();
		
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
     * This function is called once before autonomous
     */
    public void autonomousInit()
    {
    	elevator.brake();
    	dashboard.openLogFile();
    	auto.init(dashboard.getAutoType());
    	drive.init(Config.Drive.idRobotCentric, dashboard.getBotAngleOffset());
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	auto.run();
    	dashboard.update();
//    	dashboard.displayCurrent();
    
    
    }
    
    /**
     * This function is called once before teleop
     */
    public void teleopInit()
    {
    	teleopRan = true;
    	dashboard.openLogFile();
    	elevator.brake();
//    	cam.init();
    
    
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	contrDrive.update();
    	drive.run();
    	elevator.runPID();
    	claw.run();
        dashboard.update();
//        dashboard.displayCurrent();
//        cam.run();
        // System.out.println(lidar.getDistance());
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
    
    public void testInit()
    {
    	elevator.brake();
    	tmTest.start();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    	switch(testStep)
    	{
    		case 0:
    		{
    			drive.setLeftCAN();
    			System.out.println("Ran Left CAN");
    			break;
    		}
    		
    		case 1:
    		{
    			drive.setLeft();
    			System.out.println("Ran Left");
    			break;
    		}
    		
    		case 2:
    		{
    			drive.setRightCAN();
    			System.out.println("Ran Right CAN");
    			break;
    		}
    		
    		case 3:
    		{
    			drive.setRight();
    			System.out.println("Ran Right");
    			break;
    		}
    		
    		case 4:
    		{
    			if(!elevatorUpRan)
    				elevator.setToteLevel(2);
    			
    			elevatorUpRan = true;
    			System.out.println("Set Tote Level to Two");
    			drive.setSpeed(0, 0, 0, false);
    			break;
    		}
    		
    		case 5:
    		{
    			if(!elevatorDownRan)
    				elevator.setToteLevel(0);
    			
    			elevatorDownRan = true;
    			System.out.println("Set Tote Level to One");
    			break;
    		}
    		
    		case 6:
    		{
    			claw.openAlignClaw();
    			claw.openTopClaw();
    			elevator.setToteLevel(1);
    			System.out.println("Open Claws");
    			break;
    		}
    		
    		case 7:
    		{
    			claw.closeAlignClaw();
    			claw.closeTopClaw();
    			System.out.println("Close Claws");
    			break;
    		}
    		
    	}
	
		elevator.update();
		System.out.println(tmTest.get());
		
		if(tmTest.get() > Config.Test.motorTime && testStep < 4)
		{
			testStep++;
			tmTest.reset();
		}
		
		else if(tmTest.get() > Config.Test.elevatorTime && testStep >= 4 )
		{
			testStep++;
			tmTest.reset();
		}

    }
}