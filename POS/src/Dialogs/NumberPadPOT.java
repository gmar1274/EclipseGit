package Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import MainFrame.POSFrame;
import POS_Panels.PointOfTransaction;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.Component;

public class NumberPadPOT extends JDialog {

	private double amount;
	private JTextPane text;
	
	
	/**
	 * Create the dialog.
	 * @param finalSaleTA 
	 */
	public NumberPadPOT(PointOfTransaction pot) {
		amount=0;
		setBounds(100, 100, 374, 400);
		getContentPane().setLayout(null);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		this.setTitle("Customer Amount");
		this.setIconImage(new ImageIcon(POSFrame.PICTURE_DIRECTORY+"\\acba.png").getImage());
		 text = new JTextPane();
		text.setText("");
		text.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		text.setAlignmentX(Component.RIGHT_ALIGNMENT);
		text.setFont(new Font("Tahoma", Font.BOLD, 35));
		text.setEditable(false);
		text.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		text.setContentType("text/text");
		text.setBounds(0, 0, 358, 80);
		getContentPane().add(text);

		JButton btn_1 = new JButton("1");
		btn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				text.setText(text.getText() + "1");
			}
		});
		btn_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		btn_1.setBounds(0, 81, 89, 70);
		getContentPane().add(btn_1);

		JButton button = new JButton("2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "2");
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 20));
		button.setBounds(91, 81, 89, 70);
		getContentPane().add(button);

		JButton button_1 = new JButton("3");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "3");
			}
		});
		button_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_1.setBounds(181, 81, 89, 70);
		getContentPane().add(button_1);

		JButton button_2 = new JButton("4");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "4");
			}
		});
		button_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_2.setBounds(0, 150, 89, 70);
		getContentPane().add(button_2);

		JButton button_3 = new JButton("5");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "5");
			}
		});
		button_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_3.setBounds(91, 150, 89, 70);
		getContentPane().add(button_3);

		JButton button_4 = new JButton("6");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "6");
			}
		});
		button_4.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_4.setBounds(181, 150, 89, 70);
		getContentPane().add(button_4);

		JButton button_5 = new JButton("7");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "7");
			}
		});
		button_5.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_5.setBounds(0, 220, 89, 70);
		getContentPane().add(button_5);

		JButton button_6 = new JButton("8");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "8");
			}
		});
		button_6.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_6.setBounds(91, 220, 89, 70);
		getContentPane().add(button_6);

		JButton button_7 = new JButton("9");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "9");
			}
		});
		button_7.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_7.setBounds(181, 220, 89, 70);
		getContentPane().add(button_7);

		JButton button_8 = new JButton("0");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(text.getText() + "0");
			}
		});
		button_8.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_8.setBounds(91, 291, 89, 70);
		getContentPane().add(button_8);

		JButton button_9 = new JButton(".");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Double.parseDouble(text.getText() + ".");
					text.setText(text.getText() + ".");
				} catch (Exception ee) {

				}

			}
		});
		button_9.setFont(new Font("Tahoma", Font.BOLD, 20));
		button_9.setBounds(181, 291, 89, 70);
		getContentPane().add(button_9);

		JButton btnExactChange = new JButton("<html>Exact<br>Amount</html>");
		btnExactChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(new DecimalFormat("#.##").format((pot.getSaleTotal())));
			}
		});
		btnExactChange.setForeground(Color.BLUE);
		btnExactChange.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExactChange.setBounds(0, 291, 89, 70);
		getContentPane().add(btnExactChange);

		JButton btnC = new JButton("<html><font size=25>&larr</font></html>");
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text.getText().length() > 0) text.setText(text.getText().substring(0, text.getText().length() - 1));
			}
		});
		btnC.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnC.setBounds(269, 81, 89, 70);
		getContentPane().add(btnC);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("");
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnClear.setBounds(269, 150, 89, 70);
		getContentPane().add(btnClear);

		JButton btnEnter = new JButton("Tender");
		btnEnter.setForeground(Color.RED);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					amount = Double.parseDouble(text.getText());
					setVisible(false);
				}catch(Exception ee ){}
			}
		});
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnEnter.setBounds(269, 220, 89, 141);
		getContentPane().add(btnEnter);
		this.setVisible(true);
	}
	public double getAmount(){
		return this.amount;
	}
}
