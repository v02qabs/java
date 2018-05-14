import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	final static int PORT = 8001;	// 待受ポート番号

	public static void main(String[] args) {
		String outputFilepath = "log.txt";       // 受信したファイルの保存先
		byte[] buffer         = new byte[512]; // ファイル受信時のバッファ

		try {
			// ソケットの準備
			ServerSocket serverSocket = new ServerSocket(PORT);
			Socket       socket       = serverSocket.accept();

			// ストリームの準備
			InputStream  inputStream  = socket.getInputStream();
			OutputStream outputStream = new FileOutputStream(outputFilepath);

			// ファイルをストリームで受信
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}

			// 終了処理
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}