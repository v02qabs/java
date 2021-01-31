import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://sn39s.work/index.html").get();
        // 各記事のaタグを取得。jQueryのセレクターと同じ感じで記載
        Elements newsHeadlines = doc.select("div.content_title");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.text());
        }
    }
}
