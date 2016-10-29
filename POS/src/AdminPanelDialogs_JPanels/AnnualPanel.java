package AdminPanelDialogs_JPanels;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.toedter.calendar.JDateChooser;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AnnualPanel extends JPanel {
	private JTextArea textArea;
	/**
	 * Create the panel.
	 */
	public AnnualPanel() {
		this.setBounds(0, 0, 500, 560);
		setLayout(null);
		JDateChooser lowerDate = new JDateChooser();
		lowerDate.setDateFormatString("YYYY-MM-dd");
		lowerDate.setBounds(104, 8, 151, 20);
		this.add(lowerDate);
		JLabel lblStartDate = new JLabel("Start Date:");
		lblStartDate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStartDate.setBounds(10, 6, 91, 22);
		add(lblStartDate);
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblEndDate.setBounds(10, 39, 91, 22);
		add(lblEndDate);
		JDateChooser upperDate = new JDateChooser();
		upperDate.setDateFormatString("YYYY-MM-dd");
		upperDate.setBounds(104, 39, 151, 20);
		add(upperDate);
		JButton btnNewButton = new JButton("View financial statements");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(292, 9, 198, 52);
		add(btnNewButton);
		JCheckBox chckbxSearchForJust = new JCheckBox("Search for just a specified date");
		chckbxSearchForJust.setBounds(4, 68, 210, 23);
		chckbxSearchForJust.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if (chckbxSearchForJust.isSelected()){
					upperDate.setVisible(false);
				} else{
					upperDate.setVisible(true);
				}
			}
		});
		add(chckbxSearchForJust);
		this.setVisible(true);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 98, 480, 426);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 98, 480, 400);
		add(scrollPane);
	}
}
