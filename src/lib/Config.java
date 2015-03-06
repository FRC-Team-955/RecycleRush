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
		public static final int maxButtons = 12;
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
		public static final int maxButtons = 12;
		public static final int chn = 0;
		public static final int chnLeftX = 0;
		public static final int chnLeftY = 1;
		public static final int chnRightX = 2;
		public static final int chnRightY = 3;
		
		// XBox Buttons for controller
//		public static final int btClawToggle = 6;
//		public static final int btElevatorUp = 4;
//		public static final int btElevatorDown = 1;
//		public static final int btLvlOne = 1;
//		public static final int btLvlTwo = 2;
//		public static final int btLvlThree = 4;
//		public static final int btLvlFour = 3;
//		public static final int btLvlFive = 10;
		//public static final int btLvlSix = 6;
//		public static final int btToggleBrake = 5;
//		public static final int btDropOff = 9;
		
		// PS3 Buttons
		public static final int btClawToggle = 6;
		public static final int btElevatorUp = 4;
		public static final int btElevatorDown = 1;
		public static final int btLvlOne = 3;
		public static final int btLvlTwo = 2;
		public static final int btLvlThree = 1;
		public static final int btLvlFour = 4;
		public static final int btLvlFive = 12;
		//public static final int btLvlSix = 6;
		public static final int btToggleBrake = 5;
		public static final int btDropOff = 11;
		
		// TODO: WE'RE NOT ACTUALLY USING THESE YET
//		public static final int btModeSwitch = 12;
//		public static final int btUp = 1;
//		public static final int btDown = 3;
//		public static final int btBrake = 4;
//		public static final int btUnbrake = 2;
	}
	
	public class AutoPID
	{
		// Ids for SmartDashboard chooser
		public static final int idDoNothing = 0;
		public static final int idStackAllTotesLeft = 1;
		public static final int idToAutoZone = 2;
		
//		public static final int idDriveForward = 1;
//		public static final int idDriveForwardBearing= 2;
//		public static final int idGetOneTote = 3;
//		public static final int idGetOneToteBearing = 4;
//		public static final int idGetAllTotesLeft = 5;
//		public static final int idGetAllTotesCenter = 6;
//		public static final int idGetAllTotesRight = 7;
//		public static final int idGetAllTotesBearingLeft = 8;
//		public static final int idGetAllTotesBearingCenter = 9;
//		public static final int idGetAllTotesBearingRight = 10;
//		
		// TODO: Actual set this values, these aren't legit
		// Encoder distance values, inches
		public static final double distInFrontOfBin = 5;
		public static final double distToNextTote = 10;
		public static final double distToAutoZone = 10;
		public static final double distToClearStackedTotes = 2;
//		public static final double encStrafeDistance = 2;
//		public static final double encDistanceBetweenTotes = 2;
//		public static final double encDriveForwardDistance = 2;
		
//		public static final double encTolerance = 10;
		
		// Timer values
//		public static final double timeIntakeClose = .5; 
//		public static final double timeElevatorStack = timeIntakeClose + 1;
//		public static final double timeIntakeOpen = timeElevatorStack + .5;
	}
	
	public class Auto
	{
		// Ids for SmartDashboard chooser
		public static final int idDoNothing = 0;
		public static final int idDriveForwardTimer = 1;
		public static final int idDriveForwardEnc = 2;
		public static final int idGetOneToteTimer = 3;
		public static final int idGetOneToteEnc = 4;
		public static final int idGetAllTotesLeftTimer = 5;
		public static final int idGetAllTotesCenterTimer = 6;
		public static final int idGetAllTotesRightTimer = 7;
		public static final int idGetAllTotesLeftEnc = 8;
		public static final int idGetAllTotesCenterEnc = 9;
		public static final int idGetAllTotesRightEnc = 10;
		public static final int idGetRecycleBinEnc = 11;
		public static final int idGetRecycleBinTimer = 12;
		
		// Encoder distance values
		public static final double encStrafeDistance = 2;
		public static final double encDistanceBetweenTotes = 2;
		public static final double encDriveForwardDistance = 2;
		
		public static final double encTolerance = 10;
		
		// Timer values
		public static final double timeIntakeOpen = .5;
		public static final double timeIntakeClose = timeIntakeOpen + .5;
		public static final double timeStrafe = .75;
		public static final double timeDriveTowardTote = timeStrafe + 1;
		public static final double timeStrafeBackwards = timeDriveTowardTote + .75;
		public static final double timeDriveForward = 1;
		public static final double timeElevatorStack = .5;
		
		// Speeds for timerbased Auto
		public static final double driveForwardSpeed = 1;
		public static final double strafeSpeed = 1;
		public static final double driveTowardToteSpeed = .5;
		
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
		public static final double kDownP = 0.05;
		public static final double kDownI = 0.0;
		public static final double kDownD = 0.0254;
		
		// PID max and min Errors
		public static final double minErrorSum = -100; // TODO Find correct value
		public static final double maxErrorSum = 12.5; // Was 625 before the pid deltaT placement change
		
		// Timer constants values, seconds
		public static final double brakeDisengageTime = .02;	// Minimum timer value to for brake to disengage seconds
		public static final double maxEncStallTime = 0.5;
		public static final double maxRampRate = 0.254;
		public static final double minElevatorSpeed = -1;
		public static final double maxElevatorSpeed = 1;
		
		// Height constants values, inches
		public static final double maxHeightDiff = .420; // .3 Blaze it
		public static final double toteHeight = 13;		   
		public static final double toteClearanceHeight = 2;
		public static final double toteLossHeight = 1;
				
		// Minimum encoder rate for elevator to be able to be braked without damage occurring
		public static final double minEncRate = .6; // .5
		
		// Height modes for elevator
		public static final int heightTypeGround = 0;
		public static final int heightTypeStep = 1;
		public static final int heightTypeScoring = 2;
		
		// Base height offset for elevator, inches
		public static final double baseHeightGround = 0;
		public static final double baseHeightStep = 6.25;
		public static final double baseHeightScoring = 2;
		
		// Min/Max tote level for the elevator
		public static final int minToteLevel = 1;
		public static final int maxToteLevel = 7;
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
		public static final double minTurnJoyVal = 0.025;
		public static final double minLeftJoyMag = 0.1;
		
		// Ramping
		public static final double rampSideRate = .5; // was .254
		public static final double rampCenterRate = .254;
		
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
        public static final double kStrafeP = 0.0190;
        public static final double kStrafeI = 0;
        public static final double kStrafeD = 0;
        
        public static final double kLeftP = 0;
        public static final double kLeftI = 0;
        public static final double kLeftD = 0;
        
        public static final double kRightP = 0;
        public static final double kRightI = 0;
        public static final double kRightD = 0;
        
        public static final double kFrontP = 0;
        public static final double kFrontI = 0;
        public static final double kFrontD = 0;
        
        public static final double kBackP = 0;
        public static final double kBackI = 0;
        public static final double kBackD = 0;
        
        public static final double robotCircumfrence = Math.PI * 48; // Inches
        public static final double maxDistanceDiff = 1;              // Inches
        
        // Turn Speed Adjustments
        public static final double minTurnSpeed = -.5;
        public static final double maxTurnSpeed = .5;
        public static final double turnRampRate = .254;
	}
	
	public class NavX
	{
		// Update rate for navX, baud rate for navX
		public static final byte updateRateHz = 50;
		public static final int baudRate = 57600;
	}
}
