package auto;

import edu.wpi.first.wpilibj.Timer;
import core.Drive;
import core.Dashboard;
import core.Claw;
import core.Elevator;
import lib.Config;

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
	
	/**
	 * Switches which auto routine you are doing 
	 * @param autoId the id of the routine you want to run
	 */
	public void run(int autoId)
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
				//drive.setTalonMode(true);
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
				//drive.setTalonMode(true);
				getAllTotesLeftEnc();
				break;
			}
			
			case Config.Auto.idGetAllTotesCenterEnc:
			{
				//drive.setTalonMode(true);
				getAllTotesCenterEnc();
				break;
			}
			
			case Config.Auto.idGetAllTotesRightEnc:
			{
				//drive.setTalonMode(true);
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
	
	/**
	 * Drives forward using encoder values
	 */
	private void driveForwardEnc()
	{
		//drive.setPos(0, 0, Config.Auto.encDriveForwardDistance, Config.Auto.encDriveForwardDistance);
	}
	
	/**
	 * Drives forward using timer values
	 */
	private void driveForwardTimer()
	{
		if(timer.get() < Config.Auto.timeDriveForward){
			drive.setSpeed(0, 0, Config.Auto.driveForwardSpeed, Config.Auto.driveForwardSpeed, true);
			System.out.println(timer.get());}
		
		else
		{
			drive.setSpeed(0, 0, 0, 0, false);
			autoStep++;
		}
	}
	
	/**
	 * Gets all totes from the starting leftmost starting position use encoder values
	 */
	private void getAllTotesLeftEnc()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				strafeRightEnc();
				break;
			}
			
			case 2:
			{
				pickUpTimer();
				break;
			}
			
			case 3:
			{
				strafeRightEnc();
				break;
			}
			
			case 4:
			{
				pickUpTimer();
				break;
			}
			
			case 5:
			{
				driveForwardEnc();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting center starting position use encoder values
	 */
	private void getAllTotesCenterEnc()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				strafeRightEnc();
				break;
			}
			
			case 2:
			{
				pickUpTimer();
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
				pickUpTimer();
				break;
			}
			
			case 6:
			{
				driveForwardEnc();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting rightmost starting position use encoder values
	 */
	private void getAllTotesRightEnc()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				strafeLeftEnc();
				break;
			}
			
			case 2:
			{
				pickUpTimer();
				break;
			}
			
			case 3:
			{
				strafeLeftEnc();
				break;
			}
			
			case 4:
			{
				pickUpTimer();
				break;
			}
			
			case 5:
			{
				driveForwardEnc();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting leftmost starting position use timer values
	 */
	private void getAllTotesLeftTimer()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				strafeRightTimer();
				break;
			}
			
			case 2:
			{
				pickUpTimer();
				break;
			}
			
			case 3:
			{
				strafeRightTimer();
				break;
			}
			
			case 4:
			{
				pickUpTimer();
				break;
			}
			
			case 5:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting center starting position use timer values
	 */
	private void getAllTotesCenterTimer()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				strafeLeftTimer();
				break;
			}
			
			case 2:
			{
				pickUpTimer();
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
				pickUpTimer();
				break;
			}
			
			case 6:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	/**
	 * Gets all totes from the starting rightmost starting position use timer values
	 */
	private void getAllTotesRightTimer()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				strafeLeftTimer();
				break;
			}
			
			case 2:
			{
				pickUpTimer();
				break;
			}
			
			case 3:
			{
				strafeLeftTimer();
				break;
			}
			
			case 4:
			{
				pickUpTimer();
				break;
			}
			
			case 5:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	/**
	 * Gets one tote then drives out of auto zone
	 */
	private void getOneToteTimer()
	{
		switch(autoStep)
		{
			case 0:
			{
				pickUpTimer();
				break;
			}
			
			case 1:
			{
				driveForwardTimer();
				break;
			}
		}	
	}
	
	/**
	 * STrafes right based on encoder values
	 */
	public void strafeRightEnc()
	{
//		drive.setPos(0, 0, Config.Auto.encStrafeDistance, Config.Auto.encStrafeDistance);
//		if(Math.abs(drive.getFrontEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
//		{
//			drive.setPos(Config.Auto.encDistanceBetweenTotes, Config.Auto.encDistanceBetweenTotes, Config.Auto.encStrafeDistance, Config.Auto.encStrafeDistance);
//			
//			if(Math.abs(drive.getLeftEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
//			{
//				drive.setPos(Config.Auto.encDistanceBetweenTotes, Config.Auto.encDistanceBetweenTotes, 0, 0);
//				
//				if((Math.abs(drive.getFrontEncDist()) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist()) < Config.Auto.encTolerance))
//					endRoutine();				
//			}
//		}
	}
	
	/**
	 * Strafes left based on encoder values
	 */
	public void strafeLeftEnc()
	{
//		drive.setPos(0, 0, -Config.Auto.encStrafeDistance, -Config.Auto.encStrafeDistance);
//		if(Math.abs(drive.getFrontEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist() - Config.Auto.encStrafeDistance) < Config.Auto.encTolerance)
//		{
//			drive.setPos(-Config.Auto.encDistanceBetweenTotes, -Config.Auto.encDistanceBetweenTotes, -Config.Auto.encStrafeDistance, -Config.Auto.encStrafeDistance);
//			
//			if(Math.abs(drive.getLeftEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance && Math.abs(drive.getRightEncDist() - Config.Auto.encDistanceBetweenTotes) < Config.Auto.encTolerance)
//			{
//				drive.setPos(-Config.Auto.encDistanceBetweenTotes, -Config.Auto.encDistanceBetweenTotes, 0, 0);
//				
//				if((Math.abs(drive.getFrontEncDist()) < Config.Auto.encTolerance && Math.abs(drive.getBackEncDist()) < Config.Auto.encTolerance))
//					endRoutine();				
//			}
//		}
	}
	
	/**
	 * Picks up an object
	 */
	public void pickUpTimer()
	{		
		System.out.println(timer.get());
		if(timer.get() < Config.Auto.timeIntakeOpen)
		{
			//claw.openClaw();
			System.out.println("in second if");
		}
				
		else if(timer.get() < Config.Auto.timeElevatorStack)
		{
			//elevator.setLevel(elevatorLevel);
			//elevatorLevel++;
		}
		
		else if(timer.get() < Config.Auto.timeIntakeClose)
		{
			System.out.println("in first if");
			//claw.closeClaw();
		}
		
		else
			endRoutine();
	}
	
	/**
	 * Strafes right based on timer values 
	 */
	public void strafeRightTimer()
	{
		if(timer.get() < Config.Auto.timeStrafe)
		{
			drive.setSpeed(0, 0, Config.Auto.strafeSpeed, Config.Auto.strafeSpeed, true);
		}
		
		else if(timer.get() < Config.Auto.timeDriveTowardTote)
		{
			System.out.println("in second else if");
			drive.setSpeed(Config.Auto.driveTowardToteSpeed, Config.Auto.driveTowardToteSpeed, 0, 0, true);
		}
		
		else if(timer.get() < Config.Auto.timeStrafeBackwards)
			drive.setSpeed(0, 0, -Config.Auto.strafeSpeed, -Config.Auto.strafeSpeed, true);
		
		else
			endRoutine();
	}
	
	/**
	 * Strafes left based on timer values
	 */
	public void strafeLeftTimer()
	{
		if(timer.get() < Config.Auto.timeStrafe)
		{
			drive.setSpeed(0, 0, Config.Auto.strafeSpeed, Config.Auto.strafeSpeed, true);
		}
		
		else if(timer.get() < Config.Auto.timeDriveTowardTote)
		{
			System.out.println("in second else if");
			drive.setSpeed(-Config.Auto.driveTowardToteSpeed, -Config.Auto.driveTowardToteSpeed, 0, 0, true);
		}
		
		else if(timer.get() < Config.Auto.timeStrafeBackwards)
			drive.setSpeed(0, 0, -Config.Auto.strafeSpeed, -Config.Auto.strafeSpeed, true);
		
		else
			endRoutine();
	}
	
	/**
	 * Resets the timer and auto step
	 */
	public void reset()
	{
		timer.stop();
		timer.reset();
		autoStep = 0;
	}
	
	/**
	 * Resets timer and encoder values and increases autoStep
	 */
	public void endRoutine()
	{
		timer.stop();
		timer.reset();
		timer.start();
		drive.encReset();
		autoStep++;
	}
}