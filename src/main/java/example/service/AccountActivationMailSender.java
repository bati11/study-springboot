package example.service;

import example.mail.MailParam;
import example.mail.MyMailSender;
import example.model.AccountActivationMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class AccountActivationMailSender {
    private final MyMailSender myMailSender;

    @Autowired
    @Qualifier("emailTemplateEngine")
    private TemplateEngine mailTemplateEngine;

    public AccountActivationMailSender(MyMailSender myMailSender) {
        this.myMailSender = myMailSender;
    }

    public void exec(AccountActivationMail mail) {
        Context ctx = new Context();
        ctx.setVariables(mail.getParams());
        String mailContent = mailTemplateEngine.process(mail.getContentTemplate(), ctx);

        myMailSender.send(new MailParam(mail.getFrom(), mail.getTo(), mail.getSubject(), mailContent));
    }
}
