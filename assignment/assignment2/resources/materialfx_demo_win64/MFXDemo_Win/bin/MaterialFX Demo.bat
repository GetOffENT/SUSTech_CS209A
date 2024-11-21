@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\java"



pushd %DIR% & %JAVA_EXEC% %CDS_JVM_OPTS% -Dglass.disableGrab=true -p "%~dp0/../app" -m MaterialFX.Demo/io.github.palexdev.materialfx.demo.Demo  %* & popd
