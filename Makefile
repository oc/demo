SHELL := /bin/bash

all: war publish deploy

war: 
	play war demo -o build
	@mkdir dist
	jar cvf dist/demo.war -C build/ .

publish:
	mvn install:install-file -DgroupId=bekkopen -DartifactId=demo -Dversion=1 -Dpackaging=war -DgeneratePom=true -DcreateChecksum=true -Dfile=dist/demo.war

deploy:
	mvn deploy -f demo-jetty/pom.xml

clean:
	mvn clean -f demo-jetty/pom.xml
	rm -fr build
	rm -fr dist

again: clean all
