import java.io.IOException;

import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		new MainActivity().init();
	}
	private void init()
	{
		try {
			org.jsoup.nodes.Document document = Jsoup.connect("http://takos.chobi.net").get();
			Elements elements = document.select("h1");
			for (Element element : elements) {
			    System.out.println(element.text());
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
