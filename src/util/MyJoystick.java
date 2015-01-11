package util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Adjusts joystick value to accommodate the refresh time of the program
 * @author Seraj B
 */
public class MyJoystick extends Joystick 
{  
	private final int maxButtons;
    private boolean[] lastButtonState;
    private boolean[] buttonState;
    private boolean flipLeftStickX = false;
    private boolean flipLeftStickY = false;
    private boolean flipRightStickX = false;
    private boolean flipRightStickY = false;
    
    /**
     * Constructor
     * @param portNum port number for the joystick 
     */
    public MyJoystick(int portNum, int numberOfButtons, boolean leftStickXFlip, boolean leftStickYFlip, boolean rightStickXFlip, boolean rightStickYFlip)
    {
        super(portNum);
        maxButtons = numberOfButtons;
        lastButtonState = new boolean[maxButtons];
        buttonState = new boolean[maxButtons];
        flipLeftStickX = leftStickXFlip;
        flipLeftStickY = leftStickYFlip;
        flipRightStickX = rightStickXFlip;
        flipRightStickY = rightStickYFlip;
        
        for(int i = 0; i < maxButtons; i++)
        {
            lastButtonState[i] = false;
            buttonState[i] = false;
        }
    }
    
    /**
     * Updates the button values for the joystick
     */
    public void update()
    {   
        for(int i = 0; i < maxButtons; i++)
        {
            buttonState[i] = !lastButtonState[i] && super.getRawButton(i + 1);
            lastButtonState[i] = super.getRawButton(i + 1);
        }
    }
    
    /**
     * Gives button value
     * @param button the button number on the joystick
     * @return button value
     */
    public boolean getButton(int button)
    {
        return buttonState[button - 1];
    }
    
    public double getLeftStickX()
    {
    	return super.getRawAxis(Config.MyJoystick.chnLeftStickX) * (flipLeftStickX ? -1 : 1);
    }
    
    public double getLeftStickY()
    {
    	return super.getRawAxis(Config.MyJoystick.chnLeftStickY) * (flipLeftStickY ? -1 : 1);
    }
    
    public double getRightStickX()
    {
    	return super.getRawAxis(Config.MyJoystick.chnRightStickX) * (flipRightStickX ? -1 : 1);
    }
    
    public double getRightStickY()
    {
    	return super.getRawAxis(Config.MyJoystick.chnRightStickY) * (flipRightStickY ? -1 : 1);
    }
}