import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloader {
    public static void main(String[] args) {
        // ダウンロードするファイルのURL
        String fileUrl = "https://www.google.com";

        // 保存先のファイルパス
        Path savePath = Paths.get("./index.html");

        // HttpClient の作成
        HttpClient client = HttpClient.newHttpClient();

        // リクエストの作成
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .GET()
                .build();

        try {
            // レスポンスの取得（ファイルとして保存）
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(savePath));

            // ステータスコードの確認
            if (response.statusCode() == 200) {
                System.out.println("ファイルをダウンロードしました: " + savePath.toAbsolutePath());
            } else {
                System.out.println("ダウンロード失敗: ステータスコード " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
