import java.util.*;
import java.io.*;

class popAndPush
{
	public static void main(String[] args)
	{
		new popAndPush();
	}
	public popAndPush()
	{
		init();
	}
	private void init()
	{
		System.out.println("pop and push");
		push("/");
		push("home/");
		push("takesue090/");
		push("Downloads");
		System.out.println(pop());
		System.out.println(pop());
		System.out.println(pop());
		System.out.println(pop());
	}
	private void push(String x)
	{
		if(!full()){
			S[++top] = x;
		}
		else
		{
			System.out.println("full no push");
		}

	}
	private final int maxSize = 100;
	String[] S = new String[maxSize];
	int top = -1;
	private boolean empty()
	{
		if(top<0)return true;
		else return false;
	}
	private boolean full()
	{
		if(top>=maxSize-1)return true;
		else  return false;
	}

	private String pop()
	{
		if(!empty())
		{
			return S[top--];
		}
		else
		{
			System.out.println("empty!");
		}
		return S[top--];
	}
}

