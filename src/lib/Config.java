package lib;

/**
 *
 * @author Programming
 */
public class Config 
{
	public class ContrDrive 
	{
		// Channels, max buttons
		public static final int maxButtons = 10;
		public static final int chn = 0;
		public static final int chnLeftX = 0;
		public static final int chnLeftY = 1;
		public static final int chnRightX = 2;
		public static final int chnRightY = 3;
		
		// Buttons for controller
		public static final int btFieldCentricMode = 9;
		public static final int btRobotCentricMode = 10;
	}
	
	public class ContrElevator
	{
		// Channels, max buttons
		public static final int maxButtons = 10;
		public static final int chn = 0;
		public static final int chnLeftX = 0;
		public static final int chnLeftY = 1;
		public static final int chnRightX = 2;
		public static final int chnRightY = 3;
		
		// Buttons for controller
		public static final int btClawToggle = 6;
		public static final int btEnableElevatorPID = 8;
		public static final int btDisableElevatorPID = 7;
		public static final int btElevatorUp = 4;
		public static final int btElevatorDown = 1;
		public static final int btLvlOne = 1;
		public static final int btLvlTwo = 2;
		public static final int btLvlThree = 4;
		public static final int btLvlFour = 3;
		//public static final int btLvlFive = 5;
		//public static final int btLvlSix = 6;
		public static final int btToggleBrake = 5;
		// TODO: WE'RE NOT ACTUALLY USING THESE YET
//		public static final int btModeSwitch = 12;
//		public static final int btUp = 1;
//		public static final int btDown = 3;
//		public static final int btBrake = 4;
//		public static final int btUnbrake = 2;
	}
	
	public class Claw 
	{
		// Channels for claw solenoids
		public static final int chnSolOne = 6;
		public static final int chnSolTwo = 7;
	}

	public class Elevator
	{
		// CAN Talons ids, access from roborio online
		public static final int idMtElevatorOne = 3;
		public static final int idMtElevatorTwo = 1;
		
		// TODO Check values
		public static final int pdpChnMtElevatorOneCAN = 1;
		public static final int pdpChnMtElevatorTwoCAN = 2;
				
		// Limit switch channels
		public static final int chnLimitSwitchTop = 0;
		public static final int chnLimitSwitchBottom = 1;

		// Elevator speeds for manual control, -value = up on elevator
		public static final double elevatorUpSpeed = 1;
		public static final double elevatorDownSpeed = -.25;

		// Brake solenoid channels
		public static final int chnNoidOne = 4;
		public static final int chnNoidTwo = 5;
		
		// Encoder channels
		public static final int chnEncA = 3;
		public static final int chnEncB = 2;
		
		// Encoder distance per pulse
		public static final double distancePerPulse = 63.0 / 1350;   
		
		/** PID constants **/
		// For up PID
		public static final double kUpP = 0.33;
		public static final double kUpI = 0.01;
		public static final double kUpD = 0;
		
		// For down PID
		public static final double kDownP = 0;
		public static final double kDownI = 0;
		public static final double kDownD = 0;
		
		// Timer constants values, seconds
		public static final double brakeDisengageTime = .04;	// Minimum timer value to for brake to disengage seconds
		public static final double maxEncStallTime = 0.5;
		public static final double maxRampRate = 0.254;
		
		// Height constants values, inches
		public static final double maxHeightDiff = .5;
		public static final double adjustedBaseHeight = 2;
		public static final double toteHeight = 13;		   
		public static final double toteClearanceHeight= 1; 
				
		// Minimum encoder rate for elevator to be able to be braked without damage occurring
		public static final double minEncRate = .5;
	}

	public class FileSaver 
	{
		// Save directory for all the log files
		public static final String saveDir = "/logFiles";
	}

	public class Drive
	{
		// REG Talons channels
		public static final int chnMtLeft = 1;
		public static final int chnMtRight = 3;

		// CAN Talons ids, access from roborio online
		public static final int idMtLeft = 2;
		public static final int idMtRight = 4;
		public static final int idMtFront = 5;
		public static final int idMtBack = 6;
		
		// TODO Check values
		public static final int pdpChnMtLeft = 1;
		public static final int pdpChnMtLeftCAN = 2;
		public static final int pdpChnMtRight = 3;
		public static final int pdpChnMtRightCAN = 4;
		public static final int pdpChnMtFrontCAN = 5;
		public static final int pdpChnMtBackCAN = 6;

		// Ids for SmartDashboard
		public static final int idFieldCentric = 0;
		public static final int idRobotCentric = 1;	

		// Minimum values for joysticks
		public static final double minTurnSpeed = 0.025;
		public static final double minLeftJoyMag = 0.1;
		
		// Ramping
		public static final double maxRampRate = .254;
		
		// Encoder Channel Values
		public static final int chnEncFrontA = 10; //TODO get correct values
        public static final int chnEncFrontB = 11; //TODO get correct values
       
        public static final int chnEncBackA = 12; //TODO get correct values
        public static final int chnEncBackB = 13; //TODO get correct values
        
        public static final int chnEncLeftA = 14; //TODO get correct values
        public static final int chnEncLeftB = 15; //TODO get correct values
        
        public static final int chnEncRightA = 16; // TODO get correct values
        public static final int chnEncRightB = 17; // TODO get correct values
        
        // PID constansts
        public static final double kStrafeP = 0.0234;
        public static final double kStrafeI = 0;
        public static final double kStrafeD = 0;
	}
	
	public class NavX
	{
		// Update rate for navX, baud rate for navX
		public static final byte updateRateHz = 50;
		public static final int baudRate = 57600;
	}
}