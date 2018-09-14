class Main
{
    public Main()
    {
	initalize();
    }
    
    public static void main(String[] args)
    {
	new Main();
    }
    public void initalize()
    {
	test.Message = "Japan.";
	test t = new test();
	t.echoMessage();
    }
    
}
