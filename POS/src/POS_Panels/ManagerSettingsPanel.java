package POS_Panels;

import javax.swing.JPanel;

import MainFrame.POSFrame;
import SQLclass.SQL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.border.LineBorder;

import Dialogs.AdminPanelDialog;
import Dialogs.Loading;
import Dialogs.MiscDialog;
import JCharts.JChart;

import javax.swing.JButton;

public class ManagerSettingsPanel extends JPanel implements ActionListener {

	private JTextPane screen;
	private Timer t;
	private SimpleDateFormat sdf;
	private JLabel dateLabel;
	private Process proc;
	private SQL sql;
	private ManagerSettingsPanel ms;
	private JScrollPane scrollPane;

	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public JTextPane getScreen() {
		return this.screen;
	}

	public ManagerSettingsPanel() {
		this.ms = this;
		this.sql = POSFrame.SQL;
		sdf = new SimpleDateFormat("EEE MM/dd/yyyy h:mm:ss a");
		this.setSize(POSFrame.WIDTH, POSFrame.HEIGHT);
		setLayout(null);

		dateLabel = new JLabel("");
		dateLabel.setBorder(new LineBorder(new Color(0, 0, 255), 3));
		dateLabel.setForeground(Color.RED);
		dateLabel.setFont(new Font("Arial", Font.BOLD, 20));
		dateLabel.setBounds(714, 11, 296, 40);
		add(dateLabel);
		screen = new JTextPane();
		screen.setContentType("text/html");
		screen.setEditable(false);
		scrollPane = new JScrollPane(screen);
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY, 5));
		scrollPane.setBounds(10, 59, 1000, 227);
		add(scrollPane);

		t = new Timer(100, this);
		t.start();
		this.add(dateLabel);

		JButton btnNewButton = new JButton("Log Out");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logOut();
			}
		});
		btnNewButton.setBounds(10, 652, 121, 78);
		add(btnNewButton);

		JButton jbtn_changeCashRegister = new JButton("<html>Set Cash<br>Register</html>");
		jbtn_changeCashRegister.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {

					proc = Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
					String opt = JOptionPane.showInputDialog("Enter new amount: ");
					if (opt != null) {
						try {
							double amount = Double.parseDouble(opt);
							POSFrame.SQL.setRegisterAmountBy(new BigDecimal(amount));
							JOptionPane.showMessageDialog(null, "Success.");
							proc.destroy();
							updateScreen();
						} catch (Exception ee) {
							JOptionPane.showMessageDialog(null, "Error", "Enter a valid number.", JOptionPane.ERROR_MESSAGE);
							ee.printStackTrace();
						}

					}

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Error", "Enter a valid number.", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		jbtn_changeCashRegister.setBounds(10, 297, 121, 78);
		add(jbtn_changeCashRegister);

		JButton jbtn_OpenCashRegister = new JButton("<html>Open Cash<br>Register</html>");
		jbtn_OpenCashRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MiscDialog();
			}
		});
		jbtn_OpenCashRegister.setBounds(10, 386, 121, 78);
		add(jbtn_OpenCashRegister);

		JButton weekly_JBtn = new JButton("<html>Weekly Overview<br>Charts</html>");
		weekly_JBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						POSFrame.loading=new Loading();
						JChart c = new JChart(JChart.OVERVIEW.DAILY);
						POSFrame.loading.dispose();
					}
				}).start();
			}
		});
		weekly_JBtn.setBounds(873, 386, 129, 78);
		add(weekly_JBtn);

		JButton monthly_JBtn = new JButton("Monthly Charts");
		monthly_JBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						POSFrame.loading=new Loading();
						JChart c = new JChart(JChart.OVERVIEW.MONTHLY);
						POSFrame.loading.dispose();
					}
				}).start();
			}
		});
		monthly_JBtn.setBounds(873, 475, 129, 78);
		add(monthly_JBtn);

		JButton dailycharts_JBtn = new JButton("Daily Charts");
		dailycharts_JBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						POSFrame.loading=new Loading();
						JChart c = new JChart(JChart.OVERVIEW.HOURLY);
						POSFrame.loading.dispose();
					}
				}).start();
			}
		});
		dailycharts_JBtn.setBounds(873, 297, 129, 78);
		add(dailycharts_JBtn);

		JButton admin = new JButton("<html>Admin<br>Settings</html>");
		admin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						POSFrame.loading=new Loading();
						AdminPanelDialog frame = new AdminPanelDialog();
						POSFrame.loading.dispose();
						frame.setVisible(true);
					}
				}).start();
			}
		});
		admin.setForeground(Color.RED);
		admin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		admin.setBounds(873, 652, 129, 78);
		add(admin);

		updateScreen();
	
	}

	private void updateScreen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Loading l =new Loading();
				sql.displayDailyGross(screen);
				l.dispose();
				l=null;
			}
		}).start();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(MainMenuPanel.img, 0, 0, null);
	}

	private void logOut() {

		this.setVisible(false);
		this.t.stop();
		POSFrame.frame.remove(this);
		MainMenuPanel.goBackToMainMenu();
		// POSFrame.frame.add(MainMenuPanel.MAIN_MENU_PANEL);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		this.dateLabel.setText(sdf.format(new Date()));

	}

	public void setSQL(SQL sql) {
		this.sql = sql;

	}
}
