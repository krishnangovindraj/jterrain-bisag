import java.lang.IllegalArgumentException;
class CLIOptions{
	public String args[];
	public int atIndex;
	
	public String 	elevationFile, textureFile;	
	public int textureMode;
	public int texture_wrapAt;
	public float 	xScale,yScale,zScale;
	public float yExaggeration;
	
	public boolean 	useScale, useSubMap;
	public int 		subMap_startI, subMap_endI, subMap_startJ, subMap_endJ;
	
	public int sourceType;
	public int  SOURCE_DEM = 1,
				SOURCE_XYZ = 2;
	
	public CLIOptions(String cliArgs[]){
		args = cliArgs;
		setDefaults();
	}
	
	//Edit this the values in this to suit your liking. Later migrate this to a properties file
	public void setDefaults(){
		sourceType = 0;
		yExaggeration = 1;
		elevationFile = null;
		textureMode = MyTerrain.TEXMODE_REAL;
		textureFile = null;
		useScale = false;
		texture_wrapAt= 100;
		xScale = yScale = zScale = 1;
		useSubMap = false;
		subMap_startI = subMap_endI = subMap_startJ = subMap_endJ = 0;
		
	}
	
	public void assertLength(int length, String errorMessage) throws IllegalArgumentException{
		if(args.length<length)
			throw(new IllegalArgumentException(errorMessage));
	}
	
	public void parseArgs() throws IllegalArgumentException{
		
		assertLength(3,"Atleast 2 arguments expected - \n\t Elevation file and textureMode[+file, when applicable]");
		atIndex =0;
		switch(args[0]){
			case "dem": sourceType = SOURCE_DEM;
				break;
			case "xyz": sourceType = SOURCE_XYZ;
				break;
			default: throw(new IllegalArgumentException("Invalid source type"));
		}
		
		elevationFile = args[1];
		
		atIndex = 3;
		switch(args[2]){
			case "wireframe": textureMode = MyTerrain.TEXMODE_WIREFRAME;break;
			case "real": 
						 assertLength(4,"That texture mode requires a textureFile");
						 textureMode = MyTerrain.TEXMODE_REAL;
						 textureFile = args[atIndex++];
					break;
			case "wrap": 
						 assertLength(4,"That texture mode requires a textureFile ( and takes an optional wrapLength )");
						 textureMode = MyTerrain.TEXMODE_WRAP;
						 textureFile = args[atIndex++];
						 if(args[atIndex].charAt(0)!='-')
							texture_wrapAt = Integer.parseInt(args[atIndex++]);
					break;
			
			default: throw(new IllegalArgumentException("Invalid textureMode"));
		}
		
		String option;
		while(atIndex < args.length){
			option = args[atIndex++];
			if(option.charAt(0)!='-'){
				System.out.printf("\nUnrecognizable option %s. (Did you forget the '-' ?)", option);
				continue;
			}
			switch(option){
				case "-s": 
				case "--scale": assertLength(atIndex+3, "3 arguments expected after -s/--scale ");
								useScale = true;
								xScale = Float.parseFloat(args[atIndex]);
								yScale = Float.parseFloat(args[atIndex+1]);
								zScale = Float.parseFloat(args[atIndex+2]);
								atIndex+=3;
						break;
				
				case "-y": yExaggeration = Float.parseFloat(args[atIndex++]);
						break;
				
				case "--submap": assertLength(atIndex+3, "4 arguments expected after --submap");
								 useSubMap=true;
								 subMap_startI = Integer.parseInt(args[atIndex]);
								 subMap_endI = Integer.parseInt(args[atIndex+1]);
								 subMap_startJ = Integer.parseInt(args[atIndex+2]);
								 subMap_endJ = Integer.parseInt(args[atIndex+3]);
								 atIndex+=4;;
						break;
				default: System.out.printf("\nUnrecognizable option %s", option);
			}
			
		}
		
	}
	
	public void blurtHelp(String specificError){
		System.out.println("\n\nInvalid usage: " + specificError);
		System.out.print(
			 " \n\nUsage is: "
			+"\n   >java DEMViewer <sourceType> <sourceFile> <textureMode> [<textureFile>]  [Options]"
			+"\n\n See below for details and values of textureMode"
			+"\n Example:"
			+"\n   >java DEMViewer dem demfile.dem real texture.jpg -s 0.1 0.2 0.1"
			+"\n"
			+"\n Valid values for sourceType are: dem, xyz"
			+"\n"
			+"\n Valid values for textureMode are: "
			+"\n   wireframe, real <textureFile>, wrap <textureFile>"
			+"\n "
			+"\n Options are: "
			+"\n   -s <xScale> <yScale> <zScale>"
			+"\n   -y <yExaggeration>"
			+"\n   --scale <xScale> <yScale> <zScale>"
			+"\n   --subMap <startI> <endI> <startJ> <endJ>"
			+"\n   "
			+"\n ******* "
			+"\n "
		);
 /*System.out.print(
 " Usage is: \
\n java DEMViewer <elevationFile> <textureMode> [<textureFile>]  [Options]\
\n See below for details and values of textureMode\
\n Example:\
\n java DEMViewer demfile.dem real texture.jpg -s 0.1 0.2 0.1\
\n \
\n Valid values for textureMode are: \
\n wireframe, real <textureFile>, wrap <textureFile>\
\n \
\n Options are \
\n\t -s <xScale> <yScale> <zScale>\
\n\t \
\n\t --- \
\n\
		");
	*///Why java? Why?!
	
		
	}
	
	public void blurtValues(){
		String formatString = "\nElevationFile: %s, textureMode: %d, textureFile:%s, texture_wrapAt: %d"+
							  "\nUseScale: %s, Scales (%f,%f,%f) "+
							  "\nyExaggeration: %f"+
							  "\nuseSubMap: %s, (startI,endI, startJ,endJ) = (%d,%d,%d,%d)";
		String useSubMapString= (useSubMap)?"true":"false";
		String useScaleString = (useScale)?"true":"false";
		System.out.printf( formatString, 
								elevationFile, textureMode, textureFile, texture_wrapAt,
								useScale,	xScale,yScale,zScale,  
								yExaggeration,
								useSubMapString,	subMap_startI, subMap_endI, subMap_startJ, subMap_endJ);			
	}
	
	
}