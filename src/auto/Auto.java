package auto;

import edu.wpi.first.wpilibj.Timer;
import core.Drive;
import core.Dashboard;
import core.Claw;
import core.Elevator;
import util.Config;

public class Auto
{
	private Claw claw;
	private Elevator elevator;
	private Drive drive;
	private Dashboard dash;
	private Timer timer = new Timer();
	
	private int autoId;
	private int autoStep = 0;
	private int elevatorLevel = 1;
	
	public Auto(Drive newDrive, Claw newClaw, Elevator newElevator)
	{
		claw = newClaw;
		elevator = newElevator;
		drive = newDrive;
	}
	
	public void getAutoMode()
	{
		autoId = dash.getAutoType();
	}
	
	public void run()
	{
		if(autoStep == 0)
			timer.start();

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

			case Config.Auto.idGetAllTotesLeftEnc:
			{
				getAllTotesLeftEnc();
				break;
			}
			
			case Config.Auto.idGetAllTotesCenterEnc:
			{
				getAllTotesCenterEnc();
				break;
			}
			
			case Config.Auto.idGetAllTotesRightEnc:
			{
				getAllTotesRightEnc();
				break;
			}
			
			case Config.Auto.idGetAllTotesLeftTimer:
			{
				getAllTotesLeftTimer();
				break;
			}
			
			case Config.Auto.idGetAllTotesCenterTimer:
			{
				getAllTotesCenterTimer();
				break;
			}
			
			case Config.Auto.idGetAllTotesRightTimer:
			{
				getAllTotesRightTimer();
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
		drive.setPos(0, 0, Config.Auto.encDriveForwardDistance, Config.Auto.encDriveForwardDistance);
	}
	
	private void driveForwardTimer()
	{
		if(timer.get() < Config.Auto.timeDriveForward)
			drive.setSpeed(Config.Auto.driveForwardSpeed, Config.Auto.driveForwardSpeed, 0, 0);
		
		else if(timer.get() >= Config.Auto.timeDriveForward)
		{
			drive.setSpeed(0, 0, 0, 0);
			autoStep++;
		}
	}
	
	private void getAllTotesLeftEnc()
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
				strafeRightEnc();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeRightEnc();
				break;
			}
			
			case 4:
			{
				pickUpToteTimer();
				break;
			}
			
			case 5:
			{
				driveForwardEnc();
				break;
			}
		}	
	}
	
	private void getAllTotesCenterEnc()
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
				strafeRightEnc();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeLeftEnc();
				break;
			}
			
			case 4:
			{
				strafeLeftEnc();
				break;
			}
			
			case 5:
			{
				pickUpToteTimer();
				break;
			}
			
			case 6:
			{
				driveForwardEnc();
				break;
			}
		}	
	}
	
	private void getAllTotesRightEnc()
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
				strafeLeftEnc();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeLeftEnc();
				break;
			}
			
			case 4:
			{
				pickUpToteTimer();
				break;
			}
			
			case 5:
			{
				driveForwardEnc();
				break;
			}
		}	
	}
	
	private void getAllTotesLeftTimer()
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
				strafeRightTimer();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeRightTimer();
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
	
	private void getAllTotesCenterTimer()
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
				strafeLeftTimer();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeRightTimer();
				break;
			}
			
			case 4:
			{
				strafeRightTimer();
				break;
			}
			
			case 5:
			{
				pickUpToteTimer();
				break;
			}
			
			case 6:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	private void getAllTotesRightTimer()
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
				strafeLeftTimer();
				break;
			}
			
			case 2:
			{
				pickUpToteTimer();
				break;
			}
			
			case 3:
			{
				strafeLeftTimer();
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
				driveForwardTimer();
				break;
			}
		}	
	}
	
	
	public void strafeRightEnc()
	{
		drive.setPos(0, 0, Config.Auto.encStrafeDistance, Config.Auto.encStrafeDistance);
		if(Math.abs(drive.getFrontEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
		{
			drive.setPos(Config.Auto.encDistanceBetweenTotes, Config.Auto.encDistanceBetweenTotes, Config.Auto.encStrafeDistance, Config.Auto.encStrafeDistance);
			
			if(Math.abs(drive.getLeftEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
			{
				drive.setPos(Config.Auto.encDistanceBetweenTotes, Config.Auto.encDistanceBetweenTotes, 0, 0);
				
				if((Math.abs(drive.getFrontEncDist()) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist()) < Config.Auto.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void strafeLeftEnc()
	{
		drive.setPos(0, 0, -Config.Auto.encStrafeDistance, -Config.Auto.encStrafeDistance);
		if(Math.abs(drive.getFrontEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
		{
			drive.setPos(-Config.Auto.encDistanceBetweenTotes, -Config.Auto.encDistanceBetweenTotes, -Config.Auto.encStrafeDistance, -Config.Auto.encStrafeDistance);
			
			if(Math.abs(drive.getLeftEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
			{
				drive.setPos(-Config.Auto.encDistanceBetweenTotes, -Config.Auto.encDistanceBetweenTotes, 0, 0);
				
				if((Math.abs(drive.getFrontEncDist()) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist()) < Config.Auto.encTolerance))
					endRoutine();				
			}
		}
	}
	
	public void pickUpToteTimer()
	{		
		System.out.println(timer.get());
		if(timer.get() < Config.Auto.timeIntakeClose)
		{
			System.out.println("in first if");
			claw.closeClaw();
		}
		
		else if(timer.get() < Config.Auto.timeIntakeOpen)
		{
			claw.openClaw();
			System.out.println("in second if");
		}
		
		else if(timer.get() < Config.Auto.timeElevatorStack)
		{
			elevator.setLevel(elevatorLevel);
			elevatorLevel++;
		}
		
		else
			endRoutine();
	}
	
	public void strafeRightTimer()
	{
		if(timer.get() < Config.Auto.timeStrafe)
		{
			drive.setSpeed(0, 0, Config.Auto.strafeSpeed, Config.Auto.strafeSpeed);
		}
		
		else if(timer.get() < Config.Auto.timeDriveTowardTote)
		{
			System.out.println("in second else if");
			drive.setSpeed(Config.Auto.driveTowardToteSpeed, Config.Auto.driveTowardToteSpeed, 0, 0);
		}
		
		else if(timer.get() < Config.Auto.timeStrafeBackwards)
			drive.setSpeed(0, 0, -Config.Auto.strafeSpeed, -Config.Auto.strafeSpeed);
		
		else
			endRoutine();
	}
	
	public void strafeLeftTimer()
	{
		if(timer.get() < Config.Auto.timeStrafe)
		{
			drive.setSpeed(0, 0, Config.Auto.strafeSpeed, Config.Auto.strafeSpeed);
		}
		
		else if(timer.get() < Config.Auto.timeDriveTowardTote)
		{
			System.out.println("in second else if");
			drive.setSpeed(-Config.Auto.driveTowardToteSpeed, -Config.Auto.driveTowardToteSpeed, 0, 0);
		}
		
		else if(timer.get() < Config.Auto.timeStrafeBackwards)
			drive.setSpeed(0, 0, -Config.Auto.strafeSpeed, -Config.Auto.strafeSpeed);
		
		else
			endRoutine();
	}
	
	public void reset()
	{
		timer.stop();
		timer.reset();
		autoStep = 0;
	}
	
	public void endRoutine()
	{
		timer.reset();
		timer.start();
		drive.encReset();
		autoStep++;
	}
}
