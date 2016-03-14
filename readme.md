# Java3D based Terrain viewer.
( Note: This readme was written ( in a hurry ) for a linux system ( as are all the bash files ). The dependencies come with installation instructions and the path setting is very similar in windows and linux. You shouldn't have trouble figuring it out.

I also hope someone will improve the documentation because i can't remember a thing i did 2 years ago. )
### Installation
The code has some dependencies:
- java3d: https://java3d.java.net/binary-builds.html
	- Contains the required .so file ( .dll if windows ) and 3 core java3d jars ( vecmath, j3dcore and j3dutils )
- j3d.org community project: http://aviatrix3d.j3d.org/download.html
	-  Contains many useful .jar files
- Extract the contents to the lib/ folder. 
	See the readme for a hint of what you should expect.

### Compiling 
This project needs to find the dependencies to run. The class path needs to be set to the location of all the .jar files. You can do this in your shell by running the command

`export CLASSPATH="/path/to/j3d/lib/ext/*` where ext contains all the jar files.

You will also need the libj3dcore-ogl.so file to be discoverable.

`export LD_LIBRARY_PATH="/path/to/j3d/lib/amd64/"`

It's also handy to do all this in a single bash file. View compile.sh for a better idea.

### Running 
There is a DEMViewerExample file that can be run to see how it works. Simply run 
`bash runexample.sh`. Remember to extract the provided sample data 