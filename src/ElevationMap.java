import org.j3d.loaders.dem.*;
import java.io.*;

public class ElevationMap{
	
	public int numRows, numColumns;
	public double firstX,firstY;
	public MyPoint3f elevations[][];
	
	public double gridSize;
	
	public float horizontalStep;
	float minX,maxX,minZ,maxZ, minY,maxY;
	
	public float minElevation;
	
	public ElevationMap(){};
	
	public boolean inRange(int i,int j){
		return i<numColumns && i>=0 && j<numRows && j>=0;
	}
	
	public void computeMapDimensions(){
		numColumns = elevations.length;
		numRows = elevations[0].length;
		
		computeMinMaxXYZ();
		
		horizontalStep = (maxX-minX)/numColumns;
		//Dunno whether this works ok
		float prevZ = elevations[0][0].z;
		for(int i=0;i<numColumns;i++){
			prevZ = elevations[i][0].z;
			for(int j=1;j<numRows;j++){
				if(elevations[i][j].z - prevZ  > 0){
					 gridSize = elevations[i][j].z - prevZ;
					 break;
				}
				prevZ = elevations[i][j].z;
			}
		}
		
	}
	
	public void computeMinMaxXYZ(){
		maxX = minX = elevations[0][0].x;
		maxZ = minZ = elevations[0][0].z;
		
		maxY = minY = elevations[0][0].y;
		
		for(int i=0;i<numColumns;i++){
			for(int j=0;j<numRows;j++){
				if(elevations[i][j].y < minY )		minY = elevations[i][j].y;
				if(elevations[i][j].y > maxY )		maxY = elevations[i][j].y;
				
				
				if(elevations[i][j].x < minX )		minY = elevations[i][j].x;
				if(elevations[i][j].x > maxX )		maxX = elevations[i][j].x;
				
				if(elevations[i][j].z < minZ )		minY = elevations[i][j].z;
				if(elevations[i][j].z > maxZ )		maxZ = elevations[i][j].z;
			}
		}
		
	}
	
	public void findMinElevation(){
		float min = elevations[0][0].y;
		
		for(int i=0;i<numColumns;i++){
			for(int j=0;j<numRows;j++){
				if(elevations[i][j].y < min )
					min = elevations[i][j].y;
			}
		}
		minElevation = min;
	}
	
	public void dumpArea(int cStart,int cMax,int rStart, int rMax){
		int r=rStart, c=cStart;
		while(c<cMax){
			r=rStart;
			System.out.println();
			while(r<rMax){
				System.out.printf("(%3.3f,%3.3f,%3.3f) ",elevations[c][r].x, elevations[c][r].y, elevations[c][r].z);
				r++;
			}
			c++;
		}
	}
	
	
	//Returns a small portion of the map
	public ElevationMap getSubMap(int cStart,int cEnd, int rStart, int rEnd){
		ElevationMap e= new ElevationMap();
		int cLimit = cEnd+1,
			rLimit = rEnd+1;
		int nCols= cLimit-cStart,
			nRows= rLimit-rStart;
		e.elevations = new MyPoint3f[nCols][nRows];
		
		float originalScale[] = MyPoint3f.getScale();
		MyPoint3f.setScale((float)gridSize,1f,(float)gridSize);
		e.gridSize = gridSize;
		
		for(int localI=0, i=cStart;i<cLimit;localI++, i++){
			for(int localJ=0,j=rStart;j<rLimit;localJ++, j++){
				
				e.elevations[localI][localJ] = new MyPoint3f(localI,localJ,0);
				e.elevations[localI][localJ].y = elevations[i][j].y;
			}
		}
		MyPoint3f.setScale(originalScale);
		e.computeMapDimensions();
		firstX=minX;
		firstY=minY;
		return e;
	}
	
	public ElevationMap getElevationMap(){
		ElevationMap eMap= new ElevationMap();
	
		eMap.numRows=numRows;
		eMap.numColumns=numColumns;
		eMap.firstX = firstX;
		eMap.firstY = firstY;
		eMap.elevations = elevations;

		eMap.gridSize=gridSize;
		
		eMap.horizontalStep=horizontalStep;
		eMap.minX = minX;
		eMap.maxX=maxX;
		eMap.minZ=minZ;
		eMap.maxZ=maxZ;
		eMap.minY=minY;
		eMap.maxY=maxY;
		
		eMap.minElevation = minElevation;
		return eMap ;
	}
	
}
