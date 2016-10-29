package POS_Panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import MainFrame.POSFrame;
import POSObjects.CombinedSale;
import POSObjects.Coupon;
import POSObjects.Haircut;
import POSObjects.Product;
import POSObjects.Stylist;
import ReservationScreenCustomerLogin.NumberPad;
import SQLclass.SQL;
import javax.swing.border.LineBorder;
import Dialogs.CouponDialog;
import Dialogs.CreditCardDialog;
import Dialogs.MiscDialog;
import Dialogs.NumberPadPOT;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import CashRegister.OpenCashRegister;

public class PointOfTransaction extends JPanel implements ActionListener {
	private long customer_id;
	private JLabel lblPrice;
	private JList jlist_price;
	private JList jlist_quantity;
	private JButton btn_tender;
	private JButton cancelBtn;
	public JTextPane finalSaleTA;
	private String saleText;
	private double product;
	private double tax;
	private DecimalFormat df;
	private JList jlist_prodnames;
	private JButton miscBtn;
	private JLabel labelStylist;
	private JFrame frame;
	private HashMap<String, Product> products;
	private SQL sql;
	private HashMap<String, Stylist> stylists;
	private JList jlist_stylists;
	private JList jlist_haircut;
	private HashMap<String, Haircut> haircuts;
	private JLabel jlabel_date;
	private SimpleDateFormat sdf;
	private ArrayList<Stylist> arrayList_stylists;
	private ArrayList<Product> arrayList_product;
	private ArrayList<Haircut> arrayList_haircuts;
	private ArrayList<CombinedSale> arrayList_combinedsale;
	private Timer t;
	private HashMap<String, Coupon> coupons;
	private JTextPane table;
	private ArrayList<Coupon> arrayList_coupons;
	private String orderString;
	private String stylistString;
	private String haircutString;
	private String couponString;
	private JPanel viewPanel;
	private AbstractButton addProductBtn;
	private String stringBasket;
	private String stringSaleTextPanel;
	private double totalAmount;
	private JButton btn_delete;
	private JButton clearBtn;
	private Stylist current_stylist;
	public static PointOfTransaction pot;
	private double temp_total_amount;

	/**
	 * Create the panel.
	 */
	public PointOfTransaction(JFrame f) {
		temp_total_amount=0;
		pot = this;
		this.totalAmount = 0;
		df = new DecimalFormat("$###,##0.00");
		this.sql = POSFrame.SQL;
		this.frame = f;
		setLayout(null);
		this.setBounds(0, 0, POSFrame.WIDTH, POSFrame.HEIGHT);
		this.setVisible(false);
		/// this.products = SQL.getProductDetailHashMap();
		/// this.stylists= SQL.getStylistDetailHashMap();
		/// this.coupons= SQL.getCouponDetailHashMap();
		//////////////////////////////////////////// button
		btn_tender = new JButton("Tender");
		btn_tender.setFont(new Font("Tahoma", Font.BOLD, 14));
		btn_tender.setForeground(Color.RED);
		btn_tender.addActionListener(this);
		btn_tender.setBounds(830, 677, 182, 45);
		add(btn_tender);
		cancelBtn = new JButton("Cancel");
		cancelBtn.setForeground(Color.RED);
		cancelBtn.setBounds(15, 677, 111, 45);
		this.cancelBtn.addActionListener(this);
		this.cancelBtn.setEnabled(false);
		add(cancelBtn);
		product = 0;
		tax = 0;
		// saleText = "Sale:\t" + df.format(product) + "\nTax:\t" +
		// df.format(tax) + "\nTotal:\t"
		// + df.format(product + tax);
		miscBtn = new JButton("Misc");
		miscBtn.setForeground(Color.GREEN);
		miscBtn.setBounds(136, 677, 94, 45);
		miscBtn.addActionListener(this);
		add(miscBtn);
		addProductBtn = new JButton("<html><font size=5>Add</font><br><font size=8>&larr</font></html>");
		addProductBtn.addActionListener(this);
		addProductBtn.setBounds(923, 495, 89, 89);
		add(addProductBtn);
		btn_delete = new JButton("<html><font size=5>Delete</font><br><font size=8>&larr</font></html>");
		btn_delete.setBounds(923, 586, 89, 89);
		this.btn_delete.addActionListener(this);
		add(btn_delete);
		clearBtn = new JButton("<html><font size=5 color=red>Cancel<br>All</font></html>");
		clearBtn.addActionListener(this);
		clearBtn.setBounds(923, 292, 89, 72);
		add(clearBtn);
		viewPanel = new JPanel();
		viewPanel.setBounds(10, 11, 911, 664);
		add(viewPanel);
		viewPanel.setLayout(null);
		JLabel lblProductName = new JLabel("Product Name");
		lblProductName.setBounds(567, 5, 86, 15);
		viewPanel.add(lblProductName);
		lblProductName.setFont(new Font("Tahoma", Font.BOLD, 12));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(468, 31, 266, 502);
		viewPanel.add(scrollPane);
		jlist_prodnames = new JList();
		jlist_prodnames.setFont(new Font("Tahoma", Font.PLAIN, 18));
		scrollPane.setViewportView(jlist_prodnames);
		lblPrice = new JLabel("Price");
		lblPrice.setBounds(766, 5, 46, 14);
		viewPanel.add(lblPrice);
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 12));
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(851, 5, 60, 14);
		viewPanel.add(lblQuantity);
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 12));
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(842, 31, 69, 502);
		viewPanel.add(scrollPane_2);
		jlist_quantity = new JList();
		scrollPane_2.setViewportView(jlist_quantity);
		jlist_quantity.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist_quantity.setFont(new Font("Tahoma", Font.PLAIN, 18));
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(735, 31, 108, 502);
		viewPanel.add(scrollPane_1);
		jlist_price = new JList();
		jlist_price.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(jlist_price);
		jlist_price.setFont(new Font("Tahoma", Font.PLAIN, 18));
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(0, 532, 416, 131);
		viewPanel.add(scrollPane_3);
		finalSaleTA = new JTextPane();
		finalSaleTA.setEditable(false);
		finalSaleTA.setContentType("text/html");
		scrollPane_3.setViewportView(finalSaleTA);
		stringSaleTextPanel = "<font size=5><b>Sale:<b></font>" + "&#09 &#09 &#09&#09" + df.format(this.totalAmount)
		+ "<br><font size=5><b>Tax:</b></font>&#09&#09&#09&#09 $0.00<br><font size=5><b>Total:</b></font> &#09&#09&#09&#09" + df.format(this.totalAmount);
		finalSaleTA.setText(stringSaleTextPanel);
		finalSaleTA.setFont(new Font("Monospaced", Font.BOLD, 20));
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(0, 31, 236, 502);
		viewPanel.add(scrollPane_4);
		jlist_stylists = new JList();
		jlist_stylists.setFont(new Font("Tahoma", Font.PLAIN, 18));
		scrollPane_4.setViewportView(jlist_stylists);
		JLabel lblStylist = new JLabel("Stylist");
		lblStylist.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStylist.setBounds(102, 5, 78, 14);
		viewPanel.add(lblStylist);
		JLabel lblHaicut = new JLabel("Haicut");
		lblHaicut.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHaicut.setBounds(315, 5, 63, 14);
		viewPanel.add(lblHaicut);
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(234, 31, 236, 502);
		viewPanel.add(scrollPane_5);
		jlist_haircut = new JList();
		jlist_haircut.setFont(new Font("Tahoma", Font.PLAIN, 18));
		scrollPane_5.setViewportView(jlist_haircut);
		jlabel_date = new JLabel("");
		jlabel_date.setFont(new Font("Tahoma", Font.PLAIN, 18));
		jlabel_date.setForeground(Color.RED);
		jlabel_date.setBorder(new LineBorder(Color.BLUE, 5));
		jlabel_date.setBounds(240, 677, 237, 45);
		add(jlabel_date);
		String name = MainMenuPanel.STYLIST_OBJECT.getName();
		JLabel jlabel_stylist = new JLabel("Stylist: " + name);
		jlabel_stylist.setFont(new Font("Tahoma", Font.PLAIN, 18));
		jlabel_stylist.setForeground(Color.RED);
		jlabel_stylist.setBorder(new LineBorder(Color.BLUE, 5));
		jlabel_stylist.setBounds(487, 674, 333, 45);
		add(jlabel_stylist);
		////////////////////////////////////////////////////////////// end of
		////////////////////////////////////////////////////////////// componenets
		t = new Timer(100, this);
		JButton btnaddcoupon = new JButton("Coupon");////////////////////// APPLY
														////////////////////// COUPON
		btnaddcoupon.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnaddcoupon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CouponDialog d = new CouponDialog();//////////// GET COUPON
													//////////// FIGURE OUT HOW
													//////////// TO APPLY IT
				/// System.out.println(d.getCoupon());
			}
		});
		btnaddcoupon.setBounds(923, 100, 89, 81);
		add(btnaddcoupon);
		t.start();
		this.setVisible(true);
		setupVariables();
		this.current_stylist = this.stylists.get(name);
		this.btn_tender.setEnabled(false);

		JButton deselect = new JButton("<html>Deselect<br></html>");
		deselect.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deselect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jlist_quantity.clearSelection();
				jlist_price.clearSelection();
				jlist_prodnames.clearSelection();
				jlist_haircut.clearSelection();
				jlist_stylists.clearSelection();

			}
		});
		deselect.setBounds(923, 11, 89, 89);
		add(deselect);

		JButton btn_creditcard = new JButton("<html><font size=5 color=blue>Credit<br>Card</font></html>");
		btn_creditcard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreditCardDialog c = new CreditCardDialog();

			}
		});
		btn_creditcard.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_creditcard.setBounds(923, 192, 89, 89);
		add(btn_creditcard);
	}

	public double getSaleTotal() {
		return this.totalAmount;
	}

	/**
	 * This method will instantiate all data structures needed as well as populate the JLists for the GUI. HashMaps represent the Object form (JSON) of the DB.
	 **/
	private void setupVariables() {
		this.arrayList_haircuts = new ArrayList<>();
		this.arrayList_product = new ArrayList<>();
		this.arrayList_stylists = new ArrayList<>();
		this.arrayList_coupons = new ArrayList<>();
		this.arrayList_combinedsale = new ArrayList<>();
		//////////////////////////////
		stylistString = "Stylist(s): " + this.arrayList_stylists;
		orderString = "Product(s): " + this.arrayList_product;
		haircutString = "Haircut(s): " + this.arrayList_haircuts;
		couponString = "Coupon(s): " + this.arrayList_coupons;
		table = new JTextPane();
		table.setEditable(false);
		table.setSize(471, 131);
		table.setFont(new Font("Tahoma", Font.BOLD, 18));
		table.setContentType("text/html");
		stringBasket = "<b>" + haircutString + "<br>" + orderString + "<br>" + stylistString + "<br>" + couponString + "</b>";
		table.setText(stringBasket);
		JScrollPane scrollPane_6 = new JScrollPane(table);
		scrollPane_6.setBounds(416, 532, 495, 131);
		viewPanel.add(scrollPane_6);
		scrollPane_6.setViewportView(table);
		sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		retrieveFromDB();
	}

	// Check for product and for stylists updates
	public void retrieveFromDB() {
		this.products = sql.getLoadedProducts();
		this.haircuts = sql.getLoadedHaircut();
		this.coupons = sql.getLoadedCoupons();
		this.stylists = sql.getLoadedStylists();
		DefaultListModel model_data = new DefaultListModel();
		DefaultListModel model_price = new DefaultListModel();
		DefaultListModel model_quant = new DefaultListModel();
		int i = -1;
		for (String name : this.products.keySet()) {
			model_data.add(++i, name);// data[++i] = name;
			model_price.add(i, this.products.get(name).getPrice());// price[i] =
																	// this.products.get(name).getPrice();
			model_quant.add(i, this.products.get(name).getQuantity());// quant[i]
																		// =
																		// this.products.get(name).getQuantity();
		}
		this.jlist_prodnames.setModel(model_data);
		this.jlist_price.setModel(model_price);
		this.jlist_quantity.setModel(model_quant);
		model_data = new DefaultListModel();
		i = -1;
		for (String name : this.stylists.keySet()) {
			model_data.add(++i, name);
		}
		this.jlist_stylists.setModel(model_data);
		model_data = new DefaultListModel();
		i = -1;
		for (String display : this.haircuts.keySet()) {
			model_data.add(++i, display);
		}
		this.jlist_haircut.setModel(model_data);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		jlabel_date.setText(sdf.format(new Date()));
		try {
			if (a.getSource() == addProductBtn) {
				checkOut();
			} else if (a.getSource() == this.btn_delete) {
				deleteSelected();
			} else if (a.getSource() == this.clearBtn) {
				clearAllOrders();
			} else if (a.getSource() == this.miscBtn) {
				MiscDialog m = new MiscDialog();

			} else if (a.getSource() == this.cancelBtn) {
				doneWithTransaction();
			} else if (a.getSource() == this.btn_tender) {
				new Thread(new Runnable() {
					public void run() {
						clearBtn.setEnabled(false);
						if (temp_total_amount == 0) temp_total_amount = totalAmount;
						NumberPadPOT pad = showNumberPad();
						double balance = pad.getAmount() - temp_total_amount;
						temp_total_amount -= pad.getAmount();
						updateAmountDisplay(balance, pad.getAmount());
						pad.dispose();
						if (balance < 0) {// still owe

							return;
						} else {

							new OpenCashRegister();
							sql.logCashRegister(current_stylist.getID());
							processPaymentAndRecordSale();
							btn_tender.setEnabled(false);
						}
					}
				}).start();
				;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private NumberPadPOT showNumberPad() {
		return new NumberPadPOT(this);

	}

	/***
	 * Process payment
	 */
	private void processPaymentAndRecordSale() {
		// record sale
		this.sql.recordTransaction(this.customer_id, this.totalAmount, arrayList_combinedsale, arrayList_product, arrayList_coupons, new Date());

		this.cancelBtn.setEnabled(true);
		// this.sql.logCashRegister(this.current_stylist.getID());
	}

	private void clearAllOrders() {
		this.arrayList_coupons.clear();
		this.arrayList_haircuts.clear();
		this.arrayList_product.clear();
		this.arrayList_stylists.clear();
		this.arrayList_combinedsale.clear();
		this.totalAmount = 0;
		updateOrderAndPriceText();
		zeroOutQty();
		this.btn_tender.setEnabled(false);
	}

	private void zeroOutQty() {
		DefaultListModel model = (DefaultListModel) this.jlist_quantity.getModel();
		for (int i = 0; i < model.size(); ++i) {
			if ((int) model.getElementAt(i) != 0) model.set(i, new Integer(0));
		}

	}

	private void deleteSelected() {
		Haircut h = null;
		Stylist s = null;
		if (!this.jlist_haircut.isSelectionEmpty()) {
			// delete selected
			h = this.haircuts.get(this.jlist_haircut.getSelectedValue().toString());
			this.jlist_haircut.clearSelection();
		}
		if (!this.jlist_prodnames.isSelectionEmpty()) {
			Product p = this.products.get(this.jlist_prodnames.getSelectedValue().toString());
			// JOptionPane.showMessageDialog(null, this.arrayList_product + " "
			// + p.getQuantity());
			if (this.arrayList_product.contains(p)) {
				p.deleteAProduct();
				this.arrayList_product.remove(p);
				this.totalAmount -= p.getPrice();
				int index = this.jlist_prodnames.getSelectedIndex();
				int q = Integer.parseInt(this.jlist_quantity.getModel().getElementAt(index).toString());
				q -= 1;
				DefaultListModel model = (DefaultListModel) this.jlist_quantity.getModel();
				model.remove(index);
				model.add(index, q);
				if (p.getQuantity() > 0) {
					this.arrayList_product.add(p);
				}
			}
			this.updateOrderAndPriceText();
			this.jlist_prodnames.clearSelection();
		}
		if (!this.jlist_stylists.isSelectionEmpty()) {
			s = this.stylists.get(this.jlist_stylists.getSelectedValue().toString());
			this.jlist_stylists.clearSelection();
		} else {
			s = this.stylists.get(MainMenuPanel.STYLIST_OBJECT.getName());
		}
		if (h == null || s == null) return;
		CombinedSale temp_cs = new CombinedSale(s, h);
		if (this.arrayList_combinedsale.contains(temp_cs)) {
			this.totalAmount -= h.getPrice();
			this.arrayList_combinedsale.remove(temp_cs);
			this.arrayList_haircuts.remove(h);
			this.arrayList_stylists.remove(s);
			this.updateOrderAndPriceText();
		} else {
			return;// nothin to delete
		}
	}

	//// remove from frame
	//// and setVisible to
	//// home frame (MainMenu)
	private void doneWithTransaction() {
		t.stop();
		this.t = null;
		this.setVisible(false);
		MainMenuPanel.goBackToMainMenu();
		POSFrame.frame.remove(this);
		// go back to main menu
	}

	/***
	 * This method will update all orders and stylists who cut hair or sell products
	 */
	private void checkOut() {
		Haircut hh = null;
		Stylist ss = null;
		if (!this.jlist_prodnames.isSelectionEmpty()) {
			Product p = this.products.get(this.jlist_prodnames.getSelectedValue().toString());
			if (this.arrayList_product.contains(p)) {
				p = this.arrayList_product.get(this.arrayList_product.indexOf(p));
				p.addProduct();
				this.arrayList_product.remove(p);
				this.arrayList_product.add(p);
			} else {
				p.addProduct();
				this.arrayList_product.add(p);
			}
			this.totalAmount += p.getPrice();
			int index = this.jlist_prodnames.getSelectedIndex();
			int q = Integer.parseInt(this.jlist_quantity.getModel().getElementAt(index).toString());
			q += 1;
			DefaultListModel jlist_quantities = (DefaultListModel) this.jlist_quantity.getModel(); // get
																									// a
																									// reference
																									// to
																									// the
																									// list
																									// model
			jlist_quantities.removeElementAt(index);
			jlist_quantities.add(index, q);
			; // add to the list
			this.jlist_prodnames.clearSelection();
			updateOrderAndPriceText();
			this.btn_tender.setEnabled(true);
		}
		if (!this.jlist_haircut.isSelectionEmpty()) {
			hh = this.haircuts.get(this.jlist_haircut.getSelectedValue().toString());
			this.jlist_haircut.clearSelection();
		}
		if (!this.jlist_stylists.isSelectionEmpty()) {
			ss = this.stylists.get(this.jlist_stylists.getSelectedValue().toString());
			this.jlist_stylists.clearSelection();
		} else {// is current stlyist
			ss = this.stylists.get(MainMenuPanel.STYLIST_OBJECT.getName());
		}
		if (hh == null || this.arrayList_stylists.contains(ss)) {
			// either a haircut is not selected or the stylist already inputed a
			// sale for themselves.
			return;
		}
		CombinedSale temp_cs = new CombinedSale(ss, hh);
		if (!this.arrayList_combinedsale.contains(temp_cs)) {
			this.arrayList_combinedsale.add(temp_cs);
			this.arrayList_stylists.add(ss);
			this.arrayList_haircuts.add(hh);
			this.totalAmount += hh.getPrice();
		} else {// already priced
			JOptionPane.showMessageDialog(null, temp_cs + " here DEBUG MODE. PLEASE CALL ACBA IMMEDIATELY");
			return;
		}
		updateOrderAndPriceText();///////////////////////////// update
		this.btn_tender.setEnabled(true);
	}

	/**
	 * Calculates and updates the balance and change of the Cash. Bottom Left Component of P.O.T. behavior.
	 */
	private void updateAmountDisplay(double balance, double received) {//// need to set either amount owe or change is:::

		if (balance < 0) { // you owe money
			stringSaleTextPanel = "<font size=5><b>Sale:<b></font>" + "&#09 &#09 &#09&#09" + df.format(this.totalAmount)
			+ "<br><font size=5><b>Tax:</b></font>&#09&#09&#09&#09 $0.00"
			+ "<br><font size=5><b>Received:</b></font>&#09&#09&#09" + df.format(received)
			+ "<br><font size=5><b>Balance:</b></font>&#09&#09&#09&#09" + df.format(balance);
			stylistString = "Stylist(s): " + this.arrayList_stylists;
			orderString = "Product(s): " + this.arrayList_product;
			haircutString = "Haircut(s): " + this.arrayList_haircuts;
			couponString = "Coupon(s): " + this.arrayList_coupons;
			stringBasket = "<b>" + this.haircutString + "<br>" + this.orderString + "<br>" + this.stylistString + "<br>" + this.couponString + "</b>";
			this.finalSaleTA.setText(this.stringSaleTextPanel);
			this.table.setText(this.stringBasket);
		} else {
			stringSaleTextPanel = "<font size=5><b>Sale:<b></font>" + "&#09 &#09 &#09&#09" + df.format(this.totalAmount)
			+ "<br><font size=5><b>Tax:</b></font>&#09&#09&#09&#09 $0.00"
			+ "<br><font size=5><b>Total:</b></font>&#09&#09&#09&#09"+df.format(balance-received)
			+ "<br><font size=5><b>Received:</b></font>&#09&#09&#09" + df.format(received)
			+ "<br><font size=5><b>Change:</b></font>&#09&#09&#09&#09"+df.format(balance);
			stylistString = "Stylist(s): " + this.arrayList_stylists;
			orderString = "Product(s): " + this.arrayList_product;
			haircutString = "Haircut(s): " + this.arrayList_haircuts;
			couponString = "Coupon(s): " + this.arrayList_coupons;
			stringBasket = "<b>" + this.haircutString + "<br>" + this.orderString + "<br>" + this.stylistString + "<br>" + this.couponString + "</b>";
			this.finalSaleTA.setText(this.stringSaleTextPanel);
			this.table.setText(this.stringBasket);
		}

	}

	private void updateOrderAndPriceText() {
		stringSaleTextPanel = "<font size=5><b>Sale:<b></font>" + "&#09 &#09 &#09&#09" + df.format(this.totalAmount)
		+ "<br><font size=5><b>Tax:</b></font>&#09&#09&#09&#09 $0.00<br><font size=5><b>Total:</b></font> &#09&#09&#09&#09" + df.format(this.totalAmount);
		stylistString = "Stylist(s): " + this.arrayList_stylists;
		orderString = "Product(s): " + this.arrayList_product;
		haircutString = "Haircut(s): " + this.arrayList_haircuts;
		couponString = "Coupon(s): " + this.arrayList_coupons;
		stringBasket = "<b>" + this.haircutString + "<br>" + this.orderString + "<br>" + this.stylistString + "<br>" + this.couponString + "</b>";
		this.finalSaleTA.setText(this.stringSaleTextPanel);
		this.table.setText(this.stringBasket);
	}

	public void setStylistLabel(JLabel sty) {
		this.labelStylist = sty;
		this.add(labelStylist);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(MainMenuPanel.img, 0, 0, null);
	}

	public void btn_cancelSetEnable() {
		this.cancelBtn.setEnabled(true);
	}
}
