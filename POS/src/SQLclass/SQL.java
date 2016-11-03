package SQLclass;

import java.awt.Image;
import java.awt.Window;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.PriorityQueue;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.jdbc.JDBCXYDataset;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.javafx.event.EventQueue;

import Dialogs.Loading;
import Encryption.Encryption;
import JCharts.JChart;
import MainFrame.POSFrame;
import POSObjects.Advertisment;
import POSObjects.CombinedSale;
import POSObjects.Coupon;
import POSObjects.Data;
import POSObjects.Haircut;
import POSObjects.Node;
import POSObjects.Product;
import POSObjects.Stylist;
import POSObjects.Ticket;
import ReservationScreenCustomerLogin.Customer;
import WebServices.Post;

public class SQL {
	// JDBC driver name and database URL
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private String DB_URL = "jdbc:mysql://www.acbasoftware.com:3306/acba_";// need to add username to access uniqe store db
	// "jdbc:mysql://localhost/acba";//
	private DecimalFormat df;
	private SimpleDateFormat sdf;
	private HashMap<String, Stylist> stylist_login;
	private HashMap<String, String> admin_login;
	private HashMap<String, Product> products;
	private HashMap<String, Stylist> stylists;
	private HashMap<String, Coupon> coupons;
	private HashMap<String, Haircut> haircuts;
	private HashMap<Integer, Integer> WEEKDAY;// Day and sold of a product
	private HashMap<Integer, Ticket> tickets;// ticket
	public static String USER_DB;
	private final String WebServiceURL = "http://www.acbasoftware.com";/// routeWebsite";//will have to change this later on

	public enum DAY {
		Mon, Tues, Wed, Thur, Fri, Sat, Sun
	};

	// public SQL(){

	// }
	public SQL() {

		try {
			this.tickets = new HashMap<Integer, Ticket>();
			stylist_login = new HashMap<String, Stylist>();
			admin_login = new HashMap<String, String>();
			sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a");
			df = new DecimalFormat("###,###,###.##");
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// STEP 3: Open a connection
			// if (POSFrame.USER == null && POSFrame.PASS == null) {
			// POSFrame.USER = JOptionPane.showInputDialog("DB User Name:");
			// POSFrame.PASS = JOptionPane.showInputDialog("DB Password:");
			// POSFrame.SMTP_USER=JOptionPane.showInputDialog("SMTP USER Name:");
			// POSFrame.SMTP_USER_PASS=JOptionPane.showInputDialog("SMTP Password:");
			// }

			if (POSFrame.USER != null && POSFrame.USER.length() > 0) {
				this.DB_URL += USER_DB;
				// conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
				return;
			}
			USER_DB = JOptionPane.showInputDialog(null, "Enter store username", "Store Username", JOptionPane.QUESTION_MESSAGE);// "root";
			this.DB_URL += USER_DB;
			JPasswordField pwd = new JPasswordField(10);
			pwd.addAncestorListener(new AncestorListener() {

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

			int action = JOptionPane.showConfirmDialog(null, pwd, "Store Password", JOptionPane.OK_CANCEL_OPTION);
			String pass = "";
			if (action == JOptionPane.OK_OPTION && pwd.getText().length() > 0) {
				pass = pwd.getText().toString();
			} else {
				throw new Exception();
			}
			POSFrame.loading.setVisible(true);//////////////////

			String server = Post.postRequest(WebServiceURL + "/pos/db.php",
			"code=" + Encryption.encryptPassword("acbadbacba") + "&username=" + USER_DB + "&password=" + Encryption.encryptPassword(pass));
			JSONObject json_server = Post.stringToJSON(server);
			// JSONObject json = (JSONObject) json_server.get("cred");
			// System.out.println("get: "+json);
			POSFrame.USER = json_server.getString("db_user");
			POSFrame.PASS = json_server.getString("db_password");
			POSFrame.SMTP_USER = json_server.getString("cpanel_user");
			POSFrame.SMTP_USER_PASS = json_server.getString("cpanel_pass");
			// System.out.println(POSFrame.USER +" pass: "+POSFrame.PASS);
			this.loadLoginCredentials();
			this.loadShopDetails();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot connect to DB.\nCall ACBA for help.\nProgram Terminating.", "Database connection error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} finally {
			// this.closeConnections(p, r, conn);
		}
	}

	/////////////////
	private void loadLoginCredentials() throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
		PreparedStatement p = conn.prepareStatement("select * from employee");
		ResultSet r = p.executeQuery();
		while (r.next()) {
			String id = r.getString("id");
			String fname = r.getString("fname");
			String mname = r.getString("mname");
			String lname = r.getString("lname");
			double comm = r.getDouble("commission");
			this.stylist_login.put(id, new Stylist(id, fname, mname, lname, comm));
		}
		p = conn.prepareStatement("select * from admin");
		r = p.executeQuery();
		while (r.next()) {
			this.admin_login.put(r.getString("id"), r.getString("employee_id"));
		}
		this.closeConnections(p, r, conn);
	}

	private void loadShopDetails() {
		this.products = getProductDetailHashMap();
		this.haircuts = getHaircutDetailHashMap();
		this.coupons = getCouponDetailHashMap();
		this.stylists = getStylistsDetailHashMap();
	}

	public HashMap<String, Product> getLoadedProducts() {
		return this.products;
	}

	public HashMap<String, Stylist> getLoadedStylists() {
		return this.stylists;
	}

	public HashMap<String, Coupon> getLoadedCoupons() {
		return this.coupons;
	}

	public HashMap<String, Haircut> getLoadedHaircut() {
		return this.haircuts;
	}

	//////////////////////// businessNAME
	public String getBusinessName() {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select company_name from client");
			r = p.executeQuery();
			while (r.next()) {
				return r.getString("company_name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return "No-Reply";
	}

	//////////////////////////////////////// STORE DB
	// I need to get the haircut id and subtract from amount then comminssion
	public void displayDailyGross(JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BigDecimal cashreg = this.getCashRegisterAmount();
			Date date = new Date();

			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			BigDecimal store_gross_total = new BigDecimal(0);
			BigDecimal gross_haircut_profit = new BigDecimal(0), gross_inventory_profits = new BigDecimal(0), gross_employee_earnings = new BigDecimal(0);
			p = conn.prepareStatement("select * from transaction T , haircut H where '" + sdf.format(date) + "'=Date(T.date_time) and H.id=T.haircut_id group by T.id");
			r = p.executeQuery();
			while (r.next()) {
				BigDecimal hair = r.getBigDecimal("H.price");
				gross_haircut_profit = gross_haircut_profit.add(hair);
				BigDecimal comm = new BigDecimal(r.getDouble("T.commission"));
				BigDecimal emp = hair.subtract(hair.multiply(comm));
				gross_employee_earnings = gross_employee_earnings.add(emp);
			}
			p = conn.prepareStatement(
			"SELECT (IT.quantity*I.retail_price)as total from inventory_transaction IT, inventory I where I.sku=IT.sku and Date(IT.date_time)='" + sdf.format(date) + "' GROUP BY IT.id");
			r = p.executeQuery();
			while (r.next()) {///// get all product for today
				gross_inventory_profits = gross_inventory_profits.add(r.getBigDecimal("total"));

			}
			store_gross_total = gross_haircut_profit.add(gross_inventory_profits);
			String text = "<table width=100% height=100% border=5 ><tr><th>Starting Cash Register</th><th>Gross Haircut Profits</th><th>Inventory Sales</th><th>Gross Employee Earnings</th><th><font color=blue>Daily Profit</font></th><th><font color=red>Amount Should Be In The Cash Register</font></th></tr>";
			DecimalFormat df = new DecimalFormat("$###,###,###.##");
			text += "<tr><td>" + df.format(cashreg) + "</td><td>" + df.format(gross_haircut_profit) + "</td><td>" + df.format(gross_inventory_profits) + "</td><td>-"
			+ df.format(gross_employee_earnings) + "</td><td>" + (df.format(store_gross_total.subtract(gross_employee_earnings))) + "</td><td>"
			+ df.format(store_gross_total.subtract(gross_employee_earnings).add(cashreg)) + "</td></tr>";

			text += "</table>";
			screen.setText(text);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}

	}

	public void setRegisterAmountBy(BigDecimal d) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("update store set cash_register='" + d + "'");
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public BigDecimal getCashRegisterAmount() {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select cash_register from store");
			r = p.executeQuery();
			while (r.next()) {
				return r.getBigDecimal("cash_register");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return null;
	}

	public int getBusinessOpenTimeHour() {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		String starttime = "";
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select opening_time from store");
			r = p.executeQuery();
			while (r.next()) {
				return Integer.parseInt(new SimpleDateFormat("h").format(r.getTime("opening_time")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return -1;
	}

	public int getBusinessClosingTimeHour() {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
			p = conn.prepareStatement("select closing_time from store");
			r = p.executeQuery();
			while (r.next()) {
				String time = sdf.format(r.getTimestamp("closing_time"));
				if (time.contains(":30")) {
					return Integer.parseInt(time.substring(0, time.indexOf(":"))) + 1;

				} else return Integer.parseInt(new SimpleDateFormat("HH").format(r.getTimestamp("closing_time")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return -1;
	}

	////////////////////////////////////////////////////////////////// Employee
	////////////////////////////////////////////////////////////////// db
	public String getEmployeeName(String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select Concat(fname,' ',mname,' ',lname) as name from employee where id='" + id + "'");
			r = p.executeQuery();
			while (r.next())
				return r.getString("name");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return null;
	}

	public ArrayList<String> getEmployeesArrayList() {
		ArrayList<String> emp = new ArrayList<>();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from employee");
			r = p.executeQuery();

			while (r.next()) {
				emp.add(r.getString("fname") + " " + r.getString("mname") + " " + r.getString("lname"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return emp;
	}

	public boolean wasWorkingOnDate(String id, String date) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from clockin_out where employee_id='" + id + "' AND Date(start_datetime)='" + date + "'");
			r = p.executeQuery();
			while (r.next()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return false;
	}

	public void updateTimeClockIn(String id, String date, String starttime, String endtime) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			Date startdate = sdf.parse(date + " " + starttime);
			Date enddate = sdf.parse(date + " " + endtime);
			Timestamp startdatetime = new Timestamp(startdate.getTime());
			Timestamp enddatetime = new Timestamp(enddate.getTime());

			p = conn.prepareStatement("delete from clockin_out where Date(start_datetime)='" + date + "'");
			p.execute();
			p = conn.prepareStatement("INSERT INTO `clockin_out` (`employee_id`, `start_datetime`, `end_datetime`) VALUES ('" + id + "', '" + startdatetime + "', '" + enddatetime + "');");
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayLOGCashRegister(JTextPane screen, String start, String end) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			if (start == null) {
				p = conn.prepareStatement("select * from pos_log, employee where emp_id=id order by date_time DESC");
			} else {
				p = conn.prepareStatement("select * from pos_log, employee where emp_id=id AND Date(date_time) BETWEEN '" + start + "' AND '" + end + "' order by date_time DESC");
			}
			ResultSet rs = p.executeQuery();
			r = rs;// hopefully this works
			String s = "<html><table border=1 style=width:100%><tr><th>ID</th><th>Employee First Name</th><th>Middle Name</th><th>Last Name</th><th>Date & Time Opened</th></tr>";
			while (rs.next()) {
				s += "<tr><td>" + rs.getString("id") + "</td><td>" + rs.getString("fname") + "</td><td>" + rs.getString("mname") + "</td><td>" + rs.getString("lname") + "</td><td>"
				+ rs.getString("date_time") + "</td></tr>";
			}
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	/** Display log of employee of id id and if date is specified */
	public void displayLOGCashRegister(String id, JTextPane screen, String start, String end) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			if (start == null) {
				p = conn.prepareStatement("select * from pos_log, employee where emp_id=id AND id='" + id + "' order by date_time DESC");
			} else {
				p = conn.prepareStatement("select * from pos_log, employee where emp_id=id AND id='" + id + "' AND Date(date_time) BETWEEN '" + start + "' AND '" + end + "' order by date_time DESC");
			}
			ResultSet rs = p.executeQuery();
			r = rs;
			String s = "<html><table border=1 style=width:100%><tr><th>ID</th><th>Employee First Name</th><th>Middle Name</th><th>Last Name</th><th>Date & Time Opened</th></tr>";
			while (rs.next()) {
				s += "<td>" + rs.getString("id") + "</td><td>" + rs.getString("fname") + "</td><td>" + rs.getString("mname") + "</td><td>" + rs.getString("lname") + "</td><td>"
				+ rs.getString("date_time") + "</td>";
			}
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	// /** Get the total hours worked from a specified or non specified date. */
	// public double getTotalHoursWorked(String employee_id, String start, String end) {
	// try {
	// // conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
	// PreparedStatement p;
	// if (start == null) {
	// p = conn.prepareStatement(
	// "(select sum(TIME_TO_SEC(TIMEDIFF(TIME(end_datetime),TIME(start_datetime)))) as total_hours from clockin_out where end_datetime is not null AND employee_id='" + employee_id + "')");
	// } else {
	// p = conn.prepareStatement("(select sum(TIME_TO_SEC(TIMEDIFF(TIME(end_datetime),TIME(start_datetime)))) as total_hours from clockin_out where end_datetime is not null AND employee_id='"
	// + employee_id + "' AND Date(start_datetime) BETWEEN '" + start + "' AND '" + end + "')");
	// }
	// ResultSet rs = p.executeQuery();
	// while (rs.next()) {
	// return rs.getDouble("total_hours") / (60.0 * 60.0);
	// }
	// p.close();
	// rs.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// return 0;
	// }
	// return 0;
	// }

	/** GET ALL EMPLOYEE THAT WORK HOURLY AND THEIR PAY */
	private HashMap<String, ArrayList> getEmployeeHourlyPay(String start, String end) {
		HashMap<String, ArrayList> map = new HashMap<>();

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			p = conn.prepareStatement(
			"SELECT E.id, SUM(ABS((TIME_TO_SEC(TIMEDIFF(C.end_datetime,C.start_datetime))/3600.0)*E.hourly_wage))as emp_pay, (SUM(ABS((TIME_TO_SEC(TIMEDIFF(C.end_datetime,C.start_datetime))/3600.0))))as hours_worked FROM clockin_out C, employee E WHERE employee_id=E.id AND C.end_datetime IS NOT NULL AND Date(C.start_datetime) BETWEEN '"
			+ start + "' AND '" + end + "' GROUP BY E.id ");

			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				ArrayList a = new ArrayList();
				a.add(rs.getBigDecimal("emp_pay"));
				a.add(rs.getDouble("hours_worked"));
				map.put(rs.getString("E.id"), a);
			}
			p.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/** Displays only the specified employee by id. Start is the Date: as yyyy-MM-dd */
	public void displayAllTransactionsOfEmployee(String id, JTextPane screen, String start, String end) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			if (start == null || end == null) {
				// between jan 1,year to now
				Date today = Calendar.getInstance().getTime();
				start = "" + (today.getYear() + 1900) + "-01-01";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				end = sdf.format(today);
			}
			// get the employees that have salary
			HashMap<String, ArrayList> emp_map = this.getEmployeeHourlyPay(start, end);
			// System.out.println("start: " + start + " end: " + end);a
			p = conn.prepareStatement("SELECT E.*, SUM(T.commission*H.price)as emp_pay FROM transaction T, employee E, haircut H WHERE T.haircut_id=H.id AND T.employee_id='" + id
			+ "' AND E.id=T.employee_id AND Date(T.date_time) BETWEEN '" + start + "' AND '" + end + "' GROUP BY E.id ");

			ResultSet rs = p.executeQuery();
			r = rs;
			String s = "<html><table border=1 style=width:100%><tr><th>ID</th><th>Employee Name</th><th>Commission</th><th>Employee Earnings</th><th>Hourly Wage</th><th>Hours Worked</th><th>Hourly Earnings</th></tr>";
			DecimalFormat df = new DecimalFormat("$#,###,##0.00");
			DecimalFormat dfc = new DecimalFormat("0.00%");
			double total_employee_pay = 0;
			while (rs.next()) {
				BigDecimal hourly_pay = null;
				double hours_worked = 0;
				double emp_pay = rs.getDouble("emp_pay");
				String e_id = rs.getString("E.id");
				if (emp_map.get(e_id) != null) {
					hourly_pay = (BigDecimal) emp_map.get(e_id).get(0);
					hours_worked = (double) emp_map.get(e_id).get(1);
				} else {
					hourly_pay = new BigDecimal(0);
				}
				String fname = rs.getString("E.fname");
				String mname = rs.getString("E.mname");
				String lname = rs.getString("E.lname");
				String name = fname + " " + lname;
				if (mname.length() > 0) name = fname + " " + mname + " " + lname;
				Stylist sty = this.stylists.get(name);

				s += "<tr><td>" + e_id + "</td><td>" + sty.getName() + "</td><td>" + dfc.format(sty.getCommission()) + "</td><td>" + df.format(emp_pay) + "</td><td>"
				+ df.format(rs.getBigDecimal("hourly_wage").doubleValue()) + "</td><td>" + hours_worked + "</td><td>" + df.format(hourly_pay.doubleValue()) + "</td></tr>";
				///////////////////////// FIX FORMATTING
				total_employee_pay += emp_pay + hourly_pay.doubleValue();
			}
			s += "<tr><td></td><td>Store Total Payout:</td><td></td><td>" + df.format(total_employee_pay) + "</td></tr>";
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllTransactionsOfEmployee(JTextPane screen, String start, String end) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			if (start == null || end == null) {
				// between jan 1,year to now
				Date today = Calendar.getInstance().getTime();
				start = "" + (today.getYear() + 1900) + "-01-01";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				end = sdf.format(today);
			}
			// get the employees that have salary
			HashMap<String, ArrayList> emp_map = this.getEmployeeHourlyPay(start, end);
			// System.out.println("start: " + start + " end: " + end);a
			p = conn
			.prepareStatement("SELECT E.*, SUM(T.commission*H.price)as emp_pay FROM transaction T, employee E, haircut H WHERE T.haircut_id=H.id AND T.employee_id=E.id AND Date(T.date_time) BETWEEN '"
			+ start + "' AND '" + end + "' GROUP BY E.id ");

			ResultSet rs = p.executeQuery();
			r = rs;
			String s = "<html><table border=1 style=width:100%><tr><th>ID</th><th>Employee Name</th><th>Commission</th><th>Employee Earnings</th><th>Hourly Wage</th><th>Hours Worked</th><th>Hourly Earnings</th></tr>";
			DecimalFormat df = new DecimalFormat("$#,###,###.00");
			DecimalFormat dfc = new DecimalFormat("#.00%");
			double total_employee_pay = 0;
			while (rs.next()) {
				BigDecimal hourly_pay = null;
				double hours_worked = 0;
				double emp_pay = rs.getDouble("emp_pay");
				String e_id = rs.getString("E.id");
				if (emp_map.get(e_id) != null) {
					hourly_pay = (BigDecimal) emp_map.get(e_id).get(0);
					hours_worked = (double) emp_map.get(e_id).get(1);
				} else {
					hourly_pay = new BigDecimal(0);
				}
				String fname = rs.getString("E.fname");
				String mname = rs.getString("E.mname");
				String lname = rs.getString("E.lname");
				String name = fname + " " + lname;
				if (mname.length() > 0) name = fname + " " + mname + " " + lname;
				Stylist sty = this.stylists.get(name);

				s += "<tr><td>" + e_id + "</td><td>" + sty.getName() + "</td><td>" + dfc.format(sty.getCommission()) + "</td><td>" + df.format(emp_pay) + "</td><td>"
				+ df.format(rs.getBigDecimal("hourly_wage").doubleValue()) + "</td><td>" + hours_worked + "</td><td>" + df.format(hourly_pay.doubleValue()) + "</td></tr>";
				///////////////////////// FIX FORMATTING
				total_employee_pay += emp_pay + hourly_pay.doubleValue();
			}
			s += "<tr><td></td><td>Store Total Payout:</td><td></td><td>" + df.format(total_employee_pay) + "</td></tr>";

			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllEmployeeInfo(JTextPane screen, String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			if (id == null || id.length() == 0) {
				p = conn.prepareStatement("select * from employee");
			} else {
				p = conn.prepareStatement("select * from employee E where E.id='" + id + "'");
			}

			ResultSet rs = p.executeQuery();
			r = rs;
			String s = "<html><table border=1 style=width:100%><tr><th>ID</th><th>Employee First Name</th><th>Middle Name</th><th>Last Name</th><th>Email</th><th>Phone</th><th>SSN</th><th>Salary</th><th>Hourly Pay</th><th>Commission</th><th>Monthly Rent</th><th>Clocked In</th></tr>";
			DecimalFormat df = new DecimalFormat("$#,##0.00");
			DecimalFormat dfc = new DecimalFormat("0.00%");

			while (rs.next()) {
				s += "<tr><td>" + rs.getString("id") + "</td><td>" + rs.getString("fname") + "</td><td>" + rs.getString("mname") + "</td><td>" + rs.getString("lname") + "</td><td>"
				+ rs.getString("email") + "</td><td>" + rs.getString("phone") + "</td><td>" + rs.getString("ssn") + "</td><td>" + df.format(rs.getBigDecimal("salary").doubleValue()) + "</td><td>"
				+ df.format(rs.getBigDecimal("hourly_wage").doubleValue()) + "</td><td>" + dfc.format(rs.getDouble("commission")) + "</td><td>"
				+ df.format(rs.getBigDecimal("monthly_rent").doubleValue()) + "</td><td>" + rs.getBoolean("active") + "</td></tr>";
			}
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void updatePayEmployee(String id, BigDecimal salary, BigDecimal hourly_wage, double commission, double monthly_rent) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement(
			"update employee set salary='" + salary + "' , hourly_wage='" + hourly_wage + "' ,commission='" + commission + "' ,monthly_rent='" + monthly_rent + "' where id='" + id + "'");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void deleteEmployee(String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("delete from employee where id='" + id + "'");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void addNewEmployee(String id, String fname, String mname, String lname, String email, String phone, String ssn, BigDecimal salary, BigDecimal hourly_pay, double commission,
	double monthly_rent) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement(
			"INSERT INTO `employee` (`id`, `fname`, `mname`, `lname`, `email`, `phone`, `ssn`, `salary`, `hourly_wage`, `commission`,`monthly_rent`, `active`) VALUES ('" + id + "', '" + fname + "', '"
			+ mname + "', '" + lname + "', '" + email + "', '" + phone + "', '" + ssn + "', '" + salary + "', '" + hourly_pay + "', '" + commission + "','" + monthly_rent + "', '0');");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public boolean isEmployee(String emp_pass) {
		boolean is = false;
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from employee where id='" + emp_pass + "'");
			r = p.executeQuery();
			while (r.next()) {
				is = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return is;
	}

	public void clockInOut(String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MM-dd-yyyy h:mm a");
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = new Date();
			p = conn.prepareStatement("select * from clockin_out O, employee E where O.employee_id=E.id AND E.id='" + id + "' AND O.end_datetime IS NULL");
			r = p.executeQuery();
			if (r.next()) {// clock Out
				JOptionPane.showMessageDialog(null, "Name: " + this.getEmployeeName(id) + "\nClocked in: " + sdf.format(r.getTimestamp("start_datetime")) + "\nClocked out: " + sdf.format(date));
				p = conn.prepareStatement("update employee, clockin_out set employee.active='0', clockin_out.end_datetime='" + f.format(date) + "' where  employee.id='" + id
				+ "' AND employee.id=clockin_out.employee_id AND clockin_out.end_datetime IS NULL");
				p.execute();
			} else {
				p = conn.prepareStatement("INSERT INTO `clockin_out` (`employee_id`, `start_datetime`, `end_datetime`) VALUES ('" + id + "', CURRENT_TIMESTAMP, NULL);");
				p.execute();
				p = conn.prepareStatement("update employee set active='1' where id='" + id + "'");
				p.execute();
				JOptionPane.showMessageDialog(null, "Name: " + this.getEmployeeName(id) + "\nClocked in: " + sdf.format(new Date()));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	///////////////////////////////////////////////////////////////////// Transaction
	///////////////////////////////////////////////////////////////////// DB
	public void recordTransaction(long customer_id, double total_amount, ArrayList<CombinedSale> cs, ArrayList<Product> prod, ArrayList<Coupon> coupon, Date date) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			p = conn.prepareStatement("");// EEROR????
			for (CombinedSale s : cs) {
				BigDecimal decimal = new BigDecimal(total_amount);
				p = conn.prepareStatement(
				"INSERT INTO `transaction` (`id`, `employee_id`, `amount`, `date_time`, `haircut_id`, `coupon_sku`, `commission`, `customer_id`) " + "VALUES (NULL, '" + s.getStylist().getID() + "', '"
				+ decimal + "', '" + sdf.format(date) + "', '" + s.getHaircut().getId() + "', '" + s.getCoupon().getSKU() + "', '" + s.getStylist().getCommission() + "', '" + customer_id + "');");
				p.execute();
			}
			recordInventoryTransaction(prod, customer_id, date);
			// recordCouponTransaction(coupon,customer_id);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	/////////////////////// coupon db
	////////////////////// admin db check hashmap
	public boolean isAdmin(String pass) {
		return this.admin_login.get(pass) != null;
	}

	///////////////////////////////////////////////////////////////////////////// current
	///////////////////////////////////////////////////////////////////////////// db
	public void updateStylist(String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;

		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("update current set id='" + id + "'");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	//////////////////////////////////////////////////////////////////// INVENTORY
	//////////////////////////////////////////////////////////////////// DB
	public void displayAllTransactionsOfProduct(String start, String end, String sku, JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement(
			"select *, DATE_FORMAT(IT.date_time, '%m') as 'month',COUNT(IT.quantity)as sold from inventory_transaction IT, inventory I where IT.sku=I.sku AND Date(IT.date_time) BETWEEN '" + start
			+ "' AND '" + end + "' GROUP BY DATE_FORMAT(IT.date_time, '%Y%m'), IT.sku ORDER BY IT.date_time ");// "select *, Count(IT.quantity)as sold from inventory_transaction IT, inventory I where
																												// IT.sku=I.sku AND Date(IT.date_time) BETWEEN '" + start + "' AND '" + end + "' Group
																												// BY IT.sku ORDER BY IT.date_time");
			// conn
			// .prepareStatement("select *,COUNT(IT.quantity)as sold from inventory_transaction IT, inventory I where IT.sku='" + sku + "' AND IT.sku=I.sku AND Date(IT.date_time) BETWEEN '" + start +
			// "' AND '" + end + "' Group BY IT.sku ORDER BY IT.date_time");
			ResultSet rs = p.executeQuery();
			String s = "<html><table border=1 style=width:100%><tr><th>SKU</th><th> Product Name</th><th> Retail Price</th><th>Transaction Date</th><th>Quantity sold</th><th>Customer ID</th><th>Amount Grossed</th></tr>";
			int current_month = -1;
			int total_month = 0;
			int grand_total = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM / yyyy");
			DecimalFormat df = new DecimalFormat("$#,##0.00");
			double total_grossed = 0;
			double prod_grossed = 0;
			Date date = new Date();
			while (rs.next()) {
				int quantity = rs.getInt("sold");
				int month = Integer.parseInt(rs.getString("month"));
				double retail = rs.getBigDecimal("I.retail_price").doubleValue();
				double grossed = retail * quantity;
				if (current_month > 0 && month > current_month) {// flag to be set for current month
					s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed)
					+ "</td></tr>";
					current_month = month;
					total_month = 0;
					prod_grossed = 0;

				} else {
					current_month = month;
				}
				date.setMonth(month - 1);
				s += "<tr><td>" + rs.getString("I.sku") + "</td><td>" + rs.getString("I.product_name") + "</td><td>" + df.format(retail) + "</td><td>" + sdf.format(date) + "</td><td>" + quantity
				+ "</td><td>" + rs.getString("IT.customer_id") + "</td><td>" + df.format(grossed) + "</td></tr>";
				grand_total += quantity;
				total_month += quantity;
				prod_grossed += grossed;
				total_grossed += grossed;
			}
			s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed) + "</td></tr>";

			s += "<mark><tr><td>Combined Total Sold For Search: </td><td>" + this.df.format(grand_total) + "</td><td>Combined Amount Grossed: </td><td>" + df.format(total_grossed)
			+ "</td></tr></mark>";
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllTransactionsOfProduct(String start, String end, JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement(
			"select *, DATE_FORMAT(IT.date_time, '%m') as 'month',COUNT(IT.quantity)as sold from inventory_transaction IT, inventory I where IT.sku=I.sku AND Date(IT.date_time) BETWEEN '" + start
			+ "' AND '" + end + "' GROUP BY DATE_FORMAT(IT.date_time, '%Y%m'), IT.sku ORDER BY IT.date_time ");// "select *, Count(IT.quantity)as sold from inventory_transaction IT, inventory I where
			ResultSet rs = p.executeQuery();
			String s = "<html><table border=1 style=width:100%><tr><th>SKU</th><th> Product Name</th><th> Retail Price</th><th>Transaction Date</th><th>Quantity sold</th><th>Customer ID</th><th>Amount Grossed</th></tr>";
			int current_month = -1;
			int total_month = 0;
			int grand_total = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM / yyyy");
			DecimalFormat df = new DecimalFormat("$#,##0.00");
			double total_grossed = 0;
			double prod_grossed = 0;
			Date date = new Date();
			while (rs.next()) {
				int quantity = rs.getInt("sold");
				int month = Integer.parseInt(rs.getString("month"));
				double retail = rs.getBigDecimal("I.retail_price").doubleValue();
				double grossed = retail * quantity;
				if (current_month > 0 && month > current_month) {// flag to be set for current month
					s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed)
					+ "</td></tr>";
					current_month = month;
					total_month = 0;
					prod_grossed = 0;

				} else {
					current_month = month;
				}
				date.setMonth(month - 1);
				s += "<tr><td>" + rs.getString("I.sku") + "</td><td>" + rs.getString("I.product_name") + "</td><td>" + df.format(retail) + "</td><td>" + sdf.format(date) + "</td><td>" + quantity
				+ "</td><td>" + rs.getString("IT.customer_id") + "</td><td>" + df.format(grossed) + "</td></tr>";
				grand_total += quantity;
				total_month += quantity;
				prod_grossed += grossed;
				total_grossed += grossed;
			}
			s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed) + "</td></tr>";

			s += "<mark><tr><td>Combined Total Sold For Search: </td><td>" + this.df.format(grand_total) + "</td><td>Combined Amount Grossed: </td><td>" + df.format(total_grossed)
			+ "</td></tr></mark>";
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllTransactionsOfProduct(String sku, JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement(
			"select *, DATE_FORMAT(IT.date_time, '%m') as 'month',COUNT(IT.quantity)as sold from inventory_transaction IT, inventory I where IT.sku=I.sku  GROUP BY DATE_FORMAT(IT.date_time, '%Y%m'), IT.sku ORDER BY IT.date_time ");// "select

			ResultSet rs = p.executeQuery();
			String s = "<html><table border=1 style=width:100%><tr><th>SKU</th><th> Product Name</th><th> Retail Price</th><th>Transaction Date</th><th>Quantity sold</th><th>Customer ID</th><th>Amount Grossed</th></tr>";
			int current_month = -1;
			int total_month = 0;
			int grand_total = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM / yyyy");
			DecimalFormat df = new DecimalFormat("$#,##0.00");
			double total_grossed = 0;
			double prod_grossed = 0;
			Date date = new Date();
			while (rs.next()) {
				int quantity = rs.getInt("sold");
				int month = Integer.parseInt(rs.getString("month"));
				double retail = rs.getBigDecimal("I.retail_price").doubleValue();
				double grossed = retail * quantity;
				if (current_month > 0 && month > current_month) {// flag to be set for current month
					s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed)
					+ "</td></tr>";
					current_month = month;
					total_month = 0;
					prod_grossed = 0;

				} else {
					current_month = month;
				}
				date.setMonth(month - 1);
				s += "<tr><td>" + rs.getString("I.sku") + "</td><td>" + rs.getString("I.product_name") + "</td><td>" + df.format(retail) + "</td><td>" + sdf.format(date) + "</td><td>" + quantity
				+ "</td><td>" + rs.getString("IT.customer_id") + "</td><td>" + df.format(grossed) + "</td></tr>";
				grand_total += quantity;
				total_month += quantity;
				prod_grossed += grossed;
				total_grossed += grossed;
			}
			s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed) + "</td></tr>";

			s += "<mark><tr><td>Combined Total Sold For Search: </td><td>" + this.df.format(grand_total) + "</td><td>Combined Amount Grossed: </td><td>" + df.format(total_grossed)
			+ "</td></tr></mark>";
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllTransactionsOfProduct(JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement(
			"select *, DATE_FORMAT(IT.date_time, '%m') as 'month',COUNT(IT.quantity)as sold from inventory_transaction IT, inventory I where IT.sku=I.sku GROUP BY DATE_FORMAT(IT.date_time, '%Y%m'), IT.sku ORDER BY IT.date_time ");
			// p = conn.prepareStatement("select *, Count(IT.quantity)as sold from inventory_transaction IT, inventory I where IT.sku=I.sku GROUP BY IT.sku ORDER BY IT.date_time");
			ResultSet rs = p.executeQuery();
			String s = "<html><table border=1 style=width:100%><tr><th>SKU</th><th> Product Name</th><th> Retail Price</th><th>Transaction Date</th><th>Quantity sold</th><th>Customer ID</th><th>Amount Grossed</th></tr>";
			int current_month = -1;
			int total_month = 0;
			int grand_total = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM / yyyy");
			DecimalFormat df = new DecimalFormat("$#,##0.00");
			double total_grossed = 0;
			double prod_grossed = 0;
			Date date = new Date();
			while (rs.next()) {
				int quantity = rs.getInt("sold");
				int month = Integer.parseInt(rs.getString("month"));
				double retail = rs.getBigDecimal("I.retail_price").doubleValue();
				double grossed = retail * quantity;
				if (current_month > 0 && month > current_month) {// flag to be set for current month
					s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed)
					+ "</td></tr>";
					current_month = month;
					total_month = 0;
					prod_grossed = 0;

				} else {
					current_month = month;
				}
				date.setMonth(month - 1);
				s += "<tr><td>" + rs.getString("I.sku") + "</td><td>" + rs.getString("I.product_name") + "</td><td>" + df.format(retail) + "</td><td>" + sdf.format(date) + "</td><td>" + quantity
				+ "</td><td>" + rs.getString("IT.customer_id") + "</td><td>" + df.format(grossed) + "</td></tr>";
				grand_total += quantity;
				total_month += quantity;
				prod_grossed += grossed;
				total_grossed += grossed;
			}
			s += "<tr><td>Total Amount Sold For " + sdf.format(date) + "</td><td>" + this.df.format(total_month) + "</td><td>Amount Grossed: </td><td>" + df.format(prod_grossed) + "</td></tr>";

			s += "<mark><tr><td>Combined Total Sold For Search: </td><td>" + this.df.format(grand_total) + "</td><td>Combined Amount Grossed: </td><td>" + df.format(total_grossed)
			+ "</td></tr></mark>";
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllProductInfo(JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from inventory ");
			ResultSet rs = p.executeQuery();
			r = rs;
			String s = "<html><table border=1 style=width:100%><tr><th>SKU</th><th> Product Name</th><th> Retail Price</th><th> Wholesale/Unit Price</th><th> Stock</th><th> Date Created</th></tr>";
			while (rs.next()) {
				s += "<tr><td>" + rs.getString("sku") + "</td><td>" + rs.getString("product_name") + "</td><td>" + rs.getString("retail_price") + "</td><td>" + rs.getString("wholesale_price")
				+ "</td><td>" + rs.getString("stock") + "</td><td>" + rs.getString("date_time") + "</td></tr>";
			}
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void displayAllProductInfo(String sku, JTextPane screen) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from inventory where sku='" + sku + "'");
			ResultSet rs = p.executeQuery();
			r = rs;
			String s = "<html><table border=1 style=width:100%><tr><th>SKU</th><th> Product Name</th><th> Retail Price</th><th> Wholesale/Unit Price</th><th> Stock</th><th> Date Created</th></tr>";
			while (rs.next()) {
				s += "<tr><td>" + rs.getString("sku") + "</td><td>" + rs.getString("product_name") + "</td><td>" + rs.getString("retail_price") + "</td><td>" + rs.getString("wholesale_price")
				+ "</td><td>" + rs.getString("stock") + "</td><td>" + rs.getString("date_time") + "</td></tr>";
			}
			screen.setText(s + "</table></html>");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void restockProduct(String sku, long restock) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("update inventory set stock=stock+" + restock + " where sku='" + sku + "'");
			p.execute();
			p = conn.prepareStatement("INSERT INTO `restock_order` (`sku`, `restock_amount`, `order_date`) VALUES ('" + sku + "', '" + restock + "', CURRENT_TIMESTAMP 	);");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public boolean addNewInventory(String sku, String name, double wholesale_price, double retail_price, long stock) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("INSERT INTO `inventory` (`sku`, `product_name`, `retail_price`, `wholesale_price`, `stock`,`date_time`) VALUES ('" + sku + "', '" + name + "', '"
			+ new BigDecimal(wholesale_price) + "'," + "'" + new BigDecimal(retail_price) + "', '" + stock + "',CURRENT_TIMESTAMP);");
			p.execute();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public void deleteFromInvevntory(String sku) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("delete from inventory where sku='" + sku + "'");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public boolean isProduct(String sku) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from inventory where sku='" + sku + "'");
			r = p.executeQuery();
			while (r.next()) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return false;
	}

	public void recordInventoryTransaction(ArrayList<Product> prod, long customer_id, Date date) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (Product pp : prod) {
				p = conn.prepareStatement("INSERT INTO `inventory_transaction` (`id`, `sku`, `date_time`, `customer_id`,`quantity`) VALUES (NULL, '" + pp.getSKU() + "', '" + sdf.format(date) + "', '"
				+ customer_id + "','" + pp.getQuantity() + "');");
				p.execute();
				p = conn.prepareStatement("update inventory set stock=stock-" + (long) pp.getQuantity() + " where sku='" + pp.getSKU() + "';");
				p.execute();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	private HashMap<String, Product> getProductDetailHashMap() {
		HashMap<String, Product> h = new HashMap<>();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from inventory");
			ResultSet rs = p.executeQuery();
			r = rs;
			// STEP 5: Extract data from result set
			while (rs.next()) {
				String name = rs.getString("product_name");
				String sku = rs.getString("sku");
				double price = rs.getDouble("retail_price");
				Product pp = new Product(sku, name, price);
				h.put(name, pp);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return h;
	}

	///////////////////////////////////////////////////////////////////////
	private HashMap<String, Stylist> getStylistsDetailHashMap() {
		HashMap<String, Stylist> h = new HashMap<>();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("select * from employee");
			rs = p.executeQuery();

			// STEP 5: Extract data from result set
			while (rs.next()) {
				String fname = rs.getString("fname");
				String mname = rs.getString("mname");
				String lname = rs.getString("lname");
				double comm = rs.getDouble("commission");
				String id = rs.getString("id");
				Stylist s = new Stylist(id, fname, mname, lname, comm);
				h.put(s.getName(), s);
			}
			Stylist s = new Stylist();
			h.put(s.getName(), s);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, rs, conn);
		}
		return h;
	}

	private HashMap<String, Haircut> getHaircutDetailHashMap() {
		HashMap<String, Haircut> h = new HashMap<>();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			DecimalFormat df = new DecimalFormat("###,###.##");
			p = conn.prepareStatement("select * from haircut");
			ResultSet rs = p.executeQuery();
			r = rs;
			// STEP 5: Extract data from result set
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				double price = rs.getDouble("price");
				String display = name + " : " + df.format(price).toString();
				Haircut s = new Haircut(name, price, id, display);
				h.put(display, s);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return h;
	}

	private void closeConnections(PreparedStatement p, ResultSet r, Connection conn) {
		try {
			if (r != null) r.close();
			if (p != null) p.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, Coupon> getCouponDetailHashMap() {
		HashMap<String, Coupon> h = new HashMap<>();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			p = conn.prepareStatement("select * from coupon where DATE(end_sale_date) >= '" + sdf.format(new Date()) + "'");
			ResultSet rs = p.executeQuery();
			// STEP 5: Extract data from result set
			while (rs.next()) {
				String name = rs.getString("name");
				double percent = rs.getDouble("percent");
				String sku = rs.getString("sku");
				double discount = rs.getDouble("discount");
				Coupon s = new Coupon(sku, name, percent, discount);
				h.put(name, s);
			}
			this.closeConnections(p, rs, conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return h;
	}

	/////////////////////////////////////////////////////////// LOG DB
	public void logCashRegister(String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("INSERT INTO `pos_log` (`emp_id`, `date_time`) VALUES ('" + id + "', NOW());");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	////////////////////////////////////////// JCHARTS: Employee and Inventory
	////////////////////////////////////////// DB COMBINED
	/**
	 * Gets information about the previous week
	 */
	public DefaultCategoryDataset getWeeklyProductsForCharts(JTextPane screen) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			int start = this.getBusinessOpenTimeHour();
			int close = this.getBusinessClosingTimeHour();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			p = conn.prepareStatement(
			"SELECT I.product_name,IT.date_time,IT.sku, COUNT(IT.sku)as sold FROM inventory I, inventory_transaction IT where yearweek(IT.date_time) = yearweek(curdate()-1) AND I.sku=IT.sku GROUP BY IT.date_time ORDER BY IT.date_time ");
			ResultSet rs = p.executeQuery();

			String text = "<table width=100% height=100% border=2 ><tr><th>Day</th><th>Product Name</th><th>SKU</th><th>Number of Units Sold</th></tr>";
			sdf = new SimpleDateFormat("EEE");

			HashMap<Product, HashMap<Integer, Integer>> day_sold = new HashMap<>();

			while (rs.next()) {
				Date day = rs.getTimestamp("IT.date_time");
				String name = rs.getString("I.product_name");
				int sold = rs.getInt("sold");
				String sku = rs.getString("IT.sku");

				Product prod = new Product(sku, name);
				if (day_sold.get(prod) == null) {// not in datastructure
					day_sold.put(prod, setHashMap());
				}
				day_sold.get(prod).put(day.getDay(), sold);// store daily sell
				// System.out.println(prod+" "+day+" "+sold);
			}
			this.closeConnections(p, rs, conn);
			////////////// done with query
			Calendar c = Calendar.getInstance();
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date temp = c.getTime();// monday
			for (int i = 1; i <= 7; ++i) {
				int sum = 0;
				String day = DAY.values()[i - 1].toString();
				// System.out.println("enum: " + day + " date: " + sdf.format(temp));
				for (Product prod : day_sold.keySet()) {
					int sold = day_sold.get(prod).get(i);
					data.addValue(sold, prod.getName(), sdf.format(temp));
					text += "<tr><td>" + day + "</td><td>" + prod.getName() + "</td><td>" + prod.getSKU() + "</td><td>" + sold + "</td></tr>";

					sum += sold;
				}

				data.addValue(sum, "STORE", sdf.format(temp));
				if (sum > 0) {

					text += "<tr><td><font bgcolor=yellow>" + day + "</td><td>STORE</td><td>N/A</td><td>" + sum + "</td></font></tr>";
				}
				temp.setDate(temp.getDate() + 1);
			}
			text += "</table>";
			screen.setText(text);
			return data;// return this.getRESULTS(dd, 7, trans, data, false, JChart.OVERVIEW.DAILY,screen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return data;
	}

	private HashMap<Integer, Integer> setHashMap() {// 1=monday...7=sunday
		HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
		for (int i = 1; i <= 7; ++i) {
			h.put(i, 0);
		}
		return h;
	}

	/**
	 * Just retrieving data for jcharts Purpose is to display daily inventory for JChart o.Hourly chart.
	 * 
	 * @param screen
	 */
	public DefaultCategoryDataset getDailyProductsForCharts(Date date, JTextPane screen) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			int start = this.getBusinessOpenTimeHour();
			int close = this.getBusinessClosingTimeHour();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			p = conn.prepareStatement("select * from inventory_transaction IT, inventory I where Date(IT.date_time)='" + sdf.format(date) + "' AND IT.sku=I.sku ORDER BY IT.date_time ASC");
			ResultSet rs = p.executeQuery();

			String text = "<table width=100% height=100% border=2 ><tr><th>Timestamp</th><th>Product Name</th><th>SKU</th><th>Number of Units Sold</th></tr>";
			sdf = new SimpleDateFormat("h:mm a");
			HashMap<Integer, ArrayList<Product>> time = new HashMap<>();
			for (int i = start; i <= close; ++i) {

				time.put(i, new ArrayList<>());
			}
			while (rs.next()) {
				Date day = rs.getTimestamp("IT.date_time");
				String name = rs.getString("I.product_name");
				int quantity = rs.getInt("IT.quantity");
				String sku = rs.getString("IT.sku");

				Product prod = new Product(sku, name);
				prod.setDate(day);
				prod.setQuantitySold(quantity);
				ArrayList<Product> list = time.get(day.getHours());
				list.add(prod);
				time.put(day.getHours(), list);

			}

			Date temp = new Date();
			temp.setHours(start);
			temp.setMinutes(30);
			temp.setSeconds(0);
			Date t = new Date();
			t.setHours(temp.getHours() + 1);
			t.setMinutes(0);
			for (int i = start; i < close; ++i) {
				int sum = 0;

				for (Product prod : time.get(i)) {
					data.addValue(prod.getQuantity(), prod.getName(), sdf.format(temp));
					text += "<tr><td>" + getHour(t.getHours() - 1) + "-" + sdf.format(t) + "</td><td>" + prod.getName() + "</td><td>" + prod.getSKU() + "</td><td>" + prod.getQuantity() + "</td></tr>";

					sum += prod.getQuantity();
				}

				data.addValue(sum, "STORE", sdf.format(temp));
				if (sum > 0) {

					text += "<tr><td><font bgcolor=yellow>" + getHour(t.getHours() - 1) + "-" + sdf.format(t) + "</td><td>STORE</td><td>N/A</td><td>" + sum + "</td></font></tr>";
				}

				temp.setHours(temp.getHours() + 1);
				t.setHours(t.getHours() + 1);
			}
			text += "</table>";
			screen.setText(text);
			return data;// return this.getRESULTS(dd, 7, trans, data, false, JChart.OVERVIEW.DAILY,screen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return data;
	}

	private int getHour(int hours) {
		int h = (hours) % 12;
		if (h == 0) return 12;
		return h;
	}

	/** Purpose for this method is to get a WEEKLY OVERVIEW so will display a chart Mon-Sun of transactions **/
	public DefaultCategoryDataset getDailyCustomersForCharts(JTextPane screen) {
		if (screen == null) screen = new JTextPane();
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date dd = c.getTime();
			c.setTime(dd);

			p = conn.prepareStatement("select * from transaction T, employee E where E.id=T.employee_id AND WEEK(T.date_time,1) = WEEK(NOW(),1) order by T.date_time ASC ");
			ResultSet rs = p.executeQuery();
			HashMap<String, Stylist> e = this.stylists;
			HashMap<String, Data> trans = new HashMap<String, Data>();
			for (String n : e.keySet()) {
				trans.put(n, new Data(7));
			}

			while (rs.next()) {
				Date day = rs.getDate("T.date_time");
				String name = rs.getString("E.fname") + " " + rs.getString("E.lname");
				;
				String mname = rs.getString("E.mname");

				if (mname.length() > 0) name = rs.getString("E.fname") + " " + mname + " " + rs.getString("E.lname");

				Data d = trans.get(name);
				int i = day.getDay() - 1;
				if (i < 0) i = 6;

				d.addCustomer(i);
				trans.remove(name);
				trans.put(name, d);
			}
			this.closeConnections(p, rs, conn);
			return this.getRESULTS(dd, 7, trans, data, true, JChart.OVERVIEW.DAILY, screen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return data;
	}

	/** Monthly tansaction overview of haircuts sold */
	public DefaultCategoryDataset getMonthlyCustomersForCharts(JTextPane screen) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			Calendar c = Calendar.getInstance();
			c.set(c.getTime().getYear() + 1900, Calendar.JANUARY, Calendar.SUNDAY);
			Date dd = c.getTime();
			c.setTime(dd);

			p = conn.prepareStatement(
			"select * from transaction T, employee E where E.id=T.employee_id AND Date(T.date_time) >= Subdate('" + sdf.format(dd) + "','INTERVAL 1 MONTH') order by T.date_time ASC");
			ResultSet rs = p.executeQuery();
			HashMap<String, Stylist> e = this.stylists;
			HashMap<String, Data> trans = new HashMap<String, Data>();
			for (String n : e.keySet()) {
				trans.put(n, new Data(12));
			}
			String text = "<table width=100% height=100% border=2 ><tr><th>Month</th><th>Name</th><th>Number of Customers</th></tr>";
			sdf = new SimpleDateFormat("MMMM/dd/yy");
			while (rs.next()) {
				Date month = rs.getDate("T.date_time");
				String name = rs.getString("E.fname") + " " + rs.getString("E.lname");
				String mname = rs.getString("mname");
				if (mname.length() > 0) name = rs.getString("E.fname") + " " + rs.getString("E.mname") + " " + rs.getString("E.lname");

				Data d = trans.get(name);
				d.addCustomer(month.getMonth());
				trans.remove(name);
				trans.put(name, d);
				text += "<tr><td>" + sdf.format(month) + "</td><td>" + name + "</td><td>" + d.getCustomerList()[month.getMonth()] + "</td></tr>";
			}
			text += " </table>";
			screen.setText(text);
			this.closeConnections(p, rs, conn);
			return this.getRESULTS(dd, 12, trans, dataset, true, JChart.OVERVIEW.MONTHLY, screen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}

		return dataset;
	}

	/** Montly overview of products sold */
	public DefaultCategoryDataset getMonthlyProductsForCharts(JTextPane screen) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			Calendar c = Calendar.getInstance();
			c.set(c.getTime().getYear() + 1900, Calendar.JANUARY, Calendar.SUNDAY);
			Date dd = c.getTime();
			c.setTime(dd);

			p = conn.prepareStatement(
			"select * from inventory_transaction T, inventory I where T.sku=I.sku AND Date(T.date_time) >= Subdate('" + sdf.format(dd) + "','INTERVAL 1 MONTH') order by T.date_time ASC");
			ResultSet rs = p.executeQuery();
			HashMap<String, Product> prod = this.products;
			HashMap<String, Data> trans = new HashMap<String, Data>();
			for (String n : prod.keySet()) {
				trans.put(n, new Data(true, 12));
			}
			// String text = "<table width=100% height=100% border=2 ><tr><th>Month</th><th>Product Name</th><th>Units Sold</th></tr>";
			// sdf = new SimpleDateFormat("MMMM/dd/yy");
			while (rs.next()) {

				Date month = rs.getDate("T.date_time");
				String name = rs.getString("I.product_name");
				Data d = trans.get(name);
				d.addProduct(month.getMonth(), rs.getLong("T.quantity"));

				trans.remove(name);
				trans.put(name, d);
			}
			this.closeConnections(p, rs, conn);
			return getRESULTS(dd, 12, trans, dataset, false, JChart.OVERVIEW.MONTHLY, screen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}

		return dataset;
	}

	/** A helper function that main job is to display jchart and write to screen */
	private DefaultCategoryDataset getRESULTS(Date date, int interval, HashMap<String, Data> trans, DefaultCategoryDataset dataset, boolean employee, JChart.OVERVIEW o, JTextPane screen) {
		SimpleDateFormat sdf;
		int start = 0;
		int close = this.getBusinessClosingTimeHour();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);

		if (o == o.MONTHLY) {
			sdf = new SimpleDateFormat("MMM");
		} else if (o == o.DAILY) {
			sdf = new SimpleDateFormat("EEE");

		} else {
			sdf = new SimpleDateFormat("h:mm a");
			interval = close;
			start = this.getBusinessOpenTimeHour();
			date.setHours(start);
		}

		String text = "";
		if (employee) {
			if (o == o.DAILY) {
				text = "<table width=100% height=100% border=2 ><tr><th>Day</th><th>Name</th><th>Number of Customers</th></tr>";
			} else text = "<table width=100% height=100% border=3 ><tr><th>Name</th><th>Time</th><th>Number of Haircut</th></tr>";
		} else {
			text = "<table width=100% height=100% border=2 ><tr><th>Month</th><th>Product Name</th><th>Units Sold</th></tr>";
		}

		for (int i = start; i < interval; ++i) {
			int sum = 0;

			for (String name : trans.keySet()) {/// can be prod or employee

				Data d = trans.get(name);

				if (employee) {
					date.setMinutes(30);
					dataset.addValue(d.getCustomerList()[i], name, sdf.format(date));
					date.setMinutes(0);
					if (o == o.DAILY) {
						text += "<tr><td>" + sdf.format(date) + "</td><td>" + name + "</td><td>" + d.getCustomerList()[i] + "</td></tr>";
					} else if (o == o.HOURLY) {
						text += "<tr><td>" + name + "</td><td>" + date.getHours() + ":00-" + sdf.format(date) + "</td><td>" + d.getCustomerList()[i] + "</td></tr>";
					} else {// is monthly
						text += "<tr><td>" + sdf.format(date) + "</td><td>" + name + "</td><td>" + d.getCustomerList()[date.getMonth()] + "</td></tr>";

					}
					sum += d.getCustomerList()[i];
				} else {
					if (o == o.DAILY) {
						text += "<tr><td>" + sdf.format(date) + "</td><td>" + name + "</td><td>" + d.getProductList()[date.getMonth()] + "</td></tr>";

					} else {
						text += "<tr><td>" + sdf.format(date) + "</td><td>" + name + "</td><td>" + d.getProductList()[date.getMonth()] + "</td></tr>";

					}
					dataset.addValue(d.getProductList()[i], name, sdf.format(date));
					sum += d.getProductList()[i];
				}

			}
			date.setMinutes(30);
			dataset.addValue(sum, "STORE", sdf.format(date));
			text += "<tr><td><font bgcolor=yellow>" + sdf.format(date) + "</td><td>STORE</td><td>" + sum + "</font></td></tr>";
			if (o == o.MONTHLY) {

				date.setMonth(date.getMonth() + 1);
			} else if (o == o.DAILY) {
				date.setDate(date.getDate() + 1);
			} else {
				date.setHours(date.getHours() + 1);
			}
		}
		text += "</table>";
		screen.setText(text);
		return dataset;
	}

	/** Method loops through everyday getting the hourly results for that day */
	public DefaultCategoryDataset getHourlyData(Date date, JTextPane text) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			DefaultCategoryDataset data = new DefaultCategoryDataset();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdff = new SimpleDateFormat("h:mma");
			p = conn.prepareStatement(
			"SELECT CONCAT(Time_Format(CONCAT(Hour(T.date_time),':00'),'%h:%i%p'),'-',Time_Format(CONCAT(Hour(T.date_time)+1,':00'),'%h:%i%p')) AS timeperiod,Time(TIME_FORMAT(CONCAT(Hour(T.date_time), ':30'),'%T')) AS Hours, COUNT(*) AS `usage`,SUM(T.amount)as gp, (select SUM(IT.quantity) from inventory_transaction IT where Hour(IT.date_time)=Hour(T.date_time) AND Date(IT.date_time)='"
			+ sdf.format(date) + "')as units FROM transaction T, employee E WHERE T.employee_id=E.id AND Date(T.date_time)='" + sdf.format(date) + "' GROUP BY timeperiod");
			ResultSet rs = p.executeQuery();
			PriorityQueue<Node> pq = new PriorityQueue<>();
			String html = "<table width=100% height=100% border=2 ><tr><th>Time</th><th>Number of Clients</th><th>Number Products Sold</th><th>Gross Profit</th></tr>";
			while (rs.next()) {
				Date temp = rs.getTimestamp("Hours");
				temp.setYear(date.getYear());
				temp.setMonth(date.getMonth());
				temp.setDate(date.getDate());
				Node n = new Node(rs.getInt("usage"), rs.getBigDecimal("gp"), temp);
				pq.add(n);
				html += "<tr><td>" + rs.getString("timeperiod") + "</td><td>" + rs.getInt("usage") + "</td><td>" + rs.getLong("units") + "</td><td>$" + rs.getBigDecimal("gp") + "</td></tr>";
			}
			html += "</table>";
			text.setText(html);
			Date temp = new Date();
			int open = this.getBusinessOpenTimeHour();
			int close = this.getBusinessClosingTimeHour();

			temp.setYear(date.getYear());
			temp.setMonth(date.getMonth());
			temp.setDate(date.getDate());
			temp.setHours(open);
			temp.setMinutes(0);
			temp.setSeconds(0);
			for (int i = open; i < close; ++i) {
				Node n = new Node(0, new BigDecimal(0.00), temp);
				Node ppp = pq.peek();
				if (ppp == null) {
					data.addValue(n.getAmount(), "STORE", sdff.format(n.getDate()));
				} else if (n.getDate().getTime() < ppp.getDate().getTime()) {
					data.addValue(n.getAmount(), "STORE", sdff.format(n.getDate()));
				} else {
					ppp = pq.poll();

					data.addValue(ppp.getAmount(), "STORE", sdff.format(ppp.getDate()));
				}
				temp.setHours(temp.getHours() + 1);
			}
			while (pq.size() > 0) {
				Node n = pq.poll();
				data.addValue(n.getAmount(), "STORE", sdff.format(n.getDate()));
			}
			this.closeConnections(p, rs, conn);
			return data;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return null;
	}

	/////////////////////////////////////////////////////////////////////////// Advertisement DB
	public HashMap<String, Advertisment> getAdvertisementDetailHashMap() {
		HashMap<String, Advertisment> h = new HashMap<>();
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			p = conn.prepareStatement("select name, ad_id,email, image from advertisement");
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");

				Blob ph = rs.getBlob("image");
				// System.out.println(ph);

				InputStream in = ph.getBinaryStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				String image_path = POSFrame.PICTURE_DIRECTORY + "\\Ad\\" + name + ".jpg";// image path
				OutputStream outputStream = new FileOutputStream(image_path);

				int length = (int) ph.length();
				int bufferSize = 1024;

				byte[] buffer = new byte[bufferSize];

				while ((length = in.read(buffer)) != -1) {
					// System.out.println("writing " + length + " bytes");
					out.write(buffer, 0, length);
				}
				out.writeTo(outputStream);
				in.close();

				Advertisment a = new Advertisment(name, rs.getString("email"), rs.getString("ad_id"), image_path);
				h.put(name, a);
			}
			this.closeConnections(p, rs, conn);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return h;
	}

	/***
	 * Will generate a new record in the live_feed DB and release connection
	 */
	public void confirmNewTicket(Customer c, Connection oldcon) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			// conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);

			p = oldcon.prepareStatement("INSERT INTO `live_feed` (`date_time`, `customer_name`,`id`, `stylist_request_id`,`email`,`phone`) " + "VALUES (?,?,?,?,?,?)");
			Timestamp now = new Timestamp(new Date().getTime());
			p.setTimestamp(1, now);
			p.setString(2, c.getName());
			p.setShort(3, c.getTicketNumber());
			p.setString(4, c.getStylistID());
			p.setString(5, c.getEmail());
			p.setString(6, c.getPhoneNumber());
			p.execute();

			p = oldcon.prepareStatement("UNLOCK TABLES");
			p.execute();
			oldcon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}

	}

	public void editReservation(int number, Customer newc) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			p = conn.prepareStatement("update live_feed set stylist_request_id='" + newc.getStylistID() + "', email='" + newc.getEmail() + "' ,name='" + newc.getName() + "' where Date(date_time)='"
			+ sdf.format(date) + "' AND number='" + number + "'");
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
	}

	public int getWait(Stylist s) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			p = conn.prepareStatement("select count(*) as total from live_feed where stylist_request_id='" + s.getID() + "' AND Date(date_time)='" + sdf.format(date) + "'");
			r = p.executeQuery();
			while (r.next()) {
				return r.getInt("total");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return 0;
	}

	////////////// Stylist OBJECT get from hashmap
	public Stylist isStylist(String stylist_id) {
		return this.stylist_login.get(stylist_id);
	}

	/**
	 * this method will update the ticket screen for the pos and only include those of which is not in lc. lc is the canceled haircut. You are considered canceled/or on hold if no customer responds or
	 * isnt there when their turn is called.
	 */
	public void updatePOSTicketScreen(DefaultListModel<Ticket> lm, HashMap<Integer, Ticket> tickets, HashMap<Integer, Ticket> onHold) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {

			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			p = conn.prepareStatement("SELECT ticket_number,temp_name,stylist,datetime_submitted FROM acba_app.ticket WHERE store_id='" + this.USER_DB + "' AND Date(datetime_submitted)=Date('" + date
			+ "') UNION SELECT id,customer_name,stylist_request_id,date_time FROM acba_" + this.USER_DB + ".live_feed WHERE Date(date_time)=Date('" + date + "') ORDER BY datetime_submitted ASC");
			r = p.executeQuery();
			int space = 30;// number of max space
			while (r.next()) {
				long store_id = Long.parseLong(this.USER_DB);
				int ticket = r.getInt("ticket_number");
				String id = r.getString("stylist");
				String stylist = "No Preference";/////////// name of stylist
				Stylist s = this.stylist_login.get(id);
				if (s != null) {
					stylist = this.stylist_login.get(id).getName();
				}
				String customer = r.getString("temp_name");
				int x = Math.abs(space - stylist.length());
				// int y= Math.abs(space - customer.length());
				stylist = stylist + String.format("%1$-" + x + "s", " ");

				Ticket t = new Ticket(ticket, stylist, customer, store_id);
				String string = String.format("%1$-" + 10 + "s", ticket) + stylist + String.format("%1$" + space + "s", customer);
				t.setToString(string);
				// System.out.println(t);
				if (!tickets.containsKey(t.getNumber()) && !onHold.containsKey(t.getNumber())) {
					lm.addElement(t);
					tickets.put(t.getNumber(), t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}

	}

	///////// Updates the list of tickets
	/***
	 * This methods makes a call to the db to update the data structure of the tickets waiting. 100% READ ONLY METHOD.
	 **/
	// public void updatePOSTicketScreen(PriorityQueue<Ticket> ticket_line, DefaultListModel<Ticket> lm) {
	// try {
	//
	// //lm.clear();
	// Connection conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
	// PreparedStatement p = conn.prepareStatement("SELECT T.ticket_number,T.store_id, T.stylist, T.temp_name From `acba_app`.`ticket` T, `acba_" + this.USER_DB
	// + "`.`client` C WHERE T.store_id=C.id AND Date(T.datetime_submitted)=Date(Now()) ORDER BY T.ticket_number ASC");
	// ResultSet r = p.executeQuery();
	// int space = 30;// number of max space
	// while (r.next()) {
	// long store_id = r.getLong("T.store_id");
	// int ticket = r.getInt("T.ticket_number");
	// String id = r.getString("T.stylist");
	// String stylist = "No Preference";/// if stylist s ==null this is the default
	// Stylist s = this.stylist_login.get(id);
	// if (s != null) {
	// stylist = this.stylist_login.get(id).getName();
	// }
	// String customer = r.getString("T.temp_name");
	// int x = Math.abs(space - stylist.length());
	// // int y= Math.abs(space - customer.length());
	// stylist = stylist + String.format("%1$-" + x + "s", " ");
	//
	// String string = String.format("%1$-" + 10 + "s", ticket) + stylist + String.format("%1$" + space + "s", customer);
	// Ticket t = new Ticket(ticket, stylist, customer, store_id);
	// t.setToString(string);
	// if (!ticket_line.contains(t)) {
	// ticket_line.add(t);
	// lm.addElement(t);
	// }
	// }
	// this.closeConnections(p, r, conn);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// // this.closeConnections(p, r, conn);
	// }
	//
	// }

	/***
	 * Delete both the user's DB(acba_userid) and from the mobile APP's DB (acba_app). ONLY deleting ONE ticket but doesnt know which db its in so delete from both.
	 */
	public void deleteTicket(Ticket t) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("delete From `acba_app`.`ticket` where store_id='" + this.USER_DB + "' AND ticket_number='" + t.getNumber() + "'");
			p.execute();
			p = conn.prepareStatement("delete From `acba_" + this.USER_DB + "`.`live_feed` where id='" + t.getNumber() + "'");
			p.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}

	}

	// GETS the the next available ticket to be grabbed. This ticket will only contain the STORE_ID and NEXT AVAILABLE NUMBER
	public Ticket getCurrentTicket() {// current ticket status
		Ticket ticket = null;
		// System.out.println("here");
		// System.out.println(this.DB_URL+" "+POSFrame.USER+" "+" "+POSFrame.PASS+" "+this.USER_DB);
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("SELECT id,current_ticket FROM `acba_" + this.USER_DB + "`.`client` WHERE id='" + this.USER_DB + "' ");
			r = p.executeQuery();
			if (r.next()) {
				ticket = new Ticket(r.getInt("current_ticket"), r.getLong("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return ticket;
	}

	public boolean redeemCoupon(Advertisment a, Customer cust) {
		Connection conn = null;
		PreparedStatement p = null;

		int guarantee_ticket = -1;
		try {///////////////////////////////////// FIXXXXXXXXXXXXXXXXXXXXXXX
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("INSERT INTO `advertisement_coupon_redeemed` ( `date_time`, `customer_id`, `ad_id`) VALUES (?,?,?);");
			// p.setString(1, "NULL");

			p.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			p.setLong(2, cust.getID());
			p.setString(3, a.getBarcode());
			return p.execute();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.closeConnections(p, null, conn);
		}
	}

	/****
	 * This will lock the current session. This will be implemented along with a Timer. Normally, once the timer is stopped then the session would release its hold.
	 */
	public Connection lockTicketTable() {
		Connection conn = null;
		PreparedStatement p;
		try {///////////////////////////////////// FIXXXXXXXXXXXXXXXXXXXXXXX
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			p = conn.prepareStatement("LOCK TABLE `live_feed` WRITE;");
			p.execute();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public boolean resetTicketCounter() {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {///////////////////////////////////// FIXXXXXXXXXXXXXXXXXXXXXXX
			conn = DriverManager.getConnection(DB_URL, POSFrame.USER, POSFrame.PASS);
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			p = conn.prepareStatement("Select * FROM acba_" + this.USER_DB + ".live_feed WHERE Date(date_time)=Date('" + date + "') ORDER BY date_time ASC");
			r = p.executeQuery();
			ArrayList<Ticket> al = new ArrayList<>();
			while (r.next()) {
				String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r.getTimestamp("date_time"));
				String name = r.getString("customer_name");
				short ticket = r.getShort("id");
				String stylist_id = r.getString("stylist_request_id");
				String email = r.getString("email");
				String phone = r.getString("phone");
				Ticket t = new Ticket(dt, name, ticket, stylist_id, email, phone);
				al.add(t);
			}
			p = conn.prepareStatement("TRUNCATE TABLE acba_" + this.USER_DB + ".live_feed;");
			p.execute();
			for (Ticket t : al) {
				p = conn.prepareStatement("INSERT INTO `acba_" + this.USER_DB + "`.`live_feed` (`date_time`, `customer_name`, `id`, `stylist_request_id`, `email`, `phone`) VALUES ('" + t.getDateTime()
				+ "', '" + t.getName() + "', '" + t.getNumber() + "', '" + t.getStylistID() + "', '" + t.getEmail() + "', '" + t.getPhone() + "');");
				p.execute();
			}
			return true;

		} catch (Exception e) {
			POSFrame.network_error_map.put(POSFrame.network_error_map.size(), ()->this.resetTicketCounter());
			e.printStackTrace();
		} finally {
			this.closeConnections(p, r, conn);
		}
		return false;
	}
}