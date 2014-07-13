import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import java.io.*;

public class MyUniverse{
	static{	//We have external dependencies, namely j3dcore-ogl.dll. Check them 
		String requiredLibs[] = {"j3dcore-ogl"};
		boolean allLibsAvailable = true;

		for(int i=0;i<requiredLibs.length;i++){
			try {
				System.loadLibrary(requiredLibs[i]);
			} catch (UnsatisfiedLinkError e) {
				allLibsAvailable =false;
				System.out.println(requiredLibs[i] + " not found. run\n jar xf <thisjarfile>.jar " + requiredLibs[i] + ".dll" );
			}
		}
		if(!allLibsAvailable){
			//Note, If you're planning to make an applet, This is as far as you go. Sorry. The lib dependency will stop you. You'll have to get the user to install the dll on his PC
			System.out.println("The required libraries were not found. They are packaged with the jar. Please extract them");
			System.exit(1);
		}
	}
	
	public SimpleUniverse universe=null;
	
	
	public BranchGroup terrain = null;	//WE don't need the terrain to move around, I think :/
	//public BoundingSphere worldBounds = null;
	public Bounds worldBounds = null;
	public Canvas3D canvas = null;
	
	public MyCamera camera = null;
	public MyEventHandler eventHandler = null;
	
	public MyUniverse(){
		this.universe = new SimpleUniverse();
		//Set up camera
		this.camera = new MyCamera(this.universe);
		this.canvas = this.universe.getCanvas();
		//Init the terrain. Do not add it to the universe till the terrain is generated
		this.terrain = new BranchGroup();
		//Add lights
		addLights();
		
		//You could change the order to Lights! Camera! Terrain!!!
	}
	
	public void addLights(){
		//Ambient lighting
		/*
		AmbientLight light2 = new AmbientLight(new Color3f(1f, 1f, 1f));
		light2.setInfluencingBounds(worldBounds);
		terrain.addChild(light2);*/
		DirectionalLight light1 = new DirectionalLight(new Color3f(1f,1f,1f), new Vector3f(0f,-1f,0f));
		light1.setInfluencingBounds(worldBounds);
		terrain.addChild(light1);
	}
	
	public void computeBounds(){
		this.worldBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),500.0);
	}
	
	public void setBounds(Bounds b){
		this.worldBounds = b;
	}
	
	public void bindEventHandler(){
		this.eventHandler = new MyEventHandler(this);
		this.canvas.addKeyListener(this.eventHandler);
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
