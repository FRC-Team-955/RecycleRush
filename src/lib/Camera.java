package lib;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera
{
	CameraServer server;

	public Camera()
	{
		server = CameraServer.getInstance();
		server.setQuality(50);
		// the camera name (ex "cam0") can be found through the roborio web
		// interface
		server.startAutomaticCapture();
	}
}