install:
	ecj -nowarn it
	ecj -cp . item.java
	dx --dex --output=item.jar  .
	dalvikvm -cp item.jar item
