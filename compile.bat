@echo off
if not exist build mkdir build
javac -d build src\main\java\central_banking_system\*.java
if exist src\main\resources xcopy /E /I /Y src\main\resources build >nul
