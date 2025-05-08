install:
	javac -h . NativeExample.java
	g++ -shared mylibrary.cpp -o libmylibrary.so -I /home/user/jdk17/include/ -I /home/user/jdk17/include/linux/

all:
	java -Djava.library.path=. NativeExample
