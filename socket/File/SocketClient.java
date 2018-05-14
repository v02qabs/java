import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
	final static String HOST = "192.168.0.6"; // �ڑ���A�h���X
	final static int    PORT = 8001;        // �ڑ���|�[�g�ԍ�

	public static void main(String[] args) {
		String filepath = "f.in";             // ���M����t�@�C���̃p�X
		File   file     = new File(filepath); // ���M����t�@�C���̃I�u�W�F�N�g
		byte[] buffer   = new byte[512];      // �t�@�C�����M���̃o�b�t�@

		try {
			// �\�P�b�g�̏���
			Socket socket = new Socket(HOST, PORT);

			// �X�g���[���̏���
			InputStream  inputStream  = new FileInputStream(file);
			OutputStream outputStream = socket.getOutputStream();

			// �t�@�C�����X�g���[���ő��M
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}

			// �I������
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}