package Dev;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.sun.net.ssl.HttpsURLConnection;

import Dialogs.Loading;
import MainFrame.POSFrame;
import WebServices.Post;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class Dev {

	public static void main(String[] args) throws IOException {
//		 InetAddress localhost = InetAddress.getLocalHost();
//		    byte[] ip = localhost.getAddress();
//
//		    for (int i = 1; i <= 254; i++)
//		    {
//		    	
//		    
//		            ip[3] = (byte)i; 
//		            InetAddress address = InetAddress.getByAddress(ip);
//
//		            if (address.isReachable(100))
//		            {
//		                String output = address.toString().substring(1);
//		                System.out.print(output + " is on the network");
//		            }
//		    }
		 byte[] ip = {(byte)192, (byte)168, 1, 0}; //Note: for 192.168.0.x addresses  
	        for (int i = 1; i <= 254; i++)  
	        {  
	            ip[3] = (byte) i;  
	            InetAddress address = InetAddress.getByAddress(ip);  
	            System.out.println("InetAddress1: "+ address);
	        } 

		
		System.out.println(String.format("%1$" + 20 + "s","hello puppet"));
		System.exit(0);
//		String param="code="+Encryption.Encryption.encryptPassword("acbadbacba")+"&username=9091234567&password="+Encryption.Encryption.encryptPassword("admin123");
//		String server=Post.postRequest("http://www.acbasoftware.com/db/db.php",param);
//		System.out.println(server);
//		String credit = JOptionPane.showInputDialog("");
//		
//		JLabel label  = new JLabel();
//		//WebServices.CreditCard.charge(1234,"4242424242424242",exp_month,exp_year,label);
		JFrame f = new JFrame();
		f.setSize(360, 100);
		f.setLocationRelativeTo(null);
		f.setUndecorated(true);
		f.getContentPane().setLayout(null);
		JLabel label = new JLabel(new ImageIcon(POSFrame.PICTURE_DIRECTORY+"\\loading.gif"));
		label.setBounds(0, 0, 360, 100);
		f.getContentPane().add(label);
		
		f.setVisible(true);
	}

	// private static final String[] SMS_GATEWAY={"@message.alltel.com","@txt.att.net","@myboostmobile.com","@messaging.sprintpcs.com","@tmomail.net"
	// ,"@email.uscc.net","@vtext.com","@vmobl.com"};
	// public static boolean isValidEmailAddress(String email) {
	// boolean result = true;
	// try {
	// InternetAddress emailAddr = new InternetAddress(email);
	//
	// emailAddr.validate();
	// } catch (AddressException ex) {
	// result = false;
	// }
	// return result;
	// }
	//
	// public static void main(String[] args) {
	// if (sendEmail()) {
	// System.out.println("Email sent:");
	// } else {
	// System.out.println("some errorr....");
	// }
	// }
	//
	// public static boolean sendEmail() {
	// Properties props = new Properties();
	// props.put("mail.transport.protocol", "smtp");
	// props.put("mail.host", "gator4222.hostgator.com");
	// props.put("mail.port", "465");
	// props.put("mail.smtp.auth", "true");
	//
	// props.put("mail.smtp.starttls.enable", "true");
	//
	// Session session = Session.getInstance(props, new Authenticator() {
	// @Override
	// protected PasswordAuthentication getPasswordAuthentication() {
	// return new PasswordAuthentication("acba", "SoRqZqEiKAA8z$p#XiPt1uFpc8t4VI");
	// }
	// });
	// try {
	// Transport trans = session.getTransport();
	// Message msg = new MimeMessage(session);
	// msg.setFrom(new InternetAddress("no-reply@noreply.com", POSFrame.businessName));
	// for(String sms_gateway: SMS_GATEWAY){
	//
	// msg.addRecipient(Message.RecipientType.TO, new InternetAddress("9097028943"+sms_gateway, "Valued Client"));
	// }
	//
	// msg.setSubject("Reservation Receipt");
	// // msg.setText("Your Number: " + number + "\nRequested Stylist: " + c.getStylistName());
	// msg.setText("Its Gabe :p . Im just doing some development work got sms texting to work :). Oh and you cant respond back cuz thats a fake email address hehe");
	// //msg.setContent("<font size =20 color=red>ELLO PUPPET</font>", "text/html; charset=utf-8");
	//
	//
	//
	//
	//
	//
	// trans.send(msg);
	// trans.close();
	// } catch (AddressException e) {
	// e.printStackTrace();
	// return false;
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// return false; // ...
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// return false; // ...
	// }
	// return true;
	// }
}
