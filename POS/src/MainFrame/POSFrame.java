package MainFrame;

import java.awt.EventQueue;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JFrame;
import com.braintreegateway.BraintreeGateway;

import Dialogs.Loading;
import POS_Panels.MainMenuPanel;
import ReservationScreenCustomerLogin.AdvertismentScreen;
import SQLclass.SQL;
import WebServices.Email;


public class POSFrame extends JFrame {
	public static int WIDTH = 1100, HEIGHT = 800;
	public static POSFrame frame;
	public static SQL SQL;
	public static String USER, PASS;
	public static String PICTURE_DIRECTORY = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"\\..\\Documents\\ACBA\\soft\\programpics";
	public static String SMTP_USER_PASS;
	public static String SMTP_USER;
	public static Loading loading;
	public static ArrayList network_failure_queue;
	public static String SERVER_IP="gator4222.hostgator.com";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		network_failure_queue = new ArrayList<>();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQL = new SQL();
					//frame = new POSFrame();
					//frame.setVisible(true);
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

	public static String businessName;

	/**
	 * Create the frame.
	 */
	public POSFrame() {
		this.loading=new Loading();
		businessName = SQL.getBusinessName();
		this.loading.dispose();
		setTitle("ACBA Software & Security Systems");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(POSFrame.WIDTH, POSFrame.HEIGHT);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
		getContentPane().add(mainMenuPanel);
	}

}
