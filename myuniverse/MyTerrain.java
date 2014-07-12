import javax.vecmath.*;


import org.j3d.geom.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;

import java.util.ArrayList;


import java.awt.*;


public class MyTerrain{
	public BranchGroup branchGroup;
	public ElevationMap eMap;
	public Appearance appearance;
	public static int MAX_SHAPE_SIDE=100;
	//public int blockSizeI,blockSizej;
	public float xScale,yScale,zScale;
	
	public int  blocksI, blocksJ;
	public int  texture_mode,
				texture_unitSide;
	
	public ArrayList<Shape3D> shapes;
	public static  int 	TEXMODE_REAL = 1,	//Full image, stretch to fit
						TEXMODE_WRAP=2;		//repeat every square of size texture_unitSide
	
	public void setTextureMode(int texMode,int unitSide){
		texture_mode = texMode;
		texture_unitSide = unitSide;
	}

	public void setTextureMode(int texMode){
		setTextureMode( texMode, 1);
	}  
	
	public MyTerrain(ElevationMap EMapRef){
		xScale=yScale=zScale = 1;
		texture_mode = TEXMODE_REAL;
		
		eMap = EMapRef;
		blocksI=0;
		int remaining= eMap.numColumns;
		do{
			blocksI++;
			remaining=eMap.numColumns/blocksI;
		}while( remaining > MAX_SHAPE_SIDE);
		
		
		blocksJ = 0;
		remaining= eMap.numRows;
		do{
			blocksJ++;
			remaining = eMap.numRows/blocksJ;
		}while( remaining > MAX_SHAPE_SIDE);
	}
	
	public void setScale(float xs, float ys,float zs){
		xScale = xs;
		yScale = ys;
		zScale = zs;
	}
	
	public void createShapes(){
		int iStart = 0,jStart=0;
		int iLimit,jLimit;
		shapes = new ArrayList<Shape3D>();
		System.out.printf("\n totalCols, totalRows = %d,%d", eMap.numColumns,eMap.numRows);
		System.out.printf("\n blocksI, blocksJ = %d,%d", blocksI,blocksJ);
		for(int i=blocksI;i>0;i--){
			iLimit = iStart+(eMap.numColumns - iStart)/i;
			jStart = 0;
			for(int j=blocksJ;j>0;j--){
				jLimit = jStart+(eMap.numRows - jStart)/j;
				
				shapes.add(new Shape3D(generateGeometry(iStart,iLimit,jStart,jLimit), appearance));
				
				jStart=jLimit-1;
			}
			//iStart= iLimit;
			iStart= iLimit-1;//Because of our silly offsets
		}
	}
	
	public GeometryArray generateGeometry(int iStart, int iLimit, int jStart, int jLimit){
		
		int iLimit_1 = iLimit-1;
		int rows = jLimit -jStart;
		int vertexCount = (iLimit-iStart-1)*(jLimit-jStart)*2;
		int stripCounts[] = new int[iLimit_1-iStart];
		for(int i=0;i<stripCounts.length;i++){
			stripCounts[i] = 2*rows;
		}
		//System.out.printf("\nGenerating one FROM (%d->%d) (%d->%d)", iStart,iLimit, jStart, jLimit);
		TriangleStripArray geometryArray= new TriangleStripArray( vertexCount, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2, stripCounts);
		
		Point3f coords[] = new Point3f[vertexCount]; 
		TexCoord2f texCo[] = new TexCoord2f[vertexCount];
		
		//Do the indexes
		int offset = 0;
		float 	xDenom,zDenom;
		if(texture_mode == TEXMODE_WRAP){
			xDenom = (float)(texture_unitSide*eMap.gridSize);
			zDenom = (float)(texture_unitSide*eMap.gridSize);
		}
		else{
			xDenom = (float)((eMap.numColumns-1)*eMap.gridSize);
			zDenom = (float)((eMap.numRows - 1)*eMap.gridSize);
		}
				
		for(int i=iStart;i<iLimit_1;i++){
			for(int j=jStart;j<jLimit;j++){					
				offset = ((i-iStart)*rows+(j-jStart))*2;
				coords[offset+0] = new Point3f(eMap.elevations[i][j].x * xScale, 	 (eMap.elevations[i][j].y - eMap.minElevation) * yScale, eMap.elevations[i][j].z*zScale);
				coords[offset+1] = new Point3f(eMap.elevations[i+1][j].x * xScale, 	 (eMap.elevations[i+1][j].y- eMap.minElevation) * yScale, eMap.elevations[i+1][j].z*zScale);
				
				texCo[offset+0] = new TexCoord2f( eMap.elevations[i][j].x/xDenom, eMap.elevations[i][j].z/zDenom);
				texCo[offset+1] = new TexCoord2f( eMap.elevations[i+1][j].x/xDenom, eMap.elevations[i+1][j].z/zDenom);
			}
		}
		geometryArray.setCoordinates(0, coords);
		geometryArray.setTextureCoordinates(0,0, texCo);
		
		/*Point3f p1 = coords[0];
		Point3f p2 = coords[vertexCount-1];
		
		System.out.printf("\n FIRST POINT: (%f,%f,%f) \t LAST: (%f,%f,%f)", p1.x,p1.y,p1.z,  p2.x,p2.y,p2.z);
		*/
		
		return geometryArray;
	}
	
	public void setAppearance(Appearance ap){
		appearance = ap;
	}
}