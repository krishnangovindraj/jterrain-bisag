import java.util.ArrayList;
import java.util.Iterator;

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
	
	
	/*
	public static void loadElevationMap(String filename, ElevationMap eMap){
		int rowSize = 0;
		ArrayList<MyPoint3f> points = new ArrayList<MyPoint3f>();
		MyPoint3f p;
		
		
		try{	
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String s;
			StringTokenizer st;
			
			while((s = br.readLine()) != null) {
					p=new MyPoint3f(-1,-1,-1);
					st = new StringTokenizer(s, " ");
				//while(st.hasMoreTokens()) {//Should run once
					p.x=Float.parseFloat(st.nextToken());
					
					p.z=Float.parseFloat(st.nextToken());
					
					p.y=Float.parseFloat(st.nextToken());
				//}
				points.add(p);
			}
			
			MyPoint3f p1 = points.get(0);
			MyPoint3f p2 = points.get(1);
			
			float minX,maxX, minY,maxY, minZ, maxZ, gridSize;
			minX = maxX = p1.x;
			minY = maxY = p1.y;
			minZ = maxZ = p1.z;
			gridSize = p2.x - p1.x;
			
			MyPoint3f prevPoint=p1;
			Iterator<MyPoint3f> pointI = points.iterator();
			int xCounter=0,zCounter=1,xCount=0;
			while(pointI.hasNext()){
				xCounter++;
				p = pointI.next();
				if( p.x < minX)				minX = p.x;
				if( p.x > maxX)				maxX = p.x;
				
				if( p.y < minY)				minY = p.y;
				if( p.y > maxY)				maxY = p.y;
				
				if( p.z < minZ)				minZ = p.z;
				if( p.z > maxZ)				maxZ = p.z;
				
				if(p.z!=prevPoint.z){
					if(xCounter > xCount )
						xCount= xCounter;
					xCounter = 0;
					zCounter++;
				}
				prevPoint = p;
			}
			
			eMap.numColumns = xCount;
			eMap.numRows = zCounter;
			eMap.gridSize = gridSize;
			eMap.horizontalStep = gridSize;
			eMap.minElevation = minY;
			eMap.firstX = minX;
			eMap.firstY = minZ;
			eMap.elevations = new MyPoint3f[eMap.numColumns][eMap.numRows];
			
			System.out.printf("FOUND %d cols,%d rows, gridSize=%f, minY=%f, minX=%f,minZ=%f", eMap.numColumns, eMap.numRows, eMap.gridSize, eMap.minElevation, eMap.firstX,eMap.firstY);
			for(int i=0; i<eMap.numColumns ;i++){
				for(int j=0; j<eMap.numRows ;j++){
					eMap.elevations[i][j]= new MyPoint3f();
				}
			}
			
			pointI = points.iterator();
			MyPoint3f a;
			while(pointI.hasNext()){
				p= pointI.next();
				a = eMap.elevations[(int)((p.x-minX)/gridSize)][(int)((p.z-minZ)/gridSize)];
				a.x = p.x-minX;
				a.y = p.y-minY;
				a.z = p.z-minZ;//Major fuckup
			}
			
		}catch(IOException e){
			System.out.println("EXCEPTION: " + e);
		}
		
		
	}*/
	
	
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
		
		///*
		//ElevationMap eMap = new ElevationMap();
		XYZElevationMap eMap= new XYZElevationMap(".\\DEM\\2001.txt");
		eMap.loadElevations();
		MyTerrain qt = new MyTerrain(eMap);
		
		
		float xScale = 1f;
		float yScale = 1f;
		float zScale = 1f;
		if(args.length>=3){
			xScale = Float.parseFloat(args[0]);
			yScale = Float.parseFloat(args[1]);
			zScale = Float.parseFloat(args[2]);
		}
		qt.setScale(xScale,yScale,zScale);
		qt.setAppearance(getAppearance());
		
		
		System.out.printf("\nFound %d rows, %d columns. Gridsize=%f. MinEast is %f, Min South is %f",  eMap.numRows,eMap.numColumns, eMap.gridSize, eMap.firstX, eMap.firstY);
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