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
	private Timer tmPickUp = new Timer();

	private int autoId = 0;
	private int autoStep = 0;
	private int elevatorLevel = 1;
	private boolean drivePosSet = false;
	private boolean driveAngSet = false;
	private boolean elevatorPosSet = false;

	public AutoPID(Drive newDrive, Elevator newElevator, Claw newClaw)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
	}

	public void init(int newAutoId)
	{
		autoId = newAutoId;
	}
	
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
			
			default:
				doNothing();
		}
	}

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
				stepDriveTo(0, Config.AutoPID.distToLineUpFromBin);
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
				stepDriveTo(0, Config.AutoPID.distToAutoZoneBin);
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
				stepDriveTo(0, Config.AutoPID.distToAlignAutoZone);
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
				stepPickupToteFromGround();
				break;
			}
	
			// Go forward to go around the bin
			case 1:
			{
				stepDriveTo(90, Config.AutoPID.distInFrontOfBin);
				break;
			}
	
			// Go left, towards next tote
			case 2:
			{
				stepDriveTo(0, Config.AutoPID.distToNextTote);
				break;
			}
	
			// Go backwards towards next tote
			case 3:
			{
				stepDriveTo(-90, Config.AutoPID.distInFrontOfBin);
				break;
			}
	
			// Open claw, set elevator to pick up level 1 ground mode
			case 4:
			{
				stepDropToteOnTote();
				break;
			}
	
			// Close claw, set elevator to drop off level 2 ground mode
			case 5:
			{
				stepPickupToteFromGround();
				break;
			}
	
			// Go forward to go around the bin
			case 6:
			{
				stepDriveTo(90, Config.AutoPID.distInFrontOfBin);
				break;
			}
	
			// Go left towards the next tote
			case 7:
			{
				stepDriveTo(0, Config.AutoPID.distToNextTote);
				break;
			}
	
			// Go backwards towards next tote
			case 8:
			{
				stepDriveTo(-90, Config.AutoPID.distInFrontOfBin);
				break;
			}
	
			// Open claw, set elevator to pick up level 1 ground mode
			case 9:
			{
				stepDropToteOnTote();
				break;
			}
	
			// Close claw, set elevator to pick up level 1 scoring mode
			case 10:
			{
				stepPickupToteForScoring();
				break;
			}
	
			// Drive to auto zone
			case 11:
			{
				stepDriveTo(90, Config.AutoPID.distToAutoZone);
				break;
			}
	
			// Open claw, set elevator to pick up level 1 scoring mode
			case 12:
			{
				stepDropToteForScoring();
				break;
			}
	
			// Drive left away totes to clear them
			case 13:
			{
				stepDriveTo(180, Config.AutoPID.distToClearStackedTotes);
				break;
			}
		}

		drive.update();
		elevator.update();
	}

	// TODO: WARNING THIS IS NOT THE ACTUAL TO AUTO ZONE ONE
//	public void toAutoZone()
//	{
//		switch(autoStep)
//		{
//			case 0:
//			{
//				System.out.println(drive.getAngle());
//				stepDriveTo(90, 30);//Config.AutoPID.distToAutoZone);
//				break;
//			}
//		}
//		
//		drive.update();
//	}
	
	/**
	 * Goes to auto zone from landfill
	 */
	public void toAutoZoneLandfill()
	{
		switch(autoStep)
		{
			case 0:
			{
				stepDriveTo(0, Config.AutoPID.distToAutoZoneLandfill);
				break;
			}
		}
		
		drive.update();
	}
	
	// TODO: THIS IS THE ACTUAL TO AUTO ZONE THE OTHER ONE IS FOR TESTING PURPOSES
	public void toAutoZone()
	{
		switch(autoStep)
		{
			case 0:
			{
				stepDriveTo(0, Config.AutoPID.distToAutoZone);
				break;
			}
		}
		
		drive.update();
	}
	
	/**
	 * Closes claw, set elevator to drop off level 2 ground mode
	 */
	public void stepPickupToteFromGround()
	{
//		if (!claw.getTopClaw())
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
	public void stepDriveTo(double heading, double distance)
	{
		// Set new driving position if it hasn't been set
		if (!drivePosSet)
		{
			drive.setHeading(heading, distance);
			drivePosSet = true;
		}

		// Move on to next auto step if drive has finished aka reached its
		// destination
		else if (!drive.isRunning())
		{
			drive.setSpeed(0, 0, 0, false);
			drivePosSet = false;
			autoStep++;
		}
	}

	// public void run(int autoId)
	// {
	// switch(autoId)
	// {
	// case Config.AutoPID.idDoNothing:
	// {
	// doNothing();
	// break;
	// }
	//
	// case Config.AutoPID.idGetOneToteBearing:
	// {
	// getOneToteBearng();
	// break;
	// }
	//
	// case Config.AutoPID.idGetAllTotesBearingLeft:
	// {
	// getAllTotesBearingLeft();
	// break;
	// }
	//
	// case Config.AutoPID.idGetAllTotesBearingCenter:
	// {
	// getAllTotesBearingCenter();
	// break;
	// }
	//
	// case Config.AutoPID.idGetAllTotesBearingRight:
	// {
	// getAllTotesBearingRight();
	// break;
	// }
	//
	//
	// case Config.AutoPID.idDriveForward:
	// {
	// driveForward();
	// break;
	// }
	//
	// case Config.AutoPID.idGetOneTote:
	// {
	// getOneTote();
	// break;
	// }
	//
	// case Config.AutoPID.idGetAllTotesLeft:
	// {
	// getAllTotesLeft();
	// break;
	// }
	//
	// case Config.AutoPID.idGetAllTotesCenter:
	// {
	// getAllTotesCenter();
	// break;
	// }
	//
	// case Config.AutoPID.idGetAllTotesRight:
	// {
	// getAllTotesRight();
	// break;
	// }
	// }
	//
	// drive.update();
	// }
	//
	// public void doNothing()
	// {
	//
	// }
	//
	// public void driveForwardBearing()
	// {
	// drive.setHeading(0, drive.getAngle(),
	// Config.AutoPID.encDriveForwardDistance);
	// }
	//
	// public void driveForward()
	// {
	// drive.setHeading(0, Config.AutoPID.encDriveForwardDistance);
	// }
	//
	// public void getOneToteBearng()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// driveForwardBearing();
	// }
	// }
	// }
	//
	// /**
	// * Gets all totes from the starting leftmost starting position
	// */
	// private void getAllTotesBearingLeft()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// strafeRightBearing();
	// break;
	// }
	//
	// case 2:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 3:
	// {
	// strafeRightBearing();
	// break;
	// }
	//
	// case 4:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 5:
	// {
	// driveForwardBearing();
	// break;
	// }
	// }
	// }
	//
	// /**
	// * Gets all totes from the starting center starting position
	// */
	// private void getAllTotesBearingCenter()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// strafeRightBearing();
	// break;
	// }
	//
	// case 2:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 3:
	// {
	// strafeLeftBearing();
	// break;
	// }
	//
	// case 4:
	// {
	// strafeLeftBearing();
	// break;
	// }
	//
	// case 5:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 6:
	// {
	// driveForwardBearing();
	// break;
	// }
	// }
	// }
	//
	// /**
	// * Gets all totes from the starting rightmost starting position
	// */
	// private void getAllTotesBearingRight()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// strafeLeftBearing();
	// break;
	// }
	//
	// case 2:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 3:
	// {
	// strafeLeftBearing();
	// break;
	// }
	//
	// case 4:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 5:
	// {
	// driveForwardBearing();
	// break;
	// }
	// }
	// }
	//
	// public void getOneTote()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// driveForward();
	// }
	// }
	// }
	//
	// /**
	// * Gets all totes from the starting leftmost starting position
	// */
	// private void getAllTotesLeft()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// strafeRight();
	// break;
	// }
	//
	// case 2:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 3:
	// {
	// strafeRight();
	// break;
	// }
	//
	// case 4:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 5:
	// {
	// driveForward();
	// break;
	// }
	// }
	// }
	//
	// /**
	// * Gets all totes from the starting center starting position
	// */
	// private void getAllTotesCenter()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// strafeRight();
	// break;
	// }
	//
	// case 2:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 3:
	// {
	// strafeLeft();
	// break;
	// }
	//
	// case 4:
	// {
	// strafeLeft();
	// break;
	// }
	//
	// case 5:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 6:
	// {
	// driveForward();
	// break;
	// }
	// }
	// }
	//
	// /**
	// * Gets all totes from the starting rightmost starting position
	// */
	// private void getAllTotesRight()
	// {
	// switch(autoStep)
	// {
	// case 0:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 1:
	// {
	// strafeLeft();
	// break;
	// }
	//
	// case 2:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 3:
	// {
	// strafeLeft();
	// break;
	// }
	//
	// case 4:
	// {
	// pickUp();
	// break;
	// }
	//
	// case 5:
	// {
	// driveForward();
	// break;
	// }
	// }
	// }
	//
	// public void strafeRightBearing()
	// {
	// // drive.setHeading(0, drive.getAngle(),
	// Config.AutoPID.encStrafeDistance);
	// // if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) <
	// Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() -
	// Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
	// // {
	// // drive.setHeading(90, drive.getAngle(),
	// Config.AutoPID.encDistanceBetweenTotes);
	// //
	// // if(Math.abs(drive.getLeftEnc() -
	// Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance &&
	// Math.abs(drive.getRightEnc() - Config.AutoPID.encDistanceBetweenTotes) <
	// Config.AutoPID.encTolerance)
	// // {
	// // drive.setHeading(0, drive.getAngle(),
	// -Config.AutoPID.encStrafeDistance);
	// //
	// // if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance &&
	// Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
	// // endRoutine();
	// // }
	// // }
	// }
	//
	// public void strafeLeftBearing()
	// {
	// // drive.setHeading(0, drive.getAngle(),
	// Config.AutoPID.encStrafeDistance);
	// // if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) <
	// Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() -
	// Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
	// // {
	// // drive.setHeading(270, drive.getAngle(),
	// Config.AutoPID.encDistanceBetweenTotes);
	// //
	// // if(Math.abs(drive.getLeftEnc() -
	// Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance &&
	// Math.abs(drive.getRightEnc() - Config.AutoPID.encDistanceBetweenTotes) <
	// Config.AutoPID.encTolerance)
	// // {
	// // drive.setHeading(drive.getAngle(), -Config.AutoPID.encStrafeDistance);
	// //
	// // if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance &&
	// Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
	// // endRoutine();
	// // }
	// // }
	// }
	//
	// public void strafeLeft()
	// {
	// drive.setHeading(0, Config.AutoPID.encStrafeDistance);
	// if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) <
	// Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() -
	// Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
	// {
	// drive.setHeading(270, Config.AutoPID.encDistanceBetweenTotes);
	//
	// if(Math.abs(drive.getLeftEnc() - Config.AutoPID.encDistanceBetweenTotes)
	// < Config.AutoPID.encTolerance && Math.abs(drive.getRightEnc() -
	// Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance)
	// {
	// drive.setHeading(180, Config.AutoPID.encStrafeDistance);
	//
	// if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance &&
	// Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
	// endRoutine();
	// }
	// }
	// }
	//
	// public void strafeRight()
	// {
	// drive.setHeading(0,Config.AutoPID.encStrafeDistance);
	// if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) <
	// Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() -
	// Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
	// {
	// drive.setHeading(90, Config.AutoPID.encDistanceBetweenTotes);
	//
	// if(Math.abs(drive.getLeftEnc() - Config.AutoPID.encDistanceBetweenTotes)
	// < Config.AutoPID.encTolerance && Math.abs(drive.getRightEnc() -
	// Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance)
	// {
	// drive.setHeading(0, -Config.AutoPID.encStrafeDistance);
	//
	// if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance &&
	// Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
	// endRoutine();
	// }
	// }
	// }
	//
	// public void pickUp()
	// {
	// if(tmPickUp.get() < Config.AutoPID.timeIntakeClose)
	// {
	// claw.closeClaw();
	// }
	//
	// else if(tmPickUp.get() < Config.AutoPID.timeElevatorStack)
	// {
	// elevator.setHeight(elevatorLevel);
	// elevatorLevel++;
	// }
	//
	// else if(tmPickUp.get() < Config.AutoPID.timeIntakeClose)
	// {
	// claw.openClaw();
	// }
	//
	// else
	// endRoutine();
	// }
	//
	// public void endRoutine()
	// {
	// tmPickUp.stop();
	// tmPickUp.reset();
	// autoStep++;
	// }
}
