SHELL := /bin/bash

all: war install

war: 
	play war demo -o dist
	jar cvf demo.war -C dist/ .

install:
	mvn install:install-file -DgroupId=bekkopen -DartifactId=demo -Dversion=1 -Dpackaging=war -DgeneratePom=true -DcreateChecksum=true -Dfile=demo.war

clean:
	rm -rf dist
	rm demo.war

again: clean all