package weaver.interfaces.workflow.action.basehelper;

import com.sun.mail.util.MailSSLSocketFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;




import javax.activation.DataHandler;
import javax.activation.DataSource;

import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;

import java.net.URL;



public class sendMailHelper {
    private static String from = "645266648@qq.com";
    // 发件人电子邮箱授权码
    private static String pass = "nhihzhfxmjrtbbah";
    //设置QQ邮箱服务器
    private static String host = "smtp.qq.com";


    /**
     * 发送带附件的邮件
     *
     * @param receive  收件人
     * @param subject  邮件主题
     * @param msg      邮件内容
     * @param courseList 课件附件的地址集合
     * @return
     * @throws GeneralSecurityException
     */
    public static void sendMail(String receive, String subject, String msg, List<ActivityCourse> courseList)
            throws GeneralSecurityException, MalformedURLException {

        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() { // qq邮箱服务器账户、第三方登录授权码
                return new PasswordAuthentication(from, pass); // 发件人邮件用户名、密码
            }
        });

        try {
            //创建MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            //设置发送人
            message.setFrom(new InternetAddress(from));
            //设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receive));
            //设置邮件主题
            message.setSubject(subject);

            // 创建普通消息部分
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);

            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            // 设置多重消息的文本消息部分
            multipart.addBodyPart(messageBodyPart);
            //设置多重消息的附件部分
            int size = courseList.size();
            URL url;
            for (int i = 0; i < size; i++) {
                // 发送多个附件部分
                messageBodyPart = new MimeBodyPart();
                String path = courseList.get(i).filePath.toString();
                String fileName = courseList.get(i).fileName.toString();
                // 设置要发送附件的文件IO流
                url = new URL(path);
                DataSource source = new URLDataSource(url);
                messageBodyPart.setDataHandler(new DataHandler(source));
                //设置附件名称
                messageBodyPart.setFileName(MimeUtility.encodeText(fileName));
                multipart.addBodyPart(messageBodyPart);
            }

            //设置最终的邮件
            message.setContent(multipart);
            //发送
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /*
     * Server 邮件服务器
     * serndUid 发件人账号
     * sendPwd 发件人密码
     * ToUid 收件人邮箱账号
     * title 邮件主题
     * Content 邮件内容
     * courseList 附件集合
     */

    public static void sendEmail(String Server,String serndUid,String sendPwd,String ToUid,String title,String Content, List<ActivityCourse> courseList) throws Exception {
        /*
         * 1. 得到session
         */
        Properties props = new Properties(); //创建配置对象
        props.setProperty("mail.host", Server); // 配置邮箱服务，也就是邮箱地址，这里我用的163邮箱，所以地址就是smtp.163.com。你用哪个邮箱，你就百度搜一下它的服务地址，写在这就行
        props.setProperty("mail.smtp.auth", "true"); //这里的true表示需要验证才能登录

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(serndUid, sendPwd);
            } // 这一步是创建认证对象，并配置上自己的用户名和密码。前者就写自己邮箱的全称，后者就是你刚才生成的那个口令
        };
        Session session = Session.getInstance(props, auth);// 这一步是把配置信息和认证信息传给了邮箱服务，并拿到了连接对象

        /*
         * 2. 创建MimeMessage
         */
        MimeMessage msg = new MimeMessage(session);// 创建一封邮件
        msg.setFrom(new InternetAddress(serndUid));//设置发件人
        msg.setRecipients(Message.RecipientType.TO, ToUid);//设置收件人，自己做测试，发件人用我的163邮箱，收件人用我的qq邮箱。。。RecipientType.TO表示发送给对方，RecipientType.CC表示抄送
        msg.setSubject(title);//设置邮件主题
        msg.setContent("哈哈哈哈", "text/html;charset=utf-8");//前者为邮件内容，想写啥写啥，后者为固定参数，不要改（设置了页面格式和编码）

        MimeMultipart list = new MimeMultipart();//创建内容列表

        MimeBodyPart part1 = new MimeBodyPart();//创建内容对象
        part1.setContent(Content, "text/html;charset=utf-8");//添加文本内容
        list.addBodyPart(part1);//把上面有文本内容的部分添加到列表

        //添加附件
        for (ActivityCourse cur: courseList
        ) {
            MimeBodyPart part2 = new MimeBodyPart();//创建内容对象2
            part2.attachFile(new File(cur.filePath));//要添加文件的绝对路径
            list.addBodyPart(part2);//把这一部分添加到列表中
        }


        msg.setContent(list);//设置邮件内容，内容就是刚才创建的列表

        /*
         * 3. 发送
         */
        Transport.send(msg);//调用方法完成发送
        System.out.println("发送完成");
    }

    public static void deleteFolders(String filePath) {

        Path path = Paths.get(filePath);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir,
                                                          IOException exc) throws IOException {
                    Files.delete(dir);

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
