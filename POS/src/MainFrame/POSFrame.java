package MainFrame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.Timer;

import com.braintreegateway.BraintreeGateway;

import Dialogs.Loading;
import POSObjects.Ticket;
import POS_Panels.MainMenuPanel;
import ReservationScreenCustomerLogin.AdvertismentScreen;
import SQLclass.SQL;
import Util.Log;
import WebServices.Email;

public class POSFrame extends JFrame implements ActionListener {
	public static final int WIDTH = 1100, HEIGHT = 800;
	public static POSFrame frame;
	public static SQL SQL;
	public static String USER, PASS;
	public static final String PICTURE_DIRECTORY = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "\\..\\Documents\\ACBA\\soft\\programpics";
	public static String SMTP_USER_PASS;/// email user password
	public static String SMTP_USER;/// for sending emails.
	public static Loading loading;
	public static final String SERVER_IP = "gator4222.hostgator.com";
	public static String businessName;
	public static DefaultListModel<Ticket> ListModel;
	public static HashMap<Integer, Ticket> Tickets, Canceled_Tickets;// needed to share ticket info between customer Ad screen and POS main screen
	public static ArrayList<Runnable> network_error_map;
	private final Timer t;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ListModel = new DefaultListModel<>();
		Tickets = new HashMap<Integer, Ticket>();
		Canceled_Tickets = new HashMap<Integer, Ticket>();
		loading = new Loading();
		loading.setVisible(false);
		network_error_map = new ArrayList<>();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQL = new SQL();

					frame = new POSFrame();
					frame.setVisible(true);
					loading.setVisible(false);
					loading.dispose();
					loading = null;
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								AdvertismentScreen a = new AdvertismentScreen(SQL);
								a.setVisible(true);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/****
	 * Attempt to send ovetr network if succeeds then release from hashtable.
	 */
	public void reattemptNetworkConnection() {
		for (int i =0; i < network_error_map.size();++i) {
			try {
				network_error_map.get(i).run();
				network_error_map.remove(i);
			} catch (Exception e) {
				e.printStackTrace();
				Log.logError(e.getStackTrace());
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public POSFrame() {
		t = new Timer(1000 * 60, this);// 1000=sec x 60 = min;
		businessName = SQL.getBusinessName();
		setTitle("ACBA Software & Security Systems");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(POSFrame.WIDTH, POSFrame.HEIGHT);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
		getContentPane().add(mainMenuPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				reattemptNetworkConnection();
			}
		}).start();
	}
}