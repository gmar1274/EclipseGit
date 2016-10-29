package AdminPanelDialogs_JPanels;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import com.toedter.calendar.JDateChooser;
import Dialogs.AdminPanelDialog;
import Dialogs.FixEmployeeClockIn;
import Dialogs.Loading;
import MainFrame.POSFrame;

import javax.swing.JScrollPane;

public class EmployeePanel extends JPanel {
	public JPanel empSettingBtns;
	private JTextField emp_fn_tf;
	private JTextField emp_mn_tf;
	private JTextField id_tf;
	private JTextField commission_tf;
	public SchedulePanel schedule;
	private JTextPane screen;
	private JTextField emp_ln_tf;
	private JScrollPane scrollPane;
	private JDateChooser startdate_DATE;
	private JDateChooser enddate_DATE;
	private JTextField rent_tf;
	private JTextField email_tf;
	private JTextField phone_tf;
	private JTextField ssn_tf;
	private JTextField salary_tf;
	private JTextField hourly_tf;

	/**
	 * Create the panel.
	 */
	public EmployeePanel() {
		schedule = new SchedulePanel(this);
		schedule.setVisible(false);
		this.setBounds(0, 0, 500, 560);
		setLayout(null);
		this.setVisible(true);
		empSettingBtns = new JPanel();
		empSettingBtns.setBounds(10, 11, 206, 440);
		add(empSettingBtns);
		empSettingBtns.setLayout(new GridLayout(10, 1, 0, 5));
		JButton btnAddNewEmployee = new JButton("Add new employee");
		btnAddNewEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				assumePreviousErrorsAreFixed();
				if (id_tf.getText().length() == 0 || AdminPanelDialog.sql.isEmployee(id_tf.getText())) {
					errorFillIn(id_tf);
					errorMsg();
				} else if (emp_fn_tf.getText().length() == 0) {
					errorFillIn(emp_fn_tf);
					errorMsg();
				} else if (emp_ln_tf.getText().length() == 0) {
					errorFillIn(emp_ln_tf);
					errorMsg();
				} else if (isPayFieldsEmpty()) {
					//
				} else if (ssn_tf.getText().length() != 9) {
					errorFillIn(ssn_tf);
					JOptionPane.showMessageDialog(null, "Input only numbers.");
					errorMsg();
				} else {
					try {
						if (!isAdmin()) errorMsg();
						else {
							double comm = 0;
							if (hourly_tf.getText().length() == 0) {
								hourly_tf.setText("0");
							}
							if (salary_tf.getText().length() == 0) {
								salary_tf.setText("0");
							}
							if (commission_tf.getText().length() == 0) {
								commission_tf.setText("0");
							} else if (commission_tf.getText().contains(".")) {
								comm = Double.parseDouble(commission_tf.getText());
							} else {
								comm = Double.parseDouble(commission_tf.getText()) / 100.;
								if (comm > 1) {
									errorFillIn(commission_tf);
									JOptionPane.showMessageDialog(null, "Commission cannot be larger than 1.");
									errorMsg();
									return;
								}
							}
							if (rent_tf.getText().length() == 0) {
								rent_tf.setText("0");
							}
							AdminPanelDialog.sql.addNewEmployee(id_tf.getText(), emp_fn_tf.getText(), emp_mn_tf.getText(), emp_ln_tf.getText(), email_tf.getText(), phone_tf.getText(),
							ssn_tf.getText(), new BigDecimal(salary_tf.getText()), new BigDecimal(hourly_tf.getText()), comm, Double.parseDouble(rent_tf.getText()));
							successMsg("Employee with ID of [" + id_tf.getText() + "] has been successfully added.");
							assumePreviousErrorsAreFixed();
						}
					} catch (Exception E) {
						JOptionPane.showMessageDialog(null, "Input numeric values only.");
						errorMsg();
					}
				}
			}
		});
		empSettingBtns.add(btnAddNewEmployee);
		empSettingBtns.setVisible(true);
		JButton btnDeleteEmployee = new JButton("Delete employee");
		btnDeleteEmployee.setForeground(Color.RED);
		btnDeleteEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				assumePreviousErrorsAreFixed();
				String start = ((JTextField) startdate_DATE.getDateEditor().getUiComponent()).getText();
				String end = ((JTextField) enddate_DATE.getDateEditor().getUiComponent()).getText();

				if (id_tf.getText().length() == 0 || !AdminPanelDialog.sql.isEmployee(id_tf.getText())) {
					errorFillIn(id_tf);
					errorMsg();
				} else {
					if (!isAdmin()) {
						errorMsg();
					} else {
						AdminPanelDialog.sql.deleteEmployee(id_tf.getText());
						successMsg("Employee ID of [" + id_tf.getText() + "] has been successfully removed.");
					}
				}
			}
		});
		empSettingBtns.add(btnDeleteEmployee);
		JButton btnChangeEmployeePay = new JButton("<html>Update employee<br>pay/commission<html>");
		btnChangeEmployeePay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assumePreviousErrorsAreFixed();
				if (id_tf.getText().length() == 0 || !AdminPanelDialog.sql.isEmployee(id_tf.getText())) {
					errorFillIn(id_tf);
					errorMsg();
				} else if (isPayFieldsEmpty()) {
					//
				} else {
					double comm = 0;
					try {
						if (hourly_tf.getText().length() == 0) {
							hourly_tf.setText("0");
						}
						if (salary_tf.getText().length() == 0) {
							salary_tf.setText("0");
						}
						if (commission_tf.getText().length() == 0) {
							commission_tf.setText("0");
						} else if (commission_tf.getText().contains(".")) {
							comm = Double.parseDouble(commission_tf.getText());
						} else {
							comm = Double.parseDouble(commission_tf.getText()) / 100.;
							if (comm > 1) {
								errorFillIn(commission_tf);
								JOptionPane.showMessageDialog(null, "Commission cannot be larger than 1.");
								errorMsg();
								return;
							}
						}
						if (rent_tf.getText().length() == 0) {
							rent_tf.setText("0");
						}
					} catch (Exception ee) {
						errorMsg();
						return;
					}
					if (!isAdmin()) {
						errorMsg();
					} else {
						AdminPanelDialog.sql.updatePayEmployee(id_tf.getText(), new BigDecimal(salary_tf.getText()), new BigDecimal(hourly_tf.getText()), comm, Double.parseDouble(rent_tf.getText()));
						successMsg("Employee ID of [" + id_tf.getText() + "] has been successfully updated.");
					}
				}
			}
		});
		empSettingBtns.add(btnChangeEmployeePay);
		JButton btnViewEmployeeInformation = new JButton("View employee information");
		btnViewEmployeeInformation.setForeground(Color.BLUE);
		btnViewEmployeeInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String id =id_tf.getText(); 
				if (id.length() > 0) {
					AdminPanelDialog.sql.displayAllEmployeeInfo(screen,id);
				} else {
					AdminPanelDialog.sql.displayAllEmployeeInfo(screen,null);
				}
			}
		});
		empSettingBtns.add(btnViewEmployeeInformation);
		JButton btnViewAllTransactions = new JButton("View employee earnings");
		btnViewAllTransactions.setForeground(Color.BLUE);
		btnViewAllTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						String start = ((JTextField) startdate_DATE.getDateEditor().getUiComponent()).getText();
						String end = ((JTextField) enddate_DATE.getDateEditor().getUiComponent()).getText();
						POSFrame.loading = new Loading();
						if (id_tf.getText().length() == 0) {
							if (start.length() == 0 || end.length() == 0) start = end = null;
							AdminPanelDialog.sql.displayAllTransactionsOfEmployee(screen, start, end);
						} else {
							if (start.length() == 0 || end.length() == 0) start = end = null;

							AdminPanelDialog.sql.displayAllTransactionsOfEmployee(id_tf.getText(), screen, start, end);
						}
						POSFrame.loading.dispose();
					}
				}).start();

			}
		});
		empSettingBtns.add(btnViewAllTransactions);
		JButton btnViewLogOf = new JButton("View log of cash register");
		btnViewLogOf.setForeground(Color.BLUE);
		btnViewLogOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String start = ((JTextField) startdate_DATE.getDateEditor().getUiComponent()).getText();
				String end = ((JTextField) enddate_DATE.getDateEditor().getUiComponent()).getText();
				if (id_tf.getText().length() == 0 && start.length() == 0) {// display all
					start = end = null;
					AdminPanelDialog.sql.displayLOGCashRegister(screen, start, end);
				} else if (id_tf.getText().length() == 0 && start.length() > 0 && end.length() > 0) {
					AdminPanelDialog.sql.displayLOGCashRegister(screen, start, end);
				} else {
					if (start.length() == 0) {
						start = end = null;
					}
					AdminPanelDialog.sql.displayLOGCashRegister(id_tf.getText(), screen, start, end);
				}

			}
		});
		empSettingBtns.add(btnViewLogOf);
		JButton changeTimeClockBtn = new JButton("<html>Fix employee<br>clock in/out time<html>");
		changeTimeClockBtn.setEnabled(false);
		changeTimeClockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String start = ((JTextField) startdate_DATE.getDateEditor().getUiComponent()).getText();

				assumePreviousErrorsAreFixed();
				if (id_tf.getText().length() == 0 || !AdminPanelDialog.sql.isEmployee(id_tf.getText())) {
					errorFillIn(id_tf);
					errorMsg();
				} else if (start.length() == 0) {
					JOptionPane.showMessageDialog(null, "Enter a 'Start Date'.", "Error", JOptionPane.ERROR_MESSAGE);
					errorFillIn(startdate_DATE);
					errorMsg();
				} else if (!AdminPanelDialog.sql.wasWorkingOnDate(id_tf.getText(), start)) {
					JOptionPane.showMessageDialog(null, "No record of employee working on [" + start + "].", "Error", JOptionPane.ERROR_MESSAGE);
					errorFillIn(startdate_DATE);
					errorMsg();
				} else {

					FixEmployeeClockIn f = new FixEmployeeClockIn(id_tf.getText(), start);

				}
			}
		});
		empSettingBtns.add(changeTimeClockBtn);
		JButton viewScheduleBtn = new JButton("View/Create weekly schedule");
		viewScheduleBtn.setEnabled(false);
		viewScheduleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);/// open up new panel
				schedule.setVisible(true);
			}
		});
		empSettingBtns.add(viewScheduleBtn);
		JButton expandBtn = new JButton("Expand Screen");
		expandBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog f = new JDialog();
				f.setModal(true);
				f.setDefaultCloseOperation(f.DISPOSE_ON_CLOSE);
				f.setSize(850, 600);
				f.setLocationRelativeTo(null);
				JTextPane a = new JTextPane();
				a.setContentType("text/html");
				a.setText(screen.getText());
				a.setEditable(false);
				JScrollPane p = new JScrollPane(a);
				f.getContentPane().add(p);
				f.setVisible(true);
			}
		});
		empSettingBtns.add(expandBtn);
		JLabel lblEmployeeFirstName = new JLabel("Employee First Name:");
		lblEmployeeFirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmployeeFirstName.setBounds(226, 11, 122, 33);
		add(lblEmployeeFirstName);
		JLabel lblEmployeeLastName = new JLabel("Employee Last Name:");
		lblEmployeeLastName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmployeeLastName.setBounds(226, 55, 122, 33);
		add(lblEmployeeLastName);
		JLabel lblEmployeeId = new JLabel("Employee ID: ");
		lblEmployeeId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmployeeId.setBounds(226, 91, 122, 20);
		add(lblEmployeeId);
		emp_fn_tf = new JTextField();
		emp_fn_tf.setBounds(358, 18, 132, 20);
		add(emp_fn_tf);
		emp_fn_tf.setColumns(10);
		emp_mn_tf = new JTextField();
		emp_mn_tf.setColumns(10);
		emp_mn_tf.setBounds(358, 42, 132, 20);
		add(emp_mn_tf);
		id_tf = new JTextField();
		id_tf.setColumns(10);
		id_tf.setBounds(358, 92, 132, 20);
		add(id_tf);
		JLabel lblUpdatePaycommission = new JLabel("<html>Update<br>Pay/Commission:</html>");
		lblUpdatePaycommission.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUpdatePaycommission.setBounds(226, 143, 122, 33);
		add(lblUpdatePaycommission);
		commission_tf = new JTextField();
		commission_tf.setBounds(358, 156, 132, 20);
		add(commission_tf);
		commission_tf.setColumns(10);
		startdate_DATE = new JDateChooser();
		startdate_DATE.setDateFormatString("YYYY-MM-dd");
		startdate_DATE.setBounds(358, 323, 132, 20);
		add(startdate_DATE);
		enddate_DATE = new JDateChooser();
		enddate_DATE.setDateFormatString("YYYY-MM-dd");
		enddate_DATE.setBounds(358, 342, 132, 20);
		add(enddate_DATE);
		JLabel lblStartDate = new JLabel("Start Date:");
		lblStartDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStartDate.setBounds(226, 323, 122, 20);
		add(lblStartDate);
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEndDate.setBounds(226, 342, 122, 20);
		add(lblEndDate);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(226, 373, 264, 176);
		add(scrollPane);
		screen = new JTextPane();
		scrollPane.setViewportView(screen);
		screen.setEditable(false);
		screen.setContentType("text/html");
		JLabel lblEmployeeMiddleName = new JLabel("Middle Name:");
		lblEmployeeMiddleName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmployeeMiddleName.setBounds(226, 35, 132, 33);
		add(lblEmployeeMiddleName);
		emp_ln_tf = new JTextField();
		emp_ln_tf.setColumns(10);
		emp_ln_tf.setBounds(358, 62, 132, 20);
		add(emp_ln_tf);
		JLabel lblRent = new JLabel("Monthly Rent:");
		lblRent.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRent.setBounds(226, 187, 122, 20);
		add(lblRent);
		rent_tf = new JTextField();
		rent_tf.setColumns(10);
		rent_tf.setBounds(358, 188, 132, 20);
		add(rent_tf);
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(226, 256, 122, 20);
		add(lblEmail);
		email_tf = new JTextField();
		email_tf.setColumns(10);
		email_tf.setBounds(358, 257, 132, 20);
		add(email_tf);
		phone_tf = new JTextField();
		phone_tf.setColumns(10);
		phone_tf.setBounds(358, 281, 132, 20);
		add(phone_tf);
		JLabel lblPhone = new JLabel("Phone #:");
		lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPhone.setBounds(226, 280, 122, 20);
		add(lblPhone);
		JLabel lblSsn = new JLabel("SSN:");
		lblSsn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSsn.setBounds(226, 299, 122, 20);
		add(lblSsn);
		ssn_tf = new JTextField();
		ssn_tf.setColumns(10);
		ssn_tf.setBounds(358, 300, 132, 20);
		add(ssn_tf);
		JLabel lblSalary = new JLabel("Salary:");
		lblSalary.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSalary.setBounds(226, 122, 122, 20);
		add(lblSalary);
		salary_tf = new JTextField();
		salary_tf.setColumns(10);
		salary_tf.setBounds(358, 123, 132, 20);
		add(salary_tf);
		JLabel lblHourlyPay = new JLabel("Hourly Pay:");
		lblHourlyPay.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHourlyPay.setBounds(226, 210, 122, 20);
		add(lblHourlyPay);
		hourly_tf = new JTextField();
		hourly_tf.setColumns(10);
		hourly_tf.setBounds(358, 211, 132, 20);
		add(hourly_tf);
	}

	private void errorMsg() {
		JOptionPane.showMessageDialog(null, "No changes have been made.", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void assumePreviousErrorsAreFixed() {
		for (Component o : this.getComponents()) {
			if (o instanceof JTextField) {
				JTextField tf = (JTextField) o;
				tf.setBackground(Color.white);
			}
		}
	}

	private void resetFields() {
		for (Component o : this.getComponents()) {
			if (o instanceof JTextField) {
				JTextField tf = (JTextField) o;
				tf.setBackground(Color.white);
				tf.setText("");
			}
		}
	}

	private void errorFillIn(Object o) {
		if (o instanceof JTextField) {
			JTextField tf = (JTextField) o;
			tf.setBackground(Color.red);
		} else {

			JDateChooser dc = (JDateChooser) o;

			dc.setBackground(Color.red);
		}
	}

	private boolean isAdmin() {
		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (okCxl == JOptionPane.OK_OPTION) {
			String admin = new String(pf.getPassword());
			return AdminPanelDialog.sql.isAdmin(admin);
		} else return false;
	}

	private void successMsg(String msg) {
		if (msg == null) msg = "";
		JOptionPane.showMessageDialog(null, "Success. " + msg);
	}

	private boolean isPayFieldsEmpty() {
		if (rent_tf.getText().length() == 0 && commission_tf.getText().length() == 0 && hourly_tf.getText().length() == 0 && salary_tf.getText().length() == 0) {
			errorFillIn(rent_tf);
			errorFillIn(commission_tf);
			errorFillIn(hourly_tf);
			errorFillIn(salary_tf);
			// choose one
			JOptionPane.showMessageDialog(null, "Choose either commission, salary, hourly pay, or monthly rent.");// choose
			errorMsg();
			return true;
		} else return false;
	}
}
