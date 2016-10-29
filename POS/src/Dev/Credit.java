package Dev;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import Encryption.Encryption;

public class Credit implements Runnable {

	private String card_number, ccv;
	private int expr_month, expr_year, amount;
	private Token token;
	private String[] params;
	private String sk;
	private String pk;
	private boolean card_error, network_error;

	public Credit(String[] args) {
		this.params = args;
		network_error = false;
		card_error = false;
	}

	public boolean isCardError() {
		return this.card_error;
	}

	public boolean isNetworkError() {
		return this.network_error;
	}

	private String doInBackground(String[] params) {
		try {

			card_number = params[0];
			ccv = params[1];
			expr_month = Integer.parseInt(params[2]);
			expr_year = Integer.parseInt(params[3]);
			amount = Integer.parseInt(params[4]);

			String link = "http://acbasoftware.com/creditcard/credit.php";
			String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbacreditacba"), "UTF-8");

			URL url = new URL(link);
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(data);
			wr.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				break;
			}

			return sb.toString();
		} catch (Exception e) {

			network_error = true;
			return null;
		}
	}

	private void onPostExecute(String result) throws JSONException, AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		if (result == null) return;
		JSONObject jObject = new JSONObject(result);
		JSONArray jArray = jObject.getJSONArray("stripe");
		for (int i = 0; i < jArray.length(); i++) {

			JSONObject oneObject = jArray.getJSONObject(i);

			sk = oneObject.getString("sk");
			pk = oneObject.getString("pk");
		}
		this.charge(sk, pk);

	}

	private void charge(String sk, String pk) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		Stripe.apiKey = sk;

		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> cardParams = new HashMap<String, Object>();
		cardParams.put("number", this.card_number);
		cardParams.put("exp_month", this.expr_month);
		cardParams.put("exp_year", this.expr_year);
		cardParams.put("cvc", this.ccv);
		tokenParams.put("card", cardParams);

		token = Token.create(tokenParams);
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", amount);
		chargeParams.put("currency", "usd");
		chargeParams.put("source", token.getId()); // obtained with Stripe.js
		chargeParams.put("description", "Charge for POS SYSTEM");

		Charge.create(chargeParams);

	}

	private void saveCustomer(Card request) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		// Set your secret key: remember to change this to your live secret key in production
		// See your keys here https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = sk;

		// Get the credit card details submitted by the form
		// String token = request.getParameter("stripeToken");

		// Create a Customer
		Map<String, Object> customerParams = new HashMap<String, Object>();
		// customerParams.put("source", token);
		customerParams.put("description", "Example customer");

		Customer customer = null;

		customer = Customer.create(customerParams);

		// Charge the Customer instead of the card
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", 1000); // amount in cents, again
		chargeParams.put("currency", "usd");
		chargeParams.put("customer", customer.getId());

		try {
			Charge.create(chargeParams);
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// YOUR CODE: Save the customer ID and other info in a database for later!

		// YOUR CODE: When it's time to charge the customer again, retrieve the customer ID!

		Map<String, Object> otherChargeParams = new HashMap<String, Object>();
		otherChargeParams.put("amount", 1500); // $15.00 this time
		otherChargeParams.put("currency", "usd");
		// otherChargeParams.put("customer", customerId); // Previously stored, then retrieved

		Charge.create(otherChargeParams);

	}

	public void setCreditCard(String card) {
		this.card_number = card;
	}

	public void setCCV(String ccv) {
		this.ccv = ccv;
	}

	public void setExprMonth(int m) {
		this.expr_month = m;

	}

	public void setExprYear(int y) {
		this.expr_year = y;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	// public static void main(String[] args) {
	// String[] a = { "5555555555554444", "123", "04", "2017", "1121" };
	// Credit c = new Credit(a);
	//
	// c.run();
	//
	// if (c.isNetworkError()) {
	// JOptionPane.showMessageDialog(null, "Network Error. Could not establish a connection. Either your Internet is off or our servers are down. Please contact ACBA support team.",
	// "Network Error", JOptionPane.ERROR_MESSAGE);
	// } else if (c.isCardError()) {
	// JOptionPane.showMessageDialog(null, "Card was declined.", "Network Error", JOptionPane.ERROR_MESSAGE);
	//
	// } else {
	// JOptionPane.showMessageDialog(null, "Transaction successful", "Authorization was a success", JOptionPane.PLAIN_MESSAGE);
	//
	// }
	// }

	@Override
	public void run() {

		try {
			this.onPostExecute(this.doInBackground(params));
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException | APIException | JSONException e) {
			this.card_error = true;
		}

	}

}
