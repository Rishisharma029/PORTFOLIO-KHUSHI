@echo off
echo ===================================================
echo   Building and Exporting Portfolio for GitHub Pages
echo ===================================================
call .\mvnw.cmd test -Dtest=StaticSiteExportTest
if %ERRORLEVEL% equ 0 (
    echo.
    echo [SUCCESS] Static site generated in folder: ./dist
    echo You can open ./dist/index.html in your browser!
) else (
    echo.
    echo [ERROR] Build failed. Please check error logs above.
)
pause
