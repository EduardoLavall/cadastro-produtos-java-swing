@echo off
cd /d "%~dp0"
where ant >nul 2>&1
if errorlevel 1 (
    echo Apache Ant nao encontrado. Abra o projeto pelo Apache NetBeans.
    pause
    exit /b 1
)
call ant run
if errorlevel 1 pause
