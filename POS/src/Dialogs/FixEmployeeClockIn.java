package Dialogs;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FixEmployeeClockIn extends JDialog {
	private JTextField	textField;
	private String		id;
	private JComboBox	comboBox_hour;
	private JComboBox	comboBox_min;
	private JComboBox	comboBox_ampm;
	private JComboBox	comboBox_endhour;
	private JComboBox	comboBox_endmin;
	private JComboBox	comboBox_endampm;
	private String		date;
	/**
	 * Create the dialog.
	 */
	public FixEmployeeClockIn(String id, String date) {
		this.id = id;
		this.date = date;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(375, 280);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setVisible(true);
		JLabel lblEmployeeId = new JLabel("Employee ID:");
		lblEmployeeId.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEmployeeId.setBounds(10, 11, 82, 14);
		getContentPane().add(lblEmployeeId);
		JLabel lblDate = new JLabel("Date:");
		lblDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDate.setBounds(10, 36, 82, 14);
		getContentPane().add(lblDate);
		JLabel lblStartTime = new JLabel("Start Time:");
		lblStartTime.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStartTime.setBounds(10, 61, 82, 14);
		getContentPane().add(lblStartTime);
		JLabel lblEndTime = new JLabel("End Time:");
		lblEndTime.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEndTime.setBounds(10, 86, 82, 14);
		getContentPane().add(lblEndTime);
		textField = new JTextField();
		textField.setText(id);
		textField.setEditable(false);
		textField.setBounds(102, 8, 173, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		/////////////////////////////////////////////
		DefaultComboBoxModel model_hour = new DefaultComboBoxModel();
		DefaultComboBoxModel model_min = new DefaultComboBoxModel();
		DefaultComboBoxModel model_hour2 = new DefaultComboBoxModel();
		DefaultComboBoxModel model_min2 = new DefaultComboBoxModel();
		for (int i = 0; i <= 59; ++i){
			if (i < 10){
				model_hour.addElement("0" + i);
				model_hour2.addElement("0" + i);
				model_min.addElement("0" + i);
				model_min2.addElement("0" + i);
			} else if (i <= 12){
				model_hour.addElement( i);
				model_hour2.addElement(i);
				model_min.addElement(i);
				model_min2.addElement(i);
			}else{
			model_min.addElement(i);
			model_min2.addElement(i);
			}
		}
		DefaultComboBoxModel ampm = new DefaultComboBoxModel();
		ampm.addElement("AM");
		ampm.addElement("PM");
		DefaultComboBoxModel ampm2 = new DefaultComboBoxModel();
		ampm2.addElement("AM");
		ampm2.addElement("PM");
		comboBox_hour = new JComboBox(model_hour);
		comboBox_hour.setBounds(102, 58, 51, 20);
		getContentPane().add(comboBox_hour);
		comboBox_min = new JComboBox(model_min);
		comboBox_min.setBounds(163, 58, 51, 20);
		getContentPane().add(comboBox_min);
		comboBox_ampm = new JComboBox(ampm);
		comboBox_ampm.setBounds(224, 58, 51, 20);
		getContentPane().add(comboBox_ampm);
		comboBox_endhour = new JComboBox(model_hour2);
		comboBox_endhour.setBounds(102, 83, 51, 20);
		getContentPane().add(comboBox_endhour);
		comboBox_endmin = new JComboBox(model_min2);
		comboBox_endmin.setBounds(163, 83, 51, 20);
		getContentPane().add(comboBox_endmin);
		comboBox_endampm = new JComboBox(ampm2);
		comboBox_endampm.setBounds(224, 83, 51, 20);
		getContentPane().add(comboBox_endampm);
		JButton btnMakeChange = new JButton("Make Change");
		btnMakeChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				int h = Integer.parseInt(comboBox_hour.getSelectedItem().toString());
				int h2 = Integer.parseInt(comboBox_endhour.getSelectedItem().toString());
				if ( h== 0 ||h2 == 0){
					JOptionPane.showMessageDialog(null, "Both times need to be corrected.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else{
					
					int m = Integer.parseInt(comboBox_min.getSelectedItem().toString());
					int m2 = Integer.parseInt(comboBox_endmin.getSelectedItem().toString());
//					if (h2 <= h && m2 <= m){
//						JOptionPane.showMessageDialog(null,
//								"Cannot have the END TIME be less than or equal to the START TIME.\nBoth times need to be corrected.",
//								"Error", JOptionPane.ERROR_MESSAGE);
//					} else 
						if (!isAdmin()){
						JOptionPane.showMessageDialog(null, "Password invalid. No changes have been made.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else{
						String starttime = comboBox_hour.getSelectedItem().toString() + ":"
								+ comboBox_min.getSelectedItem().toString() + " "
								+ comboBox_ampm.getSelectedItem().toString();
						String endtime = comboBox_endhour.getSelectedItem().toString() + ":"
								+ comboBox_endmin.getSelectedItem().toString() + " "
								+ comboBox_endampm.getSelectedItem().toString();
						AdminPanelDialog.sql.updateTimeClockIn(id, date, starttime, endtime);
						successMsg("Time has been successfully updated.");
					}
				}
			}
		});
		btnMakeChange.setBounds(10, 167, 125, 63);
		getContentPane().add(btnMakeChange);
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				dispose();
			}
		});
		btnExit.setBounds(224, 167, 125, 63);
		getContentPane().add(btnExit);
	}
	private boolean isAdmin(){
		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (okCxl == JOptionPane.OK_OPTION){
			String admin = new String(pf.getPassword());
			return AdminPanelDialog.sql.isAdmin(admin);
		} else return false;
	}
	private void successMsg(String msg){
		if (msg == null) msg = "";
		JOptionPane.showMessageDialog(null, "Success. " + msg);
	}
}
