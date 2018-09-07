class Split
{
	public Split()
	{
	}
	public void getSplit(String com)
	{
		String s0 = com;
		String s1[] = s0.split(" ");
		System.out.println("s1 = " + s1[1]);
		String s12 = s1[1];
		FileP fp = new FileP();
		fp.getPath(s12);
	}
}

