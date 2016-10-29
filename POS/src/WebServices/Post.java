package WebServices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Post {
	/** Post request and return server response */
	public static String postRequest(String url, String urlParameters) {
		// HTTP POST request
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// add reuqest header
			con.setRequestMethod("POST");
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			if (response.toString().length() > 0) { return response.toString(); }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static JSONObject stringToJSON(String json) throws JSONException {
		return new JSONObject(json);
	}

	public static JSONArray getJSONArray(JSONObject obj, String array_name) throws JSONException {
		return obj.getJSONArray(array_name);
	}
}