import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;
import javax.media.j3d.*;


//Testing
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.*;


public class Test{


	public static void changeCameraAngle(MyUniverse u){
		MyCamera camera = u.camera;
		
		//Move camera a bit to the right and far enough for you to see the cube
		camera.setPosition(2.4f,0.0d,2.4f);
		
		//Rotate camera to look at the cube
		camera.rotateCamera(0.0d,Math.PI/4.0d,0.0d);
		
		//Use camera.setNominalView();	//for default view
	}
	
	public static void main(String args[]){
		
		MyUniverse world = new MyUniverse();
		world.computeBounds();
		//Generate the terrain
		world.terrain.addChild(new ColorCube(0.4));	
		changeCameraAngle(world);
		
		//Go live
		world.goLive();
		
	}
	

}