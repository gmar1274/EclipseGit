package ReservationScreenCustomerLogin;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class NumberPad extends JPanel {

	/**
	 * Create the panel.
	 */
	public NumberPad(JTextField tf, JDialog parent) {
		setLayout(null);
		this.setSize(420, 370);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 308, 370);
		add(panel);

		JButton btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "1");
			}
		});
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		btn1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn1);

		JButton btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "2");
			}
		});
		btn2.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn2);

		JButton btn3 = new JButton("3");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "3");
			}
		});
		btn3.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn3);

		JButton btn4 = new JButton("4");
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "4");
			}
		});
		btn4.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn4);

		JButton btn5 = new JButton("5");
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "5");
			}
		});
		btn5.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn5);

		JButton btn6 = new JButton("6");
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "6");
			}
		});
		btn6.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn6);

		JButton btn7 = new JButton("7");
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "7");
			}
		});
		btn7.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn7);

		JButton btn8 = new JButton("8");
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "8");
			}
		});
		btn8.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn8);

		JButton btn9 = new JButton("9");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "9");
			}
		});
		btn9.setFont(new Font("Times New Roman", Font.BOLD, 20));
		panel.add(btn9);
		
		JButton button_1 = new JButton("");
		button_1.setEnabled(false);
		panel.add(button_1);
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton button = new JButton("0");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() >= 10) return;
				tf.setText(tf.getText() + "0");
			}
		});
		panel.add(button);
		button.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		JButton button_2 = new JButton("");
		button_2.setEnabled(false);
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(button_2);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(309, 0, 101, 370);
		add(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnDelete = new JButton("Delete");
		btnDelete.setForeground(Color.RED);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().length() > 0) {
					tf.setText(tf.getText().substring(0, tf.getText().length() - 1));
				}
			}
		});
		btnDelete.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panel_1.add(btnDelete);

		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
			}
		});
		btnEnter.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panel_1.add(btnEnter);

	}
}
