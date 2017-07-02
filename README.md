RayTracer
=========

This is a ray tracer project implemented in Java.
In order to run RayTracer clone the Master branch and build it using maven.
Maven 3 or above is required. Also `Oracle Java 8.0` or above must be available in your system.
Use the command `mvn package -Dmaven.test.skip=true` to build the project without running 
tests since some tests are currently failing (work is ongoing to fix them).

You will have two packages under target directory after a successful build.
Choose the package with dependencies.
Run it by the command `java -jar PACKAGE_NAME`.
From the gui window select a xml file under `${basedir}/src/main/resources/ppm` 
folder and click on `Trace the scene` button!

