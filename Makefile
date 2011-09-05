SHELL := /bin/bash

all: war install

war: 
	play war demo -o build
	@mkdir dist
	jar cvf dist/demo.war -C build/ .

install:
	mvn install:install-file -DgroupId=bekkopen -DartifactId=demo -Dversion=1 -Dpackaging=war -DgeneratePom=true -DcreateChecksum=true -Dfile=dist/demo.war

clean:
	rm -fr build
	rm -fr dist

again: clean all