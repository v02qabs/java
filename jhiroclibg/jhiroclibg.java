import java.io.*;
import java.lang.*;
import java.util.*;


class jhiroclibg {
       public static void main(String[] args){
       	System.out.println("Hello jhiroclibg");
	new jhiroclibg();

       }
       public jhiroclibg(){
		test_blue();
       }

	public void clear(){
	  System.out.printf("\033[2J]");	
	}
	public static String blue = "\u001b[00;34m";
	public void test_blue(){
		System.out.println(blue + "Hello");
	}


}

