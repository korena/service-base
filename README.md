embedded jetty 9.2.2 webservices and CDI evaluation 
=========================

evaluation of embedded jetty as a server for jersey based web services 


This is an adaptation from an old embedded jetty setup. Basically, the following was achieved for the purpose of evaluation:

1. Update to the latest Jetty version to date (9.2.2.v20140723).
2. Latest weld CDI was incorporated.
3. Latest Jersey version added with Servlet API 3.1 annotation based initialization

This project is based on maven, which is arguably the most sensible way to work with java applications. I tried my best to stay as close
to the standard WAR deployable web application setup as possible, and will probably re-iterate on the setup for production. One major divergence
from the standard is the jetty dependency unpacked packages at the root directory of the WAR, for the sake of completely elimination the need
for JavaEE containers on the deployment server, and limiting Server requirements to a working JVM. So I could deploy my application by runing "java -jar webapp.war" on the production server. There are a couple of issues that make dealing with this project's setup a bit unconventional, 
and I am sure expert eyes can enhance and improve, your help is most welcome and appreciated!

Prerequisites:

Development machine:

1. java 1.7  (change the necessary POM dependencies for 1.6, but don't go bellow!)
2. maven, preferably 3.0.5
3. Linux machine (others might work, but I don't know if setup steps wold work exactly the same)
 

Deployment machine:

1. Linux Server (Not really required, but recommended)
2. JVM 1.7 (change the necessary POM dependencies for 1.6, but don't go bellow!)


Setup:

1. clone the project, cd into directory.
2. run "mvn clean package"
3. run "java -jar webapp.war"
4. browse to  "http://localhost:8080/webapp"    for static contents
5. browse to  "http://localhost:8080/webapp/testServlet"    to test CDI injection in servlets.
6. browse to  "http://localhost:8080/webapp/api/Service/usrAuth"    to test jersey.
7. browse to  "http://localhost:8080/webapp/api/Service/injection"  to test dependency injection in jersey service
8. suggest a fix or a better setup!

Debugging:  (better done after compiling with the -g flag)

The way I do it (rather primitive I know, but I can't bother learning the ways of an IDE) is by running:

java -Xdebug -Xrunjdwp:transport=dt_socket,address=8800,server=y,suspend=y \  -jar ./target/embedded_jetty_evaluate-1.0-SNAPSHOT.war

from the project's root directory, this will have your jvm (or rather the java process that runs your application) listen (and wait) on port 8800 for a debugger to be attached,
I usually use NetBeans (pass the port number in the Debug>attach debugger dialog).


