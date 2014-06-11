import static java.lang.System.out;
import java.applet.Applet;
import java.awt.event.*;
//import javax.media.j3d.*;


public class MyEventHandler implements KeyListener{
	public MyCamera camera;
	
	public void MyEventHandler(MyUniverse u){
		this.camera = u.camera;
	}
	
	
	//Refer drawing example for mouse events

    @Override
    public void keyPressed(KeyEvent e) {}
     
    @Override
    public void keyReleased(KeyEvent e) {}
     
    @Override
	public void keyTyped(KeyEvent e) {
		System.out.print("!");
	}
	
};