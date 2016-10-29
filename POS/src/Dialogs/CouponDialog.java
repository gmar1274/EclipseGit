package Dialogs;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import MainFrame.POSFrame;
import POSObjects.Coupon;
import SQLclass.SQL;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;

public class CouponDialog extends JDialog {
	private Coupon					coupon;
	private HashMap<String, Coupon>	couponsHM;
	private JList					list;
	/**
	 * Launch the application.
	 */
	public Coupon getCoupon(){
		return this.coupon;
	}
	/**
	 * Create the dialog.
	 */
	public CouponDialog() {
		SQL s;
		if (POSFrame.SQL == null){
			s = POSFrame.SQL;
		} else{
			s = new SQL();
		}
		this.couponsHM = s.getCouponDetailHashMap();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Coupons");
		setBounds(100, 100, 386, 400);
		getContentPane().setLayout(null);
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(249, 327, 114, 33);
		getContentPane().add(buttonPane);
		buttonPane.setLayout(null);
		JButton btn_apply = new JButton("Apply Coupon");
		btn_apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (couponsHM == null){
					return;
				} else if (!list.isSelectionEmpty()){
					coupon = couponsHM.get(list.getSelectedValue().toString());
					dispose();
				}
			}
		});
		btn_apply.setBounds(0, 0, 114, 33);
		buttonPane.add(btn_apply);
		btn_apply.setActionCommand("OK");
		getRootPane().setDefaultButton(btn_apply);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 30, 353, 295);
		getContentPane().add(scrollPane);
		Object[] data = new Object[this.couponsHM.size()];
		int i = -1;
		for (String key : this.couponsHM.keySet()){
			data[++i] = this.couponsHM.get(key);
		}
		list = new JList(data);
		scrollPane.setViewportView(list);
		JLabel lblCouponName = new JLabel("Coupon Name");
		lblCouponName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCouponName.setBounds(10, 8, 90, 14);
		getContentPane().add(lblCouponName);
		this.setVisible(true);
	}
}
