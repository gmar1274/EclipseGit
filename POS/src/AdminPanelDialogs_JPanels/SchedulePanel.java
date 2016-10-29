package AdminPanelDialogs_JPanels;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import com.toedter.calendar.JDateChooser;
import Dialogs.AdminPanelDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RepaintManager;
import javax.swing.SpinnerDateModel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import java.awt.Rectangle;
import java.awt.GridLayout;

public class SchedulePanel extends JDialog {
	private JButton				backBtn;
	private JDateChooser		end_date;
	private JDateChooser		start_date;
	private ArrayList<String>	employeenames;
	private JPanel panel;
	private JPanel names_panel;
	public enum DAYS {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	};
	/**
	 * Create the panel.
	 */
	public SchedulePanel(JPanel main) {
		this.employeenames = AdminPanelDialog.sql.getEmployeesArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setBounds(0, 0, 950, 600);
		this.setLocationRelativeTo(null);
		// setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				setVisible(false);
				main.setVisible(true);
			}
		});
		backBtn = new JButton("Go Back");
		backBtn.setBounds(0, 500, 100, 60);
		JDialog a = (JDialog) this;
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				
				setVisible(false);
				main.setVisible(true);
			}
		});
		getContentPane().add(backBtn);
		JLabel lblStartDate = new JLabel("Start Date:");
		lblStartDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStartDate.setBounds(10, 11, 90, 20);
		getContentPane().add(lblStartDate);
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setEnabled(false);
		lblEndDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEndDate.setBounds(10, 38, 90, 20);
		getContentPane().add(lblEndDate);
		end_date = new JDateChooser();
		end_date.setEnabled(false);
		end_date.setDateFormatString("yyyy-MM-dd");
		end_date.setBounds(84, 38, 132, 20);
		getContentPane().add(end_date);
		start_date = new JDateChooser();
		start_date.setDateFormatString("yyyy-MM-dd");
		start_date.setBounds(84, 11, 132, 20);
		getContentPane().add(start_date);
		JButton viewSchedBtn = new JButton("<html>View<br>Schedule</html>");
		viewSchedBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(panel!=null && names_panel!=null){
					getContentPane().remove(panel);
					getContentPane().remove(names_panel);
				}
				if (start_date.getDate() == null) {
					JOptionPane.showMessageDialog(null, "Specify a date.","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (start_date.getDate() != null){
					populateScreen(start_date.getDateEditor().getDate(),sdf);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(start_date.getDate());
					cal.add(Calendar.DATE, 6);
					end_date.setDate(cal.getTime());
					String date1 = sdf.format(start_date.getDate());
					String date2 = sdf.format(end_date.getDate());
				} else{
					String date1 = sdf.format(start_date.getDate());
					String date2 = sdf.format(end_date.getDate());
				}
			}
		});
		viewSchedBtn.setBounds(226, 11, 89, 47);
		getContentPane().add(viewSchedBtn);
		JButton btncreateschedule = new JButton("<html>Create<br>Schedule</html>");
		btncreateschedule.setBounds(325, 11, 89, 47);
		getContentPane().add(btncreateschedule);
		
		JButton btnNewButton = new JButton("Print");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					PrinterJob pjob = PrinterJob.getPrinterJob();
					PageFormat preformat = pjob.defaultPage();
					preformat.setOrientation(PageFormat.LANDSCAPE);
					PageFormat postformat = pjob.pageDialog(preformat);
					//If user does not hit cancel then print.
					if (preformat != postformat) {
					    //Set print component
					    pjob.setPrintable(new ComponentPrinter(a), postformat);
					    if (pjob.printDialog()) {
					        pjob.print();
					    }
					}
					//ComponentPrinter.printComponent(a);
				} catch (PrinterException e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(774, 500, 100, 60);
		
		getContentPane().add(btnNewButton);
		
		
		this.setModal(true);
		this.setVisible(false);
	}
	private void populateScreen(Date d,SimpleDateFormat sdf){
		 panel = new JPanel();
		panel.setBounds(152, 69, 722, 47);
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(1, 7, 0, 0));
		 names_panel = new JPanel();
		names_panel.setBounds(10, 115, 142, 374);
		names_panel.setLayout(new GridLayout(this.employeenames.size(), 1, 0, 0));
		for (DAYS day : DAYS.values()){
			JTextPane l = new JTextPane();
			l.setText(day.toString()+"\n"+sdf.format(d));
			l.setSize(5, 100);
			l.setFont(new Font("Arial", Font.BOLD, 11));
			l.setEditable(false);
			l.setVisible(true);
			panel.add(l);
			d.setDate(d.getDate()+1);
		}
		panel.setVisible(true);
		this.getContentPane().add(panel);
		for (String name : this.employeenames){
			JTextPane l = new JTextPane();
			l.setText(name);
			l.setEditable(false);
			l.setSize(50, 10);
			l.setFont(new Font("Arial", Font.BOLD, 11));
			l.setVisible(true);
			names_panel.add(l);
		}
		names_panel.setVisible(true);
		this.getContentPane().add(names_panel);
		JPanel screen = new JPanel();
		screen.setBounds(135, 102, 790, 387);
		screen.setLayout(new GridLayout(this.employeenames.size()*2, 7, 0, 0));
		Date now = start_date.getDateEditor().getDate();
		
		JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
		timeSpinner.setBounds(600, 10, 80, 50);
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
		timeSpinner.setEditor(timeEditor);
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		timeSpinner.setValue(now); // will only show the current time
		this.getContentPane().add(timeSpinner);
		for(int i=1; i <= 7*this.employeenames.size()*2;++i){
			JTextField tf =new JTextField();
			
			tf.setName(now+"");
			tf.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					tf.setText(sdff.format(new Date(tf.getName())));
				}
				
			});
			if(i%7==0){
				now.setDate(now.getDate()-6);
			}else {
				now.setDate(now.getDate()+1);
			}
			screen.add(tf);//screen.add(timeSpinner);
		}
		
		getContentPane().add(screen);
		this.setMinimumSize(new Dimension(this.getWidth(),this.getHeight()));
		this.pack();
		this.getContentPane().repaint();
		this.repaint();
		
	}
	public static class ComponentPrinter extends Object implements Printable {
		// *****************************************************************************
		// INSTANCE PROPERTIES
		// *****************************************************************************
		private Component component; // the component to print
		// *****************************************************************************
		// INSTANCE CREATE/DELETE
		// *****************************************************************************
		public ComponentPrinter(Component com) {
			component = com;
		}
		// *****************************************************************************
		// INSTANCE METHODS
		// *****************************************************************************
		public void print() throws PrinterException{
			PrinterJob printJob = PrinterJob.getPrinterJob();
			printJob.setPrintable(this);
			//if (printJob.printDialog()){
				printJob.print();
			//}
		}
		public int print(Graphics gc, PageFormat format, int page_index){
			if (page_index > 0){ return Printable.NO_SUCH_PAGE; }
			format.setOrientation(PageFormat.LANDSCAPE);
			// get the bounds of the component
			Dimension dim = component.getSize();
			double cHeight = dim.getHeight();
			double cWidth = dim.getWidth();
			// get the bounds of the printable area
			double pHeight = format.getImageableHeight();
			double pWidth = format.getImageableWidth();
			double pXStart = format.getImageableX();
			double pYStart = format.getImageableY();
			double xRatio = pWidth / cWidth;
			double yRatio = pHeight / cHeight;
			Graphics2D g2 = (Graphics2D) gc;
			g2.translate(pXStart, pYStart);
			g2.scale(xRatio, yRatio);
			component.paint(g2);
			return PAGE_EXISTS;
		}
		// *****************************************************************************
		// STATIC METHODS
		// *****************************************************************************
		static public void printComponent(Component com) throws PrinterException{
			new ComponentPrinter(com).print();
		}
	} // END PUBLIC CLASS
}
