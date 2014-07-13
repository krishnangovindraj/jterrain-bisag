
//import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

class DEMViewerExample{
	
	public static void main(String args[]){
		
		CLIOptions opts= new CLIOptions(args);
		try{
			opts.parseArgs();
		}
		catch(IllegalArgumentException e){
			opts.blurtHelp(""+e);
			System.out.println("Exiting...");
			System.exit(0);
		}
		
		
		
		ElevationMap eMap;
		if(opts.sourceType== opts.SOURCE_DEM){
			DEMElevationMap DEMEMap = new DEMElevationMap(opts.elevationFile);
			
			System.out.println("Computing elevationmap size...");
			DEMEMap.getSize();
			System.out.println("Loading elevations...");
			DEMEMap.loadElevations();
			eMap = DEMEMap.getElevationMap();
			
		}
	else if(opts.sourceType== opts.SOURCE_XYZ){
		XYZElevationMap xyzEMap= new XYZElevationMap(opts.elevationFile);
		System.out.println("Loading elevations...");
		xyzEMap.loadElevations();
		eMap = xyzEMap.getElevationMap();
	}
	else{
		eMap = null;
		System.out.println("This particular application doesn't yet support that sourceType");
		System.out.println("Exiting...");
		System.exit(0);
	}
	//eMap.dumpArea(0,40,0,40);
		
		MyTerrain mt = new MyTerrain(eMap);
		
		
		
		float eMapXRange = eMap.maxX - eMap.minX;
		if(opts.useScale){
			mt.setScale(opts.xScale, opts.yScale*opts.yExaggeration, opts.zScale);
		}
		else{
			float xScale, yScale, zScale;
			float scaleFactor = (eMapXRange<100)?1 : MyTerrain.IDEAL_TERRAIN_SIZE/(eMap.maxX-eMap.minX);
			xScale = yScale = zScale = scaleFactor;
			mt.setScale(xScale, yScale*opts.yExaggeration, zScale);
		}
		
		
		if(opts.textureMode == MyTerrain.TEXMODE_WRAP || opts.textureMode==MyTerrain.TEXMODE_REAL)
			mt.setTextureAppearance(opts.textureFile, opts.textureMode, opts.texture_wrapAt);
		else
			mt.setWireFrameAppearance();
		
		
		
		MyUniverse world = new MyUniverse();
		
		System.out.println("Creating Shape3D objects...");
		mt.createShapes();
		BranchGroup bg = mt.getBranchGroup();
		
		
		world.terrain.addChild(bg);
		world.terrain.compile();
		world.bindEventHandler();
		world.camera.setNominalView();
		
		world.camera.setCameraTransform(mt.getCoolViewTransform());
		
		System.out.println("GOING LIVE!...");
		world.goLive();
	}
	
}