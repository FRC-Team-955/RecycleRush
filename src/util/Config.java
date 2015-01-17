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