install:
	cd ./src
	javac -cp ../lib/*.jar: fenc.java -d ../bin
	cd ../bin
	dx --dex --output=fenc.jar fenc.class ../lib/*.jar
