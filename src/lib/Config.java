package lib;

import edu.wpi.first.wpilibj.DigitalSource;

/**
 *
 * @author Programming
 */
public class Config {

	public class ContrDrive 
	{
		public static final double linearity = 1.5;
		public static final int maxButtons = 10;

		// Channel for Joysticks
		public static final int chn = 0;
		public static final int chnLeftX = 0;
		public static final int chnLeftY = 1;
		public static final int chnRightX = 2;
		public static final int chnRightY = 3;
	}
	
	public class ContrXBox
	{
		public static final int btElevatorUp = 4;
		public static final int btElevatorUp2 = 2;
		public static final int btElevatorDown = 1;
		public static final int btClawToggle = 6;
		public static final int btSwitchPID = 1;
		public static final int btSwitchZero = 3;
	}
	
	public class Claw {
		public static final int chnSolOne = 6;
		public static final int chnSolTwo = 7;

//		public static final int btOpen = 5;
//		public static final int btClose = 6;
		public static final int btOpenClose = 6;
	}

	public class Elevator {
		public static final int btModeSwitch = 12;
		public static final int btUp = 1;
		public static final int btDown = 3;
		public static final int btBrake = 4;
		public static final int btUnbrake = 2;

		public static final int idMtElevatorOne = 3;
		public static final int idMtElevatorTwo = 1;
		public static final int chnLimitSwitchBottom = 1;
		public static final int chnLimitSwitchTop = 0;

		public static final double elevatorUpSpeed = -1;// .25;
		public static final double elevatorDownSpeed = .25;

		public static final int chnNoidOne = 4;
		public static final int chnNoidTwo = 5;
		
		public static final double motorBrakeTime = .5;
		public static final double motorBrakeSpeed = .15;

		// PID
//		public static final double kIncrease = -.006;
//		public static final double kSpeedLimit = -.05;

		// Minimum timer value to brake
		public static final double minTimerVal = .1;
		public static final double moveUpPrevTime = .1;

	}

	public class ElevatorEnc {
		public static final int btModeSwitch = 12;
		public static final int btLvlOne = 1;
		public static final int btLvlTwo = 2;
		public static final int btLvlThree = 3;
		public static final int btLvlFour = 4;
		public static final int btLvlFive = 5;
		public static final int btLvlSix = 6;

		public static final int idMtElevator = 8;
		public static final int chnLimitSwitchBottom = 1;
		public static final int chnLimitSwitchTop = 0;
		
		public static final int chnEncOne = 3;
		public static final int chnEncTwo = 2;
		public static final double distancePerPulse = 63.0 / 1350;

		// Measurments
		public static final double adjustedBaseHeight = 212;
		public static final double toteHeight = 130;
		
		/** PID constants **/
		// For up PID
		public static final double kUpP = .254;
		public static final double kUpI = 0.033; // Was .001
		public static final double kUpD = 0;
		// For down PID
		public static final double kDownP = 0;
		public static final double kDownI = 0;
		public static final double kDownD = 0;
	}

	public class FileSaver {
		public static final String saveDir = "/logFiles";

	}

	public class Drive {
		public static final int btFieldCentricMode = 9;
		public static final int btRobotCentricMode = 10;

		public static final int chnMtLeft = 1;
		public static final int chnMtRight = 3;

		// Access from roboRio
		public static final int idMtLeftCAN = 2;
		public static final int idMtRightCAN = 4;
		public static final int idMtFrontCAN = 5;
		public static final int idMtBackCAN = 6;
		
		// TODO Check values
		public static final int pdpChnMtLeft = 1;
		public static final int pdpChnMtLeftCAN = 2;
		public static final int pdpChnMtRight = 3;
		public static final int pdpChnMtRightCAN = 4;
		public static final int pdpChnMtFrontCAN = 5;
		public static final int pdpChnMtBackCAN = 6;

		// NavX
		public static final int navXBaudRate = 57600;
		public static final int chnNavX = 0;

		// Ids for SmartDashboard
		public static final int idFieldCentric = 0;
		public static final int idRobotCentric = 1;	

		public static final double minRightJoyValue = .025;
		public static final double minTurnSpeed = 0.025;
		public static final double minLeftJoyVal = 0.1;
		
		// Ramping
		public static final double maxRampRate = .254;
		public static final int leftRampId = 1;
		public static final int rightRampId = 1;
		public static final int frontRampId = 1;
		public static final int backRampId = 1;

		public static final double robotCentricStrafingScalar = 1;
		public static final double strafeAdjustment = .0234;
		
		// Encoder Channel Values
		public static final int chnFrontEncA = 10; //TODO get correct values
        public static final int chnFrontEncB = 11; //TODO get correct values
       
        public static final int chnBackEncA = 12; //TODO get correct values
        public static final int chnBackEncB = 13; //TODO get correct values
        
        public static final int chnLeftEncA = 14; //TODO get correct values
        public static final int chnLeftEncB = 15; //TODO get correct values
        
        public static final int chnRightEncA = 16; //TODO get correct values
        public static final int chnRightEncB = 17; //TODO get correct values
	}
}