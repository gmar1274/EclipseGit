package Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import MainFrame.POSFrame;
import WebServices.CreditCard;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.Scanner;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreditCardDialog extends JDialog {
	private boolean processing_done;
	private JLabel pic;
	private JButton btn_exit;
	private JButton btn_redo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CreditCardDialog dialog = new CreditCardDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CreditCardDialog() {
		processing_done = false;
		getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		setTitle("Credit Card");
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		pic = new JLabel("");
		pic.setBackground(Color.WHITE);
		pic.setIcon(new ImageIcon(POSFrame.PICTURE_DIRECTORY + "\\ready.png"));
		pic.setBounds(26, 0, 358, 198);
		getContentPane().add(pic);

		btn_exit = new JButton("Cancel");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btn_exit.setFont(new Font("Tahoma", Font.BOLD, 18));
		btn_exit.setForeground(Color.RED);
		btn_exit.setBounds(212, 198, 172, 63);
		getContentPane().add(btn_exit);

		btn_redo = new JButton("Try Again");
		btn_redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				charge();
			}
		});
		btn_redo.setForeground(Color.BLUE);
		btn_redo.setFont(new Font("Tahoma", Font.BOLD, 18));
		btn_redo.setBounds(0, 198, 172, 63);
		getContentPane().add(btn_redo);
		this.setVisible(true);
		charge();

	}

	private void charge() {
		boolean charge = CreditCard.charge(999, pic);
		if (charge) {
			btn_exit.setText("Exit");
			btn_redo.setEnabled(false);
		}
	}
}
