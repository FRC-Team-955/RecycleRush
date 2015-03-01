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
			case Config.Auto.idDoNothing:
			{
				doNothing();
				break;
			}
			
			case Config.Auto.idDriveForwardBearing:
			{
				//drive.setTalonMode(true);
				driveForwardBearing();
				break;
			}		
			
			case Config.Auto.idGetOneToteBearing:
			{
				getOneToteBearng();
				break;
			}

			case Config.Auto.idGetAllTotesBearingLeft:
			{
				//drive.setTalonMode(true);
				getAllTotesBearingLeft();
				break;
			}
			
			case Config.Auto.idGetAllTotesBearingCenter:
			{
				//drive.setTalonMode(true);
				getAllTotesBearingCenter();
				break;
			}
			
			case Config.Auto.idGetAllTotesBearingRight:
			{
				//drive.setTalonMode(true);
				getAllTotesBearingRight();
				break;
			}
		}
	}
	
	public void doNothing()
	{
		
	}
	
	public void driveForwardBearing()
	{
		drive.setHeading(0, drive.getAngle(), Config.Auto.encDriveForwardDistance);
	}
	
	public void getOneToteBearng()
	{
		
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
	
	public void strafeRightBearing()
	{
		drive.setHeading(0, drive.getAngle(), Config.Auto.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
		{
			drive.setHeading(90, drive.getAngle(), Config.Auto.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
			{
				drive.setHeading(0, drive.getAngle(), -Config.Auto.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc()) < Config.Auto.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void strafeLeftBearing()
	{
		drive.setHeading(0, drive.getAngle(), Config.Auto.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
		{
			drive.setHeading(270, drive.getAngle(), Config.Auto.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
			{
				drive.setHeading(0, drive.getAngle(), -Config.Auto.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc()) < Config.Auto.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void strafeLeft()
	{
		drive.setHeading(0, Config.Auto.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
		{
			drive.setHeading(270, Config.Auto.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
			{
				drive.setHeading(0, -Config.Auto.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc()) < Config.Auto.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void strafeRight()
	{
		drive.setHeading(0,Config.Auto.encStrafeDistance);
		if(Math.abs(drive.getFrontEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
		{
			drive.setHeading(90, Config.Auto.encDistanceBetweenTotes);
			
			if(Math.abs(drive.getLeftEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEnc() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
			{
				drive.setHeading(0, -Config.Auto.encStrafeDistance);
				
				if((Math.abs(drive.getFrontEnc()) < Config.Auto.encTolerance && Math.abs(drive.getBackEnc()) < Config.Auto.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void pickUp()
	{	
		if(tmPickUp.get() < Config.Auto.timeIntakeClose)
		{
			claw.closeClaw();
		}
				
		else if(tmPickUp.get() < Config.Auto.timeElevatorStack)
		{
			elevator.setHeight(elevatorLevel);
			elevatorLevel++;
		}
		
		else if(tmPickUp.get() < Config.Auto.timeIntakeClose)
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
