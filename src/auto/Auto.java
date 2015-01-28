package auto;

import util.Config;
import core.Drive;
import core.Elevator;
import core.Claw;
import core.Dashboard;

public class Auto {
	private Claw claw;
	private Elevator elevator;
	private Drive drive;
	private Dashboard dash;
	private int autoId;
	
	public Auto(Claw newClaw, Elevator newElevator, Drive newDrive)
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
		
	}
	
	private void getAllTotesEnc()
	{
		
	}
	
	private void getAllTotesTimer()
	{
		
	}
	
	private void getOneToteEnc()
	{
		
	}
	
	private void getOneToteTimer()
	{
		
	}
}
