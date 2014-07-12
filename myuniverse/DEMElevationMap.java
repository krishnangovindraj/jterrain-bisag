import org.j3d.loaders.dem.*;
import java.io.*;

public class DEMElevationMap extends ElevationMap{
	
	public static int FIRST_BLOCK_CAPACITY = 146;	//A block that is the first of a new record
	public static int EXTRA_BLOCK_CAPACITY = 170;	//A block that is an continuation of the previous
	public MyDEM dem;
	
	
	
	public DEMElevationMap(MyDEM srcDEM){
		dem = srcDEM;
	}
	
	
	public void getSize(){
		//TRUST NOONE! I'd rather not rely on headers when i can get it from the data itself
		int limit = dem.typeB.length;
		int maxRows = 0;
		int maxRowIndex = 0;
		int colCount =0;
		
		DEMTypeBRecord rec;
		
		rec = dem.typeB[0];
		
		int firstNonZeroTypeBFXIndex=1;
		for(int i=1;i<dem.typeB.length;i++){
			if(dem.typeB[i].firstPositionX!=0){
				firstNonZeroTypeBFXIndex = i;
				break;
			}
		}
		gridSize = dem.typeB[firstNonZeroTypeBFXIndex].firstPositionX - dem.typeB[0].firstPositionX;
		double fx = rec.firstPositionX;
		double fy = rec.firstPositionY;
		
		int newMax = 0;
		int offset=0;
		
		for(int i=0;i<limit;i++){
			rec = dem.typeB[i];
			if(rec.numRows==0)//Continuation of previous
				continue;
			
			if(rec.numRows>maxRows){
				maxRows = rec.numRows;
				maxRowIndex = i;
			}
			
			if(rec.firstPositionX <fx)
				fx = rec.firstPositionX;
			if(rec.firstPositionY <fy)
				fy = rec.firstPositionY;
			
			colCount++;
		}
		
		
		//Assign members
		numColumns = colCount;
		firstX = fx;
		firstY = fy;
		
		//Another loop to find the number of rows we have to accomodate
		
		for(int i=0;i<limit;i++){
			rec = dem.typeB[i];
			offset = (int)((rec.firstPositionY - firstY)/gridSize);
			newMax = offset+ rec.numRows;
			if( newMax > maxRows)
				maxRows = newMax;
		}
		
		//And set it
		numRows = maxRows;
	}
	
	public void loadElevations(){
		this.elevations = new MyPoint3f[numColumns][numRows];	//Geographers \(-_-)/
		//Let's just initialize things to be safe
		for(int i=0;i<numColumns;i++){
			for(int j=0;j<numRows;j++){
				elevations[i][j] = new MyPoint3f(i,j,0);
			}
		}
		
		//Keep safe distance
		int c=-1,r=0;
		DEMTypeBRecord rec;
		int limit = dem.typeB.length;
		// r=> ElevationMap row, c=>ElevationMap Column, i=>demTypeB index, j=>demTypeB.elevation index
		
		
		int recRows = 0;//The number of rows in the record. Can't rely or rec.numRows because it's 0 for a new block
		int rowsLeft = 0;//The number of rows left to read from this record
		int maxRowsThisBlock = 0;//The max number of rows this record can contain
		int offset = 0;	//If rec.firstPositionX doesn't match global firstX, WE have to offset it
		int rLimit = 0;
		MyPoint3f.setLimits(numColumns,numRows);
		
		//Set scale, remember to reset it later
		float originalScale[] = MyPoint3f.getScale();
		MyPoint3f.setScale((float)gridSize,1f,(float)gridSize);
		
		for(int i=0;i<limit;i++){
			rec = dem.typeB[i];
			
			maxRowsThisBlock=EXTRA_BLOCK_CAPACITY;
			
			if(rec.numRows!=0){	//is a new row
				
				c++;
				recRows = rec.numRows;
				rowsLeft = recRows;
				maxRowsThisBlock =FIRST_BLOCK_CAPACITY;
				r=0;
				offset = (int)((rec.firstPositionY - firstY)/ gridSize);
				
				rLimit = offset+recRows;
				while(r<offset){
					elevations[c][r++]=new MyPoint3f(c,r, rec.elevations[0]);	//To make up for the difference in firstY
				}
			}
			
			//Copy the elevations in that record to our array. Why don't they just use bitmaps for heights -_-
			int j=0;
			while(r<rLimit&&  j<maxRowsThisBlock){	
				elevations[c][r] = new MyPoint3f(c,r, rec.elevations[j]);
				j++;
				r++;
			}
			rowsLeft-= j;
			
			j=j-1;
			if(rowsLeft<=0){
				while(r<rLimit)
					elevations[c][r++]= new MyPoint3f(c,r, rec.elevations[j-1]);//For now, Else we'll do some nice (inter/extra)polation
			}
		}
		horizontalStep = (elevations[numColumns-1][0].x-elevations[0][0].x)/numColumns;
		
		findMinElevation();
		
		computeMinMaxXYZ();
		
		
		computeMinMaxXYZ();
		float actualHeightRange = (float)(dem.typeA.maxHeight - dem.typeA.minHeight);
		float eMapHeightRange = maxY-minY;
		if( actualHeightRange != eMapHeightRange ){
			float heightScaleFactor = actualHeightRange / eMapHeightRange;
			System.out.println("Fixing heights");
			for(int i=0;i<numColumns;i++){
				for(int j=0;j<numColumns;j++){
					elevations[i][j].y*= heightScaleFactor;
				}
			}
			minY*= heightScaleFactor;
			maxY*= heightScaleFactor;
		}
		
		System.out.printf("\nRecords: minHeght: %f, maxHeight: %f. Emap: minY:%f ,maxY:%f", dem.typeA.minHeight, dem.typeA.maxHeight, minY,maxY);
		System.out.printf("\n groundUnitOfMeasure:%d, elevationUnitfOfMeasure:%d", dem.typeA.groundUnitOfMeasure, dem.typeA.elevationUnitOfMeasure);
		System.out.printf("\n Spatial resolutions %f,%f,%f", dem.typeA.spatialResolution[0], dem.typeA.spatialResolution[1],dem.typeA.spatialResolution[2]);
		//reset MyPoint3f scale
		MyPoint3f.setScale(originalScale);
	}
	
}
