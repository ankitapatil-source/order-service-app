@echo off
REM seed-git-history.bat
REM
REM Initialises this project as a Git repository with a small, realistic commit
REM history (instead of one big "Initial commit"). Gives you something to
REM explore in Assignment 2.
REM
REM Usage (from the project root):
REM   scripts\seed-git-history.bat
REM
REM Then:
REM   git remote add origin <YOUR_REPO_URL>
REM   git push -u origin main

setlocal
cd /d "%~dp0.."

if exist ".git" (
  echo This project already has a .git directory. Remove it first to re-seed.
  exit /b 1
)

git init -q
git branch -M main

call :commit 2026-07-14 "Project scaffold: Maven build, README, gitignore" pom.xml .gitignore README.md logs\.gitkeep
call :commit 2026-07-15 "Add domain model (product, customer, coupon, order)" src\main\java\com\rsl\orderservice\model
call :commit 2026-07-16 "Add in-memory repositories" src\main\java\com\rsl\orderservice\repository
call :commit 2026-07-17 "Add logging setup" src\main\java\com\rsl\orderservice\util
call :commit 2026-07-18 "Add pricing and inventory services" src\main\java\com\rsl\orderservice\service\PricingService.java src\main\java\com\rsl\orderservice\service\InventoryService.java
call :commit 2026-07-21 "Add discount/coupon logic and order flow" src\main\java\com\rsl\orderservice\service\DiscountService.java src\main\java\com\rsl\orderservice\service\OrderService.java
call :commit 2026-07-22 "Add application entry point with sample scenarios" src\main\java\com\rsl\orderservice\App.java
call :commit 2026-07-23 "Add unit tests" src\test

echo.
echo Done. Commit history:
git --no-pager log --oneline
echo.
echo Next: add your GitHub remote and push, e.g.
echo   git remote add origin ^<YOUR_REPO_URL^>
echo   git push -u origin main
exit /b 0

:commit
set "CDATE=%~1"
set "CMSG=%~2"
shift
shift
:addloop
if "%~1"=="" goto docommit
git add "%~1"
shift
goto addloop
:docommit
set "GIT_AUTHOR_DATE=%CDATE%T10:00:00"
set "GIT_COMMITTER_DATE=%CDATE%T10:00:00"
git commit -q -m "%CMSG%"
echo   committed: %CMSG%
exit /b 0
