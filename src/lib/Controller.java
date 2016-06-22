package lib;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 */
public class Controller extends Joystick 
{
	private final int maxButtons;
	private int chnLeftX = 0;
    private int chnLeftY = 1;
    private int chnRightX = 2;
    private int chnRightY = 3;
    private boolean flipLeftX = false;
    private boolean flipLeftY = false;
    private boolean flipRightX = false;
    private boolean flipRightY = false;
    private boolean[] lastButtonState;
    private boolean[] buttonState;
    
    /**
     * Constructor
     * @param portNum port number for the controller 
     */
    public Controller(int portNum, int maxButtons)
    {
        super(portNum);
        this.maxButtons = maxButtons;
        lastButtonState = new boolean[maxButtons];
        buttonState = new boolean[maxButtons];
        
        for(int i = 0; i < maxButtons; i++)
        {
            lastButtonState[i] = false;
            buttonState[i] = false;
        }
    }
    
    /**
     * Updates the button values for the controller
     */
    public void update()
    {   
        for(int i = 0; i < maxButtons; i++)
        {
            buttonState[i] = !lastButtonState[i] && super.getRawButton(i + 1);
            lastButtonState[i] = super.getRawButton(i + 1);
        }
    }
    
    public double[] getRTheta() {
		double x = this.getRawRightX()*-1;
		double y = this.getRawLeftY();
		
		double r = Math.sqrt((x * x) + (y * y));
		double theta = Math.atan2(y, x);

		double[] rTheta = { r, theta };
		
		return rTheta;
		
	}
    
    /**
     * Gives button value
     * @param button the button number on the controller
     * @return button value
     */
    public boolean getButton(int button)
    {
        return buttonState[button - 1];
    }
    
    /**
     * Gets the x value of the left joystick
     * @return the x value of the Left joystick
     */
    public double getRawLeftX()
    {
    	return super.getRawAxis(chnLeftX) * (flipLeftX ? -1 : 1);
    }
    
    /**
     * Gets the y value of the left joystick
     * @return the y value of the left joystick
     */
    public double getRawLeftY()
    {
    	return super.getRawAxis(chnLeftY) * (flipLeftY ? -1 : 1);
    }
    
    /**
     * Gets the x value of the right joystick
     * @return the x value of the right joystick
     */
    public double getRawRightX()
    {
    	return super.getRawAxis(chnRightX) * (flipRightX ? -1 : 1);
    }
    
    /**
     * Gets the y value of the right joystick
     * @return the y value of the right joystick
     */
    public double getRawRightY()
    {
    	return super.getRawAxis(chnRightY) * (flipRightY ? -1 : 1);
    }
    
    /**
     * Flips the left x coordinates
     * @param flip whether the coordinates need to be flipped
     */
    public void flipLeftX(boolean flip)
    {
    	flipLeftX = flip;
    }
    
    /**
     * Flips the left y coordinates
     * @param flip whether the coordinates need to be flipped
     */
    public void flipLeftY(boolean flip)
    {
    	flipLeftY = flip;
    }
    
    /**
     * Flips the right x coordinates
     * @param flip whether the coordinates need to be flipped
     */
    public void flipRightX(boolean flip)
    {
    	flipRightX = flip;
    }
    
    /**
     * Flips the right y coordinates
     * @param flip whether the coordinates need to be flipped
     */
    public void flipRightY(boolean flip)
    {
    	flipRightY = flip;
    }
    
    /**
     * Sets the channel of the left x stick
     * @param chn channel number
     */
    public void setChnLeftX(int chn)
    {
    	chnLeftX = chn;
    }
    
    /**
     * Sets the channel of the left y stick
     * @param chn channel number
     */
    public void setChnLeftY(int chn)
    {
    	chnLeftY = chn;
    }
    
    /**
     * Sets the channel of the right x stick
     * @param chn channel number
     */
    public void setChnRightX(int chn)
    {
    	chnRightX = chn;
    }
    
    /**
     * Sets the channel of the right y stick
     * @param chn channel number
     */
    public void setChnRightY(int chn)
    {
    	chnRightY = chn;
    }
    
    /**
     * Checks if the dpad is pressed in the up direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadUp()
    {
    	return super.getPOV(0) == 0;
    }
    
    /**
     * Checks if the dpad is pressed in the right direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadRight()
    {
    	return super.getPOV(0) == 90;
    }

    /**
     * Checks if the dpad is pressed in the down direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadDown()
    {
    	return super.getPOV(0) == 180;
    }
    
    /**
     * Checks if the dpad is pressed in the left direction 
     * @return is the dpad is pressed
     */
    public boolean getDpadLeft()
    {
    	return super.getPOV(0) == 270;
    }
}