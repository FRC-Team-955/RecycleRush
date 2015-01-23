package util;

/**
 *
 * @author Programming
 */
public class Config
{
    public class contrDrive
    {
    	public static final double linearity = 1.5;
        public static final int maxButtons = 12;
        public static final int chn = 0;
        public static final int chnLeftX = 0;
        public static final int chnLeftY = 1;
        public static final int chnRightX = 2;
        public static final int chnRightY = 3;
        public static final int elevatorUp = 5;
        public static final int elevatorDown = 6;
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
    	public static final double encDistance = 5;
    	public static final double ElevatorSpeed = .7;
        public static final int adjustedBaseHeight = 212;
        public static final int toteHeight = 130;
    }

    public class Drive 
    {
        public static final int chnMtLeftOne = 6;
        public static final int chnMtLeftTwo = 7;
        public static final int chnMtRightOne = 2;
        public static final int chnMtRightTwo = 3;
        public static final int chnMtFront = 4;
        public static final int chnMtBack = 5;
        public static final int chnGyro = 0;
        public static final double gyroAngOffset = 0;
        public static final double tolerance = .01;
        public static final double negInteriaAdjust = 1;
    }  
}