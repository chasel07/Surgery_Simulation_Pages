javac -cp jfreechart-1.5.0.jar -d bin model/*.java controller/*.java view/*.java

set JRoot=bin\src\page6

xcopy /y raw %JRoot%\raw\ /E 
xcopy /y data %JRoot%\data\ /E
xcopy /y image %JRoot%\image\ /E 
copy jfreechart-1.5.0.jar bin

mkdir %JRoot%\data\calender
mkdir %JRoot%\data\distribution
mkdir %JRoot%\data\script
mkdir %JRoot%\data\Standard_Deviation

cd bin
java -cp .;jfreechart-1.5.0.jar page6.controller.Main

cmd /k
