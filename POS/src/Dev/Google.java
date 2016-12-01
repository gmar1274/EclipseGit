package Dev;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import MainFrame.POSFrame;
import Util.Log;

public class Google {
	private static String DB_URL;
	private final String USER_AGENT = "Mozilla/5.0";

	public Google() {
		
	}

	public static void main(String[] s) {
//		String res = WebServices.Post.postRequest("http://acbasoftware.com/pos/lockdb.php", "");
//		System.out.println(res);
//		return;
		System.out.println("connecting to google ... ");
		Google g = new Google();
		ArrayList<GooglePlace> place_list = g.getPlaceID(34.505079, -119.640851, 50000);
		System.out.println("---");
		System.out.println("Result size: " + place_list.size());
		System.out.println("After Result search list: " + place_list);
		for (GooglePlace gp : place_list) {
			//g.googleSearchBrowser(gp);
			return;
		}
	}

	public void googleSearchBrowser(GooglePlace gp) {
		String params = gp.name.replace(" ", "+")+"+hours";
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://www.google.com/search?q=" + params).openConnection();

			// add reuqest header
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			connection.setRequestProperty("Content-Length", "0");

			// Send post request
			connection.setDoOutput(true);
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
			String sb="";
			boolean found = false;
			String html = "", line = "";
			FileWriter fw = new FileWriter(new File("googleresult.txt"),true);
			fw.append(System.getProperty("line.separator")+"---NEW RESPONSE--"+System.getProperty("line.separator"));
			while ((line = in.readLine()) != null) {
				fw.append(line+System.getProperty("line.separator"));
				if (line.contains("<div class=\"srg\">") || line.contains("<div class=\"mod\"")) {
					found = false;
					break;
				} else if (found) {// store area of interest
					html += line;
				} else if (line.contains("<div class=\"_M4k\">") || line.contains("Address</a>")) {
					html += line;
					found = true;
				}
				sb += line;
			}
			System.out.println("--- Begin HTML ---");
			System.out.println(html);
			System.out.println("--END HTML: --");
			in.close();
			fw.close();
			JFrame f = new JFrame("Ex.");
			f.setSize(700, 500);
			f.setLocationRelativeTo(null);
			f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
			JTextPane tp = new JTextPane();
			tp.setContentType("text/html");
			tp.setEditable(false);
			tp.setText("<html>"+sb+"</html>");
			tp.setVisible(true);
			JScrollPane s = new JScrollPane(tp);
			f.add(s);
			f.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** Google geocode api get Location for a specific address */
	public void googleLocation() {
		try {
			String param = "address=2023 e boca raton ct. ontario, ca&key= AIzaSyAlGB3JBYA1HMkdwJ23UwLbI89rq38tFr0";
			param = param.replace(" ", "+");
			String response = WebServices.Post.postRequest("https://maps.googleapis.com/maps/api/geocode/json?" + param, "");
			System.out.println("----------");

			JSONObject obj = WebServices.Post.stringToJSON(response);
			JSONArray a = WebServices.Post.getJSONArray(obj, "results");
			JSONObject results = a.getJSONObject(0);
			String place_id = results.getString("place_id");
			JSONArray address = WebServices.Post.getJSONArray(results, "address_components");
			String street = address.getJSONObject(0).getString("long_name");
			String addr = address.getJSONObject(1).getString("long_name");
			String city = address.getJSONObject(2).getString("long_name");
			String state = address.getJSONObject(4).getString("short_name");
			String country = address.getJSONObject(5).getString("short_name");
			String zip = address.getJSONObject(6).getString("long_name");
			/////////////////////////////////////////////// got all location information
			///////// need place details about the location info
			param = "place_id=" + place_id + "&key= AIzaSyAlGB3JBYA1HMkdwJ23UwLbI89rq38tFr0";
			param = param.replace(" ", "");
			String place_response = WebServices.Post.postRequest("https://maps.googleapis.com/maps/api/place/details/json?" + param, "");

			JSONObject l = WebServices.Post.stringToJSON(results.getJSONObject("geometry").toString());
			JSONObject ll = WebServices.Post.stringToJSON(l.get("location").toString());
			double lng = ll.getDouble("lng");
			double lat = ll.getDouble("lat");
			System.out.println(lng);// lat then long
			System.out.println(lat);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get Details of a place using google api to search nearby from radius at a geo point
	 * API only allows for location(lat,lng),place id, name, icon
	 */
	public ArrayList<GooglePlace> getPlaceID(double lat, double lng, int radius_km) {
		ArrayList<GooglePlace> id = new ArrayList<>();
		///////
		try {
			System.out.println("----Radius search----");// |salon works
			// radius in meters...1km ~ .6 miles
			String param = "location=" + lat + "," + lng + "&radius=" + radius_km + "&type=hair_care&keyword=barber|salon&key=AIzaSyAlGB3JBYA1HMkdwJ23UwLbI89rq38tFr0";
			param = param.replace(" ", "");
			String serv = WebServices.Post.postRequest("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + param, "");
			JSONObject server_json_response = WebServices.Post.stringToJSON(serv);
			JSONArray results = WebServices.Post.getJSONArray(server_json_response, "results");
			System.out.println("Result size: " + results.length());
			for (int i = 0; i < results.length(); ++i) {
				String place_id = results.getJSONObject(i).getString("place_id");

				JSONObject o = results.getJSONObject(i);
				JSONObject oo = WebServices.Post.stringToJSON(o.getJSONObject("geometry").toString());
				JSONObject ooo = WebServices.Post.stringToJSON(oo.getJSONObject("location").toString());
				String name = o.getString("name");
				double lat_result = ooo.getDouble("lat");
				double lng_result = ooo.getDouble("lng");
				String url = results.getJSONObject(i).getString("icon");
				GooglePlace gp = new GooglePlace(name, place_id, lat_result, lng_result,url);
				id.add(gp);
				this.insertStoreDB(gp);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		///////
		return id;
	}

	public void insertStoreDB(GooglePlace gp) {
		Connection conn = null;
		PreparedStatement p = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 DB_URL = "jdbc:mysql://www.acbasoftware.com:3306/acba_app";// need to add username to access uniqe store db
	
			 URL url = new URL(gp.url_icon);
			Image image = ImageIO.read(url);
			BufferedImage buf_image= (BufferedImage) image; // this is BufferedImage reference you got after converting it from Image
			byte[] imageByteArray = bufferedImageToByteArray(buf_image,".png");

			conn = DriverManager.getConnection(DB_URL,"acba_admin" , "7N-v26#d2(}mDt(wK%");
			//14 TOTAL params
			p = conn.prepareStatement("INSERT INTO `acba_app`.`app_store` (`name`, `lat`, `lon`, `icon`, `google_place_id`) "
			+ "VALUES (?,?,?,?,?);");
			p.setString(1, gp.name);
			p.setDouble(2, gp.lat);
			p.setDouble(3, gp.lng);
			p.setBytes(4, imageByteArray);
			p.setString(5, gp.place_id);
			p.execute();
			p.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static byte[] bufferedImageToByteArray(BufferedImage image, String format){
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(image, format, baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return baos.toByteArray();
	}
	private class Store {
		public Store() {

		}
	}

	private class GooglePlace {
		public String name;
		public String place_id;
		public double lat, lng;
		public String url_icon;

		public GooglePlace(String name, String id, double lat, double lng,String url) {
			this.name = name;
			this.place_id = id;
			this.lng = lng;
			this.lat = lat;
			this.url_icon=url;
		}

		public String toString() {
			return "name: " + name + " place_id: " + place_id + " lat: " + lat + " lng: " + lng;
		}
	}
}
