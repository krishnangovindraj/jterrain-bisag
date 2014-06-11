import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.ColorCube;

public class MyUniverse{
	public SimpleUniverse universe=null;
	public ViewingPlatform viewingPlatform=null;
	public TransformGroup camera = null;
	public BranchGroup terrain = null;	//WE don't need the terrain to move around, I think :/
	public BoundingSphere worldBounds = null;
	
	
	public MyUniverse(){
		this.universe = new SimpleUniverse();
		//Set up camera
		this.viewingPlatform = this.universe.getViewingPlatform();
		this.camera = this.viewingPlatform.getViewPlatformTransform();
		/*//Let's assume these 2 are allowed already
		this.camera.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.camera.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		*/
		//Init the terrain? And maybe add it to the universe?
		this.terrain = new BranchGroup();
		//Add lights?
		addLights();
	}
	
	public void addLights(){
		AmbientLight light2 = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
		light2.setInfluencingBounds(worldBounds);
		terrain.addChild(light2);
	}
	
	public void computeBounds(){
		this.worldBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
	}
	
	public void moveCamera(){
		
	}
	
	public void goLive(){
		this.universe.addBranchGraph(this.terrain);
	}
	
	public static void main(String args[]){
		MyUniverse world = new MyUniverse();
		world.terrain.addChild(new ColorCube(0.4));
		world.viewingPlatform.setNominalViewingTransform();
		world.goLive();
	}
	
}