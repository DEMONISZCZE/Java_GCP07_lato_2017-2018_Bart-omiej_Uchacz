package kolekcje_i_algorytmy;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public interface Logger {
	void log(String status, Student student);
}
class ConsoleLogger implements Logger
{
    public void log(String status, Student student)
    {
        System.out.println(status + " " + student.getMark() + " " + student.getFirstName() + " " + student.getLastName() + " " + student.getAge());
    }
}
class MailLogger implements Logger {
    String to;
    String from;
    String pass;
    String subject="Java mail test";
    String host;
    String msgText;

    public MailLogger(String to, String from, String host, String pass){
        this.to=to;
        this.from=from;
        this.host=host;
        this.pass=pass;
    }

    public void log(String status, Student student){
        subject="Crawler Notification ("+status+" person)";
        msgText = status+" : "+student;

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.setProperty("mail.smtp.user", from);
        props.setProperty("mail.smtp.password", pass);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", "587");
        Session session = Session.getInstance(props, null);

        try {

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(msgText);
            Transport.send(msg,from ,pass);
        } catch (MessagingException mex){
            mex.printStackTrace();
        }
    }
}
