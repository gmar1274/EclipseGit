package AdminPanelDialogs_JPanels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import Dialogs.HairServicesDialog;
import MainFrame.POSFrame;
import SQLclass.SQL;

import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ReservationSettingsPanel extends JPanel {

	private SQL sql = POSFrame.SQL;
	private JLabel label_currentTicketPrice;
	private JTextField textField_name;
	private JSpinner spinner_hours;
	private JSpinner spinner_mins;
	private JComboBox comboBox_service_dollars;
	private JComboBox comboBox_service_cents;
	private JPanel parent;

	/**
	 * Create the panel.
	 */
	public ReservationSettingsPanel(JPanel p) {
		this.parent=p;
		this.parent.setVisible(false);
		if (sql == null) {
			sql = new SQL();
		}
		setLayout(null);
		setSize(495, 560);

		JLabel lblAppSettings = new JLabel("Reservation Settings:");
		lblAppSettings.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, Color.BLUE));
		lblAppSettings.setForeground(Color.BLUE);
		lblAppSettings.setFont(new Font("Cambria Math", Font.BOLD, 18));
		lblAppSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblAppSettings.setBounds(10, 11, 475, 33);
		add(lblAppSettings);

		JLabel lblSetTicketPrice = new JLabel("<html>Change<br/> ticket price:</html>");
		lblSetTicketPrice.setBorder(null);
		lblSetTicketPrice.setVerticalAlignment(SwingConstants.BOTTOM);
		lblSetTicketPrice.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		lblSetTicketPrice.setBounds(20, 79, 120, 33);
		add(lblSetTicketPrice);

		JLabel label = new JLabel("$");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Cambria Math", Font.BOLD, 24));
		label.setBounds(150, 94, 19, 42);
		add(label);

		JLabel label_1 = new JLabel(".");
		label_1.setVerticalAlignment(SwingConstants.TOP);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Cambria Math", Font.BOLD, 40));
		label_1.setBounds(236, 93, 26, 42);
		add(label_1);

		JComboBox comboBox_dollars = new JComboBox();
		comboBox_dollars.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		comboBox_dollars.setBounds(179, 94, 61, 39);
		add(comboBox_dollars);

		JComboBox comboBox_cents = new JComboBox();
		comboBox_cents.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		comboBox_cents.setBounds(257, 94, 50, 39);
		add(comboBox_cents);

		JLabel label_2 = new JLabel("");
		label_2.setVerticalAlignment(SwingConstants.BOTTOM);
		label_2.setFont(new Font("Cambria Math", Font.BOLD, 18));
		label_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Update Ticket Price", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(255, 0, 0)));
		label_2.setBounds(10, 55, 318, 109);
		add(label_2);

		JButton btnUpdatePrice = new JButton("Update Price");

		btnUpdatePrice.setForeground(Color.RED);
		btnUpdatePrice.setFont(new Font("Cambria Math", Font.BOLD, 12));
		btnUpdatePrice.setBounds(20, 123, 121, 33);
		add(btnUpdatePrice);

		label_currentTicketPrice = new JLabel("");
		label_currentTicketPrice.setHorizontalTextPosition(SwingConstants.CENTER);
		label_currentTicketPrice.setHorizontalAlignment(SwingConstants.CENTER);
		label_currentTicketPrice.setFont(new Font("Cambria Math", Font.BOLD, 18));
		label_currentTicketPrice.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Current Ticket Price", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(255, 0, 0)));
		label_currentTicketPrice.setBounds(328, 55, 157, 109);
		add(label_currentTicketPrice);
		DefaultComboBoxModel cb = new DefaultComboBoxModel();
		DefaultComboBoxModel cb2 = new DefaultComboBoxModel();
		new Thread(new Runnable() {

			@Override
			public void run() {
				DecimalFormat df = new DecimalFormat("00");
				for (int i = 0; i <= 100; ++i) {
					if (i == 100) {
						cb.addElement(i);
					} else {
						cb.addElement(i);
						cb2.addElement(df.format(i));
					}
				}
				comboBox_cents.setModel(cb2);
				comboBox_dollars.setModel(cb);
			}

		}).start();
		this.displayCurrentTicketPrice(label_currentTicketPrice);

		JButton button = new JButton("Go Back");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				parent.setVisible(true);
			}
		});
		button.setFont(new Font("Cambria Math", Font.BOLD, 12));
		button.setBounds(10, 489, 100, 60);
		add(button);

		JLabel label_3 = new JLabel("");
		label_3.setVerticalAlignment(SwingConstants.BOTTOM);
		label_3.setFont(new Font("Cambria Math", Font.BOLD, 18));
		label_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Reservation Services", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLUE));
		label_3.setBounds(10, 167, 475, 311);
		add(label_3);

		JLabel lblNameOfService = new JLabel("Name of service:");
		lblNameOfService.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNameOfService.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		lblNameOfService.setBorder(null);
		lblNameOfService.setBounds(20, 188, 120, 33);
		add(lblNameOfService);

		textField_name = new JTextField();
		textField_name.setFont(new Font("Cambria Math", Font.PLAIN, 12));
		textField_name.setBounds(179, 200, 277, 33);
		add(textField_name);
		textField_name.setColumns(10);

		JLabel lblestimatedTicketPrice = new JLabel("<html>Estimated duration of session: </html>");
		lblestimatedTicketPrice.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		lblestimatedTicketPrice.setBorder(null);
		lblestimatedTicketPrice.setBounds(20, 232, 120, 60);
		add(lblestimatedTicketPrice);

		spinner_hours = new JSpinner();
		spinner_hours.setModel(new SpinnerNumberModel(new Short((short) 0), new Short((short) 0), new Short((short) 24), new Short((short) 1)));
		spinner_hours.setFont(new Font("Cambria Math", Font.PLAIN, 25));
		spinner_hours.setBounds(179, 254, 65, 38);
		add(spinner_hours);

		JLabel lblHrs = new JLabel("hrs");
		lblHrs.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblHrs.setVerticalAlignment(SwingConstants.BOTTOM);
		lblHrs.setFont(new Font("Cambria Math", Font.BOLD, 20));
		lblHrs.setBorder(null);
		lblHrs.setBounds(257, 259, 39, 33);
		add(lblHrs);

		spinner_mins = new JSpinner();
		spinner_mins.setModel(new SpinnerNumberModel(new Short((short) 0), new Short((short) 0), new Short((short) 59), new Short((short) 1)));
		spinner_mins.setFont(new Font("Cambria Math", Font.PLAIN, 25));
		spinner_mins.setBounds(321, 254, 65, 38);
		add(spinner_mins);

		JLabel lblMins = new JLabel("mins");
		lblMins.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblMins.setVerticalAlignment(SwingConstants.BOTTOM);
		lblMins.setFont(new Font("Cambria Math", Font.BOLD, 20));
		lblMins.setBorder(null);
		lblMins.setBounds(396, 259, 60, 33);
		add(lblMins);

		JLabel lblPriceOfService = new JLabel("Price of service:");
		lblPriceOfService.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPriceOfService.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		lblPriceOfService.setBorder(null);
		lblPriceOfService.setBounds(20, 303, 120, 33);
		add(lblPriceOfService);

		JLabel label_4 = new JLabel("$");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("Cambria Math", Font.BOLD, 24));
		label_4.setBounds(150, 303, 19, 42);
		add(label_4);

		comboBox_service_dollars = new JComboBox();
		comboBox_service_dollars.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		comboBox_service_dollars.setBounds(179, 303, 128, 39);
		add(comboBox_service_dollars);

		JLabel label_5 = new JLabel(".");
		label_5.setVerticalAlignment(SwingConstants.TOP);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("Cambria Math", Font.BOLD, 40));
		label_5.setBounds(306, 303, 15, 42);
		add(label_5);

		comboBox_service_cents = new JComboBox();
		comboBox_service_cents.setFont(new Font("Cambria Math", Font.PLAIN, 16));
		comboBox_service_cents.setBounds(321, 303, 50, 39);
		add(comboBox_service_cents);

		JButton btn_createService = new JButton("Create Service");
		btn_createService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createService();// sql
			}
		});
		btn_createService.setForeground(Color.RED);
		btn_createService.setFont(new Font("Cambria Math", Font.BOLD, 12));
		btn_createService.setBounds(328, 414, 144, 51);
		add(btn_createService);

		JButton btnShowAllServices = new JButton("Show All Services");
		btnShowAllServices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new HairServicesDialog(sql).setVisible(true);
			}
		});
		btnShowAllServices.setForeground(Color.BLUE);
		btnShowAllServices.setFont(new Font("Cambria Math", Font.BOLD, 12));
		btnShowAllServices.setBounds(25, 414, 144, 51);
		add(btnShowAllServices);
		btnUpdatePrice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updatePrice(comboBox_dollars, comboBox_cents);

			}

		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				DefaultComboBoxModel cb = new DefaultComboBoxModel();
				DefaultComboBoxModel cb2 = new DefaultComboBoxModel();
				DecimalFormat df = new DecimalFormat("#,#00");
				for (int i = 0; i <= 2000; ++i) {
					if (i < 100) {
						cb.addElement(i);
						cb2.addElement(df.format(i));
					} else {
						cb.addElement(df.format(i));
					}
				}
				//
				comboBox_service_dollars.setModel(cb);
				comboBox_service_cents.setModel(cb2);
			}

		}).start();
	}

	private void createService() {
		String name = this.textField_name.getText().toUpperCase();
		BigDecimal price = new BigDecimal(
		Integer.parseInt(this.comboBox_service_dollars.getSelectedItem().toString()) + (Double.parseDouble(this.comboBox_service_cents.getSelectedItem().toString()) / 100.));
		//long hr_to_sec = Long.parseLong(this.spinner_hours.getValue().toString()) * 60 * 60;
		long min_to_sec = Long.parseLong(this.spinner_mins.getValue().toString()) * 60;
		////long sec_to_msec = (hr_to_sec + min_to_sec) * 1000;// sum seconds to milli seconds
		//Time duration = new Time(sec_to_msec); // this.spinner_hours.getValue()
		//System.out.println(duration);
		DecimalFormat df = new DecimalFormat("00");
		String duration=df.format(Double.parseDouble(this.spinner_hours.getModel().getValue().toString()))+":"+df.format(Double.parseDouble(this.spinner_mins.getModel().getValue().toString()))+":00";
		
		if (name.length() == 0 || duration.contains("00:00:00")) {
			JOptionPane.showMessageDialog(null, "No data has been saved.", "Error", JOptionPane.ERROR_MESSAGE);

		} else if (sql.addNewService(name, price, duration)) {
			JOptionPane.showMessageDialog(null, "Service has been created!", "Success", JOptionPane.PLAIN_MESSAGE);
			updateServices();
		} else {

			JOptionPane.showMessageDialog(null, "No data has been saved.", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void updateServices() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				sql.updateHairServiceHashMap();
			}
		}).start();

	}

	private void updatePrice(JComboBox dollars, JComboBox cents) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int d = Integer.parseInt(dollars.getSelectedItem().toString());
				double c = Double.parseDouble(cents.getSelectedItem().toString()) / 100.;
				BigDecimal price = new BigDecimal(d + c);
				if (!isNewPrice(price, label_currentTicketPrice)) {
					JOptionPane.showMessageDialog(null, "No changes have been made.", "No Change", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (sql.updateTicketPrice(price)) {
					JOptionPane.showMessageDialog(null, "Price has been updated!", "Success", JOptionPane.PLAIN_MESSAGE);
					displayCurrentTicketPrice(label_currentTicketPrice);
				}
			}

		}).start();

	}

	private void displayCurrentTicketPrice(JLabel label) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				BigDecimal bd = sql.getCurrentTicketPrice();
				DecimalFormat df = new DecimalFormat("$##0.00");
				label.setText(df.format(bd));
				repaint();

			}
		}).start();
	}

	private boolean isNewPrice(BigDecimal newprice, JLabel curr) {
		try {
			if (curr.getText().length() == 0) return true;// no price set
			BigDecimal currprice = new BigDecimal(Double.parseDouble(curr.getText().replace("$", "")));
			return newprice.compareTo(currprice) != 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
}
