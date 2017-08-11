package example.service;

import example.mail.MailParam;
import example.mail.MyMailSender;
import example.model.AccountActivationMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Service
public class SendAccountActivationMailService {
    private MyMailSender myMailSender;

    @Autowired
    @Qualifier("emailTemplateEngine")
    private TemplateEngine mailTemplateEngine;

    public SendAccountActivationMailService(MyMailSender myMailSender) {
        this.myMailSender = myMailSender;
    }

    public void execute(AccountActivationMail mail) {
        Context ctx = new Context();
        ctx.setVariables(mail.getParams());
        String mailContent = mailTemplateEngine.process(mail.getContentTemplate(), ctx);

        MimeMessage mimeMessage = myMailSender.createMimeMessage(new MailParam(mail.getFrom(), mail.getTo(), mail.getSubject(), mailContent));
        myMailSender.send(mimeMessage);
    }
}
