embedded jetty 9.2.14 Jersey webservices and WELD CDI (Siphyc project template)
=========================
Example project for embedded jetty as a server for jersey based web services 

This project tries to stay as close to the standard WAR deployable web application
 setup as possible. One major divergence from the standard is the jetty dependency
 unpacked packages at the root directory of the WAR, for the sake of completely elimination the need
for JavaEE containers or standalone servlet containers on the deployment/development servers,
 and limiting Server requirements to a working JVM. So the java web application could be run by
 typing "java -jar webapp.war" in a commandline. 
There are a couple of issues that make dealing with this project's setup a bit unconventional, 
and I am sure expert eyes can enhance and improve, your help is most welcome and appreciated!

Prerequisites:

Development machine:

1. java 1.7  (change the necessary POM dependencies for 1.6, but don't go bellow!)
2. maven, preferably 3.0.5
3. Linux machine (others might work, but I don't know if setup steps wold work exactly the same)
 

Deployment machine:

1. Linux Server (Not really required, but recommended)
2. JVM 1.7


Setup:

1. clone the project, cd into directory.
2. run "mvn clean package"
3. run "java -jar target/service-base-1.0-SNAPSHOT.war"
4. read misc/curls file to test the application web services.
5. suggest a fix or a better setup!
6. use the project to your needs as you see fit.


Configuration (with maven profiles):

The default configuration is set to 'dev' maven profile, which uses H2 memory database
and disables ssl and http to https redirection, to change this configuration, change 
the properties defined in src/main/resources/dev/config.properties, 
and see what happens, you're gonna need to provide a signed certificate, a keystore ...
There's another maven profile called dev2, which uses a mysql database as the web applications's 
data source, this requires setting up a mysql database (import misc/base.sql), checkout src/main/resources/dev2/config.properties
 

Debugging:  (better done after compiling with the -g flag)

The way I do it (rather primitive I know, but I can't bother learning the ways of an IDE) is by running:

java -Xdebug -Xrunjdwp:transport=dt_socket,address=8800,server=y,suspend=y \  
-jar .target/service-base-1.0-SNAPSHOT.war

from the project's root directory, this will have your jvm (or rather the java 
process that runs your application) listen (and wait) on port 8800 for a debugger to be attached,
I usually use NetBeans (pass the port number in the Debug>attach debugger dialog).

To learn more, read <a href="">embedded jetty web services example project</a>

