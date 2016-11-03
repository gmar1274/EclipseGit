package Dialogs;

import javax.swing.JDialog;

import POSObjects.Advertisment;
import POSObjects.Coupon;
import ReservationScreenCustomerLogin.Customer;
import ReservationScreenCustomerLogin.KeyBoard;
import WebServices.Email;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;

public class ReedemCoupon extends JDialog implements FocusListener {

	private Advertisment a;
	private JTextField email;
	private JTextField name;
	private JLabel lblName;
	private KeyBoard kb;

	public ReedemCoupon(Advertisment a) {
		getContentPane().setBackground(Color.BLACK);
		setTitle("ACBA: Redeem Coupon");
		setModal(true);
		setResizable(false);
		this.setMinimumSize(new Dimension(448, 300));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.a = a;
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Email:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Cambria Math", Font.PLAIN, 22));
		lblNewLabel.setBounds(10, 11, 60, 39);
		getContentPane().add(lblNewLabel);
		lblNewLabel.requestFocus();
		;
		email = new JTextField();
		email.setBounds(83, 11, 351, 34);
		getContentPane().add(email);
		email.setColumns(10);

		lblName = new JLabel("Name:");
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("Cambria Math", Font.PLAIN, 22));
		lblName.setBounds(10, 52, 70, 39);
		getContentPane().add(lblName);

		name = new JTextField();
		name.setColumns(10);
		name.setBounds(83, 56, 351, 34);
		getContentPane().add(name);

		JButton btn_redeem = new JButton("Reedem Coupon");
		btn_redeem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isDefault(name) || isDefault(email)) {
					error();
				} else {
					reset();
					generateBarcodeAndSendEmail();
				}
			}
		});
		btn_redeem.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		btn_redeem.setBounds(5, 143, 182, 123);
		getContentPane().add(btn_redeem);

		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		btnNewButton_1.setBounds(255, 143, 182, 123);
		getContentPane().add(btnNewButton_1);
		this.name.addFocusListener(this);
		this.email.addFocusListener(this);
		name.setText("Required");
		email.setText("Required");
		name.setForeground(new Color(150, 150, 150));
		email.setForeground(new Color(150, 150, 150));

		this.getRootPane().setDefaultButton(btn_redeem);
		btn_redeem.setFocusable(true);
		this.lblName.setFocusable(true);
		// name.setFocusable(false);
		// email.setFocusable(false);
		setVisible(true);
	}

	private void error() {
		if (name.getText().length() == 0 || name.getText().toLowerCase().contains("required")) {
			name.setBackground(Color.red);
		}
		if (email.getText().length() == 0 || email.getText().toLowerCase().contains("required")) {
			email.setBackground(Color.red);
		}
	}

	private void reset() {
		name.setBackground(Color.white);
		email.setBackground(Color.white);
	}

	private void generateBarcodeAndSendEmail() {
		// send MSG through email then tell them it has been sent!
		new Thread(new Runnable() {

			@Override
			public void run() {
				Customer c = new Customer(name.getText(),email.getText());
				
				
				if (Email.sendEmail(c,a)) {

				} else {
					JOptionPane.showMessageDialog(null, "Network Error. Sorry connection could not be made.", "Network Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		}).start();
		JOptionPane.showMessageDialog(null, "Email has been sent thank you!", "Success", JOptionPane.PLAIN_MESSAGE);
		this.dispose();
	}

	private boolean isDefault(JTextField tf) {
		return (tf.getText().length() == 0 || tf.getText().toLowerCase().contains("required"));

	}

	public void exit() {
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void focusGained(FocusEvent e) {
		JTextField tf = (JTextField) e.getSource();
		if (kb != null && tf == kb.getJTextField()) { return; }
		tf.setBackground(Color.WHITE);

		if (!name.isFocusable() && !email.isFocusable()) {
			exit();
		}

		kb = new KeyBoard(tf, KeyBoard.KEYBOARD.KEYBOARD);
		if (this.isDefault(tf)) tf.setText("Required");
		tf.setFocusable(false);
		if (tf == name) {
			if (this.isDefault(tf)) {
				exit();
			} else {
				name.setFocusable(false);
				if (!this.isDefault(email)) {
					email.setFocusable(false);
				} else {
					email.setFocusable(true);
				}
			}
		} else {
			if (this.isDefault(tf)) exit();
		}
	}

	@Override
	public void focusLost(FocusEvent e) {

	}
}
