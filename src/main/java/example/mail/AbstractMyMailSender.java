package example.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StreamUtils;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractMyMailSender implements MyMailSender {

    @Override
    public String getMailContent(MimeMessage mimeMessage) {
        try {
            StringBuilder sb = new StringBuilder();
            MimeMultipart mmp = (MimeMultipart) mimeMessage.getContent();
            for (int i = 0; i < mmp.getCount(); i++) {
                sb.append(getText(mmp.getBodyPart(i)));
            }
            return sb.toString();
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected MimeMessage createMessage(JavaMailSender javaMailSender, MailParam mailParam) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

        setMessageInfo(messageHelper, mailParam);

        return message;
    }

    protected void setMessageInfo(MimeMessageHelper messageHelper, MailParam mailParam) throws MessagingException {
        messageHelper.setFrom(mailParam.getFrom());
        messageHelper.setTo(mailParam.getTo());
        messageHelper.setSubject(mailParam.getSubject());
        messageHelper.setText(mailParam.getMailContent());
    }

    private String getText(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return StreamUtils.copyToString(part.getInputStream(), StandardCharsets.UTF_8);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart)part.getContent();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part bodyPart = multipart.getBodyPart(i);
                sb.append(getText(bodyPart));
                sb.append("\n");
            }
            return sb.toString();
        }
        return null;
    }
}
