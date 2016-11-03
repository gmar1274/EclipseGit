package Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import MainFrame.POSFrame;

import javax.swing.JLabel;

public class Loading extends JDialog {

	/**
	 * Create the dialog.
	 */
	public Loading() {
		setResizable(false);
		setSize(310, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		String string =POSFrame.PICTURE_DIRECTORY + "\\loading.gif";
		getContentPane().setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 304, 71);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		ImageIcon i = new ImageIcon(string);
		JLabel label = new JLabel(i);
		label.setOpaque(true);
		panel.add(label);
		label.setIcon(new ImageIcon(string));
		label.setSize(360,100);
		setVisible(true);
	}
}