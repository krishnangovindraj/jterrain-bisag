import javax.media.j3d.*;

import org.j3d.geom.*;
import com.sun.j3d.utils.geometry.*;

import java.io.*;
import java.util.StringTokenizer;

import java.util.ArrayList;
import java.util.Iterator;


class XYZElevationMap extends ElevationMap{
	public String filename;
	public XYZElevationMap(String fname){
		filename = fname;
	}
	
	public void loadElevations(){
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
			
			numColumns = xCount;
			numRows = zCounter;
			
			horizontalStep = (float)gridSize;
			minElevation = minY;
			firstX = minX;
			firstY = minZ;
			elevations = new MyPoint3f[numColumns][numRows];
			
			for(int i=0; i<numColumns ;i++){
				for(int j=0; j<numRows ;j++){
					elevations[i][j]= new MyPoint3f();
				}
			}
			
			pointI = points.iterator();
			MyPoint3f a;
			while(pointI.hasNext()){
				p= pointI.next();
				a = elevations[(int)((p.x-minX)/gridSize)][(int)((p.z-minZ)/gridSize)];
				a.x = p.x-minX;
				a.y = p.y-minY;
				a.z = p.z-minZ;//Major fuckup
			}
			
		}catch(IOException e){
			System.out.println("EXCEPTION: " + e);
		}
		
		
	}
}