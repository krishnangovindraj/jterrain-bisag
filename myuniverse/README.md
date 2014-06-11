----------------
1. Introduction
----------------
Abstraction to give easy camera control for the 3D viewer.

-----------------------------
2. Installing required java3d
-----------------------------
Determine where you want to install java3d. I've used the install path X:\j3d\



1. Download sun's java3d ( )
	Extract the contents to 
		X:\j3d\

2. Download j3d.org's java3d ( ftp://ftp.j3d.org/pub/code/j3d-org-code-1.1.0.tar.gz )
	Extract the contents to 
		X:\j3d\


CHECKPOINT:
	The directory 
	X:\j3d\ should now contain the subdirs:
		bin\
			j3dcore-ogl.dll
		jars\
			j3d-org-XXXX_V.V.V.jar
			(Lots of files with filenames with the above format where V.V.V is the version and XXXX reflects the contents)
			
		lib\
			ext\
				j3dcore
				j3dutils
				vecmath
	
	If it doesn't, Move stuff around and make it this way

3. Add these paths to your CLASS_PATH Environment variable:
		;X:\j3d\lib\ext\vecmath.jar;X:\j3d\lib\ext\j3dcore.jar;X:\j3d\lib\ext\j3dutils.jar;X:\j3d\jars\j3d-org-all_1.1.0.jar;X:\j3d\jars\j3d-org-elumens_1.1.0.jar;X:\j3d\jars\j3d-org-geom-core_1.1.0.jar;X:\j3d\jars\j3d-org-geom-hanim_1.1.0.jar;X:\j3d\jars\j3d-org-geom-particle_1.1.0.jar;X:\j3d\jars\j3d-org-geom-spline_1.1.0.jar;X:\j3d\jars\j3d-org-geom-spring_1.1.0.jar;X:\j3d\jars\j3d-org-geom-terrain_1.1.0.jar;X:\j3d\jars\j3d-org-inputdevice_1.1.0.jar;X:\j3d\jars\j3d-org-loader-3ds_1.1.0.jar;X:\j3d\jars\j3d-org-loader-c3d_1.1.0.jar;X:\j3d\jars\j3d-org-loader-core_1.1.0.jar;X:\j3d\jars\j3d-org-loader-dem_1.1.0.jar;X:\j3d\jars\j3d-org-loader-stl_1.1.0.jar;X:\j3d\jars\j3d-org-loader-vterrain_1.1.0.jar;X:\j3d\jars\j3d-org-navigation_1.1.0.jar;X:\j3d\jars\j3d-org-terrain_1.1.0.jar;X:\j3d\jars\j3d-org-texture_1.1.0.jar;X:\j3d\jars\j3d-org-util_1.1.0.jar;X:\j3d\jars\org.j3d.core_1.1.0.jar
	
	And this to your PATH variable:
		;X:\j3d\bin;


	(Take care for the semicolons. Change it as required)



-------------------------
3. Running the test file
-------------------------
Once you have set up both sun's java 3d and j3d.org's java 3d and configured the Environment variables accordingly, Fire up Command prompt.
Assumption: The MyUniverse package is extracted to X:\MyUniverse\
C:\>cd X:\MyUniverse
C:\>X:
X:\MyUniverse>
X:\MyUniverse>javac Test.java
X:\MyUniverse>java Test