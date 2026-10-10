#!/usr/bin/env bash
echo "==================================================="
echo "  Building and Exporting Portfolio for GitHub Pages"
echo "==================================================="
chmod +x ./mvnw
./mvnw test -Dtest=StaticSiteExportTest
if [ $? -eq 0 ]; then
    echo ""
    echo "[SUCCESS] Static site generated in folder: ./dist"
    echo "You can open ./dist/index.html in your browser!"
else
    echo ""
    echo "[ERROR] Build failed. Please check error logs above."
fi
