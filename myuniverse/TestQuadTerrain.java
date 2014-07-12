import java.util.ArrayList;
import java.util.Iterator;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

import org.j3d.geom.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;

import javax.vecmath.*;

import java.awt.*;


public class TestQuadTerrain{
	
	public static Texture2D loadTexture(String filename){
		//ImageObserver iObserve = new ImageObserver();
		TextureLoader loader = new TextureLoader(filename, new Container());
		ImageComponent2D image = loader.getImage();
		Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, image.getWidth(), image.getHeight() );
		System.out.println(image.getWidth() + " ,  " +image.getHeight());
		texture.setImage(0, image);
		texture.setEnable( true );
		texture.setBoundaryModeS(Texture.WRAP);

		texture.setBoundaryModeT(Texture.WRAP);
		return texture;
	}
	
	public static Appearance getAppearance(String textureFilename){
		Appearance appear = new Appearance();
		Texture2D texture = loadTexture(textureFilename);
		appear.setTexture(texture);
		PolygonAttributes polygonAttributes = new PolygonAttributes();
		polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
		//appear.setTexCoordGeneration(new TexCoordGeneration());
		appear.setPolygonAttributes(polygonAttributes);
		
		return appear;
	}
	
	public static void main(String args[]){
		
		///*
		MyDEM dem = new MyDEM();
		//dem.loadRecords(".\\DEM\\bushkill_pa.dem");
		dem.loadRecords(".\\DEM\\ben.dem");
		//dem.loadRecords(".\\DEM\\hawaii.dem");//".\\DEM\\bushkill_pa.dem");

		
		DEMElevationMap eMap = new DEMElevationMap(dem);
		
		System.out.println("Computing elevationmap size");
		eMap.getSize();
		System.out.println("Loading elevations");
		eMap.loadElevations();
		int dumpI = eMap.numColumns/2,
			dumpJ = eMap.numRows/2;
		
		//eMap.dumpArea(dumpI,dumpI+20, dumpJ,dumpJ+20);
		
		dem = null;//Delete
		System.out.printf("\nFound %d rows, %d columns. Gridsize=%f. MinEast is %f, Min South is %f",  eMap.numRows,eMap.numColumns, eMap.gridSize, eMap.firstX, eMap.firstY);
		ElevationMap subMap = eMap.getSubMap(200,600,200,600);
		QuadTerrain qt = new QuadTerrain(subMap);
		//QuadTerrain qt = new QuadTerrain(eMap);
		qt.setTextureMode(QuadTerrain.TEXMODE_WRAP, 200);
		float xScale = 1f;
		float yScale = 0.1f;
		float zScale = 1f;
		if(args.length>=3){
			xScale = Float.parseFloat(args[0]);
			yScale = Float.parseFloat(args[1]);
			zScale = Float.parseFloat(args[2]);
		}
		qt.setScale(xScale,yScale,zScale);
		qt.setAppearance(getAppearance("texture1.jpg"));
		
		qt.createShapes();
		
		
		
		MyUniverse world = new MyUniverse();
		BranchGroup bg = new BranchGroup();
		
		//Add the shapes to universe terrain
		Iterator<Shape3D> shapeI = qt.shapes.iterator();
		System.out.printf("FOUND %d shapes", qt.shapes.size());
		while(shapeI.hasNext()){
			bg.addChild(shapeI.next());
		}
		
		
		world.terrain.addChild(bg);
		world.terrain.compile();
		world.bindEventHandler();
		world.camera.setNominalView();
		world.goLive();
	}
}