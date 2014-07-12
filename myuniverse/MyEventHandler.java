import static java.lang.System.out;
import java.applet.Applet;
import java.awt.event.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;


public class MyEventHandler implements KeyListener{
	public MyCamera camera;
	public static double TRANSLATIONSPEED = 0.1d;
	public static double ROTATIONANGLE = Math.PI/72;
	
	
	public MyEventHandler(MyUniverse u){
		this.camera = u.camera;
	}
	
	
	//Refer drawing example for mouse events

    @Override
    public void keyPressed(KeyEvent e) {}
     
    @Override
    public void keyReleased(KeyEvent e) {}
     
    @Override
	public void keyTyped(KeyEvent e) {
		
		boolean isTranslation=true;
		boolean isRotation=true;
		boolean isRevolution=true;
		
		Vector3d d = new Vector3d(0d,0d,0d);
		switch(e.getKeyChar()){
			case 'a':	d.x-= this.TRANSLATIONSPEED;
				break;
			case 'd': 	d.x+= this.TRANSLATIONSPEED;
				break;
			case 'w':	d.y+= this.TRANSLATIONSPEED;
				break;
			case 's':	d.y-= this.TRANSLATIONSPEED;
				break;
			
			case 'f': 	d.z+= this.TRANSLATIONSPEED;
				break;
			
			case 'r': 	d.z-= this.TRANSLATIONSPEED;
				break;
				
			
			default: isTranslation=false;
		}
		//The moment of truth!
		if(isTranslation){
			camera.translateCamera(d);
			return;
		}
		
		//Let's also have rotation for some time, 15degrees
		//	WARNING! THIS IS A TEMPORARY THING. YOU NEED TO CORRECT FOR CHANGE OF AXES DURING ROTATION. I ONLY USE THIS FOR TESTING MY DEM LOADER!
		
		double radX = 0d;
		double radY = 0d;
		double radZ = 0d;
		switch(e.getKeyChar()){
			case 'h':	radY+= this.ROTATIONANGLE;
				break;
			case 'k': 	radY-= this.ROTATIONANGLE;
				break;
			case 'u':	radX+= this.ROTATIONANGLE;
				break;
			case 'j':	radX-= this.ROTATIONANGLE;
				break;
			case 'o':  radZ+=  this.ROTATIONANGLE;
				break;
			case 'p':  radZ-= this.ROTATIONANGLE;
				break;
			default: isRotation = false;
		}
		
		if(isRotation){
			camera.rotateCamera(radX,radY,radZ);
			return;
		}
		/*
		switch(e.getKeyChar()){
			case '4':	radY-= this.REVOLUTIONANGLE;
				break;
			case '6': 	radY+= this.REVOLUTIONANGLE;
				break;
			case '8':	radX+= this.REVOLUTIONANGLE;
				break;
			case '2':	radX-= this.REVOLUTIONANGLE;
				break;
			
			default: isRevolution = false;
		}
		
		if(isRevolution){
			camera.revolve(radX,radY,radZ);
			return;
		}*/
	}
	
};