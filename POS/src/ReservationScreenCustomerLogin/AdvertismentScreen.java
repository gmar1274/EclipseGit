package ReservationScreenCustomerLogin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Dialogs.ReedemCoupon;
import MainFrame.POSFrame;
import POSObjects.Advertisment;
import POSObjects.Ticket;
import SQLclass.SQL;
import javafx.scene.paint.Color;

import java.awt.GridLayout;
import java.awt.Insets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;

public class AdvertismentScreen extends JFrame implements ActionListener {
	private SQL sql;
	// private final String file_loc = "C:\\Users\\user\\Documents\\ACBA\\soft\\programpics\\Ad\\";
	// public static PriorityQueue<Customer> queue;
	public static int NUMBER;
	private DefaultListModel<Ticket> lm;
	HashMap<Integer, Ticket> canceled_tickets;
	// public static PriorityQueue<Ticket> ticket_line;
	private Timer timer;
	private HashMap<Integer, Ticket> tickets;
	private JList list;
	private static String ticket_text = "Next Ticket: ";
	private static JLabel label_ticket;
	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// AdvertismentScreen frame = new AdvertismentScreen(null);
	// // sendSMS();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the frame.
	 */
	public AdvertismentScreen(SQL sql_param) {
		// ticket_line = new PriorityQueue<Ticket>();
		lm = POSFrame.ListModel;// new DefaultListModel<Ticket>();
		this.canceled_tickets = POSFrame.Canceled_Tickets;
		this.tickets = POSFrame.Tickets;
		timer = new Timer(60 * 1000, this);// every 1 mins
		timer.start();
		if (POSFrame.SQL == null || sql_param == null) {
			this.sql = new SQL();
		} else {
			this.sql = sql_param;
		}
		// this.NUMBER = sql.getCurrentTicket().getNumber();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1050, 761);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.RED);
		panel.setBounds(0, 0, 155, 790);
		getContentPane().add(panel);
		panel.setLayout(null);

		JButton btn_ticket = new JButton("TAKE A TICKET");////////// opens the Reservation Screen

		btn_ticket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {

						ReservationForm r = new ReservationForm(sql);
						r.setVisible(true);
					}
				}).start();
			}
		});
		btn_ticket.setFont(new Font("Cambria Math", Font.BOLD, 12));
		btn_ticket.setBounds(4, 685, 141, 80);

		panel.add(btn_ticket);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(4, 23, 141, 617);
		panel.add(scrollPane);
		list = new JList();
		list.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 2));

		scrollPane.setViewportView(list);

		JLabel lblTicket = new JLabel("Ticket # / Stylist");
		lblTicket.setFont(new Font("Cambria Math", Font.BOLD, 18));
		lblTicket.setBounds(0, 0, 164, 22);
		panel.add(lblTicket);

		label_ticket = new JLabel();
		label_ticket.setOpaque(true);
		label_ticket.setBackground(java.awt.Color.WHITE);
		label_ticket.setForeground(java.awt.Color.BLACK);
		label_ticket.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 2));
		label_ticket.setFont(new Font("Cambria Math", Font.BOLD, 18));
		label_ticket.setBounds(4, 643, 141, 39);
		panel.add(label_ticket);

		JPanel ad_panel = new JPanel();
		ad_panel.setBackground(java.awt.Color.RED);
		ad_panel.setBorder(new LineBorder(new java.awt.Color(255, 0, 0), 3));
		ad_panel.setBounds(155, 0, 939, 761);
		getContentPane().add(ad_panel);
		ad_panel.setPreferredSize(new Dimension(50,50));
		populateScreen(ad_panel);
		ad_panel.setLayout(null);

		ad_panel.setVisible(true);
		this.setUndecorated(true);
		this.setVisible(true);

	}

	/***
	 * This method will display and create the Advertisement Button display
	 */
	private void populateScreen(JPanel ad_panel) {
		HashMap<String, Advertisment> hm = this.sql.getAdvertisementDetailHashMap();
		int x=0,y=0;
		int w=295,h=200;
		for (String name : hm.keySet()) {// loop through AD's
			JButton b = new JButton();

			b.setContentAreaFilled(false);
			b.setBorder(BorderFactory.createEmptyBorder());
			b.setActionCommand(hm.get(name).getBarcode());

			b.setVisible(true);
			ImageIcon ii = new ImageIcon(hm.get(name).getImagePath());
			int scale = 1; // 2 times smaller
			int width = ii.getIconWidth();
			int newWidth = width / scale;
			b.setIcon(new ImageIcon(ii.getImage().getScaledInstance(newWidth, -1, java.awt.Image.SCALE_SMOOTH)));
			b.setBounds(x, y, w, h);
			if(x>=w*2){x=0;y+=h;}
			else{x+=w;}
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					ReedemCoupon r = new ReedemCoupon(hm.get(name));
					r.dispose();
				}
			});
			ad_panel.add(b);
		}
		this.updateList();

	}

	/**
	 * What I plan to do is update the list here i can save the thickets in a list then when someone clicks to get a ticket I can populate the screen instanteously because I saved the Tickets in a
	 * List thus no delay on creating the new dialog. I have not yet implemented this yet....
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		updateList();// updates the ticket list.
	}

	private void updateList() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				sql.updatePOSTicketScreen(lm, tickets, canceled_tickets);
				list.setModel(lm);
				Ticket t=null;
				if(lm.size()>0){
					t= lm.getElementAt(lm.size()-1);
					NUMBER = t.getNumber() + 1;// update next number for ticket.
				}else{
					NUMBER = 1;
				}
				if(NUMBER>=1000){
					NUMBER = 1;
					new Thread(new Runnable(){

						@Override
						public void run() {
							sql.resetTicketCounter();
							
						}
						
					}).start();
				}///////////end if
				updateTicketLabel();
			}

		}).start();
	}

	public static void updateTicketLabel() {
		label_ticket.setText(ticket_text + NUMBER);
	}
}
