import javax.vecmath.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Comparator;


public class MyPlane{
	float a,b,c,d,
		denom;//Denominator in distance equation
	public static float tolerance=0.0001f;
	ArrayList<MyPoint3f> points;
	TreeSet<MyPoint3f> vertices;
	TreeMap<MyPointIJ, MyPointIJ> polygonEdges;//Used toe build the polygonArray later
	
	int minI, minJ, maxI,maxJ;
	

	public MyPlane(MyPoint3f A, MyPoint3f B, MyPoint3f C){
		Vector3f v1= new Vector3f(A.x-B.x,A.y-B.y,A.z-B.z);
		Vector3f v2= new Vector3f(B.x-C.x,B.y-C.y,B.z-C.z);
		
		v2.cross(v1,v2);
		
		a = v2.x;
		b = v2.y;
		c = v2.z;
		
		Vector3f v3 = new Vector3f(A.x, A.y, A.z);
		d = -1 * v2.dot(v3);
		
		denom = (float) Math.sqrt( a*a + b*b + c*c );
		
		points = new ArrayList<MyPoint3f>();
		vertices = new TreeSet<MyPoint3f>(new MyPlanePositionComparator());
		polygonEdges = new TreeMap<MyPointIJ, MyPointIJ>(new MyPointIJComparator());
	}
	
	public static void setTolerance(float t){
		tolerance = t;
	}
	
	public boolean isOnPlane(MyPoint3f p){
		float distance = a*p.x + b*p.y + c*p.z + d;
		distance = distance/denom;
		if(distance<0)
			distance= -1*distance;
		//System.out.printf("At a distance of %f... ", distance);
		return (distance<tolerance);
	}
	
	public void push(MyPoint3f p){
		if(p.i > maxI) 
			maxI = p.i;
		else if( p.i < minI )
			minI = p.i;
		
		if(p.j > maxJ) 
			maxJ = p.j;
		else if( p.j < minJ )
			minJ = p.j;
			
		points.add(p);
	}
	
	public void addToVertices(MyPoint3f p){
		vertices.add(p);
	}
	
	
	public void addPolygonEdge(MyPoint3f P1, MyPoint3f P2){
		//polygonEdges.add(new PolygonEdge(p1,p2));//Directional. Make sure you pass p1,p2 in the right order. Start points will be unique and are our Primary Key
		//MyPointIJ p1  = new MyPointIJ(P1.i,P1.j);		MyPointIJ p2 = new MyPointIJ(P2.i,P2.j);		polygonEdges.put(p1,p2);
		polygonEdges.put(new MyPointIJ(P1.i,P1.j), new MyPointIJ(P2.i,P2.j));
		//System.out.printf("\nPUSHED {(%d,%d) : (%d,%d) } ",p1.i,p1.j,  p2.i,p2.j );
	}
	
	
	public MyPointIJ getNextPoint(MyPointIJ p){	//More control over the flag
		return (MyPointIJ)polygonEdges.get(p);
	}
}

class MyPointIJComparator implements Comparator<MyPointIJ>{
	public int compare(MyPointIJ p1, MyPointIJ p2){
		if( p1.i>p2.i )
			return 1;
		else if(p1.i < p2.i )
			return -1;
		else if(p1.i == p2.i ){
			if(p1.j>p2.j)
				return 1;
			else if(p1.j < p2.j)
				return -1;
		}
		return 0;
	}
}

class MyPlanePositionComparator implements Comparator<MyPoint3f>{
	public int compare(MyPoint3f p1, MyPoint3f p2){
		if( p1.i>p2.i )
			return 1;
		else if(p1.i < p2.i )
			return -1;
		else if(p1.i == p2.i ){
			if(p1.j>p2.j)
				return 1;
			else if(p1.j < p2.j)
				return -1;
		}
		return 0;
	}
}
