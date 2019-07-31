package cn.cescforz.molecular.api;

import javax.mail.MessagingException;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-14 16:33
 */
public interface MailService {

    /**
     * 发送文本邮件
     * @param to 目标邮箱地址
     * @param subject  邮件标题
     * @param content  邮件内容
     */
    void sendSimpleMail(String to, String subject, String content);

    void sendSimpleMail(String to, String subject, String content, String... cc);

    /**
     * 发送HTML邮件
     * @param to 目标邮箱地址
     * @param subject 邮件标题
     * @param content 邮件内容
     * @throws MessagingException
     */
    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    void sendHtmlMail(String to, String subject, String content, String... cc);

    /**
     * 发送带附件的邮件
     * @param to 目标邮箱地址
     * @param subject 邮件标题
     * @param content 邮件内容
     * @param filePath
     * @throws MessagingException
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;

    void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc);

    /**
     * 发送正文中有静态资源的邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     * @throws MessagingException
     */
    void sendResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException;

    void sendResourceMail(String to, String subject, String content, String rscPath, String rscId, String... cc);
}
