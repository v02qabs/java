import java.io.*;

class testMain{
	public static void main(String[] args){
		System.out.println("ok");
		new testMain();

	}
	public testMain(){
		jhiroclibg clib = new jhiroclibg();
		clib.clear();
		System.out.println(clib.blue);
		System.out.println("blue " + clib.blue);
	}
}


