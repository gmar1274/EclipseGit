/*****
 * This is the panel for the barber POS screen.
 */
package POS_Panels;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import MainFrame.POSFrame;
import POSObjects.Ticket;
import SQLclass.SQL;

import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;

/******
 * This class is responsible for updating incoming ticket requests to the main panel of POS
 * 
 */

public class ReservationPanel extends JPanel implements ActionListener {

	private JList list;
	private JList list_cancel;
	private Timer timer;
	private SQL sql;
	private DefaultListModel<Ticket>  lm,canceled_lm;
	private HashMap<Integer, Ticket> canceled_tickets, tickets;

	/**
	 * Create the panel.
	 */
	public ReservationPanel() {
		this.canceled_tickets = POSFrame.Canceled_Tickets;
		this.tickets = POSFrame.Tickets;
		setBackground(new Color(0, 0, 0));
		this.lm = POSFrame.ListModel;//new DefaultListModel<Ticket>();
		canceled_lm = new DefaultListModel<Ticket>();
		this.sql = POSFrame.SQL;
		timer = new Timer(60 * 1000, this);// every ONE minutes
		setBounds(618, 72, 440, 630);
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 440, 574);
		add(scrollPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(30);
		splitPane.setResizeWeight(0.9);
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		scrollPane.setViewportView(splitPane);

		list = new JList();
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		list.setForeground(new Color(255, 255, 255));
		list.setBackground(new Color(0, 0, 0));
		list.setModel(lm);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		splitPane.setLeftComponent(list);
		list_cancel = new JList();
		list_cancel.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		list_cancel.setForeground(new Color(255, 255, 255));
		list_cancel.setBackground(new Color(0, 0, 0));
		list_cancel.setModel(canceled_lm);
		list_cancel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		splitPane.setRightComponent(list_cancel);

		JButton btn_tticketDone = new JButton("Redeem");
		btn_tticketDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.isSelectionEmpty()) return;
				int index = list.getSelectedIndex();
				Ticket t = lm.get(index);
				updateDB(t);

			}
		});
		btn_tticketDone.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		btn_tticketDone.setBounds(221, 574, 219, 56);
		add(btn_tticketDone);

		JButton btnNewButton = new JButton("Hold");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.isSelectionEmpty()) { return; }
				holdTicket();
			}
		});
		btnNewButton.setFont(new Font("Cambria Math", Font.PLAIN, 18));
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setBounds(0, 574, 183, 56);
		add(btnNewButton);
		this.updateList();
		timer.start();
		this.setVisible(true);
	}

	protected void holdTicket() {
		Ticket ct = lm.remove(list.getSelectedIndex());
		canceled_lm.addElement(ct);
		canceled_tickets.put(ct.getNumber(), ct);
	}
/********
 * This method will delete the redeemed ticket from the DB and update the 
 * Current list of tickets in line on the main screen POS. 
 * */
	protected void updateDB(Ticket t) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				sql.deleteTicket(t);
			}
		}).start();
		updateList();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(new Date());/// debug
		this.updateList();
	}

	private void updateList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				sql.updatePOSTicketScreen(lm, tickets, canceled_tickets);
			}
		}).start();
	}
}
