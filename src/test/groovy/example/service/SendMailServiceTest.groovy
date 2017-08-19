package example.example.service

import example.mail.MyMailSender
import example.model.AccountActivationMail
import example.model.User
import example.service.SendMailService
import org.mockito.ArgumentCaptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.mail.Message
import javax.mail.internet.MimeMessage
import java.util.regex.Pattern

import static org.mockito.Mockito.*;

@SpringBootTest
class SendMailServiceTest extends Specification {
    @Autowired
    MyMailSender myMailSender

    @Autowired
    SendMailService target

    ArgumentCaptor<MimeMessage> captor

    def setup() {
        def spy = spy(myMailSender)
        captor = ArgumentCaptor.forClass(MimeMessage.class)
        doNothing().when(spy).send(captor.capture())

        target.myMailSender = spy
    }

    def "send account activation mail"() {
        when:
        def user = User.from(1, "hoge", "hoge@example.com")
        AccountActivationMail mail = new AccountActivationMail(user, "http://activation.example.com")
        target.execute(mail)

        then:
        MimeMessage mimeMessage = captor.getValue()
        mimeMessage.getFrom().collect { it.toString() } == ["info@example.com"]
        mimeMessage.getRecipients(Message.RecipientType.TO).collect { it.toString() } == [user.getEmail()]
        mimeMessage.getSubject() == mail.getSubject()
        def p = Pattern.compile """
Hi ${user.getName()},

Welcome to the Sample App! Click on the link below to activate your account:

http://activation.example.com
"""
        myMailSender.getMailContent(mimeMessage) =~ p
    }
}
