package AdminPanelDialogs_JPanels;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AdminCPanel extends JPanel {
	public JPanel adminSettingBtns;
	/**
	 * Create the panel.
	 */
	public AdminCPanel() {
		this.setBounds(0, 0, 500, 560);
		setLayout(null);
		this.setVisible(true);
		adminSettingBtns = new JPanel();
		adminSettingBtns.setBounds(140, 0, 206, 600);
		add(adminSettingBtns);
		adminSettingBtns.setLayout(new GridLayout(10, 1, 0, 0));
		JButton addAdminBtn = new JButton("Add admin");
		adminSettingBtns.add(addAdminBtn);
		adminSettingBtns.setVisible(true);
		JButton deleteAdminBtn = new JButton("Delete admin");
		deleteAdminBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){}
		});
		adminSettingBtns.add(deleteAdminBtn);
	}
}
