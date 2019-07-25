package com.cumt.chiduoduo.service;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：邮件发送服务接口
 *
 */
public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}
