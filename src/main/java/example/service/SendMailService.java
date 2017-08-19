package example.service;

import example.mail.MailParam;
import example.mail.MyMailSender;
import example.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Service
public class SendMailService {
    private MyMailSender myMailSender;

    @Autowired
    @Qualifier("emailTemplateEngine")
    private TemplateEngine mailTemplateEngine;

    public SendMailService(MyMailSender myMailSender) {
        this.myMailSender = myMailSender;
    }

    public void execute(Mail mail) {
        Context ctx = new Context();
        ctx.setVariables(mail.getParams());
        String mailContent = mailTemplateEngine.process(mail.getContentTemplate(), ctx);

        MimeMessage mimeMessage = myMailSender.createMimeMessage(new MailParam(mail.getFrom(), mail.getTo(), mail.getSubject(), mailContent));
        myMailSender.send(mimeMessage);
    }
}
