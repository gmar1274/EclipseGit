package AdminPanelDialogs_JPanels;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import com.toedter.calendar.JDateChooser;
import Dialogs.AdminPanelDialog;
import SQLclass.SQL;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InventoryPanel extends JPanel {
	public JPanel			invSettingBtns;
	private JTextField		sku_tf;
	private JTextField		prod_name_tf;
	private JTextField		wholesaleprice_tf;
	private JTextField		retailprice_tf;
	private JTextField		restock_tf;
	private JTextPane		textArea_SCREEN;
	private JScrollPane		scrollPane;
	private JDateChooser	startdate_DATE;
	private JDateChooser	enddate_DATE;
	/**
	 * Create the panel.
	 */
	public InventoryPanel() {
		this.setBounds(0, 0, 495, 560);
		setLayout(null);
		this.setVisible(true);
		invSettingBtns = new JPanel();
		invSettingBtns.setBounds(10, 11, 195, 409);
		add(invSettingBtns);
		invSettingBtns.setLayout(new GridLayout(10, 1, 0, 5));
		JButton addNewProductBtn = new JButton("Add new inventory");
		addNewProductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				assumePreviousErrorsAreFixed();
				if (sku_tf.getText().length() <= 0){
					errorFillIn(sku_tf);
				} else if (prod_name_tf.getText().length() <= 0){
					errorFillIn(prod_name_tf);
				} else if (wholesaleprice_tf.getText().length() <= 0){
					errorFillIn(wholesaleprice_tf);
				} else if (retailprice_tf.getText().length() <= 0){
					errorFillIn(retailprice_tf);
				} else if (restock_tf.getText().length() <= 0){
					errorFillIn(restock_tf);
				} else{
					double wp, rp;
					long s;
					try{
						wp = Double.parseDouble(wholesaleprice_tf.getText());
					} catch (Exception e){
						errorFillIn(wholesaleprice_tf);
						return;
					}
					try{
						rp = Double.parseDouble(retailprice_tf.getText());
					} catch (Exception e){
						errorFillIn(retailprice_tf);
						return;
					}
					try{
						s = Long.parseLong(restock_tf.getText());
					} catch (Exception e){
						errorFillIn(restock_tf);
						return;
					}
					if (!isAdmin()){
						errorMsg();
					} else{
						if (!AdminPanelDialog.sql.addNewInventory(sku_tf.getText(), prod_name_tf.getText(), wp, rp, s)){
							JOptionPane.showMessageDialog(null,
									"ERROR. There must be a product with a similar SKU.\nNo data has been modified.");
							errorFillIn(sku_tf);
						} else{
							resetFields();
							successMsg("[" + sku_tf.getText() + "] has been successfully added.");
						}
					}
				}
			}
		});
		invSettingBtns.add(addNewProductBtn);
		invSettingBtns.setVisible(true);
		JButton deleteProductBtn = new JButton("Delete a product");
		deleteProductBtn.setForeground(Color.RED);
		deleteProductBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				assumePreviousErrorsAreFixed();
				if (sku_tf.getText().length() <= 0 || !AdminPanelDialog.sql.isProduct(sku_tf.getText())){
					JOptionPane.showMessageDialog(null, "No changes have been made.");
					errorFillIn(sku_tf);
				} else{
					int opt = JOptionPane.showConfirmDialog(null,
							"Warning this action cannot be undone.\nYou are attempting to delete [" + sku_tf.getText()
									+ "] from your database.\nDo you wish to continue?");
					if (opt != JOptionPane.YES_OPTION){
						JOptionPane.showMessageDialog(null, "No changes have been made.");
					} else if (!isAdmin()){
						JOptionPane.showMessageDialog(null, "No changes have been made.");
					} else{
						AdminPanelDialog.sql.deleteFromInvevntory(sku_tf.getText());
						JOptionPane.showMessageDialog(null, "[" + sku_tf.getText() + "] has been successfully deleted");
					}
				}
			}
		});
		invSettingBtns.add(deleteProductBtn);
		JButton updatePriceBtn = new JButton("Update product price");
		updatePriceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				assumePreviousErrorsAreFixed();
				if (sku_tf.getText().length() == 0 || !AdminPanelDialog.sql.isProduct(sku_tf.getText())){
					errorFillIn(sku_tf);
				} else{
					long wp, rp;
					try{
						wp = Long.parseLong(wholesaleprice_tf.getText());
					} catch (Exception e){
						errorFillIn(wholesaleprice_tf);
						errorMsg();
						return;
					}
					try{
						rp = Long.parseLong(retailprice_tf.getText());
					} catch (Exception e){
						errorFillIn(retailprice_tf);
						errorMsg();
						return;
					}
					if (!isAdmin()){
						errorMsg();
					} else{
						successMsg("[" + sku_tf.getText() + "] has been updated.");
					}
				}
			}
		});
		invSettingBtns.add(updatePriceBtn);
		JButton restockBtn = new JButton("Restock a product");
		restockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				assumePreviousErrorsAreFixed();
				if (sku_tf.getText().length() == 0 || !AdminPanelDialog.sql.isProduct(sku_tf.getText())){
					errorFillIn(sku_tf);
					errorMsg();
				} else{
					long rs;
					try{
						rs = Long.parseLong(restock_tf.getText());
					} catch (Exception e){
						errorFillIn(restock_tf);
						errorMsg();
						return;
					}
					if (!isAdmin()){
						errorMsg();
					} else{
						AdminPanelDialog.sql.restockProduct(sku_tf.getText(), rs);
						successMsg("[" + sku_tf.getText() + "] has been successfully restocked.");
					}
				}
			}
		});
		invSettingBtns.add(restockBtn);
		JButton prodInfoBtn = new JButton("View product information");
		prodInfoBtn.setForeground(Color.BLUE);
		prodInfoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				assumePreviousErrorsAreFixed();
				if (sku_tf.getText().length() != 0 && !AdminPanelDialog.sql.isProduct(sku_tf.getText())){
					errorFillIn(sku_tf);
					errorMsg();
				} else if (sku_tf.getText().length() == 0){
					AdminPanelDialog.sql.displayAllProductInfo(textArea_SCREEN);
				} else{
					AdminPanelDialog.sql.displayAllProductInfo(sku_tf.getText(), textArea_SCREEN);
				}
			}
		});
		invSettingBtns.add(prodInfoBtn);
		JButton transProdsBtn = new JButton("<html>View all transactions</html>");
		transProdsBtn.setForeground(Color.BLUE);
		transProdsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				assumePreviousErrorsAreFixed();
				if (startdate_DATE.getDate() != null && enddate_DATE.getDate() != null
						&& sku_tf.getText().length() != 0){
					// start end, on a sku
					String start =((JTextField)startdate_DATE.getDateEditor().getUiComponent()).getText();
					String end=((JTextField)enddate_DATE.getDateEditor().getUiComponent()).getText();
					
					AdminPanelDialog.sql.displayAllTransactionsOfProduct(start,end,sku_tf.getText(), textArea_SCREEN);
					
				} else if (startdate_DATE.getDate() != null && enddate_DATE.getDate() != null
						&& sku_tf.getText().length() == 0){
					// start end on all trans
					String start =((JTextField)startdate_DATE.getDateEditor().getUiComponent()).getText();
					String end=((JTextField)enddate_DATE.getDateEditor().getUiComponent()).getText();
					
					AdminPanelDialog.sql.displayAllTransactionsOfProduct(start,end, textArea_SCREEN);
					
				} else if (sku_tf.getText().length() == 0){
					// just show trans of all
					AdminPanelDialog.sql.displayAllTransactionsOfProduct(textArea_SCREEN);
				} else if (sku_tf.getText().length() > 0){
					AdminPanelDialog.sql.displayAllTransactionsOfProduct(sku_tf.getText(), textArea_SCREEN);
				}
			}
		});
		invSettingBtns.add(transProdsBtn);
		JButton expandBtn = new JButton("Expand screen");
		expandBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				JDialog f = new JDialog();
				f.setModal(true);
				f.setDefaultCloseOperation(f.DISPOSE_ON_CLOSE);
				f.setSize(850, 600);
				f.setLocationRelativeTo(null);
				JTextPane a = new JTextPane();
				a.setContentType("text/html");
				a.setText(textArea_SCREEN.getText());
				a.setEditable(false);
				JScrollPane p = new JScrollPane(a);
				f.getContentPane().add(p);
				f.setVisible(true);
			}
		});
		invSettingBtns.add(expandBtn);
		JLabel lblSku = new JLabel("SKU:");
		lblSku.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSku.setBounds(215, 11, 59, 21);
		add(lblSku);
		sku_tf = new JTextField();
		sku_tf.setBounds(338, 11, 152, 20);
		add(sku_tf);
		sku_tf.setColumns(10);
		JLabel lblProductName = new JLabel("Product Name:");
		lblProductName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblProductName.setBounds(215, 40, 98, 21);
		add(lblProductName);
		prod_name_tf = new JTextField();
		prod_name_tf.setColumns(10);
		prod_name_tf.setBounds(338, 41, 152, 20);
		add(prod_name_tf);
		JLabel lblwholesalePriceperUnit = new JLabel("<html>WholeSale price:<br>(per unit)<html>");
		lblwholesalePriceperUnit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblwholesalePriceperUnit.setBounds(215, 72, 98, 36);
		add(lblwholesalePriceperUnit);
		wholesaleprice_tf = new JTextField();
		wholesaleprice_tf.setColumns(10);
		wholesaleprice_tf.setBounds(338, 72, 152, 20);
		add(wholesaleprice_tf);
		JLabel lblretailPriceperUnit = new JLabel("<html>Retail price:<br>(per unit)<html>");
		lblretailPriceperUnit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblretailPriceperUnit.setBounds(215, 108, 98, 36);
		add(lblretailPriceperUnit);
		retailprice_tf = new JTextField();
		retailprice_tf.setColumns(10);
		retailprice_tf.setBounds(338, 109, 152, 20);
		add(retailprice_tf);
		JLabel lblRestock = new JLabel("Restock amount:");
		lblRestock.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRestock.setBounds(215, 145, 98, 21);
		add(lblRestock);
		restock_tf = new JTextField();
		restock_tf.setColumns(10);
		restock_tf.setBounds(338, 146, 152, 20);
		add(restock_tf);
		startdate_DATE = new JDateChooser();
		
		startdate_DATE.setDateFormatString("YYYY-MM-dd");
		startdate_DATE.setBounds(338, 177, 152, 20);
		add(startdate_DATE);
		enddate_DATE = new JDateChooser();
		enddate_DATE.setDateFormatString("YYYY-MM-dd");
		enddate_DATE.setBounds(338, 199, 152, 20);
		add(enddate_DATE);
		JLabel label = new JLabel("Start Date:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(215, 177, 122, 20);
		add(label);
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEndDate.setBounds(215, 199, 122, 20);
		add(lblEndDate);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(215, 230, 275, 319);
		add(scrollPane);
		textArea_SCREEN = new JTextPane();
		textArea_SCREEN.setContentType("text/html");
		textArea_SCREEN.setEditable(false);
		scrollPane.setViewportView(textArea_SCREEN);
	}
	private void errorMsg(){
		JOptionPane.showMessageDialog(null, "No changes have been made.");
	}
	private void assumePreviousErrorsAreFixed(){
		for (Component o : this.getComponents()){
			if (o instanceof JTextField){
				JTextField tf = (JTextField) o;
				tf.setBackground(Color.white);
			}
		}
	}
	private void resetFields(){
		for (Component o : this.getComponents()){
			if (o instanceof JTextField){
				JTextField tf = (JTextField) o;
				tf.setBackground(Color.white);
				tf.setText("");
			}
		}
	}
	private void errorFillIn(Object o){
		if (o instanceof JTextField){
			JTextField tf = (JTextField) o;
			tf.setBackground(Color.red);
		} else{
			JDateChooser dc = (JDateChooser) o;
			dc.setBackground(Color.red);
		}
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
