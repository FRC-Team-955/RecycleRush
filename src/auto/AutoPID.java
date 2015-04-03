package auto;



import core.Claw;
import core.Drive;
import core.Elevator;
import lib.Config;
import lib.Util;
import edu.wpi.first.wpilibj.Timer;

public class AutoPID
{
	// Subsystems for auto to control
	private Drive drive;
	private Elevator elevator;
	private Claw claw;

	private int autoId = 0; //The id of the specified routine
	private int autoStep = 0; // The counter that increments the auto mode
	
	//Variables for changing the angle
	private boolean drivePosSet = false;
	private boolean driveAngSet = false;
    
	//Test Variables
	//TODO Remove when done 
	private double maxLeftEncDist = 0;
    private double minLeftEncDist = 0;
    private double maxRightEncDist = 0;
    private double minRightEncDist = 0;
    
    /**
     * 
     * Initializes the fundamentals such as drive, elevator, etc.
     * 
     * @param newDrive 
     * @param newElevator 
     * @param newClaw
     */
    public AutoPID(Drive newDrive, Elevator newElevator, Claw newClaw)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
	}

    /**
     * 
     * Initializes the auto mode
     * 
     * @param newAutoId - The id for the auto routine
     */
	public void init(int newAutoId)
	{
		autoId = newAutoId;
	}
	
	/**
	 * Selects auto mode
	 */
	public void run()
	{
		switch (autoId)
		{
			case Config.AutoPID.idToAutoZone:
			{
				// WARNING GOES FORWARD WHEN BOT FACING FORWARD, THIS USED IN STACK ALL TOTES
				toAutoZone();
				break;
			}
	
			case Config.AutoPID.idMoveOneBinToAutoZone:
			{
				moveOneBinToAutoZone();
				break;
			}
	
			case Config.AutoPID.idMoveOneBinLineUp:
			{
				moveOneBinLineUp();
				break;
			}
			
			case Config.AutoPID.idToAutoZoneLandfill:
			{
				toAutoZoneLandfill();
				break;
			}
			
			case Config.AutoPID.idDoNothing:
			{
				doNothing();
				break;
			}
			
			case Config.AutoPID.idStackAllTotesRight:
			{
				//stackAllTotesRight();
				turnOnce();
				break;
			}
					
			default:
				doNothing();
		}
	}
//**************************************************************************************
	private void moveOneBinLineUp()
	{
		switch (autoId)
		{
			case 0:
			{
				stepPickupToteFromGround();
				break;
			}

			case 1:
			{
				stepDriveTo(Config.AutoPID.distToLineUpFromBin);
				break;
			}
			
			case 2:
			{
				stepRotateToAngle(90);
				drive.encReset();
				break;
			}
			
			default:
				break;
		}
		
			elevator.update();
			drive.update();
			drive.updateAng();			
	}

	public void doNothing()
	{
		
	}
	
	public void moveOneBinToAutoZone()
	{
		switch (autoStep)
		{
			case 0:
			{
				stepPickupToteFromGround();
				break;
			}
			
			case 1:
			{
				stepDriveTo(Config.AutoPID.distToAutoZoneBin);
				break;
			}
			
			case 2:
			{
				stepRotateToAngle(90);
				drive.encReset();
				break;
			}
			
			case 3:
			{
				stepDriveTo(Config.AutoPID.distToAlignAutoZone);
				break;
			}
			
			case 4:
			{
				stepRotateToAngle(90);
				drive.encReset();
				break;
			}
			
			default:
				break;
		}

		elevator.update();
		drive.update();
		drive.updateAng();
	}

	/**
	 * Stacks all the yellow totes when starting on the right side, the bot
	 * facing the yellow tote, claw open around the yellow tote, ready to be
	 * closed immediately
	 */
	public void stackAllTotesRight()
	{
		switch (autoStep)
		{
			// Close claw, set elevator to drop off level 2 ground mode
			case 0:
			{
				claw.closeTopClaw();
				stepPickupToteFromGround();
				break;
			}
	
			// Go forward to go around the bin
			case 1:
			{
				stepRotateToAngle(-45);
				break;
			}
	
			// Go left, towards next tote
			case 2:
			{
				stepDriveTo(Config.AutoPID.distToNextTote);
				break;
			}
	
			// Go backwards towards next tote
			case 3:
			{
				stepRotateToAngle(45);
				break;
			}
			
			case 4:
			{
				stepDriveTo(Config.AutoPID.distToNextTote);
				break;
			}
	
			// Open claw, set elevator to pick up level 1 ground mode
			case 5:
			{
				stepDropToteOnTote();
				break;
			}
	
			// Close claw, set elevator to drop off level 2 ground mode
			case 6:
			{
				stepPickupToteFromGround();
				break;
			}
			// Close claw, set elevator to drop off level 2 ground mode
			case 7:
			{
				stepPickupToteFromGround();
				break;
			}
	
			// Go forward to go around the bin
			case 8:
			{
				stepRotateToAngle(-45);
				break;
			}
	
			// Go left, towards next tote
			case 9:
			{
				stepDriveTo(Config.AutoPID.distToNextTote);
				break;
			}
	
			// Go backwards towards next tote
			case 10:
			{
				stepRotateToAngle(45);
				break;
			}
			
			case 11:
			{
				stepDriveTo(Config.AutoPID.distToNextTote);
				break;
			}
	
			// Open claw, set elevator to pick up level 1 ground mode
			case 12:
			{
				stepDropToteOnTote();
				break;
			}
	
			// Close claw, set elevator to drop off level 2 ground mode
			case 13:
			{
				stepPickupToteFromGround();
				break;
			}
			// Close claw, set elevator to drop off level 2 ground mode
			case 14:
			{
				stepPickupToteFromGround();
				break;
			}
	
			// Go forward to go around the bin
			case 15:
			{
				stepRotateToAngle(-45);
				break;
			}
	
			// Go left, towards next tote
			case 16:
			{
				stepDriveTo(Config.AutoPID.distToNextTote);
				break;
			}
	
			// Go backwards towards next tote
			case 17:
			{
				stepRotateToAngle(45);
				break;
			}
			
			case 18:
			{
				stepDriveTo(Config.AutoPID.distToNextTote);
				break;
			}
	
			// Open claw, set elevator to pick up level 1 ground mode
			case 19:
			{
				stepDropToteOnTote();
				break;
			}
	
			// Close claw, set elevator to drop off level 2 ground mode
			case 20:
			{
				stepPickupToteFromGround();
				break;
			}
		
			case 21:
			{
				stepRotateToAngle(90);
				break;
			}
			
			case 22:
			{
				stepDriveTo(Config.AutoPID.distToAutoZone);
				break;
			}
		}

		drive.update();
		elevator.update();
	}

	
	/**
	 * Test routine
	 * TODO Delete when finished
	 */
	
	public void turnOnce(){
		
		boolean updateAngle = false;
		
		switch (autoStep)
		{	
			case 0:
			{
				stepDriveTo(60);
				System.out.println("Ran case 0!");
				//System.out.println(drive.getRightEncDist());
				
				if(drive.getLeftEncDist() > maxLeftEncDist)
					maxLeftEncDist = drive.getLeftEncDist();
				
				if(drive.getLeftEncDist() < minLeftEncDist)
					maxLeftEncDist = drive.getLeftEncDist();
				
				if(drive.getRightEncDist() > maxRightEncDist)
					maxRightEncDist = drive.getRightEncDist();
		
				if(drive.getRightEncDist() < minRightEncDist)
					minRightEncDist = drive.getRightEncDist();
				
				System.out.println("Max Enc Left:" + maxLeftEncDist);
				System.out.println("Min Enc Left" + minLeftEncDist);
				
				System.out.println("Max Enc Right:" + maxRightEncDist);
				System.out.println("Min Enc Right" + minRightEncDist);
				
				break;
			}
			
			case 1:
			{
				stepDriveTo(-12);	
				System.out.println("Ran case 1!");
				break;
			}
			
//			case 2: 
//			{
//				stepRotateToAngle(10);
//				System.out.println("Entered case 2: Rotate to 10 degrees");
//				updateAngle = true;
//				break;
//				
//			}
			
			
			default:
				break;
			}
		
//		if(updateAngle)	
//			drive.updateAng();
		
		drive.update();
	}
	
	/**
	 * Goes to auto zone from landfill
	 */
	public void toAutoZoneLandfill()
	{
		switch(autoStep)
		{
			case 0:
			{
				stepDriveTo(Config.AutoPID.distToAutoZoneLandfill);
				break;
			}
		}
		
		drive.update();
	}
	
	/**
	 * move to the auto zone from near the driver station wall
	 */
	public void toAutoZone()
	{
		switch(autoStep)
		{
			case 0:
			{
				stepDriveTo(Config.AutoPID.distToAutoZone);
				break;
			}
		}
		
		drive.update();
	}
	
	
	//***************************************************************************************
	
	/**
	 * Closes claw, set elevator to drop off level 2 ground mode
	 */
	public void stepPickupToteFromGround()
	{
//		if (!claw.getTopClaw())
		claw.closeAlignClaw();
		claw.closeTopClaw();

		elevator.setHeightType(Config.Elevator.heightTypeGround);
		elevator.setDropOffMode(true);
		elevator.setToteLevel(2);
		autoStep++;
	}
	
	public void stepRotateToAngle(double heading)
	{
		// Set new driving position if it hasn't been set
		if (!driveAngSet)
		{
			drive.setAng(heading);
			driveAngSet = true;
		}

		// Move on to next auto step if drive has finished aka reached its
		// destination
		if (!drive.isRunning())
		{
			drive.setSpeed(0, 0, 0, false);
			driveAngSet = false;
			autoStep++;
		}
	}

	/**
	 * Closes claw, set elevator to pick up level 1 scoring mode
	 */
	public void stepPickupToteForScoring()
	{
		if (!claw.getTopClaw())
			claw.closeTopClaw();

		elevator.setHeightType(Config.Elevator.heightTypeScoring);
		elevator.setDropOffMode(false);
		elevator.setToteLevel(1);
		autoStep++;
	}

	/**
	 * Opens claw, set elevator to pick up level 1 ground mode
	 */
	public void stepDropToteForScoring()
	{
		if (claw.getTopClaw())
			claw.openTopClaw();

		elevator.setHeightType(Config.Elevator.heightTypeGround);
		elevator.setDropOffMode(false);
		elevator.setToteLevel(1);
	}

	/**
	 * Opens claw, set elevator to pick up level 1 ground mode
	 */
	public void stepDropToteOnTote()
	{
		if (claw.getTopClaw())
			claw.openTopClaw();

		elevator.setHeightType(Config.Elevator.heightTypeGround);
		elevator.setDropOffMode(false);
		elevator.setToteLevel(1);

		// Wait for elevator to get to its destination to move to next auto step
		if (!elevator.isRunning())
			autoStep++;
	}

	/**
	 * Drives to distance, at heading angle
	 * 
	 * @param heading
	 * @param distance
	 */
	public void stepDriveTo(double distance)
	{
		drive.encReset();
		// Set new driving position if it hasn't been set
		if (!drivePosSet)
		{
			drive.setDistance(distance);
			drivePosSet = true;
		}

		// Move on to next auto step if drive has finished aka reached its
		// destination
		else if (!drive.isRunning())
		{
			drive.setSpeed(0, 0, 0, false);
			drivePosSet = false;
			autoStep++;
			System.out.println(autoStep);
		}
	}

}
