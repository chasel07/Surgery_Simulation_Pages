javac -d bin model/*.java controller/*.java view/*.java

set JRoot=bin\src\page4

xcopy /y raw %JRoot%\raw\ /E 
xcopy /y data %JRoot%\data\ /E
xcopy /y image %JRoot%\image\ /E 

mkdir %JRoot%\data\calender
mkdir %JRoot%\data\distribution
mkdir %JRoot%\data\script
mkdir %JRoot%\data\Standard_Deviation
  
cd bin
java page4.controller.Main

cmd /k
