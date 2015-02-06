package util;

/**
 *
 * @author Programming
 */
public class Config
{
	public class Auto
	{
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
		
		public static final double encStrafeDistance = 2;
		public static final double encDistanceBetweenTotes = 2;
		public static final double encDriveForwardDistance = 2;
		
		public static final double timeIntakeOpen = .5;
		public static final double timeIntakeClose = timeIntakeOpen + .5;
		public static final double timeStrafe = .75;
		public static final double timeDriveTowardTote = timeStrafe + 1;
		public static final double timeStrafeBackwards = timeDriveTowardTote + .75;
		public static final double timeDriveForward = 1;
		public static final double timeElevatorStack = .5;
		
		public static final double driveForwardSpeed = 1;
		public static final double strafeSpeed = 1;
		public static final double driveTowardToteSpeed = .5;
		
		public static final double encTolerance = 10;
	}
	
    public class ContrDrive
    {
    	public static final double linearity = 1.5;
        public static final int maxButtons = 12;
        public static final int chn = 0;
        public static final int chnLeftX = 0;
        public static final int chnLeftY = 1;
        public static final int chnRightX = 2;
        public static final int chnRightY = 3;
    }
    
    public class Claw
    {
    	public static final int chnSolOne = 5;
    	public static final int chnSolTwo = 6;
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
        public static final double adjustedBaseHeight = 212;
        public static final double toteHeight = 130;
    }

    public class Drive 
    {
        public static final int chnMtLeftOne = 6;
        public static final int chnMtLeftTwo = 7;
        public static final int chnMtRightOne = 2;
        public static final int chnMtRightTwo = 3;
        public static final int chnMtFront = 4;
        public static final int chnMtBack = 5;
        public static final int chnNavX = 0;
        public static final int chnFrontEncA = 5;
        public static final int chnFrontEncB = 5;
        public static final int chnBackEncA = 5;
        public static final int chnBackEncB = 5;
        public static final int chnLeftEncA = 5;
        public static final int chnLeftEncB = 5;
        public static final int chnRightEncA = 5;
        public static final int chnRightEncB = 5; 
        public static final double tolerance = .01;
        public static final int navXBaudRate = 57600;
    }
    
    public class CameraFeed
    {
    	public static final int imgQuality = 60;
    	public static final int btCamCenter = 1;
    	public static final int btCamRight = 2;
    }
}