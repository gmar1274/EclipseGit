package MainFrame;

import java.awt.EventQueue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import com.braintreegateway.BraintreeGateway;

import Dialogs.Loading;
import POSObjects.Ticket;
import POS_Panels.MainMenuPanel;
import ReservationScreenCustomerLogin.AdvertismentScreen;
import SQLclass.SQL;
import WebServices.Email;


public class POSFrame extends JFrame {
	public static int WIDTH = 1100, HEIGHT = 800;
	public static POSFrame frame;
	public static SQL SQL;
	public static String USER, PASS;
	public static final String PICTURE_DIRECTORY = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"\\..\\Documents\\ACBA\\soft\\programpics";
	public static String SMTP_USER_PASS;
	public static String SMTP_USER;
	public static Loading loading;
	public static final String SERVER_IP="gator4222.hostgator.com";
	public static String businessName;
	public static DefaultListModel<Ticket> ListModel;
	public static HashMap<Integer,Ticket> Tickets, Canceled_Tickets;
	public static HashMap<Integer,Runnable> network_error_map;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ListModel = new DefaultListModel<>();
		Tickets = new HashMap<Integer,Ticket>();
		Canceled_Tickets = new HashMap<Integer,Ticket>();
		//final PICTURE_DIRECTORY = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()+"\\..\\Documents\\ACBA\\soft\\programpics";
		loading  = new Loading();
		loading.setVisible(false);
		network_error_map = new HashMap<>();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQL = new SQL();
					loading.setVisible(false);
					loading=null;
//					for(int i=0;i<10;++i){
//					network_error_map.put(network_error_map.size(), ()-> POSFrame.hello() );
//					}
//					for(int i : network_error_map.keySet()){
//						network_error_map.get(i).run();
//					}
					//loading.dispose();
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
	/****
	 * Attempt to send ovetr network if succeeds then release from hashtable.
	 * */
public void reattemptNetworkConnection(){
	for(int i : network_error_map.keySet()){
		try{
		network_error_map.get(i).run();
		network_error_map.remove(i);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
	/**
	 * Create the frame.
	 */
	public POSFrame() {
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