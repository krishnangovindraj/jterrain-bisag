import javax.vecmath.*;
import java.security.InvalidParameterException;

public class MyPoint3f extends Point3f{

	public static int maxI=0,maxJ=0;
	public static int _f[] = { /*0,*/1, 2, 4, 8, 16, 32 };
	public static int _add[][] ={ 
									{0,1,1,1},
									{1,1,1,0},
									{1,0,0,-1},
									{0,-1,-1,-1},
									{-1,-1,-1,0},
									{-1,0,0,1}
								};/*{ {-1,-1,-1,-1},
									{1,0,1,1},
									{1,1,0,1},
									{0,1,-1,0},
									{-1,0,-1,-1},
									{-1,-1,0,-1},
									{0,-1,1,0}
								};	//What to add for the locations
								*/
	
	/*	Location legend:
		
		X 2 1
		3 + 0
		4 5 X
	*/
	
	public int i,j,h;
	public int crawlNext;	// Which plane to consider next in our crawling
	public static float xScale=1,yScale=1,zScale=1;
	short explored;	//flag. Each bit indicates whether a possible plane passing through the point has been considered.
					//There are 8 possible neighbours (how perfect). Numbering starts at 1 which is at 3'o clock ( units place ), increases ccw ( 1:30 => tens' place (twos' place in binary :p ))
					//Since you need 3 points for a plane, The plane through n means the plane through thisPoint, the neighbouringPoint at n and the next valid neighbouringPoint 
					//The next valid point is at (n+1)%8  if you can draw diagonals in any direction. BUT:
					//For now, Since i cut a square along only 1 constant diagonal, Most of these bits aren't going to be used ( as there are only 6 valid planes through a point), 
					//	The valid point has to be chosen as the next ccw point through which a valid plane exists
	
	
	public static void setLimits(int iLimit,int jLimit){
		maxI = iLimit-1;
		maxJ = jLimit-1;
	}
	
	public void setCrawlNext(int location){
		crawlNext = location;
	}
	
	public static void setScale(float X,float Y,float Z){
		xScale = X;
		yScale = Y;
		zScale = Z;
	}
	public static void setScale(float scale[]){
		xScale = scale[0];
		yScale = scale[1];
		zScale = scale[2];
	}
	
	public static float[] getScale(){
		float scale[] = {xScale,yScale,zScale};
		return scale;
	}
	
	public void setFlag(int location){
		explored|= _f[location];
	}
	
	public boolean isExplored(int location){
		return ((explored & _f[location])!=0);
	}
	
	public MyPoint3f(){}
	
	public MyPoint3f(int I,int J, int H){
		
		explored = 0;
		i = I;
		j = J;//
		/*//Might be unnecessary
		//Set  corners as done since no plane can exist through them?
		if( i==0 ) 		explored|= _f[2] | _f[3] | _f[4];
		if( i==maxI) 	explored|= _f[0] | _f[1] | _f[5];
		
		
		if( j==0 ) 		explored|= _f[3] | _f[4] | _f[5];
		if( j==maxJ ) 	explored|= _f[0] | _f[1] | _f[2];
		//*/
		h = H;
		
		x = xScale * i;
		y = yScale * h;
		z = zScale * j;
	}
	
	
	public int locationOf(MyPoint3f p) throws InvalidParameterException{
		int I = p.i - i;
		int J = p.j - j;
		/*	X 2 1
			3 + 0
			4 5 X	*/
		if(I==0){
			if(J==1)
				return 0;
			if(J==-1)
				return 3;
		}
		
		if(I==1){
			if(J==0)
				return 2;
			if(J==1)
				return 1;
		}
		
		if(I==-1){
			if(J==0)
				return 5;
			if(J==-1)
				return 4;
		}
		
		System.out.printf("(%d,%d) and (%d,%d) are not neighbours", i,j , p.i,p.j);
		throw( new InvalidParameterException("That point ( i,j = "+I + ", "+ J + " ) is not a valid neighbour"));
	}
	
}


	/*	Location legend:
		 ________
		|  /|  /|
		|X/2|1/0|
		|/__|/__|
		|  /|  /|
		|3/4|5/ |
		|/__|/__|
	*/