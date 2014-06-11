import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;
import javax.media.j3d.*;


public class MyUniverse{
	public SimpleUniverse universe=null;
	
	
	public BranchGroup terrain = null;	//WE don't need the terrain to move around, I think :/
	public BoundingSphere worldBounds = null;
	public MyCamera camera = null;
	
	public MyUniverse(){
		this.universe = new SimpleUniverse();
		//Set up camera
		this.camera = new MyCamera(this.universe);
		
		//Init the terrain. Do not add it to the universe till the terrain is generated
		this.terrain = new BranchGroup();
		//Add lights
		addLights();
		
		//You could change the order to Lights! Camera! Terrain!!!
	}
	
	public void addLights(){
		//Ambient lighting
		AmbientLight light2 = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
		light2.setInfluencingBounds(worldBounds);
		terrain.addChild(light2);
	}
	
	public void computeBounds(){
		this.worldBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
	}
	
	public void goLive(){
		//Add terrain to universe so it goes live
		this.universe.addBranchGraph(this.terrain);
	}
/*	
	public static void main(String args[]){
		
		MyUniverse world = new MyUniverse();
		world.computeBounds();
		//Generate the terrain
		world.terrain.addChild(new ColorCube(0.4));
		
		doStuff(world);
		
		//Go live
		world.goLive();
		
	}
	*/
}
