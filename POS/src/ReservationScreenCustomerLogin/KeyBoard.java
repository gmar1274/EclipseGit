package ReservationScreenCustomerLogin;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import ReservationScreenCustomerLogin.KeyBoard.KEYBOARD_TYPE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class KeyBoard extends JDialog {

	private JTextField tf;
	private ArrayList<JPanel> pa;

	public static enum KEYBOARD_TYPE {
		NUMBER_PAD, KEYBOARD
	};

	public KeyBoard(JTextField name, KEYBOARD_TYPE val) {
		this.setModal(true);
		this.setResizable(false);
		this.tf = name;
		setSize(new Dimension(955, 500));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (this.tf.getText().toLowerCase().contains("required")) this.tf.setText("");//
		this.tf.setForeground(new Color(50, 50, 50));/// color to gray
		if (val == KEYBOARD_TYPE.NUMBER_PAD) { //////////// NUMBER PAD
			this.setBounds(400, 370, 417, 400);
			this.getContentPane().removeAll();
			this.getContentPane().add(new NumberPad(name, this));
		} else {
			setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
			JPanel contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.setBounds(20, 500, 955, 250);
			String r0 = "!@#$%^&*()_+|";
			String r5 = "{}:<>?-=[]\\,./;'";
			r0 += r5;///

			setContentPane(contentPane);
			contentPane.setLayout(null);

			JPanel panel_num = new JPanel();
			panel_num.setBounds(10, 11, 919, 36);
			contentPane.add(panel_num);
			panel_num.setLayout(new GridLayout(1, 0, 0, 0));

			JPanel panel_1 = new JPanel();
			panel_1.setBounds(10, 47, 919, 36);
			contentPane.add(panel_1);
			panel_1.setLayout(new GridLayout(1, 0, 0, 0));

			JPanel panel_2 = new JPanel();
			panel_2.setBounds(55, 83, 815, 36);
			contentPane.add(panel_2);
			panel_2.setLayout(new GridLayout(1, 0, 0, 0));
			JPanel panel_3 = new JPanel();
			panel_3.setBounds(97, 120, 714, 36);
			contentPane.add(panel_3);
			panel_3.setLayout(new GridLayout(1, 0, 0, 0));

			JPanel panel_4 = new JPanel();
			panel_4.setBounds(0, 164, 939, 47);
			contentPane.add(panel_4);
			panel_4.setLayout(null);

			JButton btnNewButton = new JButton("Space");
			btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tf.setText(tf.getText() + " ");
				}
			});
			btnNewButton.setBounds(294, 0, 352, 47);
			panel_4.add(btnNewButton);

			JButton back = new JButton("<html>Backspace<br>&larr</html>");
			back.setForeground(Color.RED);
			back.setFont(new Font("Times New Roman", Font.BOLD, 14));
			back.setBounds(683, 0, 100, 47);
			back.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (tf.getText().length() <= 0) { return; }
					tf.setText(tf.getText().substring(0, tf.getText().length() - 1));
				}

			});
			panel_4.add(back);

			JButton btnEnter = new JButton("Enter");
			btnEnter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btnEnter.setFont(new Font("Times New Roman", Font.BOLD, 14));
			btnEnter.setBounds(839, 0, 100, 47);
			panel_4.add(btnEnter);

			JToggleButton tglbtnNewToggleButton = new JToggleButton("Caps Lock");
			tglbtnNewToggleButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
			tglbtnNewToggleButton.setBounds(0, 0, 121, 47);
			tglbtnNewToggleButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (tglbtnNewToggleButton.isSelected()) {
						capsLock(true);
					} else {
						capsLock(false);
					}

				}
			});

			panel_4.add(tglbtnNewToggleButton);

			JButton com = new JButton(".com");
			com.setFont(new Font("Times New Roman", Font.BOLD, 14));
			com.setBounds(147, 0, 100, 47);
			com.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tf.setText(tf.getText() + ".com");
				}

			});
			panel_4.add(com);
			pa = new ArrayList<>();
			pa.add(panel_num);
			pa.add(panel_1);
			pa.add(panel_2);
			pa.add(panel_3);

			JToggleButton btnSym = new JToggleButton("Symbols");
			btnSym.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (btnSym.isSelected()) {
						symbolMode();
					} else {
						String r1 = "1234567890";
						String r2 = "qwertyuiop";
						JPanel p = pa.get(0);
						JPanel pp = pa.get(1);
						p.removeAll();
						pp.removeAll();
						createButtons(r1, p);
						createButtons(r2, pp);
						pack();
					}
				}
			});
			btnSym.setFont(new Font("Times New Roman", Font.BOLD, 14));
			btnSym.setBounds(0, 120, 96, 45);
			contentPane.add(btnSym);
			normalMode();
			this.setMinimumSize(new Dimension(955, 250));
			this.setResizable(false);
		}
		this.setVisible(true);
	}

	private void symbolMode() {
		String r0 = "~`!@#$%^&*()_+-=";
		String r1 = "{}<>?/[]|\\:;\"',./";

		JPanel original_panel_row1 = pa.get(0);
		JPanel original_panel_row2 = pa.get(1);
		// this.getContentPane().remove(original_panel_row1);
		original_panel_row1.removeAll();
		original_panel_row2.removeAll();
		this.createButtons(r0, original_panel_row1);
		this.createButtons(r1, original_panel_row2);
		this.getContentPane().repaint();
		this.pack();

	}

	private void normalMode() {
		String r1 = "1234567890";
		String r2 = "qwertyuiop";
		String r3 = "asdfghjkl";
		String r4 = "zxcvbnm";
		ArrayList<String> a = new ArrayList<>();
		a.add(r1);
		a.add(r2);
		a.add(r3);
		a.add(r4);
		for (JPanel p : pa) {
			this.createButtons(a.remove(0), p);
		}

	}

	protected void capsLock(boolean caps) {
		for (JPanel p : pa) {
			for (Component c : p.getComponents()) {
				if (c instanceof JButton) {
					JButton b = (JButton) c;
					if (caps) {
						b.setText(b.getText().toUpperCase());
					} else {
						b.setText(b.getText().toLowerCase());
					}
				}
			}
		}

	}

	private void createButtons(String r, JPanel p) {
		for (char c : r.toCharArray()) {
			JButton b = new JButton("" + c);
			b.setSize(50, 50);
			b.setFont(new Font("Times New Roman", Font.PLAIN, 22));
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tf.setText(tf.getText() + b.getText());
				}
			});
			b.setVisible(true);
			p.add(b);
		}
	}

	public JTextField getJTextField() {
		return this.tf;
	}
}
