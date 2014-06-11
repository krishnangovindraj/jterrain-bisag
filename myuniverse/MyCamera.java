/*
	MyCamera class
	Defines:
		-Mouse, Keyboard event handler
		-Methods to move the camera around
		-State of the camera ( Position, Orientation )
*/

/*
	Handlers:
		Mouse scroll: Move closer/further in cameraDirection
		Mouse drag: Rotate the whole map i.e. You revolve the camera about a center point ( the clicked point on the surface )
		Mouse move: FPS look
		Keyboard (WASD): Translate the camera in Horizontal plane? Add vertical translation if needed. Not much work
*/

import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;
import javax.media.j3d.*;

public class MyCamera{
	public TransformGroup transformGroup= null;
	public ViewingPlatform viewingPlatform = null;
	public SimpleUniverse parentUniverse = null;
	
	//Space for state of the camera/Other state variables
	
	
	public MyCamera(SimpleUniverse universe){
		parentUniverse = universe;
		viewingPlatform = parentUniverse.getViewingPlatform();
		transformGroup = viewingPlatform.getViewPlatformTransform();
	}
	
	
	public void translateCamera(double moveX,double moveY, double moveZ){
		Transform3D transform= new Transform3D();
		Vector3f v = new Vector3f();
		
		//Take the existing transform, ...
		transformGroup.getTransform(transform);
		transform.get(v);
		//change it...
		v.x+= moveX;
		v.y+= moveY;
		v.z+= moveZ;
		
		//And set it back
		transform.set(v);
		transformGroup.setTransform(transform);
	}
	
	public void rotateCamera(double radX,double radY,double radZ){
		Transform3D rotateX = new Transform3D();
		Transform3D rotateY = new Transform3D();
		Transform3D rotateZ = new Transform3D();
		

		rotateX.rotX(radX);
		rotateY.rotY(radY);
		rotateX.rotY(radZ);
		
		//Get the existing Transform3D
		Transform3D transform = new Transform3D();
		transformGroup.getTransform(transform);
		
		//Combine all of them into the existing rotate
		transform.mul(rotateX);
		transform.mul(rotateY);
		transform.mul(rotateZ);
		
		//Set it back
		transformGroup.setTransform(transform);
	}
}