package Dialogs;

/***
 * This class imports JPanels as a sepearte object to modify code.
 **/
import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import AdminPanelDialogs_JPanels.*;
import MainFrame.POSFrame;
import SQLclass.SQL;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

public class AdminPanelDialog extends JDialog implements ActionListener {
	private JPanel mainPanel;
	private JPanel mainSettingsPanel;
	private JButton employeeSettingsBtn;
	private JButton inventorySettingsBtn;
	private JButton couponSettingsBtn;
	private JButton annualReportBtn;
	private JButton adminSettingsBtn;
	private JPanel inventoryPanel;
	private JPanel couponPanel;
	private JPanel annualPanel;
	private JPanel adminPanel;
	private JButton backInvBtn;
	private JButton coupBtn;
	private JButton annualBtn;
	private JButton adminBtn;
	private EmployeePanel employeePanel;
	private JButton backEmpBtn;
	public static SQL sql;
	private JButton btnExit;
	private boolean contains;
public static void main(String[]a){
	new AdminPanelDialog().setVisible(true);
}

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public AdminPanelDialog() {
		this.contains=false;
		
		this.sql = POSFrame.SQL;
		if(this.sql==null){
			this.sql=new SQL();
		}
		setModal(true);
		setTitle("Admin Panel");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 510, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
		mainSettingsPanel = new JPanel();
		mainSettingsPanel.setBounds(0, 0, 510, 451);
		mainPanel.add(mainSettingsPanel);
		mainSettingsPanel.setLayout(null);
		JPanel buttonGridPanel = new JPanel();
		buttonGridPanel.setBounds(162, 11, 168, 344);
		mainSettingsPanel.add(buttonGridPanel);
		buttonGridPanel.setLayout(new GridLayout(5, 1, 10, 10));
		employeeSettingsBtn = new JButton("Employee Settings");
		employeeSettingsBtn.setFont(new Font("Cambria Math", Font.BOLD, 12));
		employeeSettingsBtn.addActionListener(this);
		buttonGridPanel.add(employeeSettingsBtn);
		inventorySettingsBtn = new JButton("Inventory Settings");
		inventorySettingsBtn.setFont(new Font("Cambria Math", Font.BOLD, 12));
		inventorySettingsBtn.addActionListener(this);
		buttonGridPanel.add(inventorySettingsBtn);
		couponSettingsBtn = new JButton("Coupon Settings");
		couponSettingsBtn.setFont(new Font("Cambria Math", Font.BOLD, 12));
		couponSettingsBtn.addActionListener(this);
		buttonGridPanel.add(couponSettingsBtn);
		annualReportBtn = new JButton("Annual Report Status");
		annualReportBtn.setFont(new Font("Cambria Math", Font.BOLD, 12));
		annualReportBtn.addActionListener(this);
		buttonGridPanel.add(annualReportBtn);
		adminSettingsBtn = new JButton("Admin Settings");
		adminSettingsBtn.setFont(new Font("Cambria Math", Font.BOLD, 12));
		adminSettingsBtn.addActionListener(this);
		buttonGridPanel.add(adminSettingsBtn);
		btnExit = new JButton("Exit");
		btnExit.setForeground(Color.RED);
		btnExit.setFont(new Font("Cambria Math", Font.BOLD, 12));
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnExit.setBounds(162, 364, 168, 60);
		mainSettingsPanel.add(btnExit);

		JButton btn_ReservationSettings = new JButton("<html>Reservation<br/>Settings</html>");
		btn_ReservationSettings.addActionListener(new ActionListener() {
		

			public void actionPerformed(ActionEvent e) {
				goToReservationPanel();
			}
		});
		btn_ReservationSettings.setFont(new Font("Cambria Math", Font.BOLD, 12));
		btn_ReservationSettings.setBounds(357, 11, 120, 60);
		mainSettingsPanel.add(btn_ReservationSettings);
		//////////////////////////////////////////////////////////////
		employeePanel = new EmployeePanel();
		/// this.mainPanel.add(employeePanel.schedule);
		employeePanel.setVisible(false);
		backEmpBtn = new JButton("Go Back");
		backEmpBtn.setBounds(0, 500, 100, 60);
		backEmpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				employeePanel.setVisible(false);
				mainSettingsPanel.setVisible(true);
			}
		});
		this.backEmpBtn.setVisible(true);
		this.employeePanel.add(backEmpBtn);
		this.mainPanel.add(employeePanel);
		inventoryPanel = new InventoryPanel();
		inventoryPanel.setVisible(false);
		inventoryPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		mainPanel.add(inventoryPanel);
		inventoryPanel.setLayout(null);
		backInvBtn = new JButton("Go Back");
		backInvBtn.setFont(new Font("Cambria Math", Font.BOLD, 12));
		backInvBtn.setBounds(0, 500, 100, 60);
		backInvBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inventoryPanel.setVisible(false);
				mainSettingsPanel.setVisible(true);
			}
		});
		inventoryPanel.add(backInvBtn);
		couponPanel = new CouponPanel();
		couponPanel.setVisible(false);
		couponPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		mainPanel.add(couponPanel);
		couponPanel.setLayout(null);
		coupBtn = new JButton("Go Back");
		coupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				couponPanel.setVisible(false);
				mainSettingsPanel.setVisible(true);
			}
		});
		coupBtn.setBounds(0, 500, 100, 60);
		couponPanel.add(coupBtn);
		annualPanel = new AnnualPanel();
		annualPanel.setVisible(false);
		annualPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		mainPanel.add(annualPanel);
		annualPanel.setLayout(null);
		annualBtn = new JButton("Go Back");
		annualBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				annualPanel.setVisible(false);
				mainSettingsPanel.setVisible(true);
			}
		});
		annualBtn.setBounds(0, 500, 100, 60);
		annualPanel.add(annualBtn);
		adminPanel = new AdminCPanel();
		adminPanel.setVisible(false);
		adminPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		mainPanel.add(adminPanel);
		adminPanel.setLayout(null);
		adminBtn = new JButton("Go Back");
		adminBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminPanel.setVisible(false);
				mainSettingsPanel.setVisible(true);
			}
		});
		adminBtn.setBounds(0, 500, 100, 60);
		adminPanel.add(adminBtn);
		//////////////////////////////////////////////////////////////////////////////////// end
		//////////////////////////////////////////////////////////////////////////////////// panels
	}

	protected void goToReservationPanel() {
		ReservationSettingsPanel rsp = new ReservationSettingsPanel(this.mainSettingsPanel);
		rsp.setVisible(true);
		if(contains){
			getContentPane().remove(rsp);
			contains=false;
		}
		getContentPane().add(rsp);
		contains=true;
		
		this.repaint();
		this.getContentPane().repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == employeeSettingsBtn) {
			this.mainSettingsPanel.setVisible(false);
			employeePanel.setVisible(true);
		} else if (e.getSource() == inventorySettingsBtn) {
			this.mainSettingsPanel.setVisible(false);
			inventoryPanel.setVisible(true);
		} else if (e.getSource() == couponSettingsBtn) {
			this.mainSettingsPanel.setVisible(false);
			couponPanel.setVisible(true);
		} else if (e.getSource() == annualReportBtn) {
			this.mainSettingsPanel.setVisible(false);
			annualPanel.setVisible(true);
		} else if (e.getSource() == adminSettingsBtn) {
			this.mainSettingsPanel.setVisible(false);
			adminPanel.setVisible(true);
		} else {
		}
	}
}
