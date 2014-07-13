import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

import org.j3d.geom.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;

import javax.vecmath.*;

import java.awt.*;



public class TestText{
	
	
	public static Texture2D loadTexture(String filename){
		//ImageObserver iObserve = new ImageObserver();
		TextureLoader loader = new TextureLoader(filename, new Container());
		ImageComponent2D image = loader.getImage();
		Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, image.getWidth(), image.getHeight() );
		texture.setImage(0, image);
		texture.setEnable( true );
		texture.setBoundaryModeS(Texture.WRAP);

		texture.setBoundaryModeT(Texture.WRAP);
		return texture;
	}
	
	public static Appearance getWireframeAppearance() {
		Appearance app = new Appearance();
		Color awtColor = new Color(255, 255, 0);// use any Color you like
		Color3f color = new Color3f(awtColor);
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(color);
		app.setColoringAttributes(ca);
		PolygonAttributes pa = new PolygonAttributes();
		pa.setPolygonMode(pa.POLYGON_LINE);
		pa.setCullFace(pa.CULL_NONE);
		app.setPolygonAttributes(pa);
		return app;
	}
  
	public static Appearance getAppearance(){
		Appearance appear = new Appearance();
		Texture2D texture = loadTexture("DEM\\2001_resized.jpg");
		appear.setTexture(texture);
		PolygonAttributes polygonAttributes = new PolygonAttributes();
		polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
		//appear.setTexCoordGeneration(new TexCoordGeneration());
		appear.setPolygonAttributes(polygonAttributes);
		
		return appear;
	}
	
	public static void main(String args[]){
		
		
		//ElevationMap eMap = new ElevationMap();
		XYZElevationMap eMap= new XYZElevationMap(".\\DEM\\2001.txt");
		eMap.loadElevations();
		
		///*
		float xScale, yScale, zScale;
		float eMapXRange = eMap.maxX - eMap.minX;
		float scaleFactor = (eMapXRange<100)?1 : MyTerrain.IDEAL_TERRAIN_SIZE/(eMap.maxX-eMap.minX);
		xScale = yScale = zScale = scaleFactor;
		
		if(args.length>=3){
			xScale*= Float.parseFloat(args[0]);
			yScale*= Float.parseFloat(args[1]);
			zScale*= Float.parseFloat(args[2]);
		}
		
		
		MyTerrain qt = new MyTerrain(eMap);
		qt.setScale(xScale,yScale,zScale);
		
		qt.setTextureAppearance(".\\DEM\\texture1.jpg");
		//qt.setWireFrameAppearance();
		
		System.out.printf("\nFound %d rows, %d columns. Gridsize=%f. MinEast is %f, Min South is %f",  eMap.numRows,eMap.numColumns, eMap.gridSize, eMap.firstX, eMap.firstY);
		qt.createShapes();
		BranchGroup bg = qt.getBranchGroup();
		
		MyUniverse world = new MyUniverse();
		
		world.terrain.addChild(bg);
		world.terrain.compile();
		world.bindEventHandler();
		world.camera.setNominalView();
		world.goLive();
	}
}