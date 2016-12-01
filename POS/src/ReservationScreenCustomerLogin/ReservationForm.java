package ReservationScreenCustomerLogin;

// [START simple_includes]

import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// [END simple_includes]

// [START multipart_includes]

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

// [END multipart_includes]

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import POSObjects.Stylist;
import POSObjects.Ticket;
import ReservationScreenCustomerLogin.KeyBoard.KEYBOARD_TYPE;
import SQLclass.SQL;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.KeyboardFocusManager;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import MainFrame.POSFrame;

import java.awt.Color;

import javax.mail.*;
import java.awt.SystemColor;

public class ReservationForm extends JDialog implements FocusListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField name;

	private SQL sql;
	private HashMap<String, Stylist> stylists;
	// private Process pb;
	// private KeyBoard k;
	private JTextField tf_phone;
	private JButton btnReserve;
	private final String[] SMS_GATEWAY = { "@message.alltel.com", "@txt.att.net", "@myboostmobile.com", "@messaging.sprintpcs.com", "@tmomail.net", "@email.uscc.net", "@vtext.com", "@vmobl.com" };
	// private int guarantee_ticket;
	private JLabel current_ticket_label;
	private KeyBoard kb;
	private Connection conn;

	/**
	 * Create the dialog.
	 */
	public ReservationForm(SQL sql) {
		setResizable(false);
		this.setIconImage(new ImageIcon(POSFrame.PICTURE_DIRECTORY + "\\").getImage());
		getContentPane().setBackground(SystemColor.controlHighlight);
		setTitle("ACBA: Request Stylist");
		this.sql = sql;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				conn = sql.lockTicketTable();// lock current session

			}
		}).start();
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		setModal(true);
		this.setMinimumSize(new Dimension(855, 360));
		// getContentPane().setLayout(new BorderLayout());
		this.stylists = sql.getLoadedStylists();
		DefaultListModel model_data = new DefaultListModel();
		DefaultListModel mode_wait = new DefaultListModel();
		int i = -1;
		for (String name : this.stylists.keySet()) {
			if (name.toLowerCase().contains("no preference")) {
				continue;
			}
			model_data.add(++i, name);
			// int wait = sql.getWait(stylists.get(name));// approx 45min per customer
			mode_wait.add(i, "update this.. " + " waiting ~ " + getTime(1 * 45) + " wait");
		}
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 849, 326);
		contentPanel.setBackground(SystemColor.controlHighlight);

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 279, 279);
		contentPanel.add(scrollPane);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(298, 31, 258, 279);
		contentPanel.add(scrollPane_1);
		JList wait_list = new JList();
		wait_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wait_list.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wait_list.setEnabled(false);
		scrollPane_1.setViewportView(wait_list);
		JList list = new JList();
		list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		wait_list.setModel(mode_wait);
		list.setModel(model_data);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);

		JLabel lblNewLabel = new JLabel("Click to Request Stylist");
		lblNewLabel.setFont(new Font("Cambria Math", Font.BOLD, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 0, 279, 27);
		contentPanel.add(lblNewLabel);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblName.setBounds(561, 71, 64, 27);
		contentPanel.add(lblName);

		name = new JTextField("Required");
		name.setForeground(new Color(150, 150, 150));
		name.addFocusListener(this);
		name.setBounds(643, 69, 192, 28);
		contentPanel.add(name);
		name.setColumns(10);

		btnReserve = new JButton("Reserve");
		btnReserve.setFont(new Font("Cambria Math", Font.BOLD, 20));
		btnReserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reserve(list);
			}
		});
		btnReserve.setBounds(566, 192, 269, 116);
		contentPanel.add(btnReserve);

		JLabel lblWait = new JLabel("Approx. Wait");
		lblWait.setHorizontalAlignment(SwingConstants.CENTER);
		lblWait.setFont(new Font("Cambria Math", Font.BOLD, 22));
		lblWait.setBounds(298, 0, 258, 24);
		contentPanel.add(lblWait);

		JLabel lblNewLabel_2 = new JLabel("Current Ticket:");
		lblNewLabel_2.setFont(new Font("Cambria Math", Font.BOLD, 22));
		lblNewLabel_2.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel_2.setBounds(561, 0, 167, 27);
		contentPanel.add(lblNewLabel_2);

		current_ticket_label = new JLabel(AdvertismentScreen.NUMBER + "");
		current_ticket_label.setHorizontalAlignment(SwingConstants.CENTER);
		current_ticket_label.setFont(new Font("Cambria Math", Font.BOLD, 22));
		current_ticket_label.setBorder(new LineBorder(new Color(0, 0, 255), 2));
		current_ticket_label.setIgnoreRepaint(true);
		current_ticket_label.setBounds(738, 3, 97, 24);
		contentPanel.add(current_ticket_label);

		JLabel jlabel_phone = new JLabel("Phone:");
		jlabel_phone.setFont(new Font("Tahoma", Font.PLAIN, 18));
		jlabel_phone.setBounds(561, 109, 64, 27);
		contentPanel.add(jlabel_phone);

		tf_phone = new JTextField("Optional: send alert if provided");
		tf_phone.setForeground(new Color(150, 150, 150));
		tf_phone.setColumns(10);
		tf_phone.setBounds(643, 108, 192, 28);
		tf_phone.addFocusListener(this);
		contentPanel.add(tf_phone);
		this.setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (conn == null) {
						dispose();
						return;
					}
					new Thread(new Runnable() {

						@Override
						public void run() {
							sql.unlockTables(conn);
							if (conn != null) {
								try {
									conn.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}).start();
					
					dispose();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private void generateGuaranteePriority() {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// guarantee_ticket = sql.guaranteeTicket();//generates a guarantee ticket number
		// new Thread(new Runnable(){
		// @Override
		// public void run() {
		// int current_ticket=sql.getCurrentTicket().getNumber();
		// current_ticket_label.setText(""+(current_ticket));
		// }}).start();
		// your_ticket_label.setText(""+(guarantee_ticket));
		//
		// }
		//
		// }).start();
	}

	private String getTime(int time) {
		String t = "";
		if (time >= 60) {
			t += time / 60 + " hour(s) ";
		}
		int min = (time % 60);
		t += min + " min(s)";

		return t;
	}

	private boolean sendSMS(Customer c, int number) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.host", "gator4222.hostgator.com");
		props.put("mail.port", "465");
		props.put("mail.smtp.auth", "true");

		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(POSFrame.SMTP_USER, POSFrame.SMTP_USER_PASS);
			}
		});
		try {
			Transport trans = session.getTransport();
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("no-reply@noreply.com", POSFrame.businessName));

			for (String sms_gateway : this.SMS_GATEWAY) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(c.getPhoneNumber() + sms_gateway, "Valued Client"));

			}
			msg.setSubject("Reservation Receipt");
			msg.setText("Your Number: " + number + "\nRequested Stylist: " + c.getStylistName());

			trans.send(msg);
			trans.close();
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false; // ...
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false; // ...
		}
		return true;
	}

	// public boolean isValidEmailAddress(String email) {
	// boolean result = true;
	// try {
	// InternetAddress emailAddr = new InternetAddress(email);
	//
	// emailAddr.validate();
	// } catch (AddressException ex) {
	// result = false;
	// }
	// return result;
	// }
	private void reserve(JList list) {
		if (this.name.getText().toLowerCase().contains("required") || this.name.getText().length() == 0) {// && tf_phone.getText().length() == 0) {
			error(name);
			return;
		} else {
			reset();
		}
		this.btnReserve.setEnabled(false);
		Stylist s = new Stylist();// default
		if (!list.isSelectionEmpty()) {
			s = stylists.get(list.getSelectedValue().toString());// specified stylist
		}
		String phone = "";
		if (this.tf_phone.getText().length() == 10) {
			phone = this.tf_phone.getText();
		}
		Customer c = new Customer((short) AdvertismentScreen.NUMBER, name.getText(), s.getID(), phone);// create customer
		c.setStylistName(s.getName());
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (c.getPhoneNumber().length() == 10) {
					JOptionPane.showMessageDialog(null, "Notification has been sent!");// \n*Note: Email may appear in your Spam Folder.");
					dispose();
					boolean sms = sendSMS(c, c.getTicketNumber());
				}
			}
		}).start();
		////////////////// LOCAL STORAGE
		String customer = c.getName();
		int space = 30;// number of max space
		int x = Math.abs(space - c.getStylistName().length());
		// int y= Math.abs(space - customer.length());
		String stylist = c.getStylistName() + String.format("%1$-" + x + "s", " ");

		Ticket t = new Ticket(AdvertismentScreen.NUMBER, stylist, customer, Long.parseLong(POSFrame.SQL.USER_DB));
		String string = String.format("%1$-" + 10 + "s", t.getNumber()) + stylist + String.format("%1$" + space + "s", customer);
		t.setToString(string);
		POSFrame.ListModel.addElement(t);
		POSFrame.Tickets.put(t.getNumber(), t);
		AdvertismentScreen.NUMBER = t.getNumber() + 1;/// should be correct increment
		AdvertismentScreen.updateTicketLabel();
		////////////////////////////////////////////////////
		new Thread(new Runnable() {

			@Override
			public void run() {
				sql.confirmNewTicket(c, conn);/// reserve
			}
		}).start();

		this.dispose();
	}

	private void reset() {
		name.setBackground(Color.white);
		tf_phone.setBackground(Color.white);
	}

	private void error(JTextField tf) {
		tf.setBackground(Color.red);
	}

	/***
	 * new FocusListener() {
	 * 
	 * // public void focusLost(FocusEvent arg0) { // try { // Runtime.getRuntime().exec("cmd /c taskkill /IM osk.exe"); // pb.destroyForcibly(); // } catch (IOException e) { // e.printStackTrace();
	 * // } // }
	 * 
	 * public void focusGained(FocusEvent a) { try { System.out.println("focused gained"); name.setText(""); name.setForeground(new Color(50, 50, 50)); // pb = Runtime.getRuntime().exec(
	 * "cmd /c C:\\Windows\\System32\\osk.exe"); //if (k == null) { kb = new KeyBoard(name, KEYBOARD.KEYBOARD);
	 * 
	 * //} else if (name.isFocusable()) { // k = null; //}
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 * 
	 * @Override public void focusLost(FocusEvent e) { if (name.getText().length() == 0) { name.setText("Required"); name.setForeground(new Color(150, 150, 150)); } lblName.requestFocusInWindow();
	 *           lblName.requestFocus(); } }
	 */
	@Override
	public void focusGained(FocusEvent e) {

		this.current_ticket_label.requestFocusInWindow();
		this.current_ticket_label.grabFocus();

		if (e.getSource() == name) {
			kb = new KeyBoard(name, KeyBoard.KEYBOARD_TYPE.KEYBOARD);// grab focus to JLAbel
			if (name.getText().length() == 0) {
				name.setText("Required");
				name.setForeground(new Color(150, 150, 150));
			} else {
				name.setBackground(Color.WHITE);
			}
		} else {
			tf_phone.setText("");
			kb = new KeyBoard(tf_phone, KeyBoard.KEYBOARD_TYPE.NUMBER_PAD);
			if (tf_phone.getText().length() == 0) {
				tf_phone.setText("Optional: send alert if provided");
				tf_phone.setForeground(new Color(150, 150, 150));

			} else {
				tf_phone.setBackground(Color.WHITE);
			}
		}

	}

	@Override
	public void focusLost(FocusEvent e) {

	}
}
