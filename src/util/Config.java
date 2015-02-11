package util;

/**
 *
 * @author Programming
 */
public class Config
{
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
	
    public class ContrDrive
    {
    	public static final double linearity = 1.5;
        public static final int maxButtons = 12;
        
        // Channel for Joysticks
        public static final int chn = 0;
        public static final int chnLeftX = 0;
        public static final int chnLeftY = 1;
        public static final int chnRightX = 2;
        public static final int chnRightY = 3;
    }
    
    public class Claw
    {
    	public static final int chnSolOne = 0;
    	public static final int chnSolTwo = 1;
    	
    	public static final int btOpen = 3;
    	public static final int btClose = 4;
    }
    
    public class Elevator
    {
    	public static final int btModeSwitch = 12;
    	public static final int btLvlOne = 1;
    	public static final int btLvlTwo = 2;
    	public static final int btLvlThree = 3;
    	public static final int btLvlFour = 4;
    	public static final int btLvlFive = 5;
    	public static final int btLvlSix = 6;
    	
    	public static final int chnMtElevator = 8;
    	public static final int chnLimitSwitchBottom = 1;
    	public static final int chnLimitSwitchTop = 9;
    	public static final int chnEncOne = 0;
    	public static final int chnEncTwo = 7;
    	
    	// Measurments
        public static final double adjustedBaseHeight = 212;
        public static final double toteHeight = 130;
    }

    public class Drive 
    {
    	public static final int chnMtLeftOne = 1;
        public static final int chnMtLeftTwo = 2;
        public static final int chnMtRightOne = 3;
        public static final int chnMtRightTwo = 4;
        public static final int chnMtFront = 5;
        public static final int chnMtBack = 6;
        
        public static final int chnNavX = 0;
        
        public static final int chnFrontEncA = 10;
        public static final int chnFrontEncB = 11;
        public static final int chnBackEncA = 12;
        public static final int chnBackEncB = 13;
        public static final int chnLeftEncA = 14;
        public static final int chnLeftEncB = 15;
        public static final int chnRightEncA = 16;
        public static final int chnRightEncB = 17; 
        
        // NavX 
        public static final int navXBaudRate = 57600;
        
        // Ids for SmartDashboard
        public static final int idFieldCentric = 0;
        public static final int idRobotCentric = 1;
        
        public static final int idTalonSRX = 0;
        public static final int idTalon = 1;
        
        public static final double minRightJoyValue = .05;
        
        public static final double robotCentricTurningScalar = 1;
        public static final double turnAdjustment = .02;
    }
    
    public class CameraFeed
    {
    	public static final int btCamCenter = 1;
    	public static final int btCamRight = 2;
    	
    	// Resolution
    	public static final int imgQuality = 60;
    	
    	// Camera Names
    	public static final String camNameCenter = "cam0";
    	public static final String camNameRight = "cam2";    
    }
}
