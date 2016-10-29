package Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.border.LineBorder;
import CashRegister.OpenCashRegister;
import MainFrame.POSFrame;
import POS_Panels.PointOfTransaction;
import SQLclass.SQL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MiscDialog extends JDialog {
	private JPasswordField passwordField;
	private SQL sql;

	/**
	 * Create the dialog.
	 */
	public MiscDialog() {
		this.sql = POSFrame.SQL;
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Miscellaneous - Admin Password Needed");
		setBounds(100, 100, 388, 434);
		getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(0, 115, 284, 290);
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(4, 3, 0, 0));
		JButton btnNewButton_1 = new JButton("1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(passwordField.getText() + 1);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_1);
		JButton btnNewButton_2 = new JButton("2");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 2);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_2);
		JButton btnNewButton_3 = new JButton("3");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 3);
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_3);
		JButton btnNewButton = new JButton("4");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 4);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton);
		JButton btnNewButton_4 = new JButton("5");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 5);
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_4);
		JButton btnNewButton_5 = new JButton("6");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 6);
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_5);
		JButton btnNewButton_6 = new JButton("7");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 7);
			}
		});
		btnNewButton_6.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_6);
		JButton btnNewButton_7 = new JButton("8");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 8);
			}
		});
		btnNewButton_7.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_7);
		JButton btnNewButton_8 = new JButton("9");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 9);
			}
		});
		btnNewButton_8.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_8);
		JButton btnNewButton_9 = new JButton("");
		btnNewButton_9.setEnabled(false);
		panel.add(btnNewButton_9);
		JButton btnNewButton_10 = new JButton("0");
		btnNewButton_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText(passwordField.getText() + 0);
			}
		});
		btnNewButton_10.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel.add(btnNewButton_10);
		JButton btnNewButton_11 = new JButton("");
		btnNewButton_11.setEnabled(false);
		panel.add(btnNewButton_11);
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sql.isAdmin(passwordField.getText())) {
					new OpenCashRegister();// open cash reg
					sql.logCashRegister(passwordField.getText());/// log open
					if (PointOfTransaction.pot != null) {
						PointOfTransaction.pot.btn_cancelSetEnable();
					}
					dispose();
				} else {
					loginError();
				}
			}
		});
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEnter.setBounds(283, 260, 99, 145);
		getContentPane().add(btnEnter);
		JButton btnlarr = new JButton("<html><font size=8>&larr</font><html>");
		btnlarr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (passwordField.getText().length() > 0) {
					passwordField.setText(passwordField.getText().substring(0, passwordField.getText().length() - 1));
				}
			}
		});
		btnlarr.setBounds(283, 115, 99, 72);
		getContentPane().add(btnlarr);
		JButton btnNewButton_12 = new JButton("Clear");
		btnNewButton_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordField.setText("");
			}
		});
		btnNewButton_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_12.setBounds(283, 187, 99, 72);
		getContentPane().add(btnNewButton_12);
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.BOLD, 70));
		passwordField.setBorder(new LineBorder(Color.BLACK, 5));
		passwordField.setBackground(Color.GREEN);
		passwordField.setEditable(false);
		passwordField.setBounds(0, 0, 382, 114);
		getContentPane().add(passwordField);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void loginError() {
		this.passwordField.setText("");
		this.passwordField.setBackground(Color.red);
	}
}
