package auto;

import core.Claw;
import core.Drive;
import core.Elevator;
import lib.Config;
import edu.wpi.first.wpilibj.Timer;;

public class AutoPID
{
	// Subsystems for auto to control
	private Drive drive;
	private Elevator elevator;
	private Claw claw;
	private Timer tmPickUp = new Timer();
	
	private int autoStep = 0;
	private int elevatorLevel = 1;
	
	public AutoPID(Drive newDrive, Elevator newElevator, Claw newClaw)
	{
		drive = newDrive;
		elevator = newElevator;
		claw = newClaw;
	}
	
	public void run(int autoId)
	{	
		switch(autoId)
		{
			case Config.AutoPID.idDoNothing:
			{
				doNothing();
				break;
			}
			
			case Config.AutoPID.idDriveForwardBearing:
			{
				driveForwardBearing();
				break;
			}		
			
			case Config.AutoPID.idGetOneToteBearing:
			{
				getOneToteBearng();
				break;
			}

			case Config.AutoPID.idGetAllTotesBearingLeft:
			{
				getAllTotesBearingLeft();
				break;
			}
			
			case Config.AutoPID.idGetAllTotesBearingCenter:
			{
				getAllTotesBearingCenter();
				break;
			}
			
			case Config.AutoPID.idGetAllTotesBearingRight:
			{
				getAllTotesBearingRight();
				break;
			}
		
		
			case Config.AutoPID.idDriveForward:
			{
				driveForward();
				break;
			}		
			
			case Config.AutoPID.idGetOneTote:
			{
				getOneTote();
				break;
			}
	
			case Config.AutoPID.idGetAllTotesLeft:
			{
				getAllTotesLeft();
				break;
			}
			
			case Config.AutoPID.idGetAllTotesCenter:
			{
				getAllTotesCenter();
				break;
			}
			
			case Config.AutoPID.idGetAllTotesRight:
			{
				getAllTotesRight();
				break;
			}
		}
		
		drive.update();
	}
	
	public void doNothing()
	{
		
	}
	
	public void driveForwardBearing()
	{
		drive.setHeading(0, drive.getAngle(), Config.AutoPID.encDriveForwardDistance);
	}
	
	public void driveForward()
	{
		drive.setHeading(0, Config.AutoPID.encDriveForwardDistance);
	}
	
	public void getOneToteBearng()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				driveForwardBearing();
			}
		}
	}
	
	/**
	 * Gets all totes from the starting leftmost starting position
	 */
	private void getAllTotesBearingLeft()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				strafeRightBearing();
				break;
			}
			
			case 2:
			{
				pickUp();
				break;
			}
			
			case 3:
			{
				strafeRightBearing();
				break;
			}
			
			case 4:
			{
				pickUp();
				break;
			}
			
			case 5:
			{
				driveForwardBearing();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting center starting position
	 */
	private void getAllTotesBearingCenter()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				strafeRightBearing();
				break;
			}
			
			case 2:
			{
				pickUp();
				break;
			}
			
			case 3:
			{
				strafeLeftBearing();
				break;
			}
			
			case 4:
			{
				strafeLeftBearing();
				break;
			}
			
			case 5:
			{
				pickUp();
				break;
			}
			
			case 6:
			{
				driveForwardBearing();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting rightmost starting position
	 */
	private void getAllTotesBearingRight()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				strafeLeftBearing();
				break;
			}
			
			case 2:
			{
				pickUp();
				break;
			}
			
			case 3:
			{
				strafeLeftBearing();
				break;
			}
			
			case 4:
			{
				pickUp();
				break;
			}
			
			case 5:
			{
				driveForwardBearing();
				break;
			}
		}	
	}
	
	public void getOneTote()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				driveForward();
			}
		}
	}
	
	/**
	 * Gets all totes from the starting leftmost starting position
	 */
	private void getAllTotesLeft()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				strafeRight();
				break;
			}
			
			case 2:
			{
				pickUp();
				break;
			}
			
			case 3:
			{
				strafeRight();
				break;
			}
			
			case 4:
			{
				pickUp();
				break;
			}
			
			case 5:
			{
				driveForward();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting center starting position
	 */
	private void getAllTotesCenter()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				strafeRight();
				break;
			}
			
			case 2:
			{
				pickUp();
				break;
			}
			
			case 3:
			{
				strafeLeft();
				break;
			}
			
			case 4:
			{
				strafeLeft();
				break;
			}
			
			case 5:
			{
				pickUp();
				break;
			}
			
			case 6:
			{
				driveForward();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting rightmost starting position
	 */
	private void getAllTotesRight()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUp();
				break;
			}
			
			case 1:
			{
				strafeLeft();
				break;
			}
			
			case 2:
			{
				pickUp();
				break;
			}
			
			case 3:
			{
				strafeLeft();
				break;
			}
			
			case 4:
			{
				pickUp();
				break;
			}
			
			case 5:
			{
				driveForward();
				break;
			}
		}	
	}
	
	public void strafeRightBearing()
	{
		drive.setHeading(0, drive.getAngle(), Config.AutoPID.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
		{
			drive.setHeading(90, drive.getAngle(), Config.AutoPID.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance && Math.abs(drive.getRightEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance)
			{
				drive.setHeading(0, drive.getAngle(), -Config.AutoPID.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
					endRoutine();				
			}
		}
	}
	
//	public void strafeLeftBearing()
//	{
//		drive.setHeading(0, drive.getAngle(), Config.AutoPID.encStrafeDistance);
//		if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
//		{
//			drive.setHeading(270, drive.getAngle(), Config.AutoPID.encDistanceBetweenTotes);
//			
//			if(Math.abs(drive.getLeftEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance && Math.abs(drive.getRightEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance)
//			{
//				drive.setHeading(drive.getAngle(), -Config.AutoPID.encStrafeDistance);
//				
//				if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
//					endRoutine();				
//			}
//		}
//	}
	
	public void strafeLeft()
	{
		drive.setHeading(0, Config.AutoPID.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
		{
			drive.setHeading(270, Config.AutoPID.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance && Math.abs(drive.getRightEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance)
			{
				drive.setHeading(180, Config.AutoPID.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void strafeRight()
	{
		drive.setHeading(0,Config.AutoPID.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc() - Config.AutoPID.encStrafeDistance) < Config.AutoPID.encTolerance)
		{
			drive.setHeading(90, Config.AutoPID.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance && Math.abs(drive.getRightEnc() - Config.AutoPID.encDistanceBetweenTotes) < Config.AutoPID.encTolerance)
			{
				drive.setHeading(0, -Config.AutoPID.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.AutoPID.encTolerance && Math.abs(drive.getBackEnc()) < Config.AutoPID.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void pickUp()
	{	
		if(tmPickUp.get() < Config.AutoPID.timeIntakeClose)
		{
			claw.closeClaw();
		}
				
		else if(tmPickUp.get() < Config.AutoPID.timeElevatorStack)
		{
			elevator.setHeight(elevatorLevel);
			elevatorLevel++;
		}
		
		else if(tmPickUp.get() < Config.AutoPID.timeIntakeClose)
		{
			claw.openClaw();
		}
		
		else
			endRoutine();
	}
	
	public void endRoutine()
	{
		tmPickUp.stop();
		tmPickUp.reset();
		autoStep++;
	}
}
