# Set this manually if your j3d is in another folder
J3D_BASE="${PWD}"
export CLASSPATH="${CLASSPATH}:.:${J3D_BASE}/lib/ext/*"
# If you're running on a 32-bit system, Edit the amd64 below to i386
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${J3D_BASE}/lib/amd64

javac src/*.java -d build 