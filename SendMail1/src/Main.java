import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * シンプルなメール送信サンプル。
 */
public class Main {
    // 日本語メールの場合には ISO-2022-JPがオススメ。
    // UTF-8だと受信時に文字化けしてしまうメーラが世の中には依然として存在しています。
    private static final String ENCODE = "ISO-2022-JP";

    public static void main(final String[] args) {
		System.out.println("メール送信。");
		sendmail s = new sendmail();
		//s.process();
		//System.out.println("メール受信");
	}

}

