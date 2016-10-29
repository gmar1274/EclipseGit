package ReservationScreenCustomerLogin;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import ReservationScreenCustomerLogin.KeyBoard.KEYBOARD;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

public class KeyBoard extends JDialog {

	private JTextField name;
	private JPanel panel;
	private JToggleButton caps;
	private JButton enter;
	private JButton space;
	private boolean isActive;

	public static enum KEYBOARD {
		NUMBER_PAD, KEYBOARD
	};

	public boolean isOnScreen() {
		return this.isActive;
	}

	public JTextField getJTextField() {
		return this.name;
	}

	public KeyBoard(JTextField name, KEYBOARD val) {
		this.isActive = true;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.name = name;
		if (this.name.getText().toLowerCase().contains("required")) this.name.setText("");
		this.name.setForeground(new Color(50, 50, 50));
		this.setModal(true);
		this.setResizable(false);

		getContentPane().setLayout(null);
		panel = new JPanel();
		panel.setBounds(0, 0, 949, 152);
		getContentPane().add(panel);
		FlowLayout fl_panel = new FlowLayout();
		fl_panel.setVgap(1);
		panel.setLayout(fl_panel);
		if (val == KEYBOARD.NUMBER_PAD) { //////////// NUMBER PAD
			this.setBounds(400, 400, 420, 350);
			this.getContentPane().removeAll();
			this.getContentPane().add(new NumberPad(name, this));
		} else {
			this.setBounds(20, 500, 957, 250);
			String r1 = "1234567890";
			String r0 = "!@#$%^&*()_+|";
			String r5 = "{}[];':\"-=\\,./<>?";
			String r2 = "qwertyuiop";
			String r3 = "asdfghjkl";
			String r4 = "zxcvbnm";

			createButtons(r0);
			createButtons(r5);
			createButtons(r1);
			createButtons(r2);
			createButtons(r3);
			createButtons(r4);
			caps = new JToggleButton("Caps Lock");
			caps.setFont(new Font("Tahoma", Font.BOLD, 12));
			caps.setBounds(0, 153, 128, 68);
			caps.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (caps.isSelected()) {
						setKeyboardToUpper();
					} else {
						setKeyboardToLower();
					}
				}

			});
			caps.setVisible(true);
			getContentPane().add(caps);
			space = new JButton("Space");
			space.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					name.setText(name.getText() + " ");
				}
			});
			space.setFont(new Font("Tahoma", Font.BOLD, 12));
			space.setBounds(276, 153, 414, 68);

			getContentPane().add(space);

			JButton com = new JButton(".com");
			com.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					name.setText(name.getText() + com.getText());
				}
			});
			com.setFont(new Font("Tahoma", Font.BOLD, 12));
			com.setBounds(700, 153, 111, 68);
			getContentPane().add(com);

			JButton backspace = new JButton("<html><font size=10>&larr</font></html>");
			backspace.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (name.getText().length() > 0) name.setText(name.getText().substring(0, name.getText().length() - 1));
				}
			});
			backspace.setBounds(138, 153, 128, 68);
			backspace.setVisible(true);
			getContentPane().add(backspace);

			this.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowClosing(WindowEvent e) {
					isActive = false;
					setVisible(false);
					dispose();

				}

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub

				}

			});
			enter = new JButton("Enter");
			enter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					isActive = false;
					// setVisible(false);
					dispose();
				}
			});
			enter.setFont(new Font("Tahoma", Font.BOLD, 12));
			enter.setBounds(821, 153, 128, 68);
			enter.setVisible(true);
			getContentPane().add(enter);
		}
		this.setVisible(true);
	}

	private void setKeyboardToUpper() {
		try {
			for (Component c : panel.getComponents()) {
				JButton b = (JButton) c;
				if (b != space || b != enter) b.setText(b.getText().toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setKeyboardToLower() {
		try {
			for (Component c : panel.getComponents()) {
				JButton b = (JButton) c;
				if (b != space || b != enter) b.setText(b.getText().toLowerCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createButtons(String r) {

		GridBagConstraints cc = new GridBagConstraints();
		cc.fill = GridBagConstraints.HORIZONTAL;

		// cc.weightx = 0.0;
		// cc.weighty = 0;
		// cc.gridx = 0;
		// cc.gridy = 0;
		// cc.gridheight=99;
		// cc.gridwidth=999;

		for (char c : r.toCharArray()) {
			JButton button = new JButton("" + c);
			button.setFont(new Font("Arial", Font.BOLD, 20));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// if(name.getText().length()<10 && )
					// {
					name.setText(name.getText() + button.getText());
					// }
				}
			});
			button.setVisible(true);
			panel.add(button, cc);
		}
	}
}
