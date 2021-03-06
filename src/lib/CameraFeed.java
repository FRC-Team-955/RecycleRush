package lib;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.CameraServer;

public class CameraFeed
{
	private final int camCenter;
	//private final int camRight;
	private int curCam;
	private Image frame;
	private CameraServer server;
	//private Controller contr;
	
	public CameraFeed()
	{
        // Get camera ids by supplying camera name ex 'cam0', found on roborio web interface
        camCenter = NIVision.IMAQdxOpenCamera(Config.Camera.name, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
     //   camRight = NIVision.IMAQdxOpenCamera(Config.CameraFeed.camNameRight, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        curCam = camCenter;
        // Img that will contain camera img
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // Server that we'll give the img to
        server = CameraServer.getInstance();
        server.setQuality(Config.Camera.imgQuality);
	}
	
	public void init()
	{
		changeCam(camCenter);
	}
	
	public void run()
	{
//		if(contr.getButton(Config.CameraFeed.btCamCenter))
//			changeCam(camCenter);
//		
//		if(contr.getButton(Config.CameraFeed.btCamRight))
//			changeCam(camRight);
//		
		updateCam();
	}
//	
//	/**
//	 * Stop aka close camera stream
//	 */
	public void end()
	{
		NIVision.IMAQdxStopAcquisition(curCam);
	}
	
	/**
	 * Change the camera to get imgs from to a different one
	 * @param newId for camera
	 */
	public void changeCam(int newId)
    {
		NIVision.IMAQdxStopAcquisition(curCam);
    	NIVision.IMAQdxConfigureGrab(newId);
    	NIVision.IMAQdxStartAcquisition(newId);
    	curCam = newId;
    }
    
	/**
	 * Get the img from current camera and give it to the server
	 */
    public void updateCam()
    {
    	NIVision.IMAQdxGrab(curCam, frame, 1);
        server.setImage(frame);
    }
    
}
    
