package ReservationScreenCustomerLogin;

import java.awt.BorderLayout;
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

public class AdvertismentScreen extends JFrame implements ActionListener {
	private SQL sql;
	// private final String file_loc = "C:\\Users\\user\\Documents\\ACBA\\soft\\programpics\\Ad\\";
	// public static PriorityQueue<Customer> queue;
	public static int NUMBER;
	private DefaultListModel<Ticket> lm;
	public static PriorityQueue<Ticket> ticket_line;
	private Timer timer;
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
		ticket_line = new PriorityQueue<Ticket>();
		timer = new Timer(60 * 1000, this);// every 1 mins
		timer.start();
		if (POSFrame.SQL == null || sql_param == null) {
			this.sql = new SQL();
		} else {
			this.sql = sql_param;
		}
		// this.NUMBER = sql.getCurrentTicket().getNumber();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1100, 790);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 163, 790);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(98, 0, 93, 55);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));

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
		btn_ticket.setFont(new Font("Cambria Math", Font.BOLD, 14));
		btn_ticket.setBounds(4, 685, 154, 80);
		
		panel.add(btn_ticket);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(4, 23, 154, 658);
		panel.add(scrollPane);
		lm = new DefaultListModel<Ticket>();
		JList list = new JList();
		list.setBorder(new LineBorder(new java.awt.Color(0, 0, 0)));
		list.setModel(lm);
		scrollPane.setViewportView(list);

		JLabel lblTicket = new JLabel("Ticket # / Stylist");
		lblTicket.setFont(new Font("Cambria Math", Font.BOLD, 18));
		lblTicket.setBounds(0, 0, 164, 22);
		panel.add(lblTicket);

		JPanel ad_panel = new JPanel();
		ad_panel.setBounds(161, 0, 939, 790);
		getContentPane().add(ad_panel);
		ad_panel.setLayout(new GridLayout(4, 4, 0, 0));
		populateScreen(ad_panel);

		ad_panel.setVisible(true);
		this.setUndecorated(true);
		this.setVisible(true);

	}

	/***
	 * This method will display and create the Advertisement Button display
	 */
	private void populateScreen(JPanel ad_panel) {
		HashMap<String, Advertisment> h = this.sql.getAdvertisementDetailHashMap();
		for (String name : h.keySet()) {// loop through AD's
			JButton b = new JButton();

			b.setContentAreaFilled(false);
			b.setBorder(BorderFactory.createEmptyBorder());
			b.setActionCommand(h.get(name).getBarcode());

			b.setVisible(true);
			ImageIcon ii = new ImageIcon(h.get(name).getImagePath());
			int scale = 1; // 2 times smaller
			int width = ii.getIconWidth();
			int newWidth = width / scale;
			b.setIcon(new ImageIcon(ii.getImage().getScaledInstance(newWidth, -1, java.awt.Image.SCALE_SMOOTH)));
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					ReedemCoupon r = new ReedemCoupon(h.get(name));
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
				sql.updatePOSTicketScreen(ticket_line, lm);
				AdvertismentScreen.NUMBER = lm.size() + 1;// update next number for ticket.
			}

		}).start();
	}
}
