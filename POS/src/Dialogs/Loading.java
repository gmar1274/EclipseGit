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
		setSize(360, 100);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);

		JLabel label = new JLabel(new ImageIcon(POSFrame.PICTURE_DIRECTORY + "\\loading.gif"));
		label.setBounds(0, 0, 360, 100);
		getContentPane().add(label);
		setAlwaysOnTop(true);
		setVisible(true);

	}
}
