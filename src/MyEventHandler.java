import static java.lang.System.out;
import java.applet.Applet;
import java.awt.event.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;


public class MyEventHandler implements KeyListener{
	public MyCamera camera;
	public static double TRANSLATIONSPEED = 0.16d;
	public static double ROTATIONANGLE = Math.PI/64;
	
	
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
	
}
/*
class MousePointFinder{
	public Point3d getPosition(MouseEvent event){
		Point3d eyePos = new Point3d();
		Point3d mousePos = new Point3d();
		canvas.getCenterEyeInImagePlate(eyePos);
		canvas.getPixelLocationInImagePlate(event.getX(),
                       event.getY(), mousePos);					//Load it into mousePos?
		Transform3D transform = new Transform3D();
		canvas.getImagePlateToVworld(transform);				//
		transform.transform(eyePos);
		transform.transform(mousePos);
		Vector3d direction = new Vector3d(eyePos);
		direction.sub(mousePos);
		// three points on the plane
		Point3d p1 = new Point3d(.5, -.5, .5);
		Point3d p2 = new Point3d(.5, .5, .5);
		Point3d p3 = new Point3d(-.5, .5, .5);
		Transform3D currentTransform = new Transform3D();
		box.getLocalToVworld(currentTransform);
		currentTransform.transform(p1);
		currentTransform.transform(p2);
		currentTransform.transform(p3);		
		Point3d intersection = getIntersection(eyePos, mousePos,
                        p1, p2, p3);
		currentTransform.invert();
		currentTransform.transform(intersection);
		return intersection;		
	}
	
	
	// Returns the point where a line crosses a plane  
	Point3d getIntersection(Point3d line1, Point3d line2, 
		Point3d plane1, Point3d plane2, Point3d plane3) {
		Vector3d p1 = new Vector3d(plane1);
		Vector3d p2 = new Vector3d(plane2);
		Vector3d p3 = new Vector3d(plane3);
		Vector3d p2minusp1 = new Vector3d(p2);
		p2minusp1.sub(p1);
		Vector3d p3minusp1 = new Vector3d(p3);
		p3minusp1.sub(p1);
		Vector3d normal = new Vector3d();
		normal.cross(p2minusp1, p3minusp1);
		// The plane can be defined by p1, n + d = 0
		double d = -p1.dot(normal);
		Vector3d i1 = new Vector3d(line1);
		Vector3d direction = new Vector3d(line1);
		direction.sub(line2);
		double dot = direction.dot(normal);
		if (dot == 0) return null;
		double t = (-d - i1.dot(normal)) / (dot);
		Vector3d intersection = new Vector3d(line1);
		Vector3d scaledDirection = new Vector3d(direction);
		scaledDirection.scale(t);
		intersection.add(scaledDirection);
		Point3d intersectionPoint = new Point3d(intersection);
		return intersectionPoint;
	}
	
}*/