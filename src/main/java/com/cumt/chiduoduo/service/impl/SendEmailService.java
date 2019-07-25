package com.cumt.chiduoduo.service.impl;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：发送用户通知邮件服务层实现类
 *
 */
@Service
public class SendEmailService {
    @Value("${spring.mail.account}")
    private String account;//登录用户名
    @Value("${spring.mail.pass}")
    private String pass; //登录密码
    @Value("${spring.mail.from}")
    private String from; //发件地址
    @Value("${spring.mail.host}")
    private String host; //服务器地址
    @Value("${spring.mail.port}")
    private String port; //端口
    @Value("${spring.mail.protocol}")
    private String protocol; //协议
    //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator {
        String u = null;
        String p = null;
        public MyAuthenricator(String u, String p) {
            this.u = u;
            this.p = p;
        }

        /**
         * 邮箱发送
         * @return
         */
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(u, p);
    } }
    public void send(String email,String otpCode,String subject,String text) {
    Properties prop = new Properties();
    //协议
    prop.setProperty("mail.transport.protocol", protocol);
   //服务器
    prop.setProperty("mail.smtp.host", host);
    //端口
    prop.setProperty("mail.smtp.port", port); //使用smtp身份验证
    prop.setProperty("mail.smtp.auth", "true"); //使用SSL，企业邮箱必需！
    //开启安全协议
    MailSSLSocketFactory sf = null;
    try {
        sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
    }
    prop.put("mail.smtp.ssl.enable", "true");
    prop.put("mail.smtp.ssl.socketFactory", sf);
    prop.put("mail.smtp.starttls.enable", "true");
    Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
    session.setDebug(true);
    MimeMessage mimeMessage = new MimeMessage(session);
    try { mimeMessage.setFrom(new InternetAddress(from, "吃哆哆"));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        mimeMessage.setSubject(subject);
        mimeMessage.setSentDate(new Date());
        mimeMessage.setText(text);
        mimeMessage.saveChanges();
        Transport.send(mimeMessage);
    } catch (MessagingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    }
}
