package WebServices;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import MainFrame.POSFrame;
import POSObjects.Advertisment;
import POSObjects.Coupon;
import ReservationScreenCustomerLogin.Customer;

public class Email {
	//can be email or number
	public static boolean sendEmail(Customer cust, Advertisment a) {
		 Properties props = new Properties();
		 props.put("mail.transport.protocol", "smtp");
		 props.put("mail.host", POSFrame.SERVER_IP);
		 props.put("mail.port", "465");
		 props.put("mail.smtp.auth", "true");
		
		 props.put("mail.smtp.starttls.enable", "true");
		
		 Session session = Session.getInstance(props, new Authenticator() {
		 @Override
		 protected PasswordAuthentication getPasswordAuthentication() {
		 return new PasswordAuthentication(POSFrame.SMTP_USER, POSFrame.SMTP_USER_PASS);
		 }
		 });
		 try {
		 Transport trans = session.getTransport();
		 Message msg = new MimeMessage(session);
		 msg.setFrom(new InternetAddress("noreply@noreply.com", POSFrame.businessName));
		// for(String sms_gateway: SMS_GATEWAY){
		//"9097028943"+sms_gateway
		 msg.addRecipient(Message.RecipientType.TO, new InternetAddress(cust.getEmail(), "Valued Client"));
		 //}
		 msg.setSubject("Reservation Receipt");
		 // msg.setText("Your Number: " + number + "\nRequested Stylist: " + c.getStylistName());
		 msg.setText("Example automative email test. Ad ID: "+a.getBarcode()+" Customer: "+cust.getName());
		 //msg.setContent("<font size =20 color=red>ELLO PUPPET</font>", "text/html; charset=utf-8");
		 trans.send(msg);
		 trans.close();
		 new Thread(new Runnable(){

			@Override
			public void run() {
			 POSFrame.SQL.redeemCoupon(a,cust);	
				
			}
			 
		 }).start();
		 } catch (AddressException e) {
		 e.printStackTrace();
		 return false;
		 } catch (MessagingException e) {
		 e.printStackTrace();
		 return false; // ...
		 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
		 return false; // ...
		 }
		 return true;
		 }
}
