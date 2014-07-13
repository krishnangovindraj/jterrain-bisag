//import com.sun.j3d.loaders;
import org.j3d.loaders.dem.*;
import java.io.*;
import static java.lang.System.*;

public class MyDEM{
	String sourceFilename;
	public DEMParser parser;
	public DEMTypeARecord typeA;
	public DEMTypeBRecord typeB[];
	public DEMTypeCRecord typeC;
	///public float[][] heights;
	public float gridStep[];
	
	public MyDEM(String fname){
		sourceFilename = fname;
	}
	
	public void loadRecords(){
		try{
			System.out.println("Loading DEM Records...");
			FileInputStream fis= new FileInputStream(sourceFilename);
			parser = new DEMParser(fis);
			parser.parse(false);
			gridStep = parser.getGridStep();
			typeA = parser.getTypeARecord();
			typeB = parser.getTypeBRecords();
			typeC = parser.getTypeCRecord();
		}
		catch(FileNotFoundException fnfE){
			System.out.println("File not found!!!");
			System.exit(1);
		}
		catch(IOException IOE){
			System.out.println("File not found!!!");
			System.exit(1);
		}
	}
	
}