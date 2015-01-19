package util;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Diagnostics {
	
	public static String[] getTalonStatus() {
		
		String[] status = new String[5];
		PowerDistributionPanel pdp = new PowerDistributionPanel();
		
		
		status[0] = "Left Talon One:"  + String.valueOf(pdp.getCurrent(Config.Drive.chnMtLeftOne));
		status[1] = "Left Talon Two:"  + String.valueOf(pdp.getCurrent(Config.Drive.chnMtLeftTwo));
		status[2] = "Right Talon One" + String.valueOf(pdp.getCurrent(Config.Drive.chnMtRightOne));
		status[3] = "Right Talon One" + String.valueOf(pdp.getCurrent(Config.Drive.chnMtRightOne));
		status[4] = "Front Talon" + String.valueOf(pdp.getCurrent(Config.Drive.chnMtFront));
		status[5] = "Back Talon" +  String.valueOf(pdp.getCurrent(Config.Drive.chnMtBack));
		
		
		return status;
	}
}