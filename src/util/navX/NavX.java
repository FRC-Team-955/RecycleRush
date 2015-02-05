package util.navX;

public class NavX extends IMUAdvanced {

	double offsetAngle;
		
	public NavX(SerialPort serial_port, byte update_rate_hz, double offsetAngle)
	{
	super(serial_port, update_rate_hz);
	this.offsetAngle = offsetAngle;
		}



public double getAngle()
	{
	
	return (double )super.getYaw() +  offsetAngle;
		
	}
}
