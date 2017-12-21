package cn.eyeblue.blog.rest.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


@Slf4j
@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Getter
    private String mailNickname = "蓝眼博客";


    //send simple email. without html
    public void textSend(String emailTo, String subject, String content) {

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(mailProperties.getUsername());
        msg.setTo(emailTo);
        msg.setSubject(subject);
        msg.setText(content);

        this.javaMailSender.send(msg);
    }

    //发送html文档
    public NotificationResult htmlSend(String emailTo, String subject, String html) {

        NotificationResult notificationResult = new NotificationResult();
        MimeMessage msg = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);

        try {
            String nick = MimeUtility.encodeText(mailNickname);
            helper.setFrom(nick + " <" + mailProperties.getUsername() + ">");
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setText(html, true);
            this.javaMailSender.send(msg);

            notificationResult.setStatus(NotificationResult.Status.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            notificationResult.setStatus(NotificationResult.Status.ERROR);
            notificationResult.setMessage("邮件发送失败！");
        }

        return notificationResult;

    }


}
