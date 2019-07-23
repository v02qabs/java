import java.util.*;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import java.io.*;

/**
 * シンプルなメール受信サンプル。
 */
public class Main {
    public static void main(final String[] args) {
        System.out.println("メール受信: 開始");

        new Main().process();

        System.out.println("メール受信: 終了");
    }

    public void process() {
        final Properties props = new Properties();

        // 基本情報。ここでは gmailへの接続例を示します。
        props.setProperty("mail.pop3.host", "pop.mail.yahoo.co.jp");
        props.setProperty("mail.pop3.port", "995");

        // タイムアウト設定
        props.setProperty("mail.pop3.connectiontimeout", "60000");
        props.setProperty("mail.pop3.timeout", "60000");

        // SSL関連設定
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.socketFactory.port", "995");

        final Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("z1bcde@yahoo.co.jp", "9fqfiz696");
            }
        });

        // デバッグを行います。標準出力にトレースが出ます。
        session.setDebug(true);

        Store store = null;
        try {
            try {
                store = session.getStore("pop3");
            } catch (NoSuchProviderException e) {
                System.out.println("メール受信: 指定プロバイダ[pop3]の取得に失敗しました。"
                        + e.toString());
                return;
            }

            try {
                store.connect();
            } catch (AuthenticationFailedException e) {
                System.out.println("メール受信: サーバ接続時に認証に失敗しました。" + e.toString());
                return;
            } catch (MessagingException e) {
                System.out.println("メール受信: サーバ接続に失敗しました。" + e.toString());
                return;
            }

            Folder folder = null;
            try {
                try {
                    // INBOXは予約語です。
                    folder = store.getFolder("INBOX");
                } catch (MessagingException e) {
                    System.out.println("メール受信: INBOXフォルダ取得に失敗しました。"
                            + e.toString());
                    return;
                }
                try {
                    folder.open(Folder.READ_ONLY);
                } catch (MessagingException e) {
                    System.out
                            .println("メール受信: フォルダオープンに失敗しました。" + e.toString());
                    return;
                }

                // メッセージ一覧を取得
                try {
                    final Message messages[] = folder.getMessages();
                    for (int index = 0; index < messages.length; index++) {
                        final Message message = messages[index];

                        // このAPI利用範囲であれば TOPコマンド止まりで、RETRコマンドは送出されない。

                        System.out.println("Subject: " + message.getSubject());
                        System.out.println("Date: " + message.getSentDate().toString());
						try
						{
							System.out.println("Message: " + message.getContent().toString());
						}
						catch(IOException error)
						{
							System.out.println("Can not get Content.");
						}

                        // TODO 0番目の配列アクセスをおこなっている点に注意。
                        final InternetAddress addrFrom = (InternetAddress) message.getFrom()[0];
                        System.out.println("  From: " + addrFrom.getAddress());
                        // MimeUtility.decodeText(addrFrom.getPersonal())

                        // To: を表示。
                        final Address[] addrsTo = message
                                .getRecipients(RecipientType.TO);
                        for (int loop = 0; loop < addrsTo.length; loop++) {
                            final InternetAddress addrTo = (InternetAddress) addrsTo[loop];
                            System.out.println("  To: " + addrTo.getAddress());
							
                        }

                        // Cc:は割愛

                        // なお、例えば message.getContentを呼び出すと RETRコマンドが送出される。
                    }
                } catch (MessagingException e) {
                    System.out.println("メール受信: メッセージ取得に失敗しました。" + e.toString());
                    return;
                }
            } finally {
                if (folder != null) {
                    try {
                        folder.close(false);
                    } catch (MessagingException e) {
                        System.out.println("メール受信: フォルダクローズに失敗しました。"
                                + e.toString());
                    }
                }
            }
        } finally {
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    System.out.println("メール受信: サーバ切断に失敗しました。" + e.toString());
                }
            }
        }
    }
}
