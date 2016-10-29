package WebServices;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import MainFrame.POSFrame;

public class CreditCard {

	public static boolean charge(int amount, JLabel label) {
		JPasswordField pwd = new JPasswordField(10);
		pwd.addAncestorListener( new AncestorListener()
		{

			@Override
			public void ancestorAdded(AncestorEvent event) {
				 pwd.requestFocusInWindow();
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	    int action = JOptionPane.showConfirmDialog(null, pwd,"Slide The Card!",JOptionPane.OK_CANCEL_OPTION);
		String credit="";
	    if(action == JOptionPane.OK_OPTION && pwd.getText().length()>0){
			credit = pwd.getText().toString();
		}else{
			return false;
		}
	    
	    String card = credit.substring(0, credit.indexOf("^") + 1);
		// System.out.println("RAW Credit CARD::: "+credit);
		credit = credit.substring(card.length(), credit.length());
		card = card.substring(2, card.indexOf("^"));//cardnumber
		// String lname= credit.substring(0,credit.indexOf("/")+1);
		String name = credit.substring(0, credit.indexOf("^") + 1);
		credit = credit.substring(name.length(), credit.length());
		int exp_year = Integer.parseInt(credit.substring(0, 2));
		int exp_month = Integer.parseInt(credit.substring(2, 4));
		// System.out.println(card);
		// System.out.println(credit);
		// System.out.println(exp_year+" month: "+exp_month);
		return charge(amount, card, exp_month, exp_year, label);
	}

	private static boolean charge(int amount, String card, int exp_month, int exp_year, JLabel label) {
		Stripe.apiKey = "sk_test_JnPKAFeuBTwyRKUQJgzvpZBP";// "sk_test_BQokikJOvBiI2HlWgH4olfQ2";

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", amount);
		chargeParams.put("currency", "usd");
		Map<String, Object> sourceParams = new HashMap<String, Object>();
		sourceParams.put("number", card);
		sourceParams.put("exp_month", exp_month);
		sourceParams.put("exp_year", exp_year);
		// sourceParams.put("cvc", "314");
		chargeParams.put("source", sourceParams);
		chargeParams.put("description", "Haircut/Salon");
		try {
			Charge.create(chargeParams);
			System.out.println("success");
			label.setIcon(new ImageIcon(POSFrame.PICTURE_DIRECTORY + "\\success.png"));
			return true;
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException e) {
			label.setIcon(new ImageIcon(POSFrame.PICTURE_DIRECTORY + "\\declined.png"));
			e.printStackTrace();
		}
		return false;
	}
}
