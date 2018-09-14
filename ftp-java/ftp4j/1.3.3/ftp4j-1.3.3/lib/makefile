install:
	ecj -cp . Main.java
	dx --dex --output=Main.jar .
	dalvikvm -cp Main.jar Main

remove:
	rm -rf *.class *.jar
