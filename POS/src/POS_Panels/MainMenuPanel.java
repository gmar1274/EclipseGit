package POS_Panels;

import javax.swing.JPanel;
import MainFrame.POSFrame;
import POSObjects.Stylist;
import SQLclass.SQL;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import Dialogs.Loading;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;

public class MainMenuPanel extends JPanel implements ActionListener {
	private JLabel labelStylist;
	private JLabel labelDate;
	private SimpleDateFormat sdf;
	private Timer timer;
	private JPasswordField passwordField;
	private SQL sql;
	private JFrame frame;
	public static MainMenuPanel MAIN_MENU_PANEL;
	public static String CURRENT_STYLIST_ID;// id
	public static Image img;
	public static Stylist STYLIST_OBJECT;/// current stylist

	/**
	 * Create the panel.
	 */
	public MainMenuPanel(JFrame f) {
		sdf = new SimpleDateFormat("EEE MM/dd/yyyy h:mm:ss a");
		this.img = new ImageIcon(POSFrame.PICTURE_DIRECTORY + "\\bg.jpg").getImage();
		this.frame = f;
		sql = POSFrame.SQL;
		this.setSize(POSFrame.WIDTH, POSFrame.HEIGHT);
		setLayout(null);
		labelStylist = new JLabel("Stylist:");
		labelStylist.setForeground(Color.WHITE);
		labelStylist.setFont(new Font("Arial", Font.BOLD, 20));
		labelStylist.setBorder(new LineBorder(Color.BLUE, 3));
		labelStylist.setHorizontalAlignment(SwingConstants.LEFT);
		labelStylist.setBounds(10, 11, 370, 50);
		add(labelStylist);
		labelDate = new JLabel("");
		labelDate.setFont(new Font("Arial", Font.BOLD, 20));
		labelDate.setForeground(Color.WHITE);
		labelDate.setBorder(new LineBorder(Color.BLUE, 3));
		labelDate.setBounds(708, 11, 302, 50);
		add(labelDate);
		JButton btnNewButton = new JButton("<html>Manager<br>Settings</html>");
		btnNewButton.setFont(new Font("Cambria Math", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						if (sql.isAdmin(passwordField.getText())) {
							resetPasswordField();
							goToManagerSettings();

						} else {
							loginError();
						}
					}
				});

			}
		});
		btnNewButton.setBounds(10, 670, 120, 63);
		add(btnNewButton);
		JButton btnclockInout = new JButton("<html>Clock In/Out</html>");
		btnclockInout.setFont(new Font("Cambria Math", Font.PLAIN, 12));
		btnclockInout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Stylist s = sql.isStylist(passwordField.getText());
				if (s != null) {
					// i can send email if they need reciept
					clockInOut(passwordField.getText());
				}
			}
		});

		btnclockInout.setBounds(10, 596, 120, 63);
		add(btnclockInout);
		this.setVisible(true);
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Monospaced", Font.BOLD, 50));
		passwordField.setBorder(new LineBorder(Color.LIGHT_GRAY, 5));
		passwordField.setBackground(Color.WHITE);
		passwordField.setEditable(false);
		passwordField.setBounds(140, 149, 436, 111);
		add(passwordField);
		timer = new Timer(100, this);
		JPanel panel = new JPanel();
		panel.setBounds(180, 485, 355, 245);
		add(panel);
		panel.setLayout(null);
		JButton button_1 = new JButton("1");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 1);
			}
		});
		button_1.setBounds(0, 0, 89, 63);
		panel.add(button_1);
		JButton button_2 = new JButton("2");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 2);
			}
		});
		button_2.setBounds(88, 0, 89, 63);
		panel.add(button_2);
		JButton button_3 = new JButton("3");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 3);
			}
		});
		button_3.setBounds(176, 0, 89, 63);
		panel.add(button_3);
		JButton btn_delete = new JButton("<html><font size=8>&larr</font></html>");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (passwordField.getText().length() > 0) passwordField.setText(passwordField.getText().substring(0, passwordField.getText().length() - 1));
			}
		});
		btn_delete.setBounds(265, 0, 89, 63);
		panel.add(btn_delete);
		JButton button_4 = new JButton("4");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 4);
			}
		});
		button_4.setBounds(0, 62, 89, 63);
		panel.add(button_4);
		JButton button_5 = new JButton("5");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 5);
			}
		});
		button_5.setBounds(88, 62, 89, 63);
		panel.add(button_5);
		JButton button_6 = new JButton("6");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 6);
			}
		});
		button_6.setBounds(176, 62, 89, 63);
		panel.add(button_6);
		JButton button_clear = new JButton("Clear");
		button_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText("");
			}
		});
		button_clear.setBounds(265, 62, 89, 63);
		panel.add(button_clear);
		JButton button_7 = new JButton("7");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 7);
			}
		});
		button_7.setBounds(0, 123, 89, 63);
		panel.add(button_7);
		JButton button_8 = new JButton("8");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 8);
			}
		});
		button_8.setBounds(88, 123, 89, 63);
		panel.add(button_8);
		JButton button_9 = new JButton("9");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 9);
			}
		});
		button_9.setBounds(176, 123, 89, 63);
		panel.add(button_9);
		JButton button_Enter = new JButton("Enter");
		button_Enter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						Stylist temp_s = sql.isStylist(passwordField.getText());
						if (temp_s != null) {
							CURRENT_STYLIST_ID = temp_s.getID();
							STYLIST_OBJECT = temp_s;
							grantAccess();
							changeToTransactionScreen();
						} else if (sql.isAdmin(passwordField.getText())) {
							grantAccess();
						} else {
							loginError();
						}
					}
				});
			}
		});
		button_Enter.setBounds(265, 123, 89, 125);
		panel.add(button_Enter);
		JButton button_11 = new JButton("");
		button_11.setEnabled(false);
		button_11.setBounds(0, 183, 89, 65);
		panel.add(button_11);
		JButton button_0 = new JButton("0");
		button_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 0);
			}
		});
		button_0.setBounds(88, 185, 89, 63);
		panel.add(button_0);
		JButton button_dot = new JButton("");
		button_dot.setEnabled(false);
		button_dot.setBounds(176, 183, 89, 65);
		panel.add(button_dot);
		
		JPanel ticket_panel = new ReservationPanel();
		ticket_panel.setBackground(Color.WHITE);
		ticket_panel.setBounds(579, 99, 441, 631);
		
		add(ticket_panel);
		
		JLabel lblTicket = new JLabel("Ticket #");
		lblTicket.setForeground(Color.RED);
		lblTicket.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTicket.setBounds(579, 72, 89, 25);
		add(lblTicket);
		
		JLabel lblStylist = new JLabel("/ Stylist");
		lblStylist.setForeground(Color.RED);
		lblStylist.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblStylist.setBounds(667, 72, 89, 25);
		add(lblStylist);
		
		JLabel lblCustomerName = new JLabel("Customer Name");
		lblCustomerName.setForeground(Color.RED);
		lblCustomerName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblCustomerName.setBounds(843, 73, 167, 22);
		add(lblCustomerName);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
	private void loginError() {
		passwordField.setText("");
		passwordField.setBackground(Color.red);
	}
	private void resetPasswordField() {
		passwordField.setText("");
		passwordField.setBackground(Color.white);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.labelDate.setText(sdf.format(new Date()));
	}
	public void updateStylistLabelName() {
		// Stylist s = sql.getStylist(this.CURRENT_STYLIST_ID);
		this.labelStylist.setText("Stylist: " + STYLIST_OBJECT.getName());
	}
	public void grantAccess() {
		// sql.updateStylist(passwordField.getText());
		updateStylistLabelName();
		resetPasswordField();
	}
	private void changeToTransactionScreen() {
		MAIN_MENU_PANEL = this;
		this.setVisible(false);
		this.timer.stop();
		PointOfTransaction pot = new PointOfTransaction(frame);
		this.frame.getContentPane().add(pot);
	}

	public static void goBackToMainMenu() {
		MAIN_MENU_PANEL.setVisible(true);
		MAIN_MENU_PANEL.timer.start();

	}

	private void clockInOut(String id) {
		this.sql.clockInOut(id);
		this.resetPasswordField();
	}

	public void goToManagerSettings() {
		MAIN_MENU_PANEL = this;
		this.setVisible(false);
		this.timer.stop();
		this.frame.getContentPane().add(new ManagerSettingsPanel());
	}
}
