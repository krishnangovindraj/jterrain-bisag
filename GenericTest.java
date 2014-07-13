class GenericTest{
	public static void main(String args[]){
		ElevationMap eMap = new ElevationMap();
		
		int copyData[][] =	{
								{1,1,1,1,1},
								{1,1,1,1,1},
								{1,1,1,1,1},
								{1,1,1,1,1},
								{1,1,1,1,1}
							};
					
					
		int size = copyData.length;
		eMap.numRows = eMap.numColumns = size;
		eMap.gridSize = size;
		eMap.elevations = new MyPoint3f[eMap.numColumns][eMap.numRows];
		
		for(int i=0;i<eMap.numColumns; i++){
			for(int j=0;j<eMap.numRows; j++){
				eMap.elevations[i][j] = new MyPoint3f(i,j,copyData[i][j]);
			}
		}
		eMap.computeMapDimensions();
		MyTerrain mt = new MyTerrain(eMap);
		
		float xScale, yScale, zScale;
		float eMapXRange = eMap.maxX - eMap.minX;
		float scaleFactor = (eMapXRange<100)?1 : MyTerrain.IDEAL_TERRAIN_SIZE/(eMap.maxX-eMap.minX);
		xScale = yScale = zScale = scaleFactor;
		
		mt.setScale(xScale,yScale,zScale);
		
		//mt.setTextureMode(MyTerrain.TEXMODE_WRAP, 200);mt.setAppearance(getAppearance(".\\DEM\\texture1.jpg"));	//You can also load it like this ;D
		mt.setWireFrameAppearance();	//You can also load it like this ;D
		
		
		
		MyUniverse world = new MyUniverse();
		
		System.out.println("Creating Shape3D objects...");
		mt.createShapes();
		
		
		world.terrain.addChild(mt.getBranchGroup());
		world.terrain.compile();
		world.bindEventHandler();
		world.camera.setNominalView();
		
		float scales[] = { xScale, yScale, zScale};
		world.camera.setCameraTransform(mt.getCoolViewTransform());
		
		//world.camera.setCameraPosition( );
		System.out.println("GOING LIVE!...");
		world.goLive();
	}
}