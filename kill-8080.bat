@echo off
setlocal EnableExtensions

rem Usage: kill-8080.bat [port]   (defaults to 8080)
set "PORT=%~1"
if "%PORT%"=="" set "PORT=8080"

echo ============================================================
echo  Kill process(es) listening on TCP port %PORT%
echo  (If a kill is denied, re-run this script as Administrator)
echo ============================================================

set "KILLED="
for /f "tokens=5" %%P in ('netstat -ano ^| findstr /C:":%PORT% " ^| findstr /C:"LISTENING"') do (
    set "KILLED=1"
    echo  Killing PID %%P ...
    rem /F force kill, /T also kills child processes (needed for Java apps)
    taskkill /F /T /PID %%P >nul 2>&1
)
if not defined KILLED echo  No process found listening on port %PORT%.

rem Verify the port is actually free (retry up to 5 times)
set /a RETRY=0
:verify
set /a RETRY+=1
netstat -ano | findstr /C:":%PORT% " | findstr /C:"LISTENING" >nul
if errorlevel 1 goto :free
if %RETRY% GEQ 5 goto :still_busy
timeout /t 1 /nobreak >nul
goto :verify

:free
echo  SUCCESS: Port %PORT% is now free.
goto :end

:still_busy
echo  WARNING: Port %PORT% is still in use after %RETRY% checks.
echo  Re-run this script as Administrator, or stop the process manually.

:end
endlocal
pause
