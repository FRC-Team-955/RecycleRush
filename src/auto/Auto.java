package auto;

import edu.wpi.first.wpilibj.Timer;
import core.Drive;
import core.Elevator;
import core.Claw;
import core.Dashboard;
import util.Config;

public class Auto
{
	private Claw claw;
	private Elevator elevator;
	private Drive drive;
	private Dashboard dash;
	private Timer timer;
	private int autoId;
	private int autoStep = 0;
	
	public Auto(Claw newClaw, Elevator newElevator, Drive newDrive)
	{
		claw = newClaw;
		elevator = newElevator;
		drive = newDrive;
		timer = new Timer();
	}
	
	public void getAutoMode()
	{
		autoId = dash.getAutoType();
	}
	
	public void run()
	{
		switch(autoId)
		{
			case Config.Auto.idDoNothing:
			{
				doNothing();
				break;
			}
			
			case Config.Auto.idDriveForwardEnc:
			{
				driveForwardEnc();
				break;
			}
			
			case Config.Auto.idDriveForwardTimer:
			{
				driveForwardTimer();
				break;
			}

			case Config.Auto.idGetAllTotesEnc:
			{
				getAllTotesEnc();
				break;
			}
			
			case Config.Auto.idGetAllTotesTimer:
			{
				getAllTotesTimer();
				break;
			}
			
			case Config.Auto.idGetOneToteEnc:
			{
				getOneToteEnc();
				break;
			}
			
			case Config.Auto.idGetOneToteTimer:
			{
				getOneToteTimer();
				break;
			}
		}
	}
	
	private void doNothing()
	{
		
	}
	
	private void driveForwardEnc()
	{
			drive.setSpeed(Config.Auto.encDriveForwardDistance, Config.Auto.encDriveForwardDistance, 0, 0);
	}
	
	private void driveForwardTimer()
	{
		timer.start();
		
		if(timer.get() < Config.Auto.driveForwardTime)
			drive.setSpeed(Config.Auto.driveForwardSpeed, Config.Auto.driveForwardSpeed, 0, 0);
		else
		{
			drive.setSpeed(0, 0, 0, 0);
			timer.stop();
			timer.reset();
			autoStep++;
		}
	}
	
	private void getAllTotesEnc()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpToteEnc();
				break;
			}
			
			case 1:
			{
				strafeEnc();
				break;
			}
			
			case 2:
			{
				pickUpToteEnc();
				break;
			}
			
			case 3:
			{
				strafeEnc();
				break;
			}
			
			case 4:
			{
				pickUpToteEnc();
				break;
			}
			
			case 5:
			{
				drive.setSpeed(Config.Auto.encDriveForwardDistance, Config.Auto.encDriveForwardDistance, 0, 0);
				break;
			}
		}	
	}
	
	private void getAllTotesTimer()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpToteTimer();
				break;
			}
			
			case 1:
			{
				strafeTimer();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeTimer();
				break;
			}
			
			case 4:
			{
				pickUpToteTimer();
				break;
			}
			
			case 5:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	private void getOneToteEnc()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpToteEnc();
				break;
			}
			
			case 1:
			{
				strafeEnc();
				break;
			}
			
			case 2:
			{
				drive.setSpeed(Config.Auto.encDriveForwardDistance, Config.Auto.encDriveForwardDistance, 0, 0);
				break;
			}
		}
	}
	
	private void getOneToteTimer()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpToteTimer();
				break;
			}
			
			case 1:
			{
				strafeTimer();
				break;
			}
			
			case 2:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	public void pickUpToteEnc()
	{
		drive.setSpeed(Config.Auto.encDistanceForwardToTote, Config.Auto.encDistanceForwardToTote, 0, 0);
		if(drive.getLeftEncDist() == Config.Auto.encDistanceForwardToTote && drive.getRightEncDist() == Config.Auto.encDistanceForwardToTote)
		{
			claw.openClaw();
			claw.closeClaw();
			drive.setSpeed(0, 0, 0, 0);
			if(drive.getLeftEncDist() == 0 && drive.getRightEncDist() == 0)
					autoStep++;
		}
	}
	
	public void strafeEnc()
	{
		drive.setSpeed(0, 0, Config.Auto.encDistanceBetweenTotes, Config.Auto.encDistanceBetweenTotes);
		if(drive.getFrontEncDist() == Config.Auto.encDistanceBetweenTotes && drive.getBackEncDist() == Config.Auto.encDistanceBetweenTotes)
		{
				autoStep++;
				drive.encReset();
		}
	}
	
	public void pickUpToteTimer()
	{
		timer.start();
		
		if(timer.get() < Config.Auto.driveTowardToteTime)
			drive.setSpeed(Config.Auto.driveTowardToteSpeed, Config.Auto.driveTowardToteSpeed, 0, 0);
		else if(timer.get() < Config.Auto.intakeOpenTime)
			claw.openClaw();
		else if(timer.get() < Config.Auto.intakeCloseTime)
			claw.closeClaw();
		else if(timer.get() < Config.Auto.driveAwayToteTime)
			drive.setSpeed(-Config.Auto.driveTowardToteSpeed, -Config.Auto.driveTowardToteSpeed, 0, 0);
		else
		{
			drive.setSpeed(0, 0, 0, 0);
			timer.stop();
			timer.reset();
			autoStep++;
		}
	}
	
	public void strafeTimer()
	{
		timer.start();
		
		if(timer.get() < Config.Auto.strafeTime)
			drive.setSpeed(0, 0, Config.Auto.strafeSpeed, Config.Auto.strafeSpeed);
		else
		{
			drive.setSpeed(0, 0, 0, 0);
			timer.stop();
			timer.reset();
			autoStep++;
		}
	}
}
