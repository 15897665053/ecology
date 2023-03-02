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
