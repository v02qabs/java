
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


public class sendmail
{
	private static final String ENCODE = "UTF-8";

    public void process() {
        final Properties props = new Properties();

        // 基本情報。ここでは gmailへの接続例を示します。
        props.setProperty("mail.smtp.host", "smtp.mail.yahoo.co.jp");
        // SSL用にポート番号を変更。
        props.setProperty("mail.smtp.port", "465");

        // タイムアウト設定
      //  props.setProperty("mail.smtp.connectiontimeout", "60000");
       // props.setProperty("mail.smtp.timeout", "60000");

        // 認証
        props.setProperty("mail.smtp.auth", "true");

        // SSL関連設定
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");

	
        final Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("z1bcde", "GqPs1#9#");
            }
        });

        // デバッグを行います。標準出力にトレースが出ます。
        session.setDebug(false);

        // メッセージ内容の設定。
        final MimeMessage message = new MimeMessage(session);
        try {
            final Address addrFrom = new InternetAddress(
                    "z1bcde@yahoo.co.jp", "送信者の表示名", ENCODE);
            message.setFrom(addrFrom);

            final Address addrTo = new InternetAddress("z1bcde@yahoo.co.jp",
                    "受信者の表示名", ENCODE);
            message.addRecipient(Message.RecipientType.TO, addrTo);

            // メールのSubject
            message.setSubject("初めてのメール", ENCODE);

            // メール本文。setTextを用いると 自動的に[text/plain]となる。
            message.setText("こんにちは。\nごきげんよう。\nさようなら。", ENCODE);

            // 仮対策: 開始
            // setTextを呼び出した後に、ヘッダーを 7bitへと上書きします。
            // これは、一部のケータイメールが quoted-printable を処理できないことへの対策となります。
            message.setHeader("Content-Transfer-Encoding", "7bit");
            // 仮対策: 終了

            // その他の付加情報。
            message.addHeader("X-Mailer", "blancoMail 0.1");
            message.setSentDate(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // メール送信。
        try {
            Transport.send(message);
        } catch (Exception e) {
            // smtpサーバへの接続失敗は ここに入ります。
            System.out.println("指定のsmtpサーバへの接続に失敗しました。" + e.toString());
            e.printStackTrace();
        }
    }
}

