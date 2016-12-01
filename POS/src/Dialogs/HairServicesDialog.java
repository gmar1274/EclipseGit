package Dialogs;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import POSObjects.HairService;
import SQLclass.SQL;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;

public class HairServicesDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	
	private JScrollPane scrollPane;
	private SQL sql;

	/**
	 * Create the dialog.
	 */
	public HairServicesDialog(SQL sql) {
		this.setResizable(false);
		this.sql=sql;
		setTitle("Hair Services");
		this.setSize(500, 600);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setModal(true);

		JLabel lblName = new JLabel("Name");
		lblName.setForeground(Color.RED);
		lblName.setFont(new Font("Cambria Math", Font.BOLD, 20));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(10, 11, 121, 50);
		getContentPane().add(lblName);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setForeground(Color.RED);
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setFont(new Font("Cambria Math", Font.BOLD, 20));
		lblPrice.setBounds(185, 11, 121, 50);
		getContentPane().add(lblPrice);

		JLabel lblEstimatedDuration = new JLabel("<html>Estimated<br/>Duration</html>");
		lblEstimatedDuration.setForeground(Color.RED);
		lblEstimatedDuration.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstimatedDuration.setFont(new Font("Cambria Math", Font.BOLD, 20));
		lblEstimatedDuration.setBounds(353, 11, 121, 50);
		getContentPane().add(lblEstimatedDuration);

		 scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 72, 464, 437);
		getContentPane().add(scrollPane);

		JButton btnNewButton = new JButton("Exit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// null dialog
			}
		});
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setFont(new Font("Cambria Math", Font.BOLD, 16));
		btnNewButton.setBounds(291, 520, 183, 30);
		getContentPane().add(btnNewButton);
		this.displayServices();
	}
	private void displayServices(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				JTextPane tp = new JTextPane();
				tp.setEditable(false);
				tp.setContentType("text/html");
				tp.setVisible(true);
				String output="<html><table width='450' cellspacing='1' border='1'>";
				DecimalFormat df = new DecimalFormat("$ #,##0.00");
				for(int id : sql.getLoadedHairServices().keySet()){
					HairService hs = sql.getLoadedHairServices().get(id);
					output+= "<tr><td>"+hs.getName()+"</td><td>"+df.format(hs.getPrice().doubleValue())+"</td><td>"+hs.getDuration()+"</td></tr>";
				}
				output+="</table></html>";
				tp.setText(output);
				scrollPane.setViewportView(tp);
				scrollPane.setVisible(true);
				repaint();
				// TODO Auto-generated method stub
				
			}
			
		}).start();
	}
}
