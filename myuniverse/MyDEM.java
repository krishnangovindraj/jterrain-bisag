//import com.sun.j3d.loaders;
import org.j3d.loaders.dem.*;
import java.io.*;
import static java.lang.System.*;

public class MyDEM{
	public DEMParser parser;
	public DEMTypeARecord typeA;
	public DEMTypeBRecord typeB[];
	public DEMTypeCRecord typeC;
	///public float[][] heights;
	public float gridStep[];
	
	public MyDEM(){}
	
	public void loadRecords(String filename){
		try{
			FileInputStream fis= new FileInputStream(filename);
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