remove:
	 rm -rf *.class *.jar

start:
	ecj test.java
	ecj -cp . Main.java
	dx --dex --output=Main.jar *.class
	dalvikvm -cp Main.jar Main
