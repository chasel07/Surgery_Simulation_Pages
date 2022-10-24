javac 	-cp jfreechart-1.5.0.jar   -d bin  ^
	src/Controller/*.java  ^
	src/Generator/*.java  ^
	src/library/*.java  ^
	src/Reality/*.java  ^
	src/View/*.java  ^
	src/View/Chart/*.java ^
	src/View/Page6/*.java ^
	src/View/Page5/*.java ^
	src/Model/Page5/*.java ^
	src/Model/*.java


xcopy /y raw bin\raw\ /E 
xcopy /y data bin\data\ /T
xcopy /y image bin\image\ /E 

mkdir bin\data\calender
mkdir bin\data\distribution
mkdir bin\data\script
mkdir bin\data\Standard_Deviation

copy jfreechart-1.5.0.jar bin 
copy colorTable.txt bin
copy ColorCodeTable.csv bin

cd bin
java -cp .;jfreechart-1.5.0.jar Reality.Main

cmd /k
