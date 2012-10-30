rem Install maven artifacts
call mvn install:install-file -DgroupId=org.stripesstuff -DartifactId=stripesstuff -Dversion=0.3 -Dpackaging=jar -Dfile=stripesstuff-0.3.jar -DgeneratePom=true -DcreateChecksum=true
