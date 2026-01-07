package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    private static final String FROM_EMAIL = "highteckcinema@gmail.com";

    private static final String APP_PASSWORD = "hpbxbegnrbwgdzur";

    public static void sendResetPassword(String toEmail, String newPassword) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
        );
        message.setSubject("Password Reset");

        String content = """
                Hello,

                Your password has been reset successfully.

                New password: %s

                Please login and change your password immediately.

                Regards,
                Cinema System
                """.formatted(newPassword);

        message.setText(content);

        Transport.send(message);
    }
}