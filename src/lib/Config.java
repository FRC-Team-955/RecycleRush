package lib;

/**
 *
 * @author Programming
 */
public class Config 
{
	public class Sockets
	{
		public static final int port = 5805;
		public static final String hostName = "10.9.55.3";
		public static final double distanceOffset = 850;
		public static final double wantHeight = 1000;
		public static final double maxHeight = 2000;
		public static final double distanceMultiplier = 0.1;
	}
	
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
		public static final int btToggleSlowMode = 8;
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
		public static final int btLvlHalf = 10; //TODO FINISH THIS
//		public static final int btLvlOne = 3;    For Silver
//		public static final int btLvlTwo = 2;
//		public static final int btLvlThree = 1;
//		public static final int btLvlFour = 4;
		public static final int btLvlFive = 12;
		public static final int btLvlSix = 11;
		public static final int btToggleBrake = 7;
		public static final int btDropOff = 11;
		public static final int btAlignClawToggle = 5;
		
		// For Red Rock Candy Contr
		public static final int btLvlOne = 2;
		public static final int btLvlTwo = 3;
		public static final int btLvlThree = 4;
		public static final int btLvlFour = 1;
		
		// TODO: WE'RE NOT ACTUALLY USING THESE YET
//		public static final int btModeSwitch = 12;
//		public static final int btUp = 1;
//		public static final int btDown = 3;
//		public static vzfinal int btBrake = 4;
//		public static final int btUnbrake = 2;
	}
	
	public class Claw 
	{
		// Channels for claw solenoids
		public static final int chnSolOne = 6;
		public static final int chnSolTwo = 7;
		public static final int chnAlignClawSolOne = 2;
		public static final int chnAlignClawSolTwo = 3;
	}

	public class Elevator
	{
		// CAN Talons ids, access from roborio online
		public static final int idMtElevatorOne = 5;
		public static final int idMtElevatorTwo = 6;

		// Elevator speeds for manual control, -value = up on elevator
		public static final double elevatorUpSpeed = 1;
		public static final double elevatorDownSpeed = -.25;

		// Brake solenoid channels
		public static final int chnNoidOne = 6;
		public static final int chnNoidTwo = 7;
		
		// Encoder channels
		public static final int chnEncA = 3;
		public static final int chnEncB = 2;
		
		// Encoder distance per pulse
		public static final double distancePerPulse = 63.0 / (1350 * 25.4);   
		
		/** PID constants **/
		// For up PID
		public static final double kUpP = 0.0033;
		public static final double kUpI = 0.0;
		public static final double kUpD = 0;
		
		// For de2own PID
		public static final double kDownP = 0.0033;
		public static final double kDownI = 0.0;
		public static final double kDownD = 0;    // 0.0254;
		
		// PID max and min Errors
		public static final double minErrorSum = -6.25; // TODO Find correct value
		public static final double maxErrorSum = 12.5; // Was 625 before the pid deltaT placement change
		
		// Timer constants values, seconds
		public static final double brakeDisengageTime = .02;	// Minimum timer value to for brake to disengage seconds
		public static final double maxEncStallTime = 0.5;
		public static final double maxRampRate = 0.254;
		public static final double minElevatorSpeed = -1;
		public static final double maxElevatorSpeed = 1;
		
		// Height constants values, inches
		public static final double maxHeightDiff = .420; // .3 Blaze it
		public static final double maxHeight = 60;
		public static final double toteHeight = 12.75;		   
		public static final double clearanceHeight = 2;
		public static final double toteLossHeight = 1/2.54;
		public static final double maxElevatorHeight = 54;    // Highest we ever set it
		public static final double maxElevatorHeightLimit = 56; // Hard Stop, if something goes wrong
		public static final double halfToteHeight = 6;
				
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
		public static final int maxToteLevel = 6;
	}

	public class Drive
	{
		// CAN Talons ids, access from roborio online
		public static final int leftC1Chn = 2;
		public static final int leftC2Chn = 1;
		public static final int rightC1Chn = 4;
		public static final int rightC2Chn = 3;
        
		public static final boolean leftC1IsFlipped = false;
		public static final boolean leftC2IsFlipped = false;
		public static final boolean rightC1IsFlipped = false;
		public static final boolean rightC2IsFlipped = false;
	}		
}