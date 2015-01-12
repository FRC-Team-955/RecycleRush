package util;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Status {
	
	public String[] Status() {
	
		String[] statusTable = new String[7];
		PowerDistributionPanel pdp = new PowerDistributionPanel();
		
		statusTable[0] = "Left Motor One:" + Double.toString(pdp.getCurrent(Config.Drive.leftChanOne));
		statusTable[1] = "Left Motor Two:" + Double.toString(pdp.getCurrent(Config.Drive.leftChanOne));
		statusTable[2] = "Right Motor One:" + Double.toString(pdp.getCurrent(Config.Drive.rightChanOne));
		statusTable[3] = "Right Motor Two:" + Double.toString(pdp.getCurrent(Config.Drive.rightChanOne));
		statusTable[4] = "Front Motor:" + Double.toString(pdp.getCurrent(Config.Drive.frontChan));
		statusTable[5] = "Back Motor:" +  Double.toString(pdp.getCurrent(Config.Drive.backChan));
		statusTable[6] = "Total Current:" + Double.toString(pdp.getTotalCurrent()); 		
	
		return statusTable;
	}
}
