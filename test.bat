@echo off
call compile.bat
java -cp build central_banking_system.SystemSmokeTest
