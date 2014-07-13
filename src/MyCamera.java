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

//import static java.lang.System.out;
import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;
import javax.media.j3d.*;

public class MyCamera{
	public TransformGroup transformGroup= null;
	public ViewingPlatform viewingPlatform = null;
	public SimpleUniverse parentUniverse = null;
	
	
	//Space for state of the camera/Other state variables
	
	
	//Constructor
	public MyCamera(SimpleUniverse universe){
		parentUniverse = universe;
		viewingPlatform = parentUniverse.getViewingPlatform();
		transformGroup = viewingPlatform.getViewPlatformTransform();
	}
	
	
	public Transform3D getTransform(){
		Transform3D transform = new Transform3D();
		transformGroup.getTransform(transform);
		TransformGroup yHinge = new TransformGroup();
		return transform;
	}
	
	
	//methods to get state
	public Vector3d getPosition(){
		Transform3D transform = new Transform3D();
		Vector3d v= new Vector3d();
		transformGroup.getTransform(transform);
		transform.get(v);
		return v;
	}
	
	/*public Vector3d getOrientation(){
		
	}*/
	//methods to set state
	public void setNominalView(){
		viewingPlatform.setNominalViewingTransform();
	}
	
	public void setPosition(double x, double y, double z){
		Transform3D transform= new Transform3D();
		Vector3d v = new Vector3d(x,y,z);
		transform.set(v);
		transformGroup.setTransform(transform);
	}
	
	public void setOrientation(){
		//Nothing yet
	}
	
	
	//Methods to transform
	
	public void translateCamera(double moveX,double moveY, double moveZ){
		//Take the existing transform, ...
		Transform3D original = new Transform3D();
		transformGroup.getTransform(original);
		
		//change it...
		Transform3D transform= new Transform3D();
		Vector3d v = new Vector3d(moveX,moveY,moveZ);
		
		//And set it back
		transform.set(v);
		original.mul(transform);
		transformGroup.setTransform(original);
	}
	
	public void translateCamera(Vector3d displacement){
		Transform3D original= new Transform3D();
		transformGroup.getTransform(original);
		
		Transform3D transform = new Transform3D();
		transform.set(displacement);
		
		original.mul(transform);
		transformGroup.setTransform(original);
	}
	
	
	public void rotateCamera(double radX,double radY,double radZ){
		Transform3D rotateX = new Transform3D();
		Transform3D rotateY = new Transform3D();
		Transform3D rotateZ = new Transform3D();
		

		rotateX.rotX(radX);
		rotateY.rotY(radY);
		rotateZ.rotZ(radZ);
		
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
	
	/*
	public void revolveCamera(double radH,double radV){
		Transform3D rotateX = new Transform3D();
		Transform3D rotateY = new Transform3D();
		

		rotateX.rotX(radX);
		rotateY.rotY(radY);
		rotateZ.rotZ(radZ);
		
		//Get the existing Transform3D
		Transform3D transform = new Transform3D();
		transformGroup.getTransform(transform);
		
		//Combine all of them into the existing rotate
		transform.mul(rotateX);
		transform.mul(rotateY);
		transform.mul(rotateZ);
		
		//Set it back
		transformGroup.setTransform(transform);
	}*/

	
	public void setCameraTransform(Transform3D transform){
		transformGroup.setTransform(transform);
	}
	
	public void transformCamera(Transform3D transform){
		Transform3D currentTransform = new Transform3D();
		transformGroup.getTransform(currentTransform);
		currentTransform.mul(transform);
		transformGroup.setTransform(currentTransform);
	}
}