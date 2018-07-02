package fr.manu.petitesannonces.web.email;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

/**
 * @author emmanuel.mura
 *
 */
public class EmailHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailHelper.class);

    private EmailHelper() {
    }

    private static MimeMessage createEmail(String to, String cc, String from, String subject,
            String bodyText) throws MessagingException {
    	
    	if (StringUtils.isEmpty(to) || StringUtils.isEmpty(from) || StringUtils.isEmpty(bodyText)) {
    		logger.error(">>>>> EmailHelper.createEmail - MessagingException : Should specify to / from email addresses and body message <<<<<");
    		throw new MessagingException("Should specify to / from email addresses and body message");
    	}
    	
        Properties props = new Properties();
        // Session session = Session.getDefaultInstance(props, null);
        Session session = Session.getInstance(props);

        MimeMessage email = new MimeMessage(session);
        InternetAddress tAddress = new InternetAddress(to);
        InternetAddress cAddress = StringUtils.isEmpty(cc) ? null : new InternetAddress(cc);
        InternetAddress fAddress = new InternetAddress(from);

        email.setFrom(fAddress);
        if (cAddress != null) {
            email.addRecipient(javax.mail.Message.RecipientType.CC, cAddress);
        }
        email.addRecipient(javax.mail.Message.RecipientType.TO, tAddress);
        email.setSubject(subject);
        email.setText(bodyText);
        
        logger.debug("Email : {}", email);
        return email;
    }

    private static Message createMessageWithEmail(MimeMessage email)
            throws MessagingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        
        logger.debug("Message : {}", message);
        return message;
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException the MessagingException
     * @throws IOException the IOException
     */
    public static MimeMessage createEmailWithAttachment(String to, String from, String subject,
            String bodyText, File file) throws MessagingException, IOException {
        Properties props = new Properties();
        // Session session = Session.getDefaultInstance(props, null);
        Session session = Session.getInstance(props);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/plain");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        logger.debug("Email : {}", email);
        return email;
    }

    public static void send(Gmail service, String recipientEmail, String ccEmail, String fromEmail,
            String title, String message) throws IOException, MessagingException {
        Message m = createMessageWithEmail(
                createEmail(recipientEmail, ccEmail, fromEmail, title, message));
        logger.debug("Message : {}", m);
        service.users().messages().send("me", m).execute();
    }
}
