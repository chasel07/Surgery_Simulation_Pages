javac -d bin model/*.java controller/*.java view/*.java

xcopy /y raw bin\src\page1\raw\ /E 
xcopy /y data bin\src\page1\data\ /E
xcopy /y image bin\src\page1\image\ /E 

mkdir bin\src\page1\data\calender
mkdir bin\src\page1\data\distribution
mkdir bin\src\page1\data\script
mkdir bin\src\page1\data\Standard_Deviation

cd bin
java page2.controller.Main

cmd /k
