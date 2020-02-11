import java.awt.*;

public class Main extends Frame
{
	public static void main(String[] args)
	{
		new Main().Init();
	}
	public void Init()
	{
		setBounds(0,0,500,500);
		setTitle("Hello");
		setParts();
		setVisible(true);
	}
	public void setParts()
	{
		Button b = new Button("OK");
		setLayout(null);
		b.setBounds(0,0,100,30);
		add(b);
	}
}

