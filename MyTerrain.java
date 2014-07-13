import javax.vecmath.*;


import org.j3d.geom.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import javax.media.j3d.*;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.*;


public class MyTerrain{
	public BranchGroup branchGroup;
	public ElevationMap eMap;
	public Appearance appearance;
	public static int MAX_SHAPE_SIDE=100;
	//public int blockSizeI,blockSizej;
	public float xScale,yScale,zScale;
	
	public int  blocksI, blocksJ;
	public int  texture_mode;
	public float texture_unitSide;
				
	float minX,maxX,minZ,maxZ, minY,maxY;
	
	public static float IDEAL_TERRAIN_SIZE = 50;//Reconfigure as you like it
	
	public ArrayList<Shape3D> shapes;
	public static  int 	TEXMODE_REAL = 1,	//Full image, stretch to fit
						TEXMODE_WRAP=2,
						TEXMODE_WIREFRAME = 3;		//repeat every square of size texture_unitSide
	
	public void setTextureMode(int texMode,float unitSide){
		texture_mode = texMode;
		texture_unitSide = unitSide;//Let's not scale it//*(xScale+yScale)/2;
	}

	public void setTextureMode(int texMode){
		setTextureMode( texMode, 1);
	}
	
	public Texture2D loadTexture(String textureFilename){
		TextureLoader loader = new TextureLoader(textureFilename, new Container());//,TextureLoader.ALLOW_NON_POWER_OF_TWO);
		ImageComponent2D image = loader.getImage();
		Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, image.getWidth(), image.getHeight() );
		texture.setImage(0, image);	//Add an index when you plan on supporting multiple superimposable terrains
		texture.setEnable( true );
		texture.setBoundaryModeS(Texture.WRAP);

		texture.setBoundaryModeT(Texture.WRAP);
		return texture;
	}
	
	
	public void setTextureAppearance(String textureFilename, int texMode){
		setTextureMode(texMode,1);
		setTextureAppearance(textureFilename);
	}
	
	public void setTextureAppearance(String textureFilename, int texMode,int unitSize){
		setTextureMode(texMode,unitSize);	
		setTextureAppearance(textureFilename);
	}
	
	public void setTextureAppearance(String textureFilename){
		Appearance appear = new Appearance();
		Texture2D texture = loadTexture(textureFilename);
		appear.setTexture(texture);
		PolygonAttributes polygonAttributes = new PolygonAttributes();
		polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
		//appear.setTexCoordGeneration(new TexCoordGeneration());
		appear.setPolygonAttributes(polygonAttributes);
		
		this.appearance = appear;
	}
	
	public void setWireFrameAppearance(){
		Appearance app = new Appearance();
		Color awtColor = new Color(192, 192, 0);// use any Color you like
		Color3f color = new Color3f(awtColor);
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(color);
		app.setColoringAttributes(ca);
		PolygonAttributes pa = new PolygonAttributes();
		pa.setPolygonMode(pa.POLYGON_LINE);
		pa.setCullFace(pa.CULL_NONE);
		app.setPolygonAttributes(pa);
		appearance = app;
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
			
			minX = eMap.minX * xScale;
			maxX = eMap.maxX * xScale;
			
			minY = eMap.minY * yScale;
			maxY = eMap.maxY * yScale;
			
			minZ = eMap.minZ * zScale;
			maxZ = eMap.maxZ * zScale;
		}
	}
	
	public BranchGroup getBranchGroup(){
		BranchGroup bg = new BranchGroup();
		Iterator<Shape3D> shapeI = shapes.iterator();
		
		while(shapeI.hasNext()){
			bg.addChild(shapeI.next());
		}
		return bg;
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
	
	
	
	public Transform3D getCoolViewTransform(){
		float 	avX = (maxX+minX)/2,
				avY = (maxY+minY)/2,
				avZ = (maxZ+minZ)/2;
		
		double lookFromZ = (avZ/4 < 30 )? avZ/4:30;
		lookFromZ*=-1;
		Point3d lookFrom = new Point3d(avX, maxY, lookFromZ);
		Point3d lookTo = new Point3d(avX , avY, avZ);
		Vector3d upVector = new Vector3d(0f,1f,0f);
		
		Transform3D t = new Transform3D();
		t.lookAt(lookFrom, lookTo, upVector);
		t.invert();
		
		return t;
	}
	
}