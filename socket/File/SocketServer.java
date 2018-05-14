import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	final static int PORT = 8001;	// �Ҏ�|�[�g�ԍ�

	public static void main(String[] args) {
		String outputFilepath = "log.txt";       // ��M�����t�@�C���̕ۑ���
		byte[] buffer         = new byte[512]; // �t�@�C����M���̃o�b�t�@

		try {
			// �\�P�b�g�̏���
			ServerSocket serverSocket = new ServerSocket(PORT);
			Socket       socket       = serverSocket.accept();

			// �X�g���[���̏���
			InputStream  inputStream  = socket.getInputStream();
			OutputStream outputStream = new FileOutputStream(outputFilepath);

			// �t�@�C�����X�g���[���Ŏ�M
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}

			// �I������
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