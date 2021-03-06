package keithapps.mobile.com.jeeves.tools;

import android.content.Context;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

/**
 * import javax.mail.Message;
 * import javax.mail.PasswordAuthentication;
 * import javax.mail.Session;
 * import javax.mail.Transport;
 * import javax.mail.internet.InternetAddress;
 * import javax.mail.internet.MimeMessage;/
 **/
//import static javax.mail.Session.getInstance;

/**
 * Created by Keith on 2/18/2016.
 * Email Tools
 */
public class Email {
    /**
     * Keith MacKay's (my) email Address - keith.mackay3@gmail.com
     */
    public static final String myEmail = "keith.mackay3@gmail.com";

    public static void sendEmailTo(final String header, final String message, final String recipient, final boolean resend, final Context c) {
        if (getPrefs(c).getBoolean(c.getString(R.string.permissions_internet), true))
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {/*//Removing the emailing for now, because the Gradle project hates the javax.mail package
                            final String username = "android.jeeves@yahoo.com";
                            final String password = "jeevspass";
                            Properties props = new Properties();
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                            props.put("mail.debug", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.port", "465");
                            props.put("mail.smtp.socketFactory.port", "465");
                            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                            props.put("mail.smtp.socketFactory.fallback", "false");
                            Session session = getInstance(props, new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });
                            try {
                                Message m = new MimeMessage(session);
                                m.setFrom(new InternetAddress(username));
                                m.setRecipients(Message.RecipientType.TO,
                                        InternetAddress.parse(recipient));
                                m.setSubject(header);
                                m.setText(message + "\n\n" + Utils.getTimestamp());
                                Transport.send(m);
                            } catch (Exception e) {
                                if (resend) sendEmailTo(header, message, recipient, false, c);
                            }/**/
                        } catch (Exception e) {
                            //Just let it go
                        }
                    }
                }).start();
            } catch (Exception e) {
                //Do nothing. Should be impossible
            }
    }

    public static void sendEmailTo(final String header, final String message, final String recipient, Context c) {
        sendEmailTo(header, message, recipient, true, c);
    }

    public static void sendEmail(final String header, final String message, Context c) {
        sendEmailTo(header, message, "android.jeeves@yahoo.com", true, c);
    }

    public static void emailException(String info, Context c, Exception e) {
        final String header = "Unexpected Exception in Jeeves",
                message = String.format("%s\n\nAn Android Device: \n\n%s\n\nExperienced an Exception " +
                                "during runtime: %s\n%s",
                        info,
                        SystemTools.getDeviceInfo(c),
                        (e == null) ? "Could not get Message" : e.getLocalizedMessage(),
                        (e == null) ? "Could not get Stack Trace" : Utils.getStackTraceString(e));
        sendEmail(header, message, c);
        sendEmailTo(header, message, myEmail, c);
        Log.logException(info, c, e);
    }
}
