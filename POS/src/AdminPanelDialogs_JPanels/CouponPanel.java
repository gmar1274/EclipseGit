package AdminPanelDialogs_JPanels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CouponPanel extends JPanel {
	public JPanel coupSettingBtns;
	/**
	 * Create the panel.
	 */
	public CouponPanel() {
	

		this.setBounds(0, 0, 500, 560);
		setLayout(null);
		this.setVisible(true);
		coupSettingBtns = new JPanel();
		coupSettingBtns.setBounds(10, 11, 187, 479);
		add(coupSettingBtns);
		coupSettingBtns.setLayout(new GridLayout(10, 1, 0, 5));
		JButton addCoupBtn = new JButton("Add new coupon");
		addCoupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		coupSettingBtns.add(addCoupBtn);
		coupSettingBtns.setVisible(true);
		JButton deleteCoupBtn = new JButton("Delete coupon");
		deleteCoupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){}
		});
		coupSettingBtns.add(deleteCoupBtn);
		JButton btnChangeEmployeePay = new JButton("View coupon information");
		coupSettingBtns.add(btnChangeEmployeePay);
		
		JButton couponTranBtn = new JButton("View coupon transaction");
		coupSettingBtns.add(couponTranBtn);
		JButton couponTransBtn = new JButton("View all coupon transactions ");
		coupSettingBtns.add(couponTransBtn);
		
		JPanel panel = new JPanel();
		panel.setBounds(199, 11, 301, 479);
		add(panel);
		
	}
}
